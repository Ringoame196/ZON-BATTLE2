package com.github.Ringoame196.Entity

import org.bukkit.entity.Player
import org.bukkit.entity.Wolf

class Wolf {
    fun summon(player: Player) {
        val world = player.world
        val location = player.getLocation()
        val wolf: Wolf = world.spawn(location, org.bukkit.entity.Wolf::class.java)
        wolf.owner = player
    }
}
