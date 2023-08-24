package com.github.Ringoame196

import com.github.Ringoame196.data.Data
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
}
