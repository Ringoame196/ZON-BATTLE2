package com.github.Ringoame196.Entity

import com.github.Ringoame196.GET
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.point
import org.bukkit.Bukkit

class Golem {
    fun Golden() {
        var RedPoint = 0
        var BluePoint = 0
        for (golem in Data.DataManager.gameData.goldenGolem) {
            if (Bukkit.getWorld("BATTLE")?.entities?.contains(golem) == false) {
                Data.DataManager.gameData.goldenGolem.remove(golem)
                continue
            }
            when {
                golem.scoreboardTags.contains("red") -> RedPoint += 10
                golem.scoreboardTags.contains("blue") -> BluePoint += 10
                else -> {}
            }
        }
        for (player in Data.DataManager.gameData.ParticipatingPlayer) {
            when (GET().TeamName(player)) {
                "red" -> if (RedPoint != 0) { point().add(player, RedPoint, false) }
                "blue" -> if (BluePoint != 0) { point().add(player, BluePoint, false) }
            }
        }
    }
}
