package com.github.Ringoame196

import com.github.Ringoame196.Game.Scoreboard
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import org.bukkit.Sound

class ParticipatingPlayer {
    fun message(message: String) {
        for (loopPlayer in Data.DataManager.gameData.participatingPlayer) {
            loopPlayer.sendMessage(message)
        }
    }
    fun sound(sound: Sound) {
        for (player in Data.DataManager.gameData.participatingPlayer) {
            player.playSound(player.location, sound, 1f, 1f)
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
            Scoreboard().set("participatingPlayer", player.name, 0)
        } else {
            Scoreboard().deleteValue("participatingPlayer", player.name)
        }
    }
}
