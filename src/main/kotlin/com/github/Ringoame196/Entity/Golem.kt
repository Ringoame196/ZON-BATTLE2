package com.github.Ringoame196.Entity

import com.github.Ringoame196.Game.Point
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.IronGolem
import org.bukkit.entity.Player
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
        val scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard
        scoreboard?.getObjective("participatingPlayer") ?: return
        for (playerName in scoreboard.entries) {
            val player = Bukkit.getPlayer(playerName) ?: continue
            when (GET().teamName(player)) {
                "red" -> if (redPoint != 0) { Point().add(player, redPoint, false) }
                "blue" -> if (bluePoint != 0) { Point().add(player, bluePoint, false) }
            }
        }
    }
    fun summon(player: Player, type: Material?): Entity? {
        val location = player.location
        val golem = location.world?.spawn(location, IronGolem::class.java)

        golem?.isPlayerCreated = true

        when (type) {
            Material.GOLD_BLOCK -> {
                golem?.health = 5.0
                golem?.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, Int.MAX_VALUE, 255))
                golem?.let { Data.DataManager.gameData.goldenGolem.add(it) }
            }
            Material.NETHERITE_BLOCK -> {
                golem?.maxHealth = 600.0
                golem?.health = 600.0
                golem?.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 15.0
            }
            Material.CHEST -> {
                golem?.maxHealth = 500.0
                golem?.health = 500.0
                golem?.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 1.0
            }
            Material.DIAMOND_BLOCK -> {
                golem?.maxHealth = 50.0
                golem?.health = 50.0
                golem?.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 0.0
                golem?.scoreboardTags?.add("diamondGolem")
                golem?.scoreboardTags?.add("bodyguard")
            }
            else -> {}
        }
        player.sendMessage("${ChatColor.YELLOW}ゴーレム召喚")
        return golem
    }
}
