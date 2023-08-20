package com.github.Ringoame196

import org.bukkit.entity.Entity

class inspection { // 調査
    fun shop(entity: Entity): Boolean {
        var affiliation = false
        if (entity.scoreboardTags.contains("shop")) {
            affiliation = true
        }
        return affiliation
    }
}
