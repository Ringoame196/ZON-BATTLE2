package com.github.Ringoame196

import com.github.Ringoame196.Entity.Zombie
import com.github.Ringoame196.data.Data
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.IronGolem
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class itemClick {
    fun system(player: Player, item: ItemStack?, block: Block?, e: PlayerInteractEvent) {
        val item_name = item?.itemMeta?.displayName.toString()
        val item_type = item?.type
        when {
            item_name.contains("[ゾンビ召喚]") -> {
                if (player.location.subtract(0.0, 1.0, 0.0).block.type != Material.GLASS) {
                    player.sendMessage("${ChatColor.RED}ガラスの上で実行してください")
                    return
                }
                Zombie().summonSystem(player, item_name)
            }
            item_type == Material.EMERALD -> {
                money(player, item_name)
            }
            item_name.contains("ゴーレム") -> {
                e.isCancelled = true
                summon_golem(player, item?.type, item_name)
            }
            item_type == Material.COMMAND_BLOCK && item_name == "ゲーム設定" -> {
                e.isCancelled = true
                GUI().gamesettingGUI(player)
                return
            }
            block?.type == Material.OAK_WALL_SIGN -> {
                e.isCancelled = true
                Sign().click(player, block)
                return
            }
            item_name == "${ChatColor.GREEN}リモートショップ" -> {
                shop().GUI(player)
                e.isCancelled = true
            }
            else -> return
        }
        removeitem(player)
    }
    fun money(player: Player, item_name: String) {
        val point: Int
        point = when (item_name) {
            "${ChatColor.GREEN}10p" -> {
                10
            }
            "${ChatColor.GREEN}100p" -> {
                100
            }
            else -> { return }
        }
        point().add(player, point, false)
    }
    fun summon_golem(player: Player, type: Material?, name: String) {
        val location = player.location
        val golem = location.world?.spawn(location, IronGolem::class.java) ?: return

        golem.customName = name
        golem.isCustomNameVisible = true
        golem.isPlayerCreated = true

        when (type) {
            Material.GOLD_BLOCK -> {
                golem.health = 5.0
                golem.customName = "${ChatColor.RED}ゴールデンゴーレム"
                golem.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, Int.MAX_VALUE, 255))
                golem.scoreboardTags.add(GET().TeamName(player))
                Data.DataManager.gameData.goldenGolem.add(golem)
            }
            Material.DIAMOND_BLOCK -> {
                golem.maxHealth = 300.0
                golem.health = 300.0
                golem.damage(10.0)
            }
            Material.NETHERITE_BLOCK -> {
                golem.maxHealth = 600.0
                golem.health = 600.0
                golem.damage(15.0)
            }
            else -> return
        }
        player.sendMessage("${ChatColor.YELLOW}ゴーレム召喚")
    }
    fun removeitem(player: Player) {
        if (player.gameMode == GameMode.CREATIVE) { return }
        val itemInHand = player.inventory.itemInMainHand
        val oneItem = itemInHand.clone()
        oneItem.amount = 1
        player.inventory.removeItem(oneItem)
    }
}
