package com.github.Ringoame196

import org.bukkit.entity.Player

class player {
    fun kill(killer: Player) {
        point().add(killer, 300, true)
    }
}
