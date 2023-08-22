package com.github.Ringoame196.data

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.inventory.Inventory

data class PlayerData(
    var point: Int = 0,
    var Hoeselect: Int = 0,
    val GUI: Inventory = Bukkit.createInventory(null, 9, "${ChatColor.DARK_GREEN}召喚の杖"),
)
