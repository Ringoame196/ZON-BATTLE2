package com.github.Ringoame196.data

import com.github.Ringoame196.Entity.Blaze
import com.github.Ringoame196.Entity.Minion
import com.github.Ringoame196.Entity.PotionShop
import com.github.Ringoame196.Game.Scoreboard
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Enderman
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.entity.Shulker
import org.bukkit.entity.Silverfish
import org.bukkit.entity.Skeleton
import org.bukkit.entity.Stray
import org.bukkit.entity.Vindicator

class PetData {
    fun switch(name: String, player: Player, block: org.bukkit.block.Block?, hp: Double?): Entity? {
        val team = GET().teamName(player)
        val petC = Scoreboard().getValue(GET().getTeamSystemScoreName(team), "petCount") < 5
        if (!petC) {
            player.sendMessage("${ChatColor.RED}5体以上召喚はできません")
            return null
        }
        val pet = when (name) {
            "${ChatColor.YELLOW}アイアンゴーレム" -> com.github.Ringoame196.Entity.Golem().summon(player, Material.IRON_BLOCK, name)
            "${ChatColor.RED}ゴールデンゴーレム" -> com.github.Ringoame196.Entity.Golem().summon(player, Material.GOLD_BLOCK, name)
            "${ChatColor.GREEN}ミニオン" -> Minion().summon(block?.location?.add(0.0, 1.0, 0.0) ?: player.location, team)
            "${ChatColor.GOLD}ポーション屋" -> PotionShop().summon(player)
            "${ChatColor.RED}ブレイズ" -> Blaze().summon(player)
            "${ChatColor.YELLOW}シュルカー" -> shulker(player)
            "${ChatColor.YELLOW}ヴィンディケーター" -> vindicator(player)
            "${ChatColor.YELLOW}シルバーフィッシュ" -> silverfish(player)
            "${ChatColor.YELLOW}スケルトン" -> skeleton(player)
            "${ChatColor.AQUA}ストレイ" -> stray(player)
            "${ChatColor.DARK_PURPLE}エンダーマン" -> enderman(player)
            else -> null
        }
        pet?.customName = name
        pet?.isCustomNameVisible = true
        pet?.scoreboardTags?.add("targetZombie")
        pet?.scoreboardTags?.add("${GET().teamName(player)}Pet")
        pet?.scoreboardTags?.add("friend")
        pet?.scoreboardTags?.add(GET().teamName(player))
        if (pet is LivingEntity && hp != null && hp != 0.0) {
            pet.health = hp
        }

        if (pet != null) { Scoreboard().add(GET().getTeamSystemScoreName(team), "petCount", 1) }

        return pet
    }
    fun shulker(player: Player): Entity {
        val mob = player.world.spawn(player.location, Shulker::class.java)
        mob.isAware = true
        return mob
    }
    fun vindicator(player: Player): Entity {
        val mob = player.world.spawn(player.location, Vindicator::class.java)
        mob.isAware = true
        mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 6.0
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
    fun enderman(player: Player): Entity {
        val mob = player.world.spawn(player.location, Enderman::class.java)
        mob.isAware = true
        return mob
    }
}
