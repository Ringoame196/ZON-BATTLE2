package com.github.Ringoame196.Entity

import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Fireball
import org.bukkit.entity.Player

class Blaze {
    fun summon(player: Player): Entity {
        val blaze: org.bukkit.entity.Blaze = player.world.spawn(player.location, org.bukkit.entity.Blaze::class.java)
        blaze.scoreboardTags.add("friendship")
        blaze.setAI(false)
        Data.DataManager.gameData.blaze.add(blaze)
        return blaze
    }
    fun attack() {
        for (blaze in Data.DataManager.gameData.blaze) {
            if (Bukkit.getWorld("BATTLE")?.entities?.contains(blaze) == false) {
                Data.DataManager.gameData.blaze.remove(blaze)
                continue
            }
            val target = GET().getNearestEntityOfType(blaze, blaze.location, EntityType.ZOMBIE, null, 100.0, null)

            if (target != null) {
                val fireball = blaze.launchProjectile(Fireball::class.java)
                val targetDirection = target.location.subtract(blaze.location).toVector().normalize()
                fireball.direction = targetDirection
                fireball.yield = 3.0F
            }
        }
    }
}
