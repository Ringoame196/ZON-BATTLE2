package com.github.Ringoame196.data

import com.github.Ringoame196.randomChest
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

data class LocationData(
    var redshop: Location? = null,
    var blueshop: Location? = null,
    var redspawn: Location? = null,
    var bluespawn: Location? = null,
    var randomChest: Location? = null
) {
    fun saveToFile(filePath: String) {
        val yamlConfiguration = YamlConfiguration()

        fun createSectionFromLocation(location: Location?): ConfigurationSection? {
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

        yamlConfiguration.set("redshop", createSectionFromLocation(redshop))
        yamlConfiguration.set("blueshop", createSectionFromLocation(blueshop))
        yamlConfiguration.set("redspawn", createSectionFromLocation(redspawn))
        yamlConfiguration.set("bluespawn", createSectionFromLocation(bluespawn))
        yamlConfiguration.set("randomChest", createSectionFromLocation(randomChest))

        try {
            yamlConfiguration.save(File(filePath))
        } catch (e: IOException) {
            println("Error while saving data: ${e.message}")
        }
    }

    fun loadLocationDataFromYaml(filePath: String) {
        val yaml = YamlConfiguration.loadConfiguration(File(filePath))
        val locationData = Data.DataManager.LocationData

        fun getLocationFromSection(section: ConfigurationSection): Location? {
            val worldName = section.getString("world") ?: return null
            val world = Bukkit.getWorld(worldName) ?: return null

            val x = section.getDouble("x")
            val y = section.getDouble("y")
            val z = section.getDouble("z")
            val yaw = section.getDouble("yaw").toFloat()
            val pitch = section.getDouble("pitch").toFloat()

            return Location(world, x, y, z, yaw, pitch)
        }

        val redshopSection = yaml.getConfigurationSection("redshop")
        if (redshopSection != null) {
            locationData.redshop = getLocationFromSection(redshopSection)
        }

        val blueshopSection = yaml.getConfigurationSection("blueshop")
        if (blueshopSection != null) {
            locationData.blueshop = getLocationFromSection(blueshopSection)
        }

        val redspawnSection = yaml.getConfigurationSection("redspawn")
        if (redspawnSection != null) {
            locationData.redspawn = getLocationFromSection(redspawnSection)
        }

        val bluespawnSection = yaml.getConfigurationSection("bluespawn")
        if (bluespawnSection != null) {
            locationData.bluespawn = getLocationFromSection(bluespawnSection)
        }

        val randomChestSection = yaml.getConfigurationSection("randomChest")
        if (randomChestSection != null) {
            locationData.randomChest = getLocationFromSection(randomChestSection)
        }
    }
}
