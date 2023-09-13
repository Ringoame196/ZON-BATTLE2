package com.github.Ringoame196

import com.github.Ringoame196.Entity.ArmorStand
import com.github.Ringoame196.Game.GameSystem
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

class Block {
    fun revival(plugin: Plugin, location: Location, cool: Int, type: Material, blockData: BlockData) {
        var cooltime = cool
        val setLocation = location.clone()
        setLocation.block.type = Material.LIGHT_BLUE_STAINED_GLASS
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
                    location.block.setBlockData(blockData)
                    armorStand.remove()
                    this.cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, 20L)
    }
    fun deleteRevival() {
        val entitys = Bukkit.getWorld("BATTLE")?.entities ?: return
        for (armorStand in entitys) {
            if (armorStand !is org.bukkit.entity.ArmorStand) { continue }
            val location = armorStand.location.clone().add(-0.5, 1.0, -0.5)
            val blockType = when {
                armorStand.scoreboardTags.contains("DIAMOND_ORE") -> Material.DIAMOND_ORE
                armorStand.scoreboardTags.contains("GOLD_ORE") -> Material.GOLD_ORE
                armorStand.scoreboardTags.contains("IRON_ORE") -> Material.IRON_ORE
                armorStand.scoreboardTags.contains("COAL_ORE") -> Material.COAL_ORE
                armorStand.scoreboardTags.contains("OAK_LOG") -> Material.OAK_LOG
                armorStand.scoreboardTags.contains("DAYLIGHT_DETECTOR") -> Material.DAYLIGHT_DETECTOR
                else -> continue // 不明なタグの場合、スキップ
            }
            armorStand.remove()
            location.block.setType(blockType)
        }
    }
    fun notAppropriate(item: ItemStack, block: Block, e: BlockDamageEvent) {
        if (block.world.name != "BATTLE") { return }
        when (block.type) {
            Material.DIAMOND_ORE -> when (item.type) {
                Material.NETHERITE_PICKAXE -> {}
                Material.DIAMOND_PICKAXE -> {}
                Material.IRON_PICKAXE -> {}
                else -> e.isCancelled = true
            }

            Material.GOLD_ORE -> when (item.type) {
                Material.NETHERITE_PICKAXE -> {}
                Material.DIAMOND_PICKAXE -> {}
                Material.IRON_PICKAXE -> {}
                else -> e.isCancelled = true
            }

            Material.IRON_ORE -> when (item.type) {
                Material.NETHERITE_PICKAXE -> {}
                Material.DIAMOND_PICKAXE -> {}
                Material.IRON_PICKAXE -> {}
                Material.STONE_PICKAXE -> {}
                else -> e.isCancelled = true
            }

            else -> {}
        }
    }
    fun operationBlock(block: Block, player: Player, e: PlayerInteractEvent) {
        val material = block.type
        when (material) {
            Material.BARREL, Material.ANVIL, Material.GRINDSTONE -> GameSystem().adventure(e, player)
            else -> {}
        }
    }
}
