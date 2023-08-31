package com.github.Ringoame196.data

class Data {
    object DataManager {
        val teamDataMap: MutableMap<String?, TeamData> = mutableMapOf()
        val LocationData = LocationData()
        var gameData = Gamedata()
    }
}
