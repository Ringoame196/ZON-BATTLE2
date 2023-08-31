package com.github.Ringoame196

import com.github.Ringoame196.Game.Point
import com.github.Ringoame196.Game.Scoreboard
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import org.bukkit.ChatColor
import org.bukkit.GameMode
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
        val team = GET().teamName(player)
        val petC: Int = Scoreboard().getValue(GET().getTeamSystemScoreName(team), "petCount")
        when {
            itemType == Material.EMERALD -> {
                money(player, itemName)
                return
            }
            itemName.contains("ゴーレム") -> {
                e.isCancelled = true
                if (petC >= 5) {
                    player.sendMessage("${ChatColor.RED}5体以上召喚はできません")
                    return
                }
                com.github.Ringoame196.Entity.Golem().summon(player, item?.type, itemName)
                Scoreboard().add(GET().getTeamSystemScoreName(team), "petCount", 1)
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
            itemName == "${ChatColor.YELLOW}シュルカー" -> {
                e.isCancelled = true
                if (petC >= 5) {
                    player.sendMessage("${ChatColor.RED}5体以上召喚はできません")
                    return
                }
                val shulker: Shulker = player.world.spawn(player.location, Shulker::class.java)
                shulker.scoreboardTags.add("targetZombie")
                shulker.scoreboardTags.add("${GET().teamName(player)}Pet")
                shulker.isAware = true
                Scoreboard().add(GET().getTeamSystemScoreName(team), "petCount", 1)
            }
            itemName == "${ChatColor.RED}ブレイズ" -> {
                e.isCancelled = true
                if (petC >= 5) {
                    player.sendMessage("${ChatColor.RED}5体以上召喚はできません")
                    return
                }
                val blaze: Blaze = player.world.spawn(player.location, Blaze::class.java)
                blaze.scoreboardTags.add("friendship")
                blaze.setAI(false)
                Data.DataManager.gameData.blaze.add(blaze)
                blaze.scoreboardTags.add("${GET().teamName(player)}Pet")
                Scoreboard().add(GET().getTeamSystemScoreName(team), "petCount", 1)
            }
            itemName == "${ChatColor.YELLOW}チャット" -> GUI().messageBook(player)
            else -> return
        }
        removeitem(player)
    }
    fun money(player: Player, itemName: String) {
        val pointString = itemName.replace("${ChatColor.GREEN}", "").replace("p", "")
        val point = pointString.toIntOrNull() ?: return
        Point().add(player, point, false)
        removeitem(player)
    }
    fun removeitem(player: org.bukkit.entity.Player) {
        if (player.gameMode == GameMode.CREATIVE) { return }
        val itemInHand = player.inventory.itemInMainHand
        val oneItem = itemInHand.clone()
        oneItem.amount = 1
        player.inventory.removeItem(oneItem)
    }
}
