package com.github.Ringoame196.Entity

import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Allay
import org.bukkit.inventory.ItemStack

class Minion {
    fun summon(location: Location, team: String?) {
        val minion: Allay? = location.world?.spawn(location, Allay::class.java)
        minion?.setAI(false)
        minion?.customName = "${ChatColor.YELLOW}ミニオン"
        minion?.scoreboardTags?.add("redPet")
        minion?.scoreboardTags?.add("red")
        minion?.isCustomNameVisible = true
        minion?.equipment?.setItemInMainHand(ItemStack(Material.IRON_PICKAXE))
    }
}
