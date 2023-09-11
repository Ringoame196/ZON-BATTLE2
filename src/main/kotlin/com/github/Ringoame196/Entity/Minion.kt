package com.github.Ringoame196.Entity

import com.github.Ringoame196.Block
import com.github.Ringoame196.Give
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Allay
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class Minion {
    fun summon(location: Location, team: String?) {
        val minion: Allay? = location.world?.spawn(location, Allay::class.java)
        minion?.setAI(false)
        minion?.customName = "${ChatColor.YELLOW}ミニオン"
        minion?.scoreboardTags?.add("${team}Pet")
        minion?.scoreboardTags?.add(team)
        minion?.isCustomNameVisible = true
        minion?.equipment?.setItemInMainHand(ItemStack(Material.IRON_PICKAXE))
        Data.DataManager.gameData.minion.add(minion!!)
    }
    fun loopMinion(plugin: Plugin) {
        for (minion in Data.DataManager.gameData.minion) {
            if (Bukkit.getWorld("BATTLE")?.entities?.contains(minion) == false) {
                Data.DataManager.gameData.minion.remove(minion)
                continue
            }
            if (minion.equipment?.itemInMainHand?.type != Material.IRON_PICKAXE) { continue }
            breakore(minion, plugin)
        }
    }
    private fun breakore(minion: Allay, plugin: Plugin) {
        val radius = 3
        val center = minion.location
        var point = 0
        val team = GET().teamName(minion) ?: return
        for (x in -radius..radius) {
            for (y in -radius..radius) {
                for (z in -radius..radius) {
                    val block = center.clone().add(x.toDouble(), y.toDouble(), z.toDouble()).block
                    val blockType = block.type
                    if (blockType == Material.BEDROCK) { continue }
                    GET().orePoint(block.type) ?: continue
                    val orepoint = GET().orePoint(block.type) ?: 0
                    Block().revival(plugin, block.location, GET().cooltime(blockType, team), blockType, block.blockData)
                    point += orepoint
                }
            }
        }
        if (point == 0) { return }
        minion.location.world?.dropItemNaturally(minion.location, Give().point(point))
    }
}
