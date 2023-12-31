package com.github.Ringoame196.Game

import com.github.Ringoame196.Entity.ArmorStand
import com.github.Ringoame196.Entity.Shop
import com.github.Ringoame196.Entity.Zombie
import com.github.Ringoame196.GUI
import com.github.Ringoame196.RandomChest
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.plugin.Plugin

class Map {
    fun mapNamber(): Int {
        return Scoreboard().getValue("gameData", "map")
    }
    fun selectworld(player: org.bukkit.entity.Player) {
        val gui = Bukkit.createInventory(null, 9, "${ChatColor.DARK_GREEN}設定画面[BATTLEGUI]")
        for (i in 1 until getMapName().size) {
            GUI().guiItem(gui, i - 1, Material.CHEST, getMapName()[i], "", true)
        }
        player.openInventory(gui)
    }
    fun getMapName(): MutableList<String> {
        return mutableListOf<String>(
            "null",
            "tutorialmap",
            "motimotimap",
            "timanmap",
            "apartmentmap"
        )
    }
    fun resetMapName() {
        if (mapNamber() > getMapName().size - 1) {
            Scoreboard().set("gameData", "map", 0)
        }
    }
    fun mapSetting() {
        val locationData = Data.DataManager.LocationData
        Shop().summon(locationData.redshop, "red")
        Shop().summon(locationData.blueshop, "blue")

        val location1 = locationData.randomChest1?.clone()
        val location2 = locationData.randomChest2?.clone()
        RandomChest().replenishment(location1!!, location2)
        val armorStand1 = location1.let { ArmorStand().summon(it, "") }
        Data.DataManager.gameData.randomChestTitle.add(armorStand1)

        if (location2 != null) {
            val armorStand2 = location2.let { it.let { it1 -> ArmorStand().summon(it1, "") } }
            Data.DataManager.gameData.randomChestTitle.add(armorStand2)
        }

        if (mapNamber() == 3 || mapNamber() == 4) {
            Scoreboard().set("gameData", "timeLimit", 15 * 60)
        }
    }
    fun summonSorting(function: String, player: org.bukkit.entity.Player, plugin: Plugin) {
        when (mapNamber()) {
            1 -> Zombie().glassSummon(player, function, plugin)
            2 -> Zombie().randomSummon(player, function, plugin)
            3 -> Zombie().randomSummon(player, function, plugin)
            4 -> Zombie().randomSummon(player, function, plugin)
        }
    }
    fun randomChest() {
        (Data.DataManager.LocationData.randomChest1)?.let { RandomChest().replenishment(it, Data.DataManager.LocationData.randomChest2) }
    }
    fun randomSummonLocationList(player: org.bukkit.entity.Player): MutableList<Location> {
        val team = GET().teamName(player)
        val locationData = Data.DataManager.LocationData
        val locationList: MutableList<Location> = mutableListOf()
        if (team == "red") {
            locationList.add(locationData.blueZombieSpawnLocation1!!)
            locationList.add(locationData.blueZombieSpawnLocation2!!)
        } else if (team == "blue") {
            locationList.add(locationData.redZombieSpawnLocation1!!)
            locationList.add(locationData.redZombieSpawnLocation2!!)
        }
        if (Data.DataManager.LocationData.redZombieSpawnLocation3 != null) {
            locationList.add(Data.DataManager.LocationData.redZombieSpawnLocation3!!)
        }
        if (Data.DataManager.LocationData.blueZombieSpawnLocation3 != null) {
            locationList.add(Data.DataManager.LocationData.blueZombieSpawnLocation3!!)
        }
        return locationList
    }
}
