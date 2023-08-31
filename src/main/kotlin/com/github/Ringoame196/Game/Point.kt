package com.github.Ringoame196.Game

import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class Point {
    fun set(player: Player, setpoint: Int) {
        Scoreboard().set("point", player.name, setpoint)
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
        val point = Scoreboard().getValue("point", player.name) ?: 0
        val newPoint = point + addpoint
        player.sendMessage("${ChatColor.GREEN}+$addpoint (${newPoint}ポイント)")
        set(player, newPoint)
    }
    fun remove(player: Player, removepoint: Int) {
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
        val point = Scoreboard().getValue("point", player.name) ?: 0
        val newPoint = point - removepoint
        player.sendMessage("${ChatColor.RED}-$removepoint (${newPoint}ポイント)")
        set(player, newPoint)
    }
    fun ore(e: org.bukkit.event.Event, player: Player, block: Block, plugin: Plugin) {
        val team = GET().teamName(player)
        val blockType = block.type
        val blockData = block.blockData
        GameSystem().adventure(e, player)
        var cooltime = GET().getTeamRevivalTime(GET().teamName(player)!!)
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
        com.github.Ringoame196.Block().revival(plugin, block.location, cooltime!!, blockType, blockData)
    }

    fun purchase(player: Player, price: String): Boolean {
        val priceInt: Int = price.replace("p", "").toInt()
        val point = Scoreboard().getValue("point", player.name) ?: 0
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
