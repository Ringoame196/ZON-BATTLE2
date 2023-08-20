package com.github.Ringoame196

import com.github.Ringoame196.data.Data
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Main : JavaPlugin() {

    override fun onEnable() {
        val eventListener = Events(this)

        server.pluginManager.registerEvents(eventListener, this)

        val dataFolder = dataFolder
        dataFolder.mkdirs()

        val locationFilePath = "$dataFolder/location_data.yml"
        val file = File(locationFilePath)
        if (file.exists()) {
            Data.DataManager.LocationData.loadLocationDataFromYaml(locationFilePath)
        }
        Bukkit.broadcastMessage("${ChatColor.YELLOW}[攻防戦]プラグインが再読込されました")
    }

    override fun onDisable() {
        super.onDisable()
    }
}
