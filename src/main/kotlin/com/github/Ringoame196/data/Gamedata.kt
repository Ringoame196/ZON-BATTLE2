package com.github.Ringoame196.data

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Blaze
import org.bukkit.entity.Golem
import org.bukkit.entity.Zombie
import org.bukkit.inventory.Inventory

class Gamedata {
    var randomChestTitle: MutableList<ArmorStand> = mutableListOf()
    var zombie: MutableList<Zombie> = mutableListOf()
    var title: MutableList<ArmorStand> = mutableListOf()
    var goldenGolem: MutableList<Golem> = mutableListOf()
    val bossBar: BossBar = Bukkit.createBossBar("ゾンビ解放まで", BarColor.BLUE, BarStyle.SEGMENTED_10)
    var blaze: MutableList<Blaze> = mutableListOf()
    val blueTeamChest: Inventory = Bukkit.createInventory(null, 27, "${ChatColor.DARK_GREEN}チームチェスト")
    val redTeamChest: Inventory = Bukkit.createInventory(null, 27, "${ChatColor.DARK_GREEN}チームチェスト")
}
