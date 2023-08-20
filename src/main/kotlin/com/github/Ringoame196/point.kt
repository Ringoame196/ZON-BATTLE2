package com.github.Ringoame196

import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.TeamData
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class point {
    fun set(player: Player, setpoint: Int) {
        Data.DataManager.playerDataMap[player.uniqueId]?.point = setpoint
    }
    fun add(player: Player, add: Int, change: Boolean) {
        var addpoint = add
        if (change) {
            if (GET().TeamName(player) == "blue" && Data.DataManager.gameData.shortage) {
                addpoint *= 2
            }
            addpoint *= Data.DataManager.gameData.magnification
        }
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
        val point = GET().point(player) + addpoint
        player.sendMessage("${ChatColor.GREEN}+$addpoint (${point}ポイント)")
        set(player, point)
    }
    fun remove(player: Player, removepoint: Int) {
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
        var point = GET().point(player)
        point -= removepoint
        player.sendMessage("${ChatColor.RED}-$removepoint (${point}ポイント)")
        set(player, point)
    }
    fun ore(e: org.bukkit.event.Event, player: Player, block: Block, team: String?, plugin: Plugin) {
        val block_type = block.type
        val blockData = block.blockData
        GameSystem().adventure(e, player)
        var cooltime = Data.DataManager.teamDataMap.getOrPut(team) { TeamData() }.blockTime
        val point: Int
        when (block_type) {
            Material.COAL_ORE -> point = 1
            Material.IRON_ORE -> point = 10
            Material.GOLD_ORE -> point = 20
            Material.DIAMOND_ORE -> {
                point = 100
                cooltime = 180 // ダイヤモンドだけ別時間
            }
            Material.BEDROCK -> {
                point = 10000
                cooltime = 1
            }
            else -> return
        }
        point().add(player, point, true)
        BreakBlock().revival(plugin, block.location, cooltime, block_type, blockData)
    }

    fun purchase(player: Player, price: String): Boolean {
        val price_int: Int = price.replace("p", "").toInt()
        val point = GET().point(player)
        return if (price_int > point) {
            PlayerSend().errormessage("${ChatColor.RED}" + (price_int - point) + "ポイント足りません", player)
            false
        } else {
            player.playSound(player, Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f)
            remove(player, price_int)
            true
        }
    }
    fun NotAppropriate(item: ItemStack, block: Block, e: BlockDamageEvent) {

        when (block.type) {
            Material.DIAMOND_ORE -> when (item.type) {
                Material.NETHERITE_PICKAXE -> {}
                Material.DIAMOND_PICKAXE -> {}
                Material.IRON_PICKAXE -> {}
                else -> e.isCancelled = true
            }

            Material.GOLD_ORE -> when (item.type) {
                Material.NETHERITE_PICKAXE -> {}
                Material.DIAMOND_PICKAXE -> {}
                Material.IRON_PICKAXE -> {}
                else -> e.isCancelled = true
            }

            Material.IRON_ORE -> when (item.type) {
                Material.NETHERITE_PICKAXE -> {}
                Material.DIAMOND_PICKAXE -> {}
                Material.IRON_PICKAXE -> {}
                Material.STONE_PICKAXE -> {}
                else -> e.isCancelled = true
            }

            else -> {}
        }
    }
}
