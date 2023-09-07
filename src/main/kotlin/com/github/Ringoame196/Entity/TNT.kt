package com.github.Ringoame196.Entity

import org.bukkit.ChatColor
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.Random

class TNT {
    fun summon(player: Player, plugin: Plugin) {
        val playerLocation = player.location
        val armorStand = ArmorStand().summon(playerLocation, "")
        var timer = Random().nextInt(1, 10)
        object : BukkitRunnable() {
            override fun run() {
                if (timer == 1) {
                    val nearbyEntities = player.getNearbyEntities(4.0, 4.0, 4.0)
                    for (entity in nearbyEntities) {
                        if (entity is Zombie) { entity.damage(20.0) }
                    }
                    playerLocation.world?.spawnParticle(Particle.EXPLOSION_HUGE, playerLocation, 1, 0.0, 0.0, 0.0, 0.1)
                    playerLocation.world?.playSound(playerLocation, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f)
                    this.cancel()
                    armorStand.remove()
                }
                timer --
                playerLocation.world?.playSound(playerLocation, Sound.UI_BUTTON_CLICK, 1f, 1f)
                armorStand.customName = "${ChatColor.GREEN}爆発まで${timer}秒"
            }
        }.runTaskTimer(plugin, 0L, 20L)
    }
}
