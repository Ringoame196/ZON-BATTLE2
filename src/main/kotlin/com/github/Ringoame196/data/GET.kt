package com.github.Ringoame196.data

import com.github.Ringoame196.Game.Scoreboard
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.Villager

class GET {
    fun teamName(player: Player): String? {
        return player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name
    }

    fun joinTeam(player: Player): Boolean {
        val jointeam = when (teamName(player)) {
            "red" -> true
            "blue" -> true
            else -> false
        }
        return jointeam
    }

    fun maxHP(shop: Villager): Double? {
        return shop.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value
    }

    fun hp(shop: Villager): Double {
        return shop.health
    }

    fun opposingTeamname(teamName: String): String? {
        val opoposingTeamname = when (teamName) {
            "red" -> "blue"
            "blue" -> "red"
            else -> null
        }
        return opoposingTeamname
    }

    fun status(): Boolean {
        return Data.DataManager.gameData.status
    }

    fun locationTitle(location: org.bukkit.Location?): String {
        if (location == null) {
            return "null"
        }

        val x = location.x.toInt()
        val y = location.y.toInt()
        val z = location.z.toInt()

        return "x:$x,y:$y,z:$z"
    }

    fun minutes(time: Int): String {
        val minutes = time / 60 + 0
        val seconds = time % 60 + 0
        return "${minutes}分${seconds}秒"
    }

    fun owner(entity: Entity): Player? {
        for (tag in entity.scoreboardTags) {
            if (!tag.contains("owner:")) {
                continue
            }
            val name = tag.replace("owner:", "")
            val player = Bukkit.getPlayer(name) ?: return null
            return player
        }
        return null
    }
    fun shop(entity: Entity): Boolean {
        return entity.scoreboardTags.contains("shop")
    }
    fun getNearestEntityOfType(location: Location, target: EntityType?, radius: Double): Entity? {
        var nearestEntity: Entity? = null
        var nearestDistanceSquared = Double.MAX_VALUE

        for (entity in location.world!!.getNearbyEntities(location, radius, radius, radius)) {
            if (entity.type == target) {
                if (entity is Player && entity.gameMode != GameMode.SURVIVAL) { continue }
                if (entity is Villager && !GET().shop(entity)) { continue }
                val distanceSquared = entity.location.distanceSquared(location)

                if (distanceSquared < nearestDistanceSquared) {
                    nearestDistanceSquared = distanceSquared
                    nearestEntity = entity
                }
            }
        }

        return nearestEntity
    }
    fun getTeamRevivalTime(teamName: String): Int {
        return Scoreboard().getValue("RedTeam", "復活時間") ?: 5
    }
    fun getTeamScoreName(teamName: String): String {
        when (teamName) {
            "red" -> return "RedTeam"
            "blue" -> return "BlueTeam"
        }
        return null.toString()
    }
}
