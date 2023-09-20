package com.github.Ringoame196.Game

import com.github.Ringoame196.data.GET
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin

class Point {
    fun set(player: Player, setpoint: Int) {
        Scoreboard().set("point", player.name, setpoint)
        player.level = setpoint
        player.exp = 1.0f
    }
    fun add(player: Player, add: Int, change: Boolean) {
        var addpoint = add
        val teamMagnification = when (GET().teamName(player)) {
            "red" -> Scoreboard().getValue("RedTeamSystem", "magnification")
            "blue" -> Scoreboard().getValue("BlueTeamSystem", "magnification")
            else -> 1
        }
        if (change) {
            addpoint *= Scoreboard().getValue("gameData", "magnification")
            addpoint *= teamMagnification
        }
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
        val point = Scoreboard().getValue("point", player.name) ?: 0
        val newPoint = point + addpoint
        com.github.Ringoame196.Entity.Player().sendActionBar(player, "${ChatColor.GREEN}+$addpoint")
        set(player, newPoint)
    }
    fun remove(player: Player, removepoint: Int) {
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f)
        val point = Scoreboard().getValue("point", player.name)
        val newPoint = point - removepoint
        com.github.Ringoame196.Entity.Player().sendActionBar(player, "${ChatColor.RED}-$removepoint")
        set(player, newPoint)
    }
    fun ore(e: org.bukkit.event.Event, player: Player, block: Block, plugin: Plugin) {
        val team = GET().teamName(player) ?: return
        val blockType = block.type
        val blockData = block.blockData
        GameSystem().adventure(e, player)
        val coolTime = GET().cooltime(blockType, team)
        val point = GET().orePoint(blockType) ?: return
        add(player, point, true)
        com.github.Ringoame196.Block().revival(plugin, block.location, coolTime, blockType, blockData)
    }

    fun purchase(player: Player, price: String): Boolean {
        val priceInt: Int = price.replace("p", "").toInt()
        val point = Scoreboard().getValue("point", player.name) ?: 0
        return if (priceInt > point) {
            com.github.Ringoame196.Entity.Player()
                .errormessage("${ChatColor.RED}" + (priceInt - point) + "ポイント足りません", player)
            false
        } else {
            player.playSound(player, Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f)
            remove(player, priceInt)
            true
        }
    }
    fun fountain() {
        for (loopPlayer in Bukkit.getOnlinePlayers()) {
            val join: Int = Scoreboard().getValue("participatingPlayer", loopPlayer.name)
            if (join != 0) {
                if (loopPlayer.location.add(0.0, -1.0, 0.0).block.type != Material.SCULK) { continue }
                val team = GET().teamName(loopPlayer)
                val point = Scoreboard().getValue(GET().getTeamSystemScoreName(team), "fountainpoint")
                Point().add(loopPlayer, point, true)
            }
        }
    }
}
