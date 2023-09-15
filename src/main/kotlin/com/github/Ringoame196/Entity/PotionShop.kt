package com.github.Ringoame196.Entity

import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class PotionShop {
    fun summon(player: Player): Entity {
        val potionShop: Villager = player.world.spawn(player.location, Villager::class.java)
        potionShop.customName = "${ChatColor.GOLD}ポーション屋"
        potionShop.isCustomNameVisible = true
        Data.DataManager.gameData.potionShop.add(potionShop)
        return potionShop
    }
    fun potion(): ItemStack {
        val potion = ItemStack(Material.SPLASH_POTION)
        val potionMeta = potion.itemMeta as PotionMeta

        val regenerationEffect = PotionEffect(PotionEffectType.HEAL, 1, 0)
        potionMeta.addCustomEffect(regenerationEffect, true)
        potionMeta.setDisplayName("スプラッシュポーション")
        potion.setItemMeta(potionMeta)
        return potion
    }
    fun give() {
        var redPotion = 0
        var bluePotion = 0
        for (shop in Data.DataManager.gameData.potionShop) {
            if (Bukkit.getWorld("BATTLE")?.entities?.contains(shop) == false) {
                Data.DataManager.gameData.potionShop.remove(shop)
                continue
            }
            when {
                shop.scoreboardTags.contains("red") -> redPotion ++
                shop.scoreboardTags.contains("blue") -> bluePotion ++
                else -> {}
            }
        }
        val scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard
        scoreboard?.getObjective("participatingPlayer") ?: return

        for (playerName in scoreboard.entries) {
            val player = Bukkit.getPlayer(playerName) ?: continue
            when (GET().teamName(player)) {
                "red" -> if (redPotion != 0) {
                    for (i in 1..redPotion) {
                        player.inventory.addItem(potion())
                    }
                }
                "blue" -> if (bluePotion != 0) {
                    for (i in 1..bluePotion) {
                        player.inventory.addItem(potion())
                    }
                }
            }
        }
    }
}
