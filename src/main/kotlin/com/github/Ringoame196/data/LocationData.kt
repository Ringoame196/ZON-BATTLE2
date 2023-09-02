package com.github.Ringoame196.data

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException

data class LocationData(
    var participationSign: Location? = null,

    var redshop: Location? = null,
    var blueshop: Location? = null,
    var redspawn: Location? = null,
    var bluespawn: Location? = null,
    var randomChest: Location? = null,

    var mredshop: Location? = null,
    var mblueshop: Location? = null,
    var mredspawn: Location? = null,
    var mbluespawn: Location? = null,
    var mrandomChest1: Location? = null,
    var mrandomChest2: Location? = null,
    var mredZombiespawn1: Location? = null,
    var mredZombiespawn2: Location? = null,
    var mblueZombiespawn1: Location? = null,
    var mblueZombiespawn2: Location? = null,

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
        yamlConfiguration.set("participationSign", createSectionFromLocation(participationSign))
        yamlConfiguration.set("redshop", createSectionFromLocation(redshop))
        yamlConfiguration.set("blueshop", createSectionFromLocation(blueshop))
        yamlConfiguration.set("redspawn", createSectionFromLocation(redspawn))
        yamlConfiguration.set("bluespawn", createSectionFromLocation(bluespawn))
        yamlConfiguration.set("randomChest", createSectionFromLocation(randomChest))

        yamlConfiguration.set("mredshop", createSectionFromLocation(mredshop))
        yamlConfiguration.set("mblueshop", createSectionFromLocation(mblueshop))
        yamlConfiguration.set("mredspawn", createSectionFromLocation(mredspawn))
        yamlConfiguration.set("mbluespawn", createSectionFromLocation(mbluespawn))
        yamlConfiguration.set("mrandomChest1", createSectionFromLocation(mrandomChest1))
        yamlConfiguration.set("mrandomChest2", createSectionFromLocation(mrandomChest2))
        yamlConfiguration.set("mredZombiespawn1", createSectionFromLocation(mredZombiespawn1))
        yamlConfiguration.set("mredZombiespawn2", createSectionFromLocation(mredZombiespawn2))
        yamlConfiguration.set("mblueZombiespawn1", createSectionFromLocation(mblueZombiespawn1))
        yamlConfiguration.set("mblueZombiespawn2", createSectionFromLocation(mblueZombiespawn2))
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
        val participationSignSection = yaml.getConfigurationSection("participationSign")
        if (participationSignSection != null) {
            locationData.participationSign = getLocationFromSection(participationSignSection)
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

        // 第二マップ
        val mredshopSection = yaml.getConfigurationSection("mredshop")
        if (mredshopSection != null) {
            locationData.mredshop = getLocationFromSection(mredshopSection)
        }

        val mblueshopSection = yaml.getConfigurationSection("mblueshop")
        if (mblueshopSection != null) {
            locationData.mblueshop = getLocationFromSection(mblueshopSection)
        }

        val mredspawnSection = yaml.getConfigurationSection("mredspawn")
        if (mredspawnSection != null) {
            locationData.mredspawn = getLocationFromSection(mredspawnSection)
        }

        val mbluespawnSection = yaml.getConfigurationSection("mbluespawn")
        if (mbluespawnSection != null) {
            locationData.mbluespawn = getLocationFromSection(mbluespawnSection)
        }

        val mrandomChestSection1 = yaml.getConfigurationSection("mrandomChest1")
        if (mrandomChestSection1 != null) {
            locationData.mrandomChest1 = getLocationFromSection(mrandomChestSection1)
        }

        val mrandomChestSection2 = yaml.getConfigurationSection("mrandomChest2")
        if (mrandomChestSection2 != null) {
            locationData.mrandomChest2 = getLocationFromSection(mrandomChestSection2)
        }

        val mredZombiespawnSetction1 = yaml.getConfigurationSection("mredZombiespawn1")
        if (mredZombiespawnSetction1 != null) {
            locationData.mredZombiespawn1 = getLocationFromSection(mredZombiespawnSetction1)
        }

        val mredZombiespawnSetction2 = yaml.getConfigurationSection("mredZombiespawn2")
        if (mredZombiespawnSetction2 != null) {
            locationData.mredZombiespawn2 = getLocationFromSection(mredZombiespawnSetction2)
        }

        val mblueZombiespawnSetction1 = yaml.getConfigurationSection("mblueZombiespawn1")
        if (mblueZombiespawnSetction1 != null) {
            locationData.mblueZombiespawn1 = getLocationFromSection(mblueZombiespawnSetction1)
        }

        val mblueZombiespawnSetction2 = yaml.getConfigurationSection("mblueZombiespawn2")
        if (mblueZombiespawnSetction2 != null) {
            locationData.mblueZombiespawn2 = getLocationFromSection(mblueZombiespawnSetction2)
        }
    }
}
