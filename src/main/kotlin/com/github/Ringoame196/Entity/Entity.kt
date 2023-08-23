package com.github.Ringoame196.Entity

import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

class Entity {
    fun getNearestEntityOfType(location: Location, target: EntityType?, radius: Double): Entity? {
        var nearestEntity: Entity? = null
        var nearestDistanceSquared = Double.MAX_VALUE

        for (entity in location.world!!.getNearbyEntities(location, radius, radius, radius)) {
            if (entity.type == target) {
                if (entity is Player && entity.gameMode != GameMode.SURVIVAL) { continue }
                val distanceSquared = entity.location.distanceSquared(location)

                if (distanceSquared < nearestDistanceSquared) {
                    nearestDistanceSquared = distanceSquared
                    nearestEntity = entity
                }
            }
        }

        return nearestEntity
    }
}
