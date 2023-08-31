package com.github.Ringoame196.data

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Mob
import org.bukkit.inventory.Inventory

data class TeamData(
    val chest: Inventory = Bukkit.createInventory(null, 27, "${ChatColor.DARK_GREEN}チームチェスト"),
    val entities: MutableList<Mob> = mutableListOf(), // ショップを取得
    var opening: Boolean = false,
    var zombieNotification: Boolean = false // ゾンビ通知
)
