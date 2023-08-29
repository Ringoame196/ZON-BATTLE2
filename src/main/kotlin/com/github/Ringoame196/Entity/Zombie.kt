package com.github.Ringoame196.Entity

import com.github.Ringoame196.Game.Point
import com.github.Ringoame196.Give
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.configuration.ConfigurationSection
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
import kotlin.random.Random

class Zombie {
    fun summonSorting(player: Player, item: ItemStack) {
        val function = item.itemMeta?.lore?.get(0).toString()
        when (Data.DataManager.gameData.playMap) {
            "map1" -> map1Summon(player, function)
            "map2" -> map2Summon(player, function)
            else -> return
        }
    }

    fun map1Summon(player: Player, function: String) {
        val location = player.getLocation()
        location.add(0.0, -3.5, 0.0)
        summon(location, function, player)
    }

    fun map2Summon(player: Player, function: String) {
        val location = randomSummonLocation(player) ?: return
        summon(location, function, player)
    }

    fun randomSummonLocation(player: Player): Location? {
        val random = Random.nextInt(1, 4)
        val team = GET().teamName(player)
        if (team == "red") {
            when (random) {
                1 -> return Data.DataManager.LocationData.mredZombiespawn1
                2 -> return Data.DataManager.LocationData.mredZombiespawn2
                3 -> return Data.DataManager.LocationData.mredZombiespawn3
            }
        } else if (team == "blue") {
            when (random) {
                1 -> return Data.DataManager.LocationData.mblueZombiespawn1
                2 -> return Data.DataManager.LocationData.mblueZombiespawn2
                3 -> return Data.DataManager.LocationData.mblueZombiespawn3
            }
        }
        return null
    }
    fun summon(location: Location, function: String, player: Player) {
        val zombieinfo = "plugins/ZON-BATTLE2/zombie/$function.yml"

        val world = location.world
        val zombie: Zombie? = world?.spawn(location, Zombie::class.java)
        make(zombieinfo, zombie, player, function)

        zombie?.let { Data.DataManager.gameData.zombie.add(it) }
    }
    fun make(zombieinfo: String, zombie: Zombie?, player: Player, function: String) {
        val file = File(zombieinfo)
        if (!file.exists()) {
            Bukkit.broadcastMessage("[ゾンビ召喚]${ChatColor.RED}$function が未設定のため使用できません")
            zombie?.remove()
            return
        }
        val yaml = YamlConfiguration.loadConfiguration(file)
        val zombieSection = yaml.getConfigurationSection("Zombie")
        zombie?.scoreboardTags?.add("owner:${player.name}")
        setStatus(zombie, zombieSection)
        val head = zombieSection?.getInt("Head", 0) ?: 0
        val chestplate = zombieSection?.getInt("Chestplate", 0) ?: 0
        val leggings = zombieSection?.getInt("Leggings", 0) ?: 0
        val boots = zombieSection?.getInt("Boots", 0) ?: 0
        val color = zombieSection?.getString("Color", "") ?: ""
        zombie?.equipment?.helmet = setHelmet(head, color)
        zombie?.equipment?.chestplate = setChestplate(chestplate, color)
        zombie?.equipment?.leggings = setLeggings(leggings, color)
        zombie?.equipment?.boots = setBoots(boots, color)

        additionalInformation(zombie, function)
    }
    fun setStatus(zombie: Zombie?, zombieSection: ConfigurationSection?) {
        val customName = zombieSection?.getString("Name")
        if (!customName.isNullOrBlank()) {
            zombie?.customName = customName
        }
        val baby = zombieSection?.getBoolean("Baby", false) ?: false
        zombie?.isBaby = baby

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
    }
    fun additionalInformation(zombie: Zombie?, function: String) {
        when (function) {
            "soldier" -> {
                val sword = ItemStack(Material.IRON_SWORD)
                sword.addEnchantment(Enchantment.KNOCKBACK, 1)
                zombie?.equipment?.setItemInMainHand(sword)
            }
            "skeletonman" -> zombie?.addPotionEffect(PotionEffect(PotionEffectType.INVISIBILITY, Int.MAX_VALUE, 0, false, false))
            "deathqueen" -> zombie?.equipment?.setItemInMainHand(ItemStack(Material.NETHERITE_HOE))
            "battleLord" -> zombie?.equipment?.setItemInMainHand(ItemStack(Material.DIAMOND_SWORD))
            "customLoad" -> zombie?.equipment?.setItemInMainHand(ItemStack(Material.DIAMOND_SWORD))
        }
    }
    fun setHelmet(head: Int, color: String): ItemStack? {
        when (head) {
            1 -> return Give().colorLEATHER(Material.LEATHER_HELMET, color)
            2 -> return ItemStack(Material.IRON_HELMET)
            3 -> return ItemStack(Material.GOLDEN_HELMET)
            4 -> return ItemStack(Material.DIAMOND_HELMET)
            5 -> return ItemStack(Material.NETHERITE_HELMET)
            6 -> return ItemStack(Material.CHAINMAIL_HELMET)
            7 -> return ItemStack(Material.SKELETON_SKULL)
        }
        return null
    }
    fun setChestplate(chestplate: Int, color: String): ItemStack? {
        when (chestplate) {
            1 -> return Give().colorLEATHER(Material.LEATHER_CHESTPLATE, color)
            2 -> return ItemStack(Material.IRON_CHESTPLATE)
            3 -> return ItemStack(Material.GOLDEN_CHESTPLATE)
            4 -> return ItemStack(Material.DIAMOND_CHESTPLATE)
            5 -> return ItemStack(Material.NETHERITE_CHESTPLATE)
            6 -> return ItemStack(Material.CHAINMAIL_CHESTPLATE)
        }
        return null
    }
    fun setLeggings(leggings: Int, color: String): ItemStack? {
        when (leggings) {
            1 -> return Give().colorLEATHER(Material.LEATHER_LEGGINGS, color)
            2 -> return ItemStack(Material.IRON_LEGGINGS)
            3 -> return ItemStack(Material.GOLDEN_LEGGINGS)
            4 -> return ItemStack(Material.DIAMOND_LEGGINGS)
            5 -> return ItemStack(Material.NETHERITE_LEGGINGS)
            6 -> return ItemStack(Material.CHAINMAIL_LEGGINGS)
        }
        return null
    }
    fun setBoots(boots: Int, color: String): ItemStack? {
        when (boots) {
            1 -> return Give().colorLEATHER(Material.LEATHER_BOOTS, color)
            2 -> return ItemStack(Material.IRON_BOOTS)
            3 -> return ItemStack(Material.GOLDEN_BOOTS)
            4 -> return ItemStack(Material.DIAMOND_BOOTS)
            5 -> return ItemStack(Material.NETHERITE_BOOTS)
            6 -> return ItemStack(Material.CHAINMAIL_BOOTS)
        }
        return null
    }

    fun damage(zombie: Zombie) {
        if (zombie.scoreboardTags.contains("noChange")) { return }
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
        when (zombie.customName) {
            "泥棒" -> {
                val owner = GET().owner(zombie)
                if (entity !is Player) { return }
                e.isCancelled = true
                val removeCoin: Int = GET().point(entity) / 2
                Point().remove(entity, removeCoin)
                entity.sendTitle("", "${ChatColor.RED}泥棒に${removeCoin}p盗まれた")
                Point().add(owner!!, removeCoin, false)
            }
        }
    }
}
