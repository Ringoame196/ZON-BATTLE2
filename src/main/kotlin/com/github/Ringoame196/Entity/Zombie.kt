package com.github.Ringoame196.Entity

import com.github.Ringoame196.Game.Map
import com.github.Ringoame196.Game.Point
import com.github.Ringoame196.Game.Scoreboard
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import com.github.Ringoame196.data.ZombieData
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class Zombie {
    fun summonSorting(player: Player, item: ItemStack) {
        val zombieName = item.itemMeta?.displayName?.replace("${ChatColor.YELLOW}", "")?.replace("[ゾンビ召喚]", "") ?: return
        Map().summonSorting(zombieName, player)
    }

    fun glassSummon(player: Player, function: String) {
        val location = player.getLocation()
        location.add(0.0, -3.5, 0.0)
        summon(location, function, player)
    }

    fun randomSummon(player: Player, function: String) {
        val location = randomSummonLocation(player) ?: return
        summon(location, function, player)
        player.sendMessage("${ChatColor.GOLD}ゾンビ召喚完了")
    }

    fun randomSummonLocation(player: Player): Location {
        val summonLocationList = Map().randomSummonLocationList(player)
        val random = Random.nextInt(0, summonLocationList?.size ?: 0)
        return summonLocationList?.get(random)!!
    }
    fun summon(location: Location, zombieName: String, player: Player) {
        val world = location.world
        val zombie: Zombie? = world?.spawn(location, Zombie::class.java)
        ZombieData().switching(zombieName, player, zombie)

        zombie?.let { Data.DataManager.gameData.zombie.add(it) }
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
                val removeCoin: Int = Scoreboard().getValue("point", entity.name) ?: (0 / 2)
                Point().remove(entity, removeCoin)
                entity.sendTitle("", "${ChatColor.RED}泥棒に${removeCoin}p盗まれた")
                Point().add(owner!!, removeCoin, false)
            }
        }
    }
    fun offhandSet(player: Player) {
        val mainHandItem = player.inventory.itemInMainHand.clone()
        val offhandItem = player.inventory.itemInOffHand.clone()
        if (offhandItem != null) {
            player.inventory.setItemInMainHand(offhandItem)
        }
        player.inventory.setItemInOffHand(mainHandItem)
    }
}
