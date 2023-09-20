package com.github.Ringoame196.Entity

import com.github.Ringoame196.data.Data
import org.bukkit.Location
import org.bukkit.entity.ArmorStand

class ArmorStand {
    fun summon(location: Location, name: String): ArmorStand {
        val world = location.world
        val armorStand: ArmorStand = world!!.spawn(location.add(0.0, 1.0, 0.0), org.bukkit.entity.ArmorStand::class.java)

        // アーマースタンドの設定
        armorStand.isVisible = false // 可視化するかどうか
        armorStand.isSmall = true // サイズを小さくするかどうか
        armorStand.isInvulnerable = true
        armorStand.customName = name
        armorStand.isCustomNameVisible = true
        armorStand.setGravity(false)
        armorStand.isMarker = true
        Data.DataManager.gameData.title.add(armorStand)
        return armorStand
    }
}
