package com.github.Ringoame196.data

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.block.Block
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Blaze
import org.bukkit.entity.Golem
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.inventory.Inventory

class Gamedata {
    var status = false
    var time = 0
    var participatingPlayer: MutableList<Player> = mutableListOf()
    var signLocation: org.bukkit.Location? = null
    var randomChestTitle: ArmorStand? = null
    var zombie: MutableList<Zombie> = mutableListOf()
    var fence: MutableList<Block> = mutableListOf()
    var title: MutableList<ArmorStand> = mutableListOf()
    var goldenGolem: MutableList<Golem> = mutableListOf()
    var shortage: Boolean = false
    var magnification = 1
    val bossBar: BossBar = Bukkit.createBossBar("ゾンビ解放まで", BarColor.BLUE, BarStyle.SEGMENTED_10)
    val opchest: Inventory = Bukkit.createInventory(null, 27, "${ChatColor.RED}OP用チェスト(一時)")
    var blaze: MutableList<Blaze> = mutableListOf()
    var playMap: String = "map1"
}
