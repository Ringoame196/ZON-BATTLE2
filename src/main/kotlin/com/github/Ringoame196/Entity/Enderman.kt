package com.github.Ringoame196.Entity

import com.github.Ringoame196.data.TeamLocation
import org.bukkit.entity.Enderman
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie

class Enderman {
    fun summon(player: Player): Entity {
        val mob = player.world.spawn(player.location, Enderman::class.java)
        mob.isAware = true
        return mob
    }
    fun zombieTP(zombie: Zombie) {
        if (zombie.scoreboardTags.contains("red")) {
            zombie.teleport(TeamLocation().redRespawn()!!)
        } else if (zombie.scoreboardTags.contains("blue")) {
            zombie.teleport(TeamLocation().blueRespawn()!!)
        }
    }
}
