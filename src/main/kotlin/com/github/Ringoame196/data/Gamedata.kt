package com.github.Ringoame196.data

import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Blaze
import org.bukkit.entity.Golem
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie

class Gamedata {
    var status = false
    var time = 0
    var participatingPlayer: MutableList<Player> = mutableListOf()
    var signLocation: org.bukkit.Location? = null
    var randomChestTitle: ArmorStand? = null
    var zombie: MutableList<Zombie> = mutableListOf()
    var title: MutableList<ArmorStand> = mutableListOf()
    var goldenGolem: MutableList<Golem> = mutableListOf()
    var shortage: Boolean = false
    var magnification = 1
    val bossBar: BossBar = Bukkit.createBossBar("ゾンビ解放まで", BarColor.BLUE, BarStyle.SEGMENTED_10)
    var blaze: MutableList<Blaze> = mutableListOf()
    var playMap: String = "map1"
    var feverTime = 0
    var fever = false
}
