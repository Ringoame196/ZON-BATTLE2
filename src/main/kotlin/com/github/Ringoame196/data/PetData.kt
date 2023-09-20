package com.github.Ringoame196.data

import com.github.Ringoame196.Entity.Blaze
import com.github.Ringoame196.Entity.Golem
import com.github.Ringoame196.Entity.Minion
import com.github.Ringoame196.Entity.PotionShop
import com.github.Ringoame196.Game.Scoreboard
import com.github.Ringoame196.Give
import com.github.Ringoame196.Item
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Bee
import org.bukkit.entity.Cow
import org.bukkit.entity.Enderman
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.MushroomCow
import org.bukkit.entity.Player
import org.bukkit.entity.Shulker
import org.bukkit.entity.Silverfish
import org.bukkit.entity.Skeleton
import org.bukkit.entity.Stray
import org.bukkit.entity.Vindicator
import org.bukkit.inventory.ItemStack

class PetData {
    fun remove(entity: Entity) {
        Scoreboard().remove(GET().getTeamSystemScoreName(GET().teamName(entity)), "petCount", 1)
    }
    fun switch(name: String, player: Player, block: org.bukkit.block.Block?, hp: Double?) {
        val team = GET().teamName(player)
        val petC = Scoreboard().getValue(GET().getTeamSystemScoreName(team), "petCount") < 5
        if (!petC) {
            player.sendMessage("${ChatColor.RED}5体以上召喚はできません")
            return
        }
        val pet = when (name) {
            "${ChatColor.YELLOW}アイアンゴーレム" -> Golem().summon(player, Material.IRON_BLOCK)
            "${ChatColor.YELLOW}ネザライトゴーレム" -> Golem().summon(player, Material.NETHERITE_BLOCK)
            "${ChatColor.YELLOW}ギフトゴーレム" -> Golem().summon(player, Material.CHEST)
            "${ChatColor.RED}ゴールデンゴーレム" -> Golem().summon(player, Material.GOLD_BLOCK)
            "${ChatColor.GREEN}ミニオン" -> Minion().summon(block?.location?.add(0.0, 1.0, 0.0) ?: player.location, team)
            "${ChatColor.GOLD}ポーション屋" -> PotionShop().summon(player)
            "${ChatColor.RED}ブレイズ" -> Blaze().summon(player)
            "${ChatColor.YELLOW}シュルカー" -> shulker(player)
            "${ChatColor.YELLOW}ヴィンディケーター" -> vindicator(player)
            "${ChatColor.YELLOW}シルバーフィッシュ" -> silverfish(player)
            "${ChatColor.YELLOW}スケルトン" -> skeleton(player)
            "${ChatColor.AQUA}ストレイ" -> stray(player)
            "${ChatColor.DARK_PURPLE}エンダーマン" -> com.github.Ringoame196.Entity.Enderman().summon(player)
            "${ChatColor.GOLD}ハチ" -> com.github.Ringoame196.Entity.Bee().summon(player)
            "${ChatColor.RED}分身" -> player(player)
            "${ChatColor.YELLOW}牛" -> cow(player)
            "${ChatColor.RED}マッシュルーム" -> mushroomCow(player)
            else -> null
        }
        pet?.customName = name
        pet?.isCustomNameVisible = true
        pet?.scoreboardTags?.add("targetZombie")
        pet?.scoreboardTags?.add("${GET().teamName(player)}Pet")
        if (pet?.customName != "${ChatColor.RED}分身" || pet.customName != "${ChatColor.YELLOW}ギフトゴーレム") {
            pet?.scoreboardTags?.add("friend")
        }
        pet?.scoreboardTags?.add(GET().teamName(player))
        if (pet is LivingEntity && hp != null && hp != 0.0) {
            pet.health = hp
        }

        if (pet != null) { Scoreboard().add(GET().getTeamSystemScoreName(team), "petCount", 1) }

        Item().removeitem(player)
    }
    fun shulker(player: Player): Entity {
        val mob = player.world.spawn(player.location, Shulker::class.java)
        mob.isAware = true
        return mob
    }
    fun vindicator(player: Player): Entity {
        val mob = player.world.spawn(player.location, Vindicator::class.java)
        mob.isAware = true
        mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 0.0
        return mob
    }
    fun silverfish(player: Player): Entity {
        val mob = player.world.spawn(player.location, Silverfish::class.java)
        mob.isAware = true
        return mob
    }
    fun skeleton(player: Player): Entity {
        val mob = player.world.spawn(player.location, Skeleton::class.java)
        mob.isAware = true
        return mob
    }
    fun stray(player: Player): Entity {
        val mob = player.world.spawn(player.location, Stray::class.java)
        mob.isAware = true
        return mob
    }
    fun player(player: Player): Entity {
        val mob = player.world.spawn(player.location, Skeleton::class.java)
        mob.isAware = true
        mob.isSilent = true
        mob.equipment?.helmet = Give().playerHead(player.name)
        mob.equipment?.chestplate = player.inventory.chestplate
        mob.equipment?.leggings = player.inventory.leggings
        mob.equipment?.boots = player.inventory.boots
        mob.equipment?.setItemInOffHand(ItemStack(Material.SHIELD))
        mob.equipment?.setItemInMainHand(com.github.Ringoame196.Entity.Player().bestSword(player))
        mob.scoreboardTags.add("bodyguard")
        return mob
    }
    fun cow(player: Player): Entity {
        val mob = player.world.spawn(player.location, Cow::class.java)
        mob.isAware = true
        return mob
    }
    fun mushroomCow(player: Player): Entity {
        val mob = player.world.spawn(player.location, MushroomCow::class.java)
        mob.isAware = true
        return mob
    }
}
