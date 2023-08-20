package com.github.Ringoame196.Entity

import com.github.Ringoame196.GET
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.point

class Golem {
    fun Golden() {
        for (golem in Data.DataManager.gameData.goldenGolem) {
            val team = when {
                golem.scoreboardTags.contains("red") -> "red"
                golem.scoreboardTags.contains("blue") -> "blue"
                else -> return
            }
            for (player in Data.DataManager.gameData.ParticipatingPlayer) {
                if (GET().TeamName(player) == team) {
                    point().add(player, 10, true)
                }
            }
        }
    }
}
