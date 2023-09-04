package com.github.Ringoame196.Entity

import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.Fireball

class Blaze {
    fun attack() {
        for (blaze in Data.DataManager.gameData.blaze) {
            if (Bukkit.getWorld("BATTLE")?.entities?.contains(blaze) == false) {
                Data.DataManager.gameData.blaze.remove(blaze)
                continue
            }
            val target = GET().getNearestEntityOfType(blaze.location, EntityType.ZOMBIE, 100.0, null)

            if (target != null) {
                val fireball = blaze.launchProjectile(Fireball::class.java)
                val targetDirection = target.location.subtract(blaze.location).toVector().normalize()
                fireball.direction = targetDirection
                fireball.yield = 2.0F
            }
        }
    }
}
