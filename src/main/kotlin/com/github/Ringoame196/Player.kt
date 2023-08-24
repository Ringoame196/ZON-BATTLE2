package com.github.Ringoame196

import org.bukkit.ChatColor
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.plugin.Plugin

class Player {
    fun kill(killer: Player) {
        Point().add(killer, 300, true)
    }
    fun showdamage(damager: Entity, entity: Player, damage: Int) {
        if (damager is Arrow && damager.shooter is Player) {
            val shooter = damager.shooter as Player
            val hp = entity.health.toInt() - damage // ダメージ後のHP
            val maxHp = entity.maxHealth // 最大HP

            shooter.sendMessage("${ChatColor.RED}HP: $hp / $maxHp")
        }
    }
    fun death(e: EntityDamageEvent, player: Player, plugin: Plugin) {
        if (!GET().joinTeam(player)) { return }
        e.isCancelled = true

        // ダメージを与えたエンティティがプレイヤーであればキル処理
        if (e !is EntityDamageByEntityEvent) {
            Team().respawn(player, plugin, e.cause.toString())
            return
        }
        val damager = e.damager
        Team().respawn(player, plugin, damager.name)
        if (damager !is Player) { return }
        kill(damager)
    }
}
