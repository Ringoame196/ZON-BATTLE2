package com.github.Ringoame196

import com.github.Ringoame196.Game.GameSystem
import com.github.Ringoame196.Game.Point
import com.github.Ringoame196.Game.Scoreboard
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.attribute.Attribute
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Shop {
    fun open(e: PlayerInteractEntityEvent, player: Player, entity: Mob, team: String) {
        e.isCancelled = true
        Data.DataManager.teamDataMap[team]?.entities?.add(entity)
        if (Scoreboard().getValue(GET().getTeamSystemScoreName(team), "ショップ解放済み") == 1 || player.gameMode == GameMode.CREATIVE) {
            GUI().selectGUI(player)
        } else {
            unopened(player)
        }
    }
    fun clickEvent(item: ItemStack, player: Player) {
        if (item.type == Material.RED_STAINED_GLASS_PANE) {
            return
        }
        val itemname = item.itemMeta?.displayName
        val price = item.itemMeta?.lore?.get(0) // 値段取得
        if (!price!!.contains("p")) { return }
        if (!Point().purchase(player, price)) { return }

        if (itemname?.contains("★")!!) {
            val setteamname = GET().teamName(player) ?: return
            GUI().clickInvocation(player, itemname, setteamname)
        } else {
            val giveitem = ItemStack(item)
            giveitem.let {
                val meta = item.itemMeta
                val lore = meta?.lore
                if (!lore.isNullOrEmpty()) {
                    lore.removeAt(0) // 1行目を削除
                    meta.lore = lore
                }
                giveitem.itemMeta = meta
                if (it.itemMeta?.displayName?.contains("[装備]") == true) {
                    Give().equipment(player, it)
                    return
                } else if (it.itemMeta?.displayName?.contains("[武器]") == true) {
                    Give().sword(player)
                    player.inventory.addItem(it)
                    GUI().weaponshop(player.openInventory.topInventory, player)
                } else if (it.itemMeta?.displayName?.contains("[ツール]") == true) {
                    Give().pickaxe(player)
                    player.inventory.addItem(it)
                    GUI().pickaxeshop(player.openInventory.topInventory, player)
                } else if (it.itemMeta?.displayName?.contains("[斧]") == true) {
                    Give().axe(player)
                    player.inventory.addItem(it)
                    GUI().pickaxeshop(player.openInventory.topInventory, player)
                } else {
                    player.inventory.addItem(it)
                }
            }
        }
    }
    fun gui(player: Player) {
        val gui = Bukkit.createInventory(null, 27, ChatColor.BLUE.toString() + "攻防戦ショップ[BATTLEGUI]")
        val point = Scoreboard().getValue("point", player.name)

        GUI().guiItem(gui, 0, Material.EMERALD, "${ChatColor.GREEN}所持ポイント:" + point + "p", "", true)
        GUI().guiItem(gui, 1, Material.IRON_PICKAXE, "${ChatColor.YELLOW}ツール", "", true)
        GUI().guiItem(gui, 3, Material.IRON_SWORD, "${ChatColor.YELLOW}武器", "", true)
        GUI().guiItem(gui, 5, Material.IRON_CHESTPLATE, "${ChatColor.YELLOW}防具", "", true)
        GUI().guiItem(gui, 7, Material.TNT, "${ChatColor.YELLOW}お邪魔アイテム", "", true)
        if (Data.DataManager.gameData.time >= 300 || player.gameMode == GameMode.CREATIVE) {
            GUI().guiItem(gui, 10, Material.ZOMBIE_SPAWN_EGG, "${ChatColor.YELLOW}ゾンビ", "", true)
        } else {
            GUI().guiItem(gui, 10, Material.BARRIER, "${ChatColor.RED}選択禁止", "", true)
        }
        GUI().guiItem(gui, 12, Material.WOLF_SPAWN_EGG, "${ChatColor.YELLOW}ペット", "", true)
        GUI().guiItem(gui, 14, Material.BEACON, "${ChatColor.YELLOW}その他", "", true)
        GUI().noSet(gui, 16)
        GUI().noSet(gui, 19)
        GUI().noSet(gui, 21)
        GUI().noSet(gui, 23)
        GUI().noSet(gui, 25)
        player.openInventory(gui)
    }
    fun unopened(player: Player) {
        val gui = Bukkit.createInventory(null, 9, "${ChatColor.DARK_GREEN}ショップ[BATTLEGUI]")
        GUI().guiItem(gui, 4, Material.GOLD_BLOCK, "${ChatColor.YELLOW}★ショップ解放", "15p", true)
        player.openInventory(gui)
    }
    fun recovery(shop: Villager, amount: Double) {
        val maxHP = GET().maxHP(shop)
        val currentHP = String.format("%.1f", shop.health + amount).toDouble()
        val newHP = maxHP?.let { if (currentHP >= it) it else currentHP }?.toString() ?: return
        name(shop, newHP, maxHP.toString())
    }
    fun name(shop: Villager, hp: String, maxHP: String) {
        shop.customName = "${ChatColor.RED}${hp}HP/${maxHP}HP"
        val hpValue = hp.toFloatOrNull()
        val hpInt = hpValue?.toInt() ?: 0
        when {
            shop.scoreboardTags.contains("red") -> {
                Scoreboard().set("RedTeam", "赤チーム(自陣)", hpInt)
                Scoreboard().set("BlueTeam", "赤チーム", hpInt)
            }
            shop.scoreboardTags.contains("blue") -> {
                Scoreboard().set("RedTeam", "青チーム", hpInt)
                Scoreboard().set("BlueTeam", "青チーム(自陣)", hpInt)
            }
        }
    }
    fun attack(e: EntityDamageByEntityEvent, damager: Entity, shop: Villager) {
        if (!GET().shop(shop)) { return }
        if (damager is Player) {
            // プレイヤーが殴るのを禁止させる
            GameSystem().adventure(e, damager)
            if (damager.gameMode != GameMode.CREATIVE) { return }
        }
        // ダメージを受けたときにメッセージを出す
        val health = String.format("%.1f", shop.health - e.damage).toDouble()
        if (health <= 0) {
            return
        }
        val message = "${ChatColor.RED}ショップがダメージを食らっています (残りHP $health)"
        val maxHP = GET().maxHP(shop)
        name(shop, health.toString(), maxHP.toString())
        val blockBelow = shop.location.subtract(0.0, 1.0, 0.0).block.type
        val setteamname = when (blockBelow) {
            Material.RED_WOOL -> "red"
            Material.BLUE_WOOL -> "blue"
            else -> return
        }

        val scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard
        scoreboard?.getObjective("participatingPlayer") ?: return
        for (playerName in scoreboard.entries) {
            val player = Bukkit.getPlayer(playerName) ?: continue
            val teamname = GET().teamName(player)
            if (teamname != setteamname) {
                continue
            }
            player.sendMessage(message)
            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
        }
    }
    fun summon(location: Location?, tag: String?) {
        val initialHP = 100.0
        val world = location?.world
        val villager: Villager = world!!.spawn(location, Villager::class.java)
        Shop().name(villager, initialHP.toString(), initialHP.toString())
        villager.isCustomNameVisible = true
        villager.scoreboardTags.add("shop")
        villager.setAI(false)
        villager.isSilent = true
        if (tag != null) { villager.scoreboardTags.add(tag) }

        val maxHPAttribute = villager.getAttribute(Attribute.GENERIC_MAX_HEALTH)
        maxHPAttribute?.baseValue = initialHP
        villager.health = initialHP
        villager.customName = "${ChatColor.RED}${villager.health}HP/${initialHP}HP" // カスタムネームの表示を更新

        // アーマースタンドを召喚
        val armorStandLocation = location.clone()
        armorStandLocation.add(0.0, 1.3, 0.0)
        com.github.Ringoame196.Entity.ArmorStand().summon(armorStandLocation, "${ChatColor.GOLD}攻防戦ショップ")
    }
    fun deletename(location: Location) {
        val nearbyEntities = location.world?.getNearbyEntities(location, 3.0, 3.0, 3.0)

        for (entity in nearbyEntities!!) {
            if (entity is ArmorStand && entity.customName == "${ChatColor.GOLD}攻防戦ショップ") {
                entity.remove() // 防具立てをキルする
                return
            }
        }
    }
    fun release(player: Player, teamname: String) {
        Scoreboard().set(GET().getTeamSystemScoreName(teamname), "ショップ解放済み", 1)
        GUI().selectGUI(player)
        Team().sendMessage("${player.name}さんがショップを解放しました", GET().teamName(player).toString())
    }
    fun teamMaxHPadd(teamname: String, player: Player, itemname: String, add: Int) {
        val entity = Data.DataManager.teamDataMap[teamname]?.entities?.lastOrNull() as? LivingEntity ?: return
        val maxHPAttribute = entity.getAttribute(Attribute.GENERIC_MAX_HEALTH) ?: return
        // 現在の最大HPを取得
        val currentMaxHP = maxHPAttribute.baseValue + add
        // 最大HPを設定
        maxHPAttribute.baseValue = currentMaxHP
        // 村人の名前を更新（HP表示を変更する場合）
        Shop().name(entity as Villager, GET().hp(entity).toString(), currentMaxHP.toString())
        GUI().villagerlevelup(player.openInventory.topInventory, player)
        Team().GiveEffect(player, itemname, null, null, 0, 0)
    }
    fun effect(player: Player, itemName: String, potion: PotionEffectType, time: Int, level: Int) {
        val entity = Data.DataManager.teamDataMap[GET().teamName(player)]?.entities?.lastOrNull() as? LivingEntity ?: return
        entity.addPotionEffect(PotionEffect(potion, time * 20, level - 1))
        Team().GiveEffect(player, itemName, null, null, 0, 0)
    }
    fun kill(mob: Villager) {
        deletename(mob.location)
        if (!Data.DataManager.gameData.status) { return }
        val winTeam: String? = when (mob.location.add(0.0, -1.0, 0.0).block.type) {
            Material.RED_WOOL -> "blue"
            Material.BLUE_WOOL -> "red"
            else -> null
        }
        winTeam?.let { GameSystem().gameend(it) }
    }
}
