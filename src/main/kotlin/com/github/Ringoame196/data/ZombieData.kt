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
import kotlin.random.Random

class ZombieData {
    fun random(player: Player): String {
        val zombieList: MutableList<String> = mutableListOf(
            "ノーマルゾンビ",
            "ノーマルゾンビ",
            "ノーマルゾンビ",
            "ノーマルゾンビ",
            "ノーマルゾンビ",
            "ノーマルゾンビ",
            "ノーマルゾンビ",
            "ノーマルゾンビ",
            "チビゾンビ",
            "チビゾンビ",
            "チビゾンビ",
            "チビゾンビ",
            "チビゾンビ",
            "ゾンビソルジャー",
            "ゾンビソルジャー",
            "ゾンビソルジャー",
            "ダッシュマン",
            "ダッシュマン",
            "ダッシュマン",
            "バトルロード",
            "カスタムロード",
            "シールドゾンビ",
            "シールドゾンビ",
            "シールドゾンビ",
            "シールドゾンビ",
            "シールドゾンビ",
            "タンクマン",
            "タンクマン",
            "スケルトンマン",
            "スケルトンマン",
            "スケルトンマン",
            "泥棒",
            "泥棒",
            "シャーマン",
            "シャーマン",
            "フロストメイジ",
            "フロストメイジ",
            "フロストメイジ",
            "フロストメイジ",
            "ネクロマンサー",
            "エンペラー",
            "デスクイーン",
            "誘拐犯",
            "大泥棒",
            "ゴースト",
            "アンペット",
            "親子"
        )
        val random = Random.nextInt(0, zombieList.size)
        val zombie = zombieList.get(random)
        player.sendMessage("${ChatColor.AQUA}${zombie}を召喚しました")
        return zombie
    }
    fun switching(zombieName: String, player: Player, zombie: Zombie?) {
        zombie?.scoreboardTags?.add("owner:${player.name}")
        zombie?.scoreboardTags?.add(GET().teamName(player))
        when (zombieName) {
            "ランダム" -> switching(random(player), player, zombie)
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
            "誘拐犯" -> kidnapping(zombie)
            "大泥棒" -> bigThief(zombie)
            "ゴースト" -> ghost(zombie)
            "アンペット" -> unpet(zombie)
            "親子" -> parentAndChild(zombie)
        }
    }
    fun normal(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "ノーマルゾンビ"
            it.maxHealth = 20.0
            it.health = 20.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.2
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 3.0
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
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 4.0
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
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 5.0
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
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 4.0
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
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 4.0
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
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 4.0
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
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 4.0
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
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 4.0
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
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 4.0
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
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 0.0
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
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 4.0
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
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 4.0
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
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 4.0
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
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 4.0
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
            it.maxHealth = 130.0
            it.health = 130.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.2
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 10.0
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
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 4.0
            it.scoreboardTags.add("targetshop")
            it.equipment?.helmet = ItemStack(Material.NETHERITE_HELMET)
            it.equipment?.chestplate = ItemStack(Material.NETHERITE_CHESTPLATE)
            it.equipment?.leggings = ItemStack(Material.NETHERITE_LEGGINGS)
            it.equipment?.boots = ItemStack(Material.NETHERITE_BOOTS)
        }
    }
    fun kidnapping(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "${ChatColor.DARK_RED}誘拐犯"
            it.maxHealth = 25.0
            it.health = 25.0
            it.equipment?.helmet = ItemStack(Material.WITHER_SKELETON_SKULL)
            it.equipment?.chestplate = Give().colorLEATHER(Material.LEATHER_CHESTPLATE, "BLACK")
            it.equipment?.leggings = Give().colorLEATHER(Material.LEATHER_LEGGINGS, "BLACK")
            it.equipment?.boots = Give().colorLEATHER(Material.LEATHER_BOOTS, "BLACK")
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.3
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 4.0
            it.scoreboardTags.add("targetPlayer")
        }
    }
    fun bigThief(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "大泥棒"
            it.maxHealth = 150.0
            it.health = 150.0
            it.equipment?.helmet = Give().colorLEATHER(Material.LEATHER_HELMET, "BLACK")
            it.equipment?.chestplate = Give().colorLEATHER(Material.LEATHER_CHESTPLATE, "GREEN")
            it.equipment?.leggings = Give().colorLEATHER(Material.LEATHER_LEGGINGS, "BLACK")
            it.equipment?.boots = Give().colorLEATHER(Material.LEATHER_BOOTS, "GREEN")
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.3
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 0.0
            it.scoreboardTags.add("targetPlayer")
        }
    }
    fun ghost(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "ゴースト"
            it.maxHealth = 44.0
            it.health = 44.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.2
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 4.0
            it.scoreboardTags.add("targetshop")
            it.scoreboardTags.add("invincible")
            it.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, Int.MAX_VALUE, 0, false, false))
        }
    }
    fun unpet(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "${ChatColor.RED}アンペット"
            it.maxHealth = 40.0
            it.health = 40.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.3
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 4.0
            it.scoreboardTags.add("targetPet")
            it.equipment?.setItemInMainHand(ItemStack(Material.DIAMOND_AXE))
            it.equipment?.chestplate = ItemStack(Material.IRON_CHESTPLATE)
        }
    }

    fun parentAndChild(zombie: Zombie?) {
        zombie?.let {
            it.isBaby = false
            it.customName = "親子"
            it.maxHealth = 20.0
            it.health = 20.0
            it.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = 0.3
            it.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 4.0
            it.scoreboardTags.add("targetshop")

            val childZombie = it.world.spawn(it.location, Zombie::class.java)
            chibi(childZombie)
            zombie.addPassenger(childZombie)
        }
    }
}
