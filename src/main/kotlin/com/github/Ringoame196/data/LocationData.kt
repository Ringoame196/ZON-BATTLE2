package com.github.Ringoame196.data

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.Plugin
import java.io.File
import java.io.IOException

data class LocationData(
    var participationSign: Location? = null,

    var redshop: Location? = null,
    var blueshop: Location? = null,

    var redspawn: Location? = null,
    var bluespawn: Location? = null,

    var randomChest1: Location? = null,
    var randomChest2: Location? = null,

    var redZombieSpawnLocation1: Location? = null,
    var redZombieSpawnLocation2: Location? = null,
    var redZombieSpawnLocation3: Location? = null,

    var blueZombieSpawnLocation1: Location? = null,
    var blueZombieSpawnLocation2: Location? = null,
    var blueZombieSpawnLocation3: Location? = null,
) {
    fun save(plugin: Plugin, key: String, location: Location) {
        val filePath = File(plugin.dataFolder, "location.yml")
        val yamlConfiguration = YamlConfiguration.loadConfiguration(filePath)

        // 既存のデータを上書き
        yamlConfiguration.set(key, createSectionFromLocation(location))

        try {
            yamlConfiguration.save(filePath)
        } catch (e: IOException) {
            println("Error while saving data: ${e.message}")
        }
    }

    fun loadParticipationSignSection(plugin: Plugin) {
        val filePath = "${plugin.dataFolder}/location.yml"
        val yaml = YamlConfiguration.loadConfiguration(File(filePath))
        val locationData = Data.DataManager.LocationData
        val participationSignSection = yaml.getConfigurationSection("participationSign")
        if (participationSignSection != null) {
            locationData.participationSign = getLocationFromSection(participationSignSection)
        }
    }
    fun load(plugin: Plugin, mapName: String) {
        val filePath = "${plugin.dataFolder}/location.yml"
        val yaml = YamlConfiguration.loadConfiguration(File(filePath))
        val locationData = Data.DataManager.LocationData

        val sectionsToLoad = listOf(
            "redshop",
            "blueshop",
            "redspawn",
            "bluespawn",
            "randomChest1",
            "randomChest2",
            "redZombieSpawn1",
            "redZombieSpawn2",
            "redZombieSpawn3",
            "blueZombieSpawn1",
            "blueZombieSpawn2",
            "blueZombieSpawn3"
        )

        sectionsToLoad.forEach { sectionName ->
            yaml.getConfigurationSection("$mapName.$sectionName")?.let { section ->
                locationData.getLocationFromSection(section)?.let { location ->
                    when (sectionName) {
                        "redshop" -> locationData.redshop = location
                        "blueshop" -> locationData.blueshop = location
                        "redspawn" -> locationData.redspawn = location
                        "bluespawn" -> locationData.bluespawn = location
                        "randomChest1" -> locationData.randomChest1 = location
                        "randomChest2" -> locationData.randomChest2 = location
                        "redZombieSpawn1" -> locationData.redZombieSpawnLocation1 = location
                        "redZombieSpawn2" -> locationData.redZombieSpawnLocation2 = location
                        "redZombieSpawn3" -> locationData.redZombieSpawnLocation3 = location
                        "blueZombieSpawn1" -> locationData.blueZombieSpawnLocation1 = location
                        "blueZombieSpawn2" -> locationData.blueZombieSpawnLocation2 = location
                        "blueZombieSpawn3" -> locationData.blueZombieSpawnLocation3 = location
                    }
                }
            }
        }
    }
    private fun createSectionFromLocation(location: Location?): ConfigurationSection? {
        val yamlConfiguration = YamlConfiguration()
        if (location == null) {
            return null
        }

        val section = yamlConfiguration.createSection("location")
        section.set("world", location.world?.name)
        section.set("x", location.x)
        section.set("y", location.y)
        section.set("z", location.z)
        section.set("yaw", location.yaw.toDouble())
        section.set("pitch", location.pitch.toDouble())
        return section
    }

    private fun getLocationFromSection(section: ConfigurationSection): Location? {
        val worldName = section.getString("world") ?: return null
        val world = Bukkit.getWorld(worldName) ?: return null

        val x = section.getDouble("x")
        val y = section.getDouble("y")
        val z = section.getDouble("z")
        val yaw = section.getDouble("yaw").toFloat()
        val pitch = section.getDouble("pitch").toFloat()

        return Location(world, x, y, z, yaw, pitch)
    }
}
