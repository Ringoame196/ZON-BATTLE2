package com.github.Ringoame196.data

import com.github.Ringoame196.Entity.Minion
import com.github.Ringoame196.Entity.PotionShop
import com.github.Ringoame196.Game.Scoreboard
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Shulker
import org.bukkit.entity.Vindicator

class PetData {
    fun switch(name: String, player: Player, block: org.bukkit.block.Block?): Entity? {
        val team = GET().teamName(player)
        val petC = Scoreboard().getValue(GET().getTeamSystemScoreName(team), "petCount") < 5
        if (!petC) {
            player.sendMessage("${ChatColor.RED}5体以上召喚はできません")
            return null
        }
        val pet = when (name) {
            "${ChatColor.YELLOW}アイアンゴーレム" -> com.github.Ringoame196.Entity.Golem().summon(player, Material.IRON_BLOCK, name)
            "${ChatColor.YELLOW}ゴールデンゴーレム" -> com.github.Ringoame196.Entity.Golem().summon(player, Material.GOLD_BLOCK, name)
            "${ChatColor.GREEN}ミニオン" -> Minion().summon(block?.location?.add(0.0, 1.0, 0.0) ?: player.location, team)
            "${ChatColor.YELLOW}ポーション屋" -> PotionShop().summon(player)
            "${ChatColor.RED}ブレイズ" -> blaze(player)
            "${ChatColor.YELLOW}シュルカー" -> shulker(player)
            "${ChatColor.YELLOW}ヴィンディケーター" -> vindicator(player)
            else -> null
        }
        pet?.scoreboardTags?.add("targetZombie")
        pet?.scoreboardTags?.add("${GET().teamName(player)}Pet")
        pet?.scoreboardTags?.add("friend")
        pet?.scoreboardTags?.add(GET().teamName(player))

        if (pet != null) { Scoreboard().add(GET().getTeamSystemScoreName(team), "petCount", 1) }

        return pet
    }
    fun blaze(player: Player): Entity {
        val blaze: org.bukkit.entity.Blaze = player.world.spawn(player.location, org.bukkit.entity.Blaze::class.java)
        blaze.scoreboardTags.add("friendship")
        blaze.setAI(false)
        Data.DataManager.gameData.blaze.add(blaze)
        return blaze
    }
    fun shulker(player: Player): Entity {
        val shulker: Shulker = player.world.spawn(player.location, Shulker::class.java)
        shulker.isAware = true
        return shulker
    }
    fun vindicator(player: Player): Entity {
        val vindicator: Vindicator = player.world.spawn(player.location, Vindicator::class.java)
        vindicator.isAware = true
        vindicator.customName = "ヴィンディケーター"
        vindicator.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)?.baseValue = 6.0
        return vindicator
    }
}
