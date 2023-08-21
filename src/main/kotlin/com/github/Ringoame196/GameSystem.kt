package com.github.Ringoame196

import com.github.Ringoame196.Entity.ArmorStand
import com.github.Ringoame196.Entity.Golem
import com.github.Ringoame196.Entity.Zombie
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.Gamedata
import com.github.Ringoame196.data.TeamData
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class GameSystem {
    private var timerTask: BukkitTask? = null
    fun system(plugin: Plugin, player: Player, item: ItemStack, e: InventoryClickEvent) {
        player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
        val displayName = item.itemMeta?.displayName
        when (displayName) {
            "${ChatColor.AQUA}ゲームスタート" -> start(plugin, player)
            "${ChatColor.RED}終了" -> stop(player)
            "${ChatColor.YELLOW}ショップ召喚" -> shop().summon(player.location, null)
            "${ChatColor.GREEN}参加" -> Team().inAndout(player)
            "${ChatColor.RED}プラグインリロード" -> {
                if (e.isShiftClick) {
                    Bukkit.dispatchCommand(player, "pluginmanager reload ZON-BATTLE2")
                    e.isCancelled = true
                }
            }
            "${ChatColor.YELLOW}実験所へ" -> Bukkit.dispatchCommand(player, "mvtp jikken")
            "${ChatColor.YELLOW}ロビーへ" -> Bukkit.dispatchCommand(player, "mvtp world")
            "${ChatColor.YELLOW}バトルへ" -> Bukkit.dispatchCommand(player, "mvtp BATTLE")
            "${ChatColor.BLUE}プレイヤー" -> {
                GUI().JoinPlayers(player)
                return
            }
            "${ChatColor.GREEN}テレポート" -> {
                val location = "${player.location.x.toInt()}.5,${player.location.y.toInt()}.0,${player.location.z.toInt()}.5"
                val item = ItemStack(Material.LANTERN)
                val meta = item.itemMeta
                meta?.setDisplayName("${ChatColor.GREEN}テレポート")
                val loreList: MutableList<String> = mutableListOf(player.world.name, location)
                meta?.lore = loreList
                item.setItemMeta(meta)
                player.inventory.addItem(item)
            }
            "${ChatColor.GREEN}OPチェスト" -> {
                player.openInventory(Data.DataManager.gameData.opchest)
                return
            }
        }
        player.closeInventory()
        if (item.type == Material.ENDER_EYE && e.isShiftClick) {
            setlocation(item, player)
            e.isCancelled = true
        }
    }

    fun start(plugin: Plugin, player: Player) {
        if (Data.DataManager.gameData.ParticipatingPlayer.size == 0) { return }
        if (GET().status()) {
            player.sendMessage("${ChatColor.RED}既にゲームはスタートしています")
            return
        }
        Data.DataManager.gameData.status = true
        reset()
        Data.DataManager.LocationData.let {
            if (it.redshop == null || it.blueshop == null || it.redspawn == null || it.bluespawn == null) {
                NotSet(player)
                return
            }
        }
        shop().summon(Data.DataManager.LocationData.redshop, "red")
        shop().summon(Data.DataManager.LocationData.blueshop, "blue")
        if (Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam("red") == null) { Team().make("red", ChatColor.RED, "${ChatColor.RED}[赤チーム]") }
        if (Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam("blue") == null) { Team().make("blue", ChatColor.BLUE, "${ChatColor.BLUE}[青チーム]") }
        randomChest().set()
        val location = Data.DataManager.LocationData.randomChest?.clone()
        location?.add(0.5, -1.0, 0.0)
        val armorStand = location?.let { ArmorStand().summon(it, "") }
        Data.DataManager.gameData.randomChestTitle = armorStand

        var c = 5
        object : BukkitRunnable() {
            override fun run() {
                if (c > 0) {
                    PlayerSend().participantmessage("${ChatColor.GREEN}[カウントダウン]開始まで$c")
                    PlayerSend().participantplaysound(Sound.BLOCK_DISPENSER_FAIL)
                    c--
                } else {
                    Team().division()
                    Sign().Numberdisplay("ゲーム進行中")
                    Bukkit.broadcastMessage("${ChatColor.GREEN}攻防戦ゲームスタート！！")
                    if (Data.DataManager.gameData.shortage) {
                        val teamDataMap = Data.DataManager.teamDataMap
                        val blocktime = teamDataMap.getOrPut("blue") { TeamData() }.blockTime
                        if (blocktime == teamDataMap.getOrPut("red") { TeamData() }.blockTime) {
                            PlayerSend().participantmessage("${ChatColor.RED}人数不足のため 青チームのポイントが2倍になりました")
                        } else {
                            PlayerSend().participantmessage("${ChatColor.RED}人数不足のため 青チームの復活速度が1上がりになりました")
                            Data.DataManager.gameData.shortage = false
                        }
                        PlayerSend().participantplaysound(Sound.BLOCK_BELL_USE)
                    }
                    PlayerSend().participantplaysound(Sound.ENTITY_ENDER_DRAGON_AMBIENT)
                    timer(plugin)
                    this.cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, 20L) // 0Lは遅延時間、20Lは繰り返し間隔（1秒=20tick
    }
    fun NotSet(player: Player) {
        player.sendMessage("${ChatColor.RED}未設定の項目があります")
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
    }
    fun timer(plugin: Plugin) {
        timerTask = object : BukkitRunnable() {
            override fun run() {
                if (!GET().status()) {
                    this.cancel()
                    return
                }
                Data.DataManager.gameData.time += 1
                Regularly()
                val randomChestTime = 300 - (Data.DataManager.gameData.time % 300)
                Data.DataManager.gameData.randomChestTitle?.customName = "${ChatColor.AQUA}${GET().minutes(randomChestTime)}"
            }
        }.runTaskTimer(plugin, 0L, 20L)
    }
    fun setlocation(item: ItemStack, player: Player) {
        when (item.itemMeta?.displayName) {
            "${ChatColor.RED}shop" -> Data.DataManager.LocationData.redshop = player.location
            "${ChatColor.BLUE}shop" -> Data.DataManager.LocationData.blueshop = player.location
            "${ChatColor.RED}spawn" -> Data.DataManager.LocationData.redspawn = player.location
            "${ChatColor.BLUE}spawn" -> Data.DataManager.LocationData.bluespawn = player.location
            "${ChatColor.YELLOW}ランダムチェスト" -> randomChest().setLocation(player)
        }
        player.sendMessage("${ChatColor.AQUA}座標設定完了")

        val filePath = "plugins/ZON-BATTLE2/location_data.yml"
        Data.DataManager.LocationData.saveToFile(filePath)
    }
    fun stop(player: Player) {
        if (!GET().status()) {
            player.sendMessage("${ChatColor.RED}ゲームは開始していません")
            return
        }
        gameEndSystem("${ChatColor.RED}攻防戦ゲーム強制終了！！", null)
    }

    fun gameend(winTeam: String) {
        gameEndSystem("${ChatColor.RED}攻防戦ゲーム終了！！", winTeam)
    }
    fun gameEndSystem(message: String, winTeam: String?) {
        Data.DataManager.gameData.bossBar.removeAll()
        Bukkit.broadcastMessage("${ChatColor.YELLOW}[攻防戦]$message")
        for (loopPlayer in Data.DataManager.gameData.ParticipatingPlayer) {
            loopPlayer.sendMessage("${ChatColor.AQUA}[ゲーム時間]${GET().minutes(Data.DataManager.gameData.time)}")
            loopPlayer.sendMessage("${ChatColor.RED}${winTeam}チームの勝利")

            loopPlayer.playSound(loopPlayer.location, Sound.BLOCK_ANVIL_USE, 1f, 1f)
            loopPlayer.inventory.clear()
            if (loopPlayer.isOp) {
                loopPlayer.inventory.addItem(Give().GameSetting())
                loopPlayer.gameMode = GameMode.CREATIVE
            } else {
                loopPlayer.gameMode = GameMode.ADVENTURE
            }
            for (effect in loopPlayer.activePotionEffects) {
                loopPlayer.removePotionEffect(effect.type)
            }
            Bukkit.getWorld("world")?.let { loopPlayer.teleport(it.spawnLocation) }
            if (winTeam == null) { continue }
            if (winTeam == GET().TeamName(loopPlayer)) {
                for (i in 1..5) {
                    loopPlayer.inventory.addItem(Give().coin())
                }
                Ranking().addScore(loopPlayer.name)
            } else {
                loopPlayer.inventory.addItem(Give().coin())
            }
        }
        for (player in Bukkit.getWorld("BATTLE")?.players!!) {
            if (player.gameMode != GameMode.CREATIVE) {
                Bukkit.getWorld("world")?.let { player.teleport(it.spawnLocation) }
            }
        }
        Sign().Numberdisplay("(参加中:0人)")
        Data.DataManager.gameData.status = false
        reset()
        Team().make("red", ChatColor.RED, "${ChatColor.RED}[赤チーム]")
        Team().make("blue", ChatColor.BLUE, "${ChatColor.BLUE}[青チーム]")
        Ranking().updateRankingScoreboard()
    }

    fun adventure(e: org.bukkit.event.Event, player: Player) {
        if (player.gameMode == GameMode.CREATIVE) { return }
        if (e is Cancellable) { e.isCancelled = true }
    }
    fun reset() {
        BreakBlock().deleteRevival()
        for (entity in Bukkit.getWorld("BATTLE")?.entities!!) {
            if (entity !is Player) {
                entity.remove()
            }
        }
        for (block in Data.DataManager.gameData.fence) {
            block.setType(Material.AIR)
        }
        Data.DataManager.teamDataMap.clear() // teamDataMap を空にする
        Data.DataManager.playerDataMap.clear() // playerDataMap を空にする
        Team().delete()
        randomChest().reset()
        if (GET().status()) { return }
        Data.DataManager.gameData = Gamedata() // gameData を新しい Gamedata インスタンスに置き換える
    }
    fun Regularly() {
        val time = Data.DataManager.gameData.time
        if (time == 1200) {
            PlayerSend().participantmessage("${ChatColor.RED}20分経ったためポイントが2倍になりました")
            PlayerSend().participantplaysound(Sound.BLOCK_ANVIL_USE)
            Data.DataManager.gameData.magnification = 2
        }
        if (time <= 300) {
            val remaining = 300 - time
            Data.DataManager.gameData.bossBar.setTitle("${ChatColor.YELLOW}ゾンビ解放まで${GET().minutes(remaining)}")
        } else if (time <= 1200) {
            val remaining = 1200 - time
            Data.DataManager.gameData.bossBar.setTitle("${ChatColor.AQUA}ポイント2倍まで${GET().minutes(remaining)}")
        }
        if (time == 300) { PlayerSend().participantmessage("${ChatColor.YELLOW}ゾンビ解放!") }
        if (time % 300 == 0) { randomChest().set() }
        if (time % 17 == 0) { Zombie().summonner("§5エンペラー", "shield", "soldier") }
        if (time % 5 == 0) { Golem().Golden() }
        if (time % 7 == 0) { Zombie().summonner("§5ネクロマンサー", "normal", "normal") }
    }
    fun playersJoin(PlayerName: String, sender: Player) {
        val player = Bukkit.getPlayer(PlayerName.replace("${ChatColor.YELLOW}", "")) ?: return
        Team().inAndout(player)
        player.sendMessage("${ChatColor.RED}※${sender.displayName}があなたのゲーム参加を操作しました")
        GUI().JoinPlayers(sender)
    }
}
