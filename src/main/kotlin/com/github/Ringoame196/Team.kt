@file:Suppress("DEPRECATION")

package com.github.Ringoame196

import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import com.github.Ringoame196.data.TeamData
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.NameTagVisibility

@Suppress("DEPRECATION")
class Team {
    fun openChest(player: Player, teamName: String) {
        player.playSound(player, Sound.BLOCK_CHEST_OPEN, 1f, 1f)
        player.openInventory(Data.DataManager.teamDataMap.getOrPut(teamName) { TeamData() }.chest)
    }
    fun fastbreaklevel(teamName: String, player: Player, itemName: String) {
        val setTime = Data.DataManager.teamDataMap.getOrPut(teamName) { TeamData() }.blockTime - 1
        Data.DataManager.teamDataMap[teamName]?.blockTime = setTime
        GUI().villagerlevelup(player.openInventory.topInventory, player)
        Team().GiveEffect(player, itemName, null, null, 6 - setTime, 0)
    }
    fun make(name: String, color: ChatColor, prefix: String) {
        Bukkit.getScoreboardManager()?.mainScoreboard?.registerNewTeam(name)
        Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam(name)?.let {
            it.setAllowFriendlyFire(false)
            it.color = color
            it.nameTagVisibility = NameTagVisibility.HIDE_FOR_OTHER_TEAMS
            it.prefix = prefix
        }
    }
    fun delete() {
        Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam("red")?.unregister()
        Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam("blue")?.unregister()
        Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam("kansen")?.unregister()
    }
    fun division() {
        val redTeam = Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam("red")
        val blueTeam = Bukkit.getScoreboardManager()?.mainScoreboard?.getTeam("blue")
        var team = true
        var blueCount = 0
        for (loopPlayer in Data.DataManager.gameData.participatingPlayer) {
            Data.DataManager.gameData.bossBar.addPlayer(loopPlayer)
            loopPlayer.setPlayerListName(null)
            loopPlayer.setDisplayName(null)
            if (team) {
                redTeam?.addPlayer(loopPlayer)
                if (Data.DataManager.gameData.playMap == "map1") {
                    loopPlayer.teleport(Data.DataManager.LocationData.redspawn!!)
                } else if (Data.DataManager.gameData.playMap == "map2") {
                    loopPlayer.teleport(Data.DataManager.LocationData.mredspawn!!)
                }
            } else {
                blueTeam?.addPlayer(loopPlayer)
                if (Data.DataManager.gameData.playMap == "map1") {
                    loopPlayer.teleport(Data.DataManager.LocationData.bluespawn!!)
                } else if (Data.DataManager.gameData.playMap == "map2") {
                    loopPlayer.teleport(Data.DataManager.LocationData.mbluespawn!!)
                }
                blueCount++
            }
            loopPlayer.scoreboardTags.remove("pvpjoin")
            loopPlayer.addPotionEffect(PotionEffect(PotionEffectType.SATURATION, Int.MAX_VALUE, 100, true, false))
            loopPlayer.addPotionEffect(PotionEffect(PotionEffectType.NIGHT_VISION, Int.MAX_VALUE, 1, true, false))
            Equipment().initial(loopPlayer)
            loopPlayer.gameMode = GameMode.SURVIVAL
            loopPlayer.health = 20.0
            if (loopPlayer.isOp) {
                loopPlayer.inventory.addItem(Give().gameSetting())
            }
            team = !team
        }
        if (!team) {
            Data.DataManager.gameData.shortage = true
            if (blueCount >= 2) {
                val blocktime = Data.DataManager.teamDataMap.getOrPut("blue") { TeamData() }.blockTime
                Data.DataManager.teamDataMap.getOrPut("blue") { TeamData() }.blockTime = blocktime - 1
            }
        }
    }
    fun waitingState(player: Player) {
        player.health = 20.0
        player.gameMode = GameMode.SPECTATOR
    }
    fun revival(player: Player) {
        player.gameMode = GameMode.SURVIVAL
        respawnTP(player)
    }
    fun respawn(player: Player, plugin: Plugin, deathmessage: String) {
        ParticipatingPlayer().message("${ChatColor.RED}[DEATH] ${player.name} by" + deathmessage)
        waitingState(player)
        var c = 6
        object : BukkitRunnable() {
            override fun run() {
                if (c> 0) {
                    c--
                    player.sendTitle("", "${ChatColor.GREEN}復活まで${c}秒")
                    player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
                } else {
                    revival(player)
                    this.cancel()
                }
            }
        }.runTaskTimer(plugin, 0, 20)
    }
    fun respawnTP(player: Player) {
        when (GET().teamName(player)) {
            "red" -> {
                if (Data.DataManager.gameData.playMap == "map1") {
                    player.teleport(Data.DataManager.LocationData.redspawn!!)
                } else if (Data.DataManager.gameData.playMap == "map2") {
                    player.teleport(Data.DataManager.LocationData.mredspawn!!)
                }
            }
            "blue" -> {
                if (Data.DataManager.gameData.playMap == "map1") {
                    player.teleport(Data.DataManager.LocationData.bluespawn!!)
                } else if (Data.DataManager.gameData.playMap == "map2") {
                    player.teleport(Data.DataManager.LocationData.mbluespawn!!)
                }
            }
            else -> player.teleport(player.world.spawnLocation)
        }
    }
    fun GiveEffect(
        player: Player,
        itemName: String,
        effect1: PotionEffectType? = null,
        effect2: PotionEffectType? = null,
        level: Int,
        time: Int
    ) {
        val playerName = player.name
        val playerTeamName = GET().teamName(player)
        var effectTeamName = GET().teamName(player)
        if (itemName.contains("[妨害]")) {
            // 反対チーム名にする
            effectTeamName = GET().opposingTeamname(playerTeamName!!)
        }
        for (loopPlayer in Data.DataManager.gameData.participatingPlayer) {
            val loopPlayerTeam = GET().teamName(loopPlayer)

            if (loopPlayerTeam == playerTeamName) {
                loopPlayer.sendMessage("${ChatColor.AQUA}[チーム]${playerName}さんが${itemName}${ChatColor.AQUA}を発動しました(レベル$level)")
                if (loopPlayerTeam == effectTeamName) {
                    effect1?.let { loopPlayer.addPotionEffect(PotionEffect(it, time * 20, level - 1)) }
                    effect2?.let {
                        loopPlayer.addPotionEffect(PotionEffect(it, time * 20, level - 1))
                        loopPlayer.playSound(loopPlayer.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
                    }
                }
            } else if (loopPlayerTeam == effectTeamName) {
                loopPlayer.sendMessage("${ChatColor.RED}[妨害]${playerTeamName}チームが${itemName}${ChatColor.RED}を発動しました(レベル $level)")
                effect1?.let { loopPlayer.addPotionEffect(PotionEffect(it, time * 20, level - 1)) }
                effect2?.let {
                    loopPlayer.addPotionEffect(PotionEffect(it, time * 20, level - 1))
                    loopPlayer.playSound(loopPlayer.location, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
                }
            }
        }
    }
    fun sendMessage(message: String, teamName: String) {
        for (loopPlayer in Data.DataManager.gameData.participatingPlayer) {
            if (GET().teamName(loopPlayer) != teamName) { continue }
            loopPlayer.sendMessage("${ChatColor.AQUA}[チーム]$message")
        }
    }
    fun sound(sound: Sound, teamName: String) {
        for (loopPlayer in Data.DataManager.gameData.participatingPlayer) {
            if (GET().teamName(loopPlayer) != teamName) { continue }
            loopPlayer.playSound(loopPlayer, sound, 1f, 1f)
        }
    }
}
