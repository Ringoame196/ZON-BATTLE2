package com.github.Ringoame196

import com.github.Ringoame196.Entity.ArmorStand
import com.github.Ringoame196.data.Data
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.data.BlockData
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

class BreakBlock {
    fun revival(plugin: Plugin, location: Location, cool: Int, type: Material, BlockData: BlockData) {
        var cooltime = cool
        val setLocation = location.clone()
        setLocation.block.setType(Material.LIGHT_BLUE_STAINED_GLASS)
        setLocation.add(0.5, -1.0, 0.5).clone()
        val armorStand: org.bukkit.entity.ArmorStand = ArmorStand().summon(setLocation, "")
        armorStand.scoreboardTags.add(type.toString())
        Data.DataManager.gameData.title.add(armorStand)
        object : BukkitRunnable() {
            override fun run() {
                if (!GET().status()) {
                    cooltime = -1
                }
                if (cooltime >= 0) {
                    armorStand.customName = "${ChatColor.GREEN}${GET().minutes(cooltime)}"
                    cooltime--
                } else {
                    location.block.setType(type)
                    location.block.setBlockData(BlockData)
                    armorStand.remove()
                    this.cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, 20L)
    }
    fun deleteRevival() {
        for (armorStand in Data.DataManager.gameData.title) {
            val location = armorStand.location
            location.add(-0.5, 1.0, -0.5)
            if (armorStand.scoreboardTags.contains("DIAMOND_ORE")) {
                location.block.setType(Material.DIAMOND_ORE)
            } else if (armorStand.scoreboardTags.contains("GOLD_ORE")) {
                location.block.setType(Material.GOLD_ORE)
            } else if (armorStand.scoreboardTags.contains("IRON_ORE")) {
                location.block.setType(Material.IRON_ORE)
            } else if (armorStand.scoreboardTags.contains("COAL_BLOCK")) {
                location.block.setType(Material.COAL_BLOCK)
            } else if (armorStand.scoreboardTags.contains("OAK_LOG")) {
                val blockData = Bukkit.createBlockData("minecraft:oak_log[axis=x]")
                location.block.setBlockData(blockData)
            }
        }
    }
}
