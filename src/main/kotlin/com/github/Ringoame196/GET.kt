package com.github.Ringoame196

import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.PlayerData
import org.bukkit.Location
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.entity.Villager

class GET {
    fun TeamName(player: Player): String? {
        val teamName = player.scoreboard.teams.firstOrNull { it.hasEntry(player.name) }?.name
        return teamName
    }
    fun JoinTeam(player: Player): Boolean {
        val jointeam = when (TeamName(player)) {
            "red" -> true
            "blue" -> true
            else -> false
        }
        return jointeam
    }
    fun MaxHP(shop: Villager): Double? {
        val maxHP = shop.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.value
        return maxHP
    }
    fun HP(shop: Villager): Double {
        val HP = shop.health
        return HP
    }
    fun point(player: Player): Int {
        val point = Data.DataManager.playerDataMap.getOrPut(player.uniqueId) { PlayerData() }.point
        return point
    }
    fun OpposingTeamname(TeamName: String): String? {
        val OpoposingTeamname = when (TeamName) {
            "red" -> "blue"
            "blue" -> "red"
            else -> null
        }
        return OpoposingTeamname
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
}
