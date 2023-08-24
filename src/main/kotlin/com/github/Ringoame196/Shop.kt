package com.github.Ringoame196

import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.PlayerData
import com.github.Ringoame196.data.TeamData
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
        if (Data.DataManager.teamDataMap[team]?.opening == true || player.gameMode == GameMode.CREATIVE) {
            GUI().selectGUI(player)
        } else {
            unopened(player)
        }
    }
    fun system(item: ItemStack, player: Player) {
        if (item.type == Material.RED_STAINED_GLASS_PANE) {
            return
        }
        val itemname = item.itemMeta?.displayName
        val price = item.itemMeta?.lore?.get(0) // 値段取得
        if (!price!!.contains("p")) { return }
        if (!Point().purchase(player, price)) { return }

        if (itemname?.contains("★")!!) {
            val setteamname = GET().teamName(player) ?: return
            GUIClick().clickInvocation(player, itemname, setteamname)
        } else {
            val giveitem = ItemStack(item)
            giveitem.let {
                val meta = item.itemMeta
                meta?.lore = null
                giveitem.setItemMeta(meta)
                val itemname = it.itemMeta?.displayName
                if (itemname?.contains("ゴーレム") == true || itemname == "${ChatColor.RED}ブレイズ" || itemname == "${ChatColor.YELLOW}シュルカー") {
                    val c = Data.DataManager.teamDataMap.getOrPut(GET().teamName(player)) { TeamData() }.golem
                    if (c >= 5) {
                        player.sendMessage("${ChatColor.YELLOW}ゴーレムを5体以上購入できません")
                        player.closeInventory()
                        when (it.itemMeta?.displayName) {
                            "${ChatColor.YELLOW}アイアンゴーレム" -> Point().add(player, 500, false)
                            "${ChatColor.YELLOW}ゴールデンゴーレム" -> Point().add(player, 1500, false)
                            "${ChatColor.RED}ブレイズ" -> Point().add(player, 400, false)
                            "${ChatColor.YELLOW}シュルカー" -> Point().add(player, 800, false)
                        }
                        return
                    }
                    Data.DataManager.teamDataMap.getOrPut(GET().teamName(player)) { TeamData() }.golem = c + 1
                    Shop().effect(player, "ゴーレム召喚($c/5)", PotionEffectType.REGENERATION, 180, 1)
                }
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
        val point = Data.DataManager.playerDataMap.getOrPut(player.uniqueId) { PlayerData() }.point

        GUI().setGUIitem(gui, 0, Material.EMERALD, "${ChatColor.GREEN}所持ポイント:" + point + "p", "", true)
        GUI().setGUIitem(gui, 1, Material.IRON_PICKAXE, "${ChatColor.YELLOW}ツール", "", true)
        GUI().setGUIitem(gui, 3, Material.IRON_SWORD, "${ChatColor.YELLOW}武器", "", true)
        GUI().setGUIitem(gui, 5, Material.IRON_CHESTPLATE, "${ChatColor.YELLOW}防具", "", true)
        GUI().setGUIitem(gui, 7, Material.TNT, "${ChatColor.YELLOW}お邪魔アイテム", "", true)
        if (Data.DataManager.gameData.time >= 300 || player.gameMode == GameMode.CREATIVE) {
            GUI().setGUIitem(gui, 10, Material.ZOMBIE_SPAWN_EGG, "${ChatColor.YELLOW}ゾンビ", "", true)
        } else {
            GUI().setGUIitem(gui, 10, Material.BARRIER, "${ChatColor.RED}選択禁止", "", true)
        }
        GUI().setGUIitem(gui, 12, Material.WOLF_SPAWN_EGG, "${ChatColor.YELLOW}ペット", "", true)
        GUI().setGUIitem(gui, 14, Material.BEACON, "${ChatColor.YELLOW}その他", "", true)
        GUI().noSet(gui, 16)
        GUI().noSet(gui, 19)
        GUI().noSet(gui, 21)
        GUI().noSet(gui, 23)
        GUI().noSet(gui, 25)
        player.openInventory(gui)
    }
    fun unopened(player: Player) {
        val gui = Bukkit.createInventory(null, 9, "${ChatColor.DARK_GREEN}ショップ[BATTLEGUI]")
        GUI().setGUIitem(gui, 4, Material.GOLD_BLOCK, "${ChatColor.YELLOW}★ショップ解放", "15p", true)
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

        for (player in Data.DataManager.gameData.participatingPlayer) {
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
        val world = location.world
        val nearbyEntities = world!!.getNearbyEntities(location, 0.0, 2.0, 0.0)
        for (armorStand in nearbyEntities.filterIsInstance<ArmorStand>()) {
            armorStand.remove()
            return
        }
    }
    fun release(player: Player, teamname: String, itemname: String) {
        val teamData = Data.DataManager.teamDataMap[teamname]
        if (teamData?.opening == null) {
            PlayerSend().errormessage("${ChatColor.RED}鉱石を破壊してお金をゲットしてください", player)
            Point().add(player, 30, false)
        } else {
            teamData.opening = true
            GUI().selectGUI(player)
            PlayerSend().teamGiveEffect(player, itemname, null, null, 0, 0)
        }
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
        PlayerSend().teamGiveEffect(player, itemname, null, null, 0, 0)
    }
    fun effect(player: Player, itemName: String, potion: PotionEffectType, time: Int, level: Int) {
        val entity = Data.DataManager.teamDataMap[GET().teamName(player)]?.entities?.lastOrNull() as? LivingEntity ?: return
        entity.addPotionEffect(PotionEffect(potion, time * 20, level - 1))
        PlayerSend().teamGiveEffect(player, itemName, null, null, 0, 0)
    }
    fun kill(mob: Villager) {
        Shop().deletename(mob.location)
        val winTeam: String? = when (mob.location.add(0.0, -1.0, 0.0).block.type) {
            Material.RED_WOOL -> "blue"
            Material.BLUE_WOOL -> "red"
            else -> null
        }
        winTeam?.let { GameSystem().gameend(it) }
    }
}
