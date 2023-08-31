package com.github.Ringoame196

import com.github.Ringoame196.Game.Scoreboard
import com.github.Ringoame196.data.GET
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Sound

class ParticipatingPlayer {
    val scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard
    fun message(message: String) {
        for (loopPlayer in Bukkit.getOnlinePlayers()) {
            val join: Int = Scoreboard().getValue("participatingPlayer", loopPlayer.name) ?: 0
            if (join != 0) {
                loopPlayer.sendMessage(message)
            }
        }
    }
    fun sound(sound: Sound) {
        for (loopPlayer in Bukkit.getOnlinePlayers()) {
            val join: Int = Scoreboard().getValue("participatingPlayer", loopPlayer.name) ?: 0
            if (join != 0) {
                loopPlayer.playSound(loopPlayer.location, sound, 1f, 1f)
            }
        }
    }
    fun inAndout(player: org.bukkit.entity.Player) {
        val join: Int = Scoreboard().getValue("participatingPlayer", player.name) ?: 0

        if (GET().status()) {
            Player().errormessage("ゲームが終わるまでしばらくお待ち下さい", player)
            return
        }

        val message: String = if (join != 0) {
            "退出"
        } else {
            "参加"
        }

        if (message == "参加") {
            Scoreboard().set("participatingPlayer", player.name, 1)
        } else {
            Scoreboard().deleteValue("participatingPlayer", player.name)
            player.sendTitle("", "${ChatColor.YELLOW}退出しました")
        }
        val size = Scoreboard().getSize("participatingPlayer")
        message("${ChatColor.AQUA}[$message]${player.name} (${size}人)")
    }
}
