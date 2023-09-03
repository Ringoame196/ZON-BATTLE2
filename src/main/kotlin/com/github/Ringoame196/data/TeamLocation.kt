package com.github.Ringoame196.data

import com.github.Ringoame196.Game.Scoreboard
import javax.xml.stream.Location

class TeamLocation {
    val map = Scoreboard().getValue("gameData", "map")
    fun redRespawn(): org.bukkit.Location? {
        when (map) {
            1 -> return Data.DataManager.LocationData.redspawn
            2 -> return Data.DataManager.LocationData.mredspawn
            3 -> return Data.DataManager.LocationData.tmredspawn
        }
        return null
    }
    fun blueRespawn(): org.bukkit.Location? {
        when (map) {
            1 -> return Data.DataManager.LocationData.bluespawn
            2 -> return Data.DataManager.LocationData.mbluespawn
            3 -> return Data.DataManager.LocationData.tmbluespawn
        }
        return null
    }
}
