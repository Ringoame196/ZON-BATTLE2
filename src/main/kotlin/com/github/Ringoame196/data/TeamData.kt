package com.github.Ringoame196.data

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Mob
import org.bukkit.inventory.Inventory

data class TeamData(
    var blockTime: Int = 5,
    val chest: Inventory = Bukkit.createInventory(null, 27, "${ChatColor.DARK_GREEN}チームチェスト"),
    val entities: MutableList<Mob> = mutableListOf(),
    var opening: Boolean = false,
)
