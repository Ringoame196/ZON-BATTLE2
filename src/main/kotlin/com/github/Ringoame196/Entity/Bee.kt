package com.github.Ringoame196.Entity

import com.github.Ringoame196.Potion
import org.bukkit.Bukkit
import org.bukkit.entity.Bee
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Bee {
    fun summon(player: Player): Entity {
        val mob = player.world.spawn(player.location, Bee::class.java)
        mob.isAware = true
        return mob
    }
    fun attack(bee: Bee, target: LivingEntity, plugin: Plugin) {
        target.addPotionEffect(PotionEffect(PotionEffectType.WITHER, 5 * 20, 1))
        Potion().summon(target.location.add(0.0, 1.0, 0.0), PotionEffectType.HEAL)
        Bukkit.getScheduler().runTaskLater(
            plugin,
            Runnable {
                bee.damage(100.0)
            },
            60L
        )
    }
}
