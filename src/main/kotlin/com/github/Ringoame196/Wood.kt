package com.github.Ringoame196

import com.github.Ringoame196.Game.GameSystem
import com.github.Ringoame196.data.GET
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class Wood {
    fun blockBreak(e: org.bukkit.event.Event, player: Player, block: Block, plugin: Plugin) {
        player.inventory.addItem(ItemStack(Material.STICK))
        val team = GET().teamName(player) ?: return
        GameSystem().adventure(e, player)
        val cooltime = GET().getTeamRevivalTime(GET().getTeamScoreName(team))
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f)
        Block().revival(plugin, block.location, cooltime, block.type, block.blockData)
    }
}
