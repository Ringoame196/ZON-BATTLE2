package com.github.Ringoame196.data

import com.github.Ringoame196.Game.Scoreboard
import javax.xml.stream.Location

class TeamLocation {
    val map = Scoreboard().getValue("gameData", "map")
    fun redRespawn(): org.bukkit.Location? {
        return Data.DataManager.LocationData.redspawn
    }
    fun blueRespawn(): org.bukkit.Location? {
        return Data.DataManager.LocationData.bluespawn
    }
}
