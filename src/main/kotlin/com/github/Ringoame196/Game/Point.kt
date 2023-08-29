package com.github.Ringoame196.Game

import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import com.github.Ringoame196.data.TeamData
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class Point {
    fun set(player: Player, setpoint: Int) {
        Data.DataManager.playerDataMap[player.uniqueId]?.point = setpoint
    }
    fun add(player: Player, add: Int, change: Boolean) {
        var addpoint = add
        if (change) {
            if (GET().teamName(player) == "blue" && Data.DataManager.gameData.shortage) {
                addpoint *= 1.5.toInt()
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
    fun ore(e: org.bukkit.event.Event, player: Player, block: Block, plugin: Plugin) {
        val team = GET().teamName(player)
        val blockType = block.type
        val blockData = block.blockData
        GameSystem().adventure(e, player)
        var cooltime = Data.DataManager.teamDataMap.getOrPut(team) { TeamData() }.blockTime
        val point: Int
        when (blockType) {
            Material.COAL_ORE -> point = 1
            Material.IRON_ORE -> point = 10
            Material.GOLD_ORE -> point = 20
            Material.DIAMOND_ORE -> {
                point = 100
                cooltime = 90 // ダイヤモンドだけ別時間
            }
            Material.BEDROCK -> {
                point = 10000
                cooltime = -1
            }
            else -> return
        }
        add(player, point, true)
        com.github.Ringoame196.Block().revival(plugin, block.location, cooltime, blockType, blockData)
    }

    fun purchase(player: Player, price: String): Boolean {
        val priceInt: Int = price.replace("p", "").toInt()
        val point = GET().point(player)
        return if (priceInt > point) {
            com.github.Ringoame196.Player().errormessage("${ChatColor.RED}" + (priceInt - point) + "ポイント足りません", player)
            false
        } else {
            player.playSound(player, Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f)
            remove(player, priceInt)
            true
        }
    }
}
