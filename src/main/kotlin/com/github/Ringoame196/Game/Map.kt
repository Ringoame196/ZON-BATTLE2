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
    private val mapNumber = Scoreboard().getValue("gameData", "map")
    fun selectworld(player: org.bukkit.entity.Player) {
        val gui = Bukkit.createInventory(null, 9, "${ChatColor.DARK_GREEN}設定画面[BATTLEGUI]")
        GUI().guiItem(gui, 0, Material.CHEST, "tutorialmap", "", true)
        GUI().guiItem(gui, 1, Material.CHEST, "motimotimap", "", true)
        GUI().guiItem(gui, 2, Material.CHEST, "timanmap", "", true)
        player.openInventory(gui)
    }
    fun getMapName(): String {
        return when (mapNumber) {
            1 -> "チュートリアルマップ"
            2 -> "もちもちマップ"
            3 -> "タイマン"
            else -> "未設定"
        }
    }
    fun resetMapName() {
        if (mapNumber > 3) {
            Scoreboard().set("gameData", "map", 0)
        }
    }
    fun mapSetting() {
        when (mapNumber) {
            1 -> {
                Shop().summon(Data.DataManager.LocationData.redshop, "red")
                Shop().summon(Data.DataManager.LocationData.blueshop, "blue")

                val location = Data.DataManager.LocationData.randomChest1?.clone()
                RandomChest().replenishment(location!!, null)
                location.add(0.5, 0.0, 0.5)
                val armorStand = location.let { ArmorStand().summon(it, "") }
                Data.DataManager.gameData.randomChestTitle.add(armorStand)
            }
            2 -> {
                Shop().summon(Data.DataManager.LocationData.redshop, "red")
                Shop().summon(Data.DataManager.LocationData.blueshop, "blue")

                val location1 = Data.DataManager.LocationData.randomChest1?.clone()
                val location2 = Data.DataManager.LocationData.randomChest2?.clone()
                RandomChest().replenishment(location1!!, location2)
                location1.add(0.5, 0.0, 0.5)
                var armorStand = location1.let { ArmorStand().summon(it, "") }
                Data.DataManager.gameData.randomChestTitle.add(armorStand)

                location2?.add(0.5, 0.0, 0.5)
                armorStand = location2.let { ArmorStand().summon(it!!, "") }
                Data.DataManager.gameData.randomChestTitle.add(armorStand)
            }
            3 -> {
                Shop().summon(Data.DataManager.LocationData.redshop, "red")
                Shop().summon(Data.DataManager.LocationData.blueshop, "blue")

                val location = Data.DataManager.LocationData.randomChest1?.clone()
                RandomChest().replenishment(location!!, null)
                location.add(0.5, 0.0, 0.5)
                val armorStand = location.let { ArmorStand().summon(it, "") }
                Data.DataManager.gameData.randomChestTitle.add(armorStand)
                Scoreboard().set("gameData", "timeLimit", 15 * 60)
            }
        }
    }
    fun summonSorting(function: String, player: org.bukkit.entity.Player, plugin: Plugin) {
        when (mapNumber) {
            1 -> Zombie().glassSummon(player, function, plugin)
            2 -> Zombie().randomSummon(player, function, plugin)
            3 -> Zombie().randomSummon(player, function, plugin)
        }
    }
    fun randomChest() {
        when (mapNumber) {
            1 -> RandomChest().replenishment(Data.DataManager.LocationData.randomChest1!!, null)
            2 -> RandomChest().replenishment(Data.DataManager.LocationData.randomChest1!!, Data.DataManager.LocationData.randomChest2!!)
            3 -> RandomChest().replenishment(Data.DataManager.LocationData.randomChest1!!, null)
        }
    }
    fun randomSummonLocationList(player: org.bukkit.entity.Player): MutableList<Location> {
        val team = GET().teamName(player)
        val locationList: MutableList<Location> = mutableListOf()
        when (Scoreboard().getValue("gameData", "map")) {
            2 -> {
                if (team == "red") {
                    locationList.add(Data.DataManager.LocationData.blueZombieSpawnLocation1!!)
                    locationList.add(Data.DataManager.LocationData.blueZombieSpawnLocation2!!)
                } else if (team == "blue") {
                    locationList.add(Data.DataManager.LocationData.redZombieSpawnLocation1!!)
                    locationList.add(Data.DataManager.LocationData.redZombieSpawnLocation2!!)
                }
            }
            3 -> {
                locationList.add(Data.DataManager.LocationData.blueZombieSpawnLocation1!!)
                locationList.add(Data.DataManager.LocationData.blueZombieSpawnLocation2!!)
                locationList.add(Data.DataManager.LocationData.blueZombieSpawnLocation3!!)
            }
        }
        return locationList
    }
}
