package com.github.Ringoame196.Entity

import com.github.Ringoame196.Game.Point
import com.github.Ringoame196.Give
import com.github.Ringoame196.Team
import com.github.Ringoame196.data.GET
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Arrow
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

class Player {
    fun showdamage(damager: Entity, entity: Player, damage: Int) {
        if (damager is Arrow && damager.shooter is Player) {
            val shooter = damager.shooter as Player
            val hp = entity.health.toInt() - damage // ダメージ後のHP
            val maxHp = entity.maxHealth // 最大HP

            shooter.sendMessage("${ChatColor.RED}HP: $hp / $maxHp")
        }
    }
    @Suppress("NAME_SHADOWING")
    fun death(e: EntityDamageEvent, player: Player, plugin: Plugin) {
        if (!GET().joinTeam(player)) { return }
        e.isCancelled = true

        for (i in 0..8) {
            val item = player.inventory.getItem(i)
            if (item?.itemMeta?.displayName != "${ChatColor.GREEN}ゾンビのお守り") { continue }
            player.inventory.removeItem(item)
            for (i in 0..item.amount) {
                val zombie = player.world.spawn(player.location, Zombie::class.java)
                zombie.customName = "${ChatColor.AQUA}${player.name}"
                zombie.isCustomNameVisible = true
                zombie.equipment?.helmet = Give().playerHead(player.name)
                zombie.equipment?.chestplate = ItemStack(Material.IRON_CHESTPLATE)
                zombie.equipment?.leggings = ItemStack(Material.LEATHER_LEGGINGS)
                zombie.equipment?.boots = ItemStack(Material.LEATHER_BOOTS)
                zombie.equipment?.setItemInMainHand(ItemStack(Material.IRON_SWORD))
            }
            return
        }

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
    fun kill(killer: Player) {
        Point().add(killer, 300, true)
    }
    fun errormessage(message: String, player: Player) {
        player.sendMessage("${ChatColor.RED}$message")
        player.closeInventory()
        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1f, 1f)
    }
    fun bestSword(player: Player): ItemStack {
        val swordList = mutableListOf<Material>(
            Material.STONE_SWORD,
            Material.IRON_SWORD,
            Material.DIAMOND_SWORD,
            Material.NETHERITE_SWORD,
        )
        var setItem = ItemStack(Material.WOODEN_SWORD)
        for (sword in swordList) {
            for (hotbarSlot in 0..8) {
                val itemStack = player.inventory.getItem(hotbarSlot) ?: continue
                if (itemStack.type == sword) {
                    setItem = itemStack
                }
            }
        }
        return setItem
    }
}
