package com.github.Ringoame196.Game

import com.github.Ringoame196.Entity.ArmorStand
import com.github.Ringoame196.Entity.Zombie
import com.github.Ringoame196.GUI
import com.github.Ringoame196.Player
import com.github.Ringoame196.RandomChest
import com.github.Ringoame196.Shop
import com.github.Ringoame196.data.Data
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material

class Map {
    private val mapNumber = Scoreboard().getValue("gameData", "map")
    fun settingOpenGUI(mapName: String, player: org.bukkit.entity.Player) {
        when (mapName) {
            "チュートリアルマップ" -> GUI().locationtutorialMap(player)
            "もちもちマップ" -> GUI().locationMotimotiMap(player)
        }
    }
    fun selectworld(player: org.bukkit.entity.Player) {
        val gui = Bukkit.createInventory(null, 9, "${ChatColor.DARK_GREEN}設定画面[BATTLEGUI]")
        GUI().guiItem(gui, 0, Material.CHEST, "チュートリアルマップ", "", true)
        GUI().guiItem(gui, 1, Material.CHEST, "もちもちマップ", "", true)
        GUI().guiItem(gui, 2, Material.CHEST, "タイマンマップ", "", true)
        player.openInventory(gui)
    }
    fun getMapName(): String {
        return when (mapNumber) {
            0 -> "未設定"
            1 -> "チュートリアルマップ"
            2 -> "もちもちマップ"
            3 -> "タイマン"
            else -> {
                Scoreboard().set("gameData", "map", 0)
                "未設定"
            }
        }
    }
    fun mapSetting() {
        when (mapNumber) {
            1 -> {
                Shop().summon(Data.DataManager.LocationData.redshop, "red")
                Shop().summon(Data.DataManager.LocationData.blueshop, "blue")

                val location = Data.DataManager.LocationData.randomChest?.clone()
                RandomChest().replenishment(location!!, null)
                location.add(0.5, 0.0, 0.5)
                val armorStand = location.let { ArmorStand().summon(it, "") }
                Data.DataManager.gameData.randomChestTitle.add(armorStand)
            }
            2 -> {
                Shop().summon(Data.DataManager.LocationData.mredshop, "red")
                Shop().summon(Data.DataManager.LocationData.mblueshop, "blue")

                val location1 = Data.DataManager.LocationData.mrandomChest1?.clone()
                val location2 = Data.DataManager.LocationData.mrandomChest2?.clone()
                RandomChest().replenishment(location1!!, location2)
                location1.add(0.5, 0.0, 0.5)
                var armorStand = location1.let { ArmorStand().summon(it, "") }
                Data.DataManager.gameData.randomChestTitle.add(armorStand)

                location2?.add(0.5, 0.0, 0.5)
                armorStand = location2.let { ArmorStand().summon(it!!, "") }
                Data.DataManager.gameData.randomChestTitle.add(armorStand)
            }
        }
    }
    fun summonSorting(function: String, player: org.bukkit.entity.Player) {
        when (mapNumber) {
            1 -> Zombie().glassSummon(player, function)
            2 -> Zombie().randomSummon(player, function)
        }
    }
    fun randomChest() {
        when (mapNumber) {
            1 -> RandomChest().replenishment(Data.DataManager.LocationData.randomChest!!, null)
            2 -> RandomChest().replenishment(Data.DataManager.LocationData.mrandomChest1!!, Data.DataManager.LocationData.mrandomChest2!!)
        }
    }
}