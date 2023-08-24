package com.github.Ringoame196.Entity

import com.github.Ringoame196.Game.GameSystem
import com.github.Ringoame196.Game.Point
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.IronGolem
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class Golem {
    fun golden() {
        var redPoint = 0
        var bluePoint = 0
        for (golem in Data.DataManager.gameData.goldenGolem) {
            if (Bukkit.getWorld("BATTLE")?.entities?.contains(golem) == false) {
                Data.DataManager.gameData.goldenGolem.remove(golem)
                continue
            }
            when {
                golem.scoreboardTags.contains("red") -> redPoint += 10
                golem.scoreboardTags.contains("blue") -> bluePoint += 10
                else -> {}
            }
        }
        for (player in Data.DataManager.gameData.participatingPlayer) {
            when (GET().teamName(player)) {
                "red" -> if (redPoint != 0) { Point().add(player, redPoint, false) }
                "blue" -> if (bluePoint != 0) { Point().add(player, bluePoint, false) }
            }
        }
    }
    fun guardPlayerAttack(damager: Entity, e: EntityDamageByEntityEvent) {
        if (damager !is Player) { return }
        GameSystem().adventure(e, damager)
    }
    fun summon(player: Player, type: Material?, name: String) {
        val location = player.location
        val golem = location.world?.spawn(location, IronGolem::class.java) ?: return

        golem.customName = name
        golem.isCustomNameVisible = true
        golem.isPlayerCreated = true
        golem.scoreboardTags.add("targetZombie")

        when (type) {
            Material.GOLD_BLOCK -> {
                golem.health = 5.0
                golem.customName = "${ChatColor.RED}ゴールデンゴーレム"
                golem.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, Int.MAX_VALUE, 255))
                golem.scoreboardTags.add(GET().teamName(player))
                Data.DataManager.gameData.goldenGolem.add(golem)
            }
            Material.DIAMOND_BLOCK -> {
                golem.maxHealth = 300.0
                golem.health = 300.0
                golem.damage(10.0)
            }
            Material.NETHERITE_BLOCK -> {
                golem.maxHealth = 600.0
                golem.health = 600.0
                golem.damage(15.0)
            }
            else -> return
        }
        player.sendMessage("${ChatColor.YELLOW}ゴーレム召喚")
    }
}
