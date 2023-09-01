package com.github.Ringoame196.data

import javax.xml.stream.Location

class TeamLocation {
    fun redRespawn(map: String): org.bukkit.Location? {
        when (map) {
            "map1" -> return Data.DataManager.LocationData.redspawn
            "map2" -> return Data.DataManager.LocationData.mredspawn
        }
        return null
    }
    fun blueRespawn(map: String): org.bukkit.Location? {
        when (map) {
            "map1" -> return Data.DataManager.LocationData.bluespawn
            "map2" -> return Data.DataManager.LocationData.mbluespawn
        }
        return null
    }
}
