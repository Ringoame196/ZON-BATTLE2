package com.github.Ringoame196

import com.github.Ringoame196.Game.Point
import com.github.Ringoame196.data.Data
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Blaze
import org.bukkit.entity.Golem
import org.bukkit.entity.Player
import org.bukkit.entity.Shulker
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class Item {
    fun clickSystem(player: Player, item: ItemStack?, block: Block?, e: PlayerInteractEvent, plugin: Plugin) {
        val itemName = item?.itemMeta?.displayName.toString()
        val itemType = item?.type
        when {
            itemType == Material.EMERALD -> {
                money(player, itemName)
            }
            itemName.contains("ゴーレム") -> {
                e.isCancelled = true
                com.github.Ringoame196.Entity.Golem().summon(player, item?.type, itemName)
            }
            itemType == Material.COMMAND_BLOCK && itemName == "ゲーム設定" -> {
                e.isCancelled = true
                GUI().gamesettingGUI(player)
                return
            }
            block?.type == Material.OAK_WALL_SIGN -> {
                e.isCancelled = true
                Sign().click(player, block, plugin)
                return
            }
            itemName == "${ChatColor.GREEN}リモートショップ" -> {
                Shop().gui(player)
                e.isCancelled = true
            }
            itemName == "${ChatColor.GREEN}テレポート" -> {
                val coordinates = item?.itemMeta?.lore?.get(1)?.split(",")?.mapNotNull { it.toDoubleOrNull() }

                if (coordinates?.size == 3) {
                    val x = coordinates[0]
                    val y = coordinates[1]
                    val z = coordinates[2]

                    val world = Bukkit.getWorld(item.itemMeta?.lore?.get(0)!!) ?: return
                    player.teleport(Location(world, x, y, z))
                    player.sendMessage("${ChatColor.GREEN}テレポートしました")
                }
                e.isCancelled = true
                return
            }
            itemName == "${ChatColor.YELLOW}シュルカー" -> {
                val shulker: Shulker = player.world.spawn(player.location, Shulker::class.java)
                shulker.scoreboardTags.add("targetZombie")
                shulker.isAware = true
            }
            itemName == "${ChatColor.RED}ブレイズ" -> {
                val blaze: Blaze = player.world.spawn(player.location, Blaze::class.java)
                blaze.scoreboardTags.add("friendship")
                blaze.setAI(false)
                Data.DataManager.gameData.blaze.add(blaze)
            }
            itemName == "${ChatColor.YELLOW}チャット" -> GUI().messageBook(player)
            else -> return
        }
        removeitem(player)
    }
    fun money(player: Player, itemName: String) {
        val point: Int = when (itemName) {
            "${ChatColor.GREEN}10p" -> 10
            "${ChatColor.GREEN}100p" -> 100
            "${ChatColor.GREEN}1000p" -> 1000
            else -> { return }
        }
        Point().add(player, point, false)
    }
    fun removeitem(player: org.bukkit.entity.Player) {
        if (player.gameMode == GameMode.CREATIVE) { return }
        val itemInHand = player.inventory.itemInMainHand
        val oneItem = itemInHand.clone()
        oneItem.amount = 1
        player.inventory.removeItem(oneItem)
    }
}
