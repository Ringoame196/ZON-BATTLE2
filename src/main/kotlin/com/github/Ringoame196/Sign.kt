package com.github.Ringoame196

import com.github.Ringoame196.data.Data
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

class Sign {
    fun make(e: SignChangeEvent) {
        val lines = e.lines
        if (lines[0] == "[BATTLE]") {
            e.setLine(0, "${ChatColor.AQUA}[BATTLE]")
            e.setLine(2, "${ChatColor.YELLOW}クリックで参加")
        }
    }
    fun click(player: Player, block: Block, plugin: Plugin) {
        val sign = block.state as Sign
        if (sign.lines[0] == ("${ChatColor.AQUA}[BATTLE]")) {
            Data.DataManager.gameData.signLocation = block.location
            Team().inAndout(player)
            player.playSound(player, Sound.UI_BUTTON_CLICK, 1f, 1f)
        } else if (sign.lines[0] == "[中央へ行く]") {
            val location = sign.lines[1]
            val coordinates = location.split(",")

            if (coordinates.size != 4) { return }
            val x = coordinates[0].toDoubleOrNull()
            val y = coordinates[1].toDoubleOrNull()
            val z = coordinates[2].toDoubleOrNull()
            val yaw = coordinates[3].toFloatOrNull()
            if (x == null || y == null || z == null || yaw == null) { return }
            player.teleport(Location(player.world, x, y, z, yaw, 0F))
            invincible(player, plugin)
        }
    }
    fun invincible(player: Player, plugin: Plugin) {
        player.addScoreboardTag("invincible")
        // 2秒後にタグを削除するタスクをスケジュール
        object : BukkitRunnable() {
            override fun run() {
                player.scoreboardTags.remove("invincible")
            }
        }.runTaskLater(plugin, 40L) // 1秒は20L
    }
    fun numberdisplay(text: String) {
        val sign = Data.DataManager.gameData.signLocation?.block?.state
        if (sign !is Sign) { return }
        sign.setLine(1, "${ChatColor.GREEN}$text")
        sign.update()
    }
}
