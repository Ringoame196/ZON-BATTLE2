package com.github.Ringoame196.Entity

import com.github.Ringoame196.GET
import com.github.Ringoame196.Give
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.player
import com.github.Ringoame196.point
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.io.File

class Zombie {
    fun summonSystem(player: Player, item_name: String) {
        var summon_name = item_name.replace("[ゾンビ召喚]", "")
        summon_name = summon_name.replace("${ChatColor.YELLOW}", "")

        val location = player.getLocation()
        location.add(0.0, -3.5, 0.0)
        val function = when (summon_name) {
            "ノーマルゾンビ" -> "normal"
            "チビゾンビ" -> "chibi"
            "シールドゾンビ" -> "shield"
            "ゾンビソルジャー" -> "soldier"
            "タンクマン" -> "tankman"
            "ダッシュマン" -> "dashman"
            "スケルトンマン" -> "skeletonman"
            "ネザライトゾンビ" -> "netherite"
            "シャーマン" -> "shaman"
            "ネクロマンサー" -> "necromancer"
            "エンペラー" -> "emperor"
            "デスクイーン" -> "deathqueen"
            "泥棒" -> "thief"
            "バトルロード" -> "battleLord"
            else -> { return }
        }
        summon(location, function, player)
    }
    @Suppress("DEPRECATION")
    fun summon(location: Location, function: String, player: Player) {
        val zombieinfo = "plugins/ZON-BATTLE2/zombie/$function.yml"
        val file = File(zombieinfo)

        if (!file.exists()) {
            Bukkit.broadcastMessage("[ゾンビ召喚]${ChatColor.RED}$function が未設定のため使用できません")
            return
        }

        val yaml = YamlConfiguration.loadConfiguration(file)
        val zombieSection = yaml.getConfigurationSection("Zombie")

        val world = location.world
        val zombie: Zombie? = world?.spawn(location, Zombie::class.java)
        zombie?.scoreboardTags?.add("owner:${player.name}")

        val customName = zombieSection?.getString("Name")
        if (!customName.isNullOrBlank()) {
            zombie?.customName = customName
        }
        val Baby = zombieSection?.getBoolean("Baby", false) ?: false
        zombie?.isBaby = Baby

        val health = zombieSection?.getDouble("HP", 20.0) ?: 20.0
        zombie?.maxHealth = health + 10.0
        zombie?.health = health + 10.0

        val movementSpeed = zombieSection?.getDouble("SPEED", 0.2) ?: 0.2
        zombie?.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)?.baseValue = movementSpeed

        val power = zombieSection?.getDouble("Power", 1.0) ?: 1.0
        zombie?.damage(power)

        val shield = zombieSection?.getBoolean("shield", false) ?: false
        if (shield) zombie?.equipment?.setItemInOffHand(ItemStack(Material.SHIELD))

        val setTag = zombieSection?.getString("tag", "") ?: ""
        val taglist = setTag.split(",")
        for (tag in taglist) {
            zombie?.scoreboardTags?.add(tag)
        }

        val Head = zombieSection?.getInt("Head", 0) ?: 0
        val Chestplate = zombieSection?.getInt("Chestplate", 0) ?: 0
        val Leggings = zombieSection?.getInt("Leggings", 0) ?: 0
        val Boots = zombieSection?.getInt("Boots", 0) ?: 0
        val color = zombieSection?.getString("Color", "") ?: ""
        when (Head) {
            1 -> zombie?.equipment?.helmet = Give().ColorLEATHER(Material.LEATHER_HELMET, color)
            2 -> zombie?.equipment?.helmet = ItemStack(Material.IRON_HELMET)
            3 -> zombie?.equipment?.helmet = ItemStack(Material.GOLDEN_HELMET)
            4 -> zombie?.equipment?.helmet = ItemStack(Material.DIAMOND_HELMET)
            5 -> zombie?.equipment?.helmet = ItemStack(Material.NETHERITE_HELMET)
            6 -> zombie?.equipment?.helmet = ItemStack(Material.CHAINMAIL_HELMET)
            7 -> zombie?.equipment?.helmet = ItemStack(Material.SKELETON_SKULL)
        }
        when (Chestplate) {
            1 -> zombie?.equipment?.chestplate = Give().ColorLEATHER(Material.LEATHER_CHESTPLATE, color)
            2 -> zombie?.equipment?.chestplate = ItemStack(Material.IRON_CHESTPLATE)
            3 -> zombie?.equipment?.chestplate = ItemStack(Material.GOLDEN_CHESTPLATE)
            4 -> zombie?.equipment?.chestplate = ItemStack(Material.DIAMOND_CHESTPLATE)
            5 -> zombie?.equipment?.chestplate = ItemStack(Material.NETHERITE_CHESTPLATE)
            6 -> zombie?.equipment?.chestplate = ItemStack(Material.CHAINMAIL_CHESTPLATE)
        }
        when (Leggings) {
            1 -> zombie?.equipment?.leggings = Give().ColorLEATHER(Material.LEATHER_LEGGINGS, color)
            2 -> zombie?.equipment?.leggings = ItemStack(Material.IRON_LEGGINGS)
            3 -> zombie?.equipment?.leggings = ItemStack(Material.GOLDEN_LEGGINGS)
            4 -> zombie?.equipment?.leggings = ItemStack(Material.DIAMOND_LEGGINGS)
            5 -> zombie?.equipment?.leggings = ItemStack(Material.NETHERITE_LEGGINGS)
            6 -> zombie?.equipment?.leggings = ItemStack(Material.CHAINMAIL_LEGGINGS)
        }
        when (Boots) {
            1 -> zombie?.equipment?.boots = Give().ColorLEATHER(Material.LEATHER_BOOTS, color)
            2 -> zombie?.equipment?.boots = ItemStack(Material.IRON_BOOTS)
            3 -> zombie?.equipment?.boots = ItemStack(Material.GOLDEN_BOOTS)
            4 -> zombie?.equipment?.boots = ItemStack(Material.DIAMOND_BOOTS)
            5 -> zombie?.equipment?.boots = ItemStack(Material.NETHERITE_BOOTS)
            6 -> zombie?.equipment?.boots = ItemStack(Material.CHAINMAIL_BOOTS)
        }

        when (function) { // 追加能力
            "soldier" -> {
                val sword = ItemStack(Material.IRON_SWORD)
                sword.addEnchantment(Enchantment.KNOCKBACK, 1)
                zombie?.equipment?.setItemInMainHand(sword)
            }
            "skeletonman" -> zombie?.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, Int.MAX_VALUE, 0, false, false))
            "deathqueen" -> zombie?.equipment?.setItemInMainHand(ItemStack(Material.NETHERITE_HOE))
            "battleLord" -> zombie?.equipment?.setItemInMainHand(ItemStack(Material.DIAMOND_SWORD))
        }

        zombie?.let { Data.DataManager.gameData.zombie.add(it) }
    }

    fun damage(zombie: Zombie) {
        if (zombie.scoreboardTags.contains("targetshop")) {
            zombie.scoreboardTags.remove("targetshop")
        }
    }
    fun summonner(zombieName: String, function1: String, function2: String) {
        val selectZombie = mutableListOf<Zombie>()
        for (zombie in Data.DataManager.gameData.zombie) {
            if (zombie.customName == zombieName) {
                selectZombie.add(zombie)
            }
        }
        for (zombie in selectZombie) {
            val location = zombie.location
            val owner = GET().owner(zombie)
            location.add(0.0, 1.0, 0.0)
            summon(location, function1, owner!!)
            summon(location, function2, owner)
        }
    }
    fun attack(zombie: Zombie, entity: Entity, e: EntityDamageByEntityEvent) {
        val zombieName = zombie.customName
        when (zombieName) {
            "泥棒" -> {
                val owner = GET().owner(zombie)
                if (entity !is Player) { return }
                e.isCancelled = true
                val removeCoin: Int = GET().point(entity) / 2
                point().remove(entity, removeCoin)
                entity.sendTitle("", "${ChatColor.RED}泥棒に${removeCoin}p盗まれた")
                point().add(owner!!, removeCoin, false)
            }
        }
    }
}
