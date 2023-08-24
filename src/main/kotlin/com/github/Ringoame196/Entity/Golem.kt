package com.github.Ringoame196.Entity

import com.github.Ringoame196.GameSystem
import com.github.Ringoame196.Point
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent

class Golem {
    fun golden() {
        var redPoint = 0
        var bluePoint = 0
        for (golem in Data.DataManager.gameData.goldenGolem) {
            if (Bukkit.getWorld("BATTLE")?.entities?.contains(golem) == false) {
                Data.DataManager.gameData.goldenGolem.remove(golem)
                continue
            }
            when {
                golem.scoreboardTags.contains("red") -> redPoint += 10
                golem.scoreboardTags.contains("blue") -> bluePoint += 10
                else -> {}
            }
        }
        for (player in Data.DataManager.gameData.participatingPlayer) {
            when (GET().teamName(player)) {
                "red" -> if (redPoint != 0) { Point().add(player, redPoint, false) }
                "blue" -> if (bluePoint != 0) { Point().add(player, bluePoint, false) }
            }
        }
    }
    fun guardPlayerAttack(damager: Entity, e: EntityDamageByEntityEvent) {
        if (damager !is Player) { return }
        GameSystem().adventure(e, damager)
    }
}
