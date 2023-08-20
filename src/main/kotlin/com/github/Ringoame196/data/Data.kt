package com.github.Ringoame196.data

import java.util.UUID

class Data {
    object DataManager {
        val teamDataMap: MutableMap<String?, TeamData> = mutableMapOf()
        val playerDataMap: MutableMap<UUID, PlayerData> = mutableMapOf()
        val LocationData = LocationData()
        var gameData = Gamedata()
    }
}
