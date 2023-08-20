package com.github.Ringoame196.data

import org.bukkit.block.Block
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Golem
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie

class Gamedata {
    var status = false
    var time = 0
    var ParticipatingPlayer: MutableList<Player> = mutableListOf()
    var signLocation: org.bukkit.Location? = null
    var randomChestTitle: ArmorStand? = null
    var zombie: MutableList<Zombie> = mutableListOf()
    var fence: MutableList<Block> = mutableListOf()
    var title: MutableList<ArmorStand> = mutableListOf()
    var goldenGolem: MutableList<Golem> = mutableListOf()
    var shortage: Boolean = false
    var magnification = 1
}
