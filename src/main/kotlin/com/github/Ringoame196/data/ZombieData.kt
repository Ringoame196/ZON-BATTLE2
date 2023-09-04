package com.github.Ringoame196.data

import com.github.Ringoame196.Give
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ZombieData {
    fun switching(zombieName: String, player: Player, zombie: Zombie?) {
        zombie?.scoreboardTags?.add("owner:${player.name}")
        when (zombieName) {
            "ノーマルゾンビ" -> normal(zombie)
            "チビゾンビ" -> chibi(zombie)
            "ゾンビソルジャー" -> solder(zombie)
            "ダッシュマン" -> dashman(zombie)
            "バトルロード" -> BattleLoad(zombie)
            "カスタムロード" -> CustomLoad(zombie)
            "シールドゾンビ" -> shield(zombie)
            "タンクマン" -> tankman(zombie)
            "スケルトンマン" -> Skeletonman(zombie)
            "泥棒" -> thief(zombie)
            "シャーマン" -> Sherman(zombie)
            "フロストメイジ" -> Frozen(zombie)
            "ネクロマンサー" -> necromancer(zombie)
            "エンペラー" -> emperor(zombie)
            "デスクイーン" -> deathQueen(zombie)
            "ネザライトゾンビ" -> netherite(zombie)
        }
    }
    fun normal(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "ノーマルゾンビ"
            it.maxHealth = 20.0
            it.health = 20.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.2
            it.damage(4.0)
            it.scoreboardTags.add("targetshop")
        }
    }
    fun chibi(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = true
            it.customName = "チビゾンビ"
            it.maxHealth = 20.0
            it.health = 20.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.2
            it.damage(4.0)
            it.scoreboardTags.add("targetshop")
        }
    }
    fun solder(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "ゾンビソルジャー"
            it.maxHealth = 20.0
            it.health = 20.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.2
            it.damage(4.0)
            it.scoreboardTags.add("targetshop")
            it.equipment?.helmet = ItemStack(Material.LEATHER_HELMET)
            it.equipment?.chestplate = ItemStack(Material.LEATHER_CHESTPLATE)
            it.equipment?.leggings = ItemStack(Material.LEATHER_LEGGINGS)
            it.equipment?.boots = ItemStack(Material.LEATHER_BOOTS)
        }
    }
    fun dashman(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "ダッシュマン"
            it.maxHealth = 20.0
            it.health = 20.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.4
            it.damage(4.0)
            it.scoreboardTags.add("targetshop")
            it.equipment?.helmet = ItemStack(Material.GOLDEN_HELMET)
            it.equipment?.boots = ItemStack(Material.GOLDEN_BOOTS)
        }
    }
    fun BattleLoad(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "${ChatColor.RED}バトルロード"
            it.maxHealth = 50.0
            it.health = 50.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.3
            it.damage(4.0)
            it.scoreboardTags.add("targetPlayer")
            it.equipment?.helmet = ItemStack(Material.DIAMOND_HELMET)
            it.equipment?.chestplate = ItemStack(Material.DIAMOND_CHESTPLATE)
            it.equipment?.leggings = ItemStack(Material.DIAMOND_LEGGINGS)
            it.equipment?.boots = ItemStack(Material.DIAMOND_BOOTS)
            it.equipment?.setItemInMainHand(ItemStack(Material.DIAMOND_SWORD))
        }
    }
    fun CustomLoad(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "${ChatColor.RED}カスタムロード"
            it.maxHealth = 40.0
            it.health = 40.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.2
            it.damage(4.0)
            it.scoreboardTags.add("targetshop")
            it.scoreboardTags.add("noChange")
            it.equipment?.helmet = ItemStack(Material.IRON_HELMET)
            it.equipment?.chestplate = ItemStack(Material.IRON_CHESTPLATE)
            it.equipment?.leggings = ItemStack(Material.IRON_LEGGINGS)
            it.equipment?.boots = ItemStack(Material.IRON_BOOTS)
            it.equipment?.setItemInMainHand(ItemStack(Material.DIAMOND_SWORD))
        }
    }
    fun shield(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "シールドゾンビ"
            it.maxHealth = 20.0
            it.health = 20.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.2
            it.damage(4.0)
            it.scoreboardTags.add("targetshop")
            it.equipment?.helmet = ItemStack(Material.IRON_HELMET)
            it.equipment?.chestplate = ItemStack(Material.IRON_CHESTPLATE)
            it.equipment?.leggings = ItemStack(Material.IRON_LEGGINGS)
            it.equipment?.boots = ItemStack(Material.IRON_BOOTS)
            it.equipment?.setItemInOffHand(ItemStack(Material.SHIELD))
        }
    }
    fun tankman(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "タンクマン"
            it.maxHealth = 300.0
            it.health = 300.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.1
            it.damage(4.0)
            it.scoreboardTags.add("targetshop")
            it.equipment?.helmet = ItemStack(Material.IRON_HELMET)
            it.equipment?.chestplate = ItemStack(Material.DIAMOND_CHESTPLATE)
        }
    }
    fun Skeletonman(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "スケルトンマン"
            it.maxHealth = 10.0
            it.health = 10.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.2
            it.damage(4.0)
            it.scoreboardTags.add("targetshop")
            it.equipment?.boots = ItemStack(Material.CHAINMAIL_BOOTS)
            it.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, Int.MAX_VALUE, 0, false, false))
        }
    }
    fun thief(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "泥棒"
            it.maxHealth = 50.0
            it.health = 50.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.3
            it.damage(0.0)
            it.scoreboardTags.add("targetPlayer")
            it.equipment?.helmet = Give().colorLEATHER(Material.LEATHER_HELMET, "GREEN")
            it.equipment?.chestplate = Give().colorLEATHER(Material.LEATHER_CHESTPLATE, "GREEN")
            it.equipment?.leggings = Give().colorLEATHER(Material.LEATHER_LEGGINGS, "GREEN")
            it.equipment?.boots = Give().colorLEATHER(Material.LEATHER_BOOTS, "GREEN")
        }
    }
    fun Sherman(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "シャーマン"
            it.maxHealth = 20.0
            it.health = 20.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.2
            it.damage(4.0)
            it.scoreboardTags.add("targetshop")
            it.scoreboardTags.add("shaman")
            it.equipment?.helmet = Give().colorLEATHER(Material.LEATHER_HELMET, "GREEN")
            it.equipment?.chestplate = Give().colorLEATHER(Material.LEATHER_CHESTPLATE, "GREEN")
            it.equipment?.leggings = Give().colorLEATHER(Material.LEATHER_LEGGINGS, "GREEN")
            it.equipment?.boots = Give().colorLEATHER(Material.LEATHER_BOOTS, "GREEN")
        }
    }
    fun Frozen(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "フロストメイジ"
            it.maxHealth = 40.0
            it.health = 40.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.15
            it.damage(4.0)
            it.scoreboardTags.add("targetshop")
            it.scoreboardTags.add("Frostmage")
            it.equipment?.helmet = Give().colorLEATHER(Material.LEATHER_HELMET, "AQUA")
            it.equipment?.chestplate = Give().colorLEATHER(Material.LEATHER_CHESTPLATE, "AQUA")
            it.equipment?.leggings = Give().colorLEATHER(Material.LEATHER_LEGGINGS, "AQUA")
            it.equipment?.boots = Give().colorLEATHER(Material.LEATHER_BOOTS, "AQUA")
        }
    }
    fun necromancer(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "${ChatColor.DARK_PURPLE}ネクロマンサー"
            it.maxHealth = 20.0
            it.health = 20.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.0
            it.damage(4.0)
            it.scoreboardTags.add("targetshop")
            it.equipment?.helmet = ItemStack(Material.SKELETON_SKULL)
            it.equipment?.chestplate = ItemStack(Material.LEATHER_CHESTPLATE)
            it.equipment?.leggings = ItemStack(Material.LEATHER_LEGGINGS)
            it.equipment?.boots = ItemStack(Material.LEATHER_BOOTS)
        }
    }
    fun emperor(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "${ChatColor.DARK_PURPLE}エンペラー"
            it.maxHealth = 20.0
            it.health = 20.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.0
            it.damage(4.0)
            it.scoreboardTags.add("targetshop")
            it.equipment?.helmet = ItemStack(Material.SKELETON_SKULL)
            it.equipment?.chestplate = ItemStack(Material.IRON_CHESTPLATE)
            it.equipment?.leggings = ItemStack(Material.IRON_LEGGINGS)
            it.equipment?.boots = ItemStack(Material.IRON_BOOTS)
        }
    }
    fun deathQueen(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "${ChatColor.RED}デスクイーン"
            it.maxHealth = 65.0
            it.health = 65.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.2
            it.damage(10.0)
            it.scoreboardTags.add("targetshop")
            it.equipment?.helmet = ItemStack(Material.SKELETON_SKULL)
            it.equipment?.chestplate = ItemStack(Material.GOLDEN_CHESTPLATE)
            it.equipment?.leggings = ItemStack(Material.GOLDEN_LEGGINGS)
            it.equipment?.boots = ItemStack(Material.LEATHER_BOOTS)
        }
    }
    fun netherite(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "ネザライトゾンビ"
            it.maxHealth = 20.0
            it.health = 20.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.1
            it.damage(4.0)
            it.scoreboardTags.add("targetshop")
            it.equipment?.helmet = ItemStack(Material.NETHERITE_HELMET)
            it.equipment?.chestplate = ItemStack(Material.NETHERITE_CHESTPLATE)
            it.equipment?.leggings = ItemStack(Material.NETHERITE_LEGGINGS)
            it.equipment?.boots = ItemStack(Material.NETHERITE_BOOTS)
        }
    }
}
