package com.github.Ringoame196.Entity

import com.github.Ringoame196.Game.Map
import com.github.Ringoame196.Game.Point
import com.github.Ringoame196.Game.Scoreboard
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import com.github.Ringoame196.data.ZombieData
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.inventory.ItemStack
import org.spigotmc.event.entity.EntityDismountEvent
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
                e.isCancelled = true
                takeAway(zombie, entity)
            }
            "${ChatColor.DARK_RED}誘拐犯" -> {
                if (entity !is Player) { return }
                zombie.addPassenger(entity)
                entity.sendTitle("${ChatColor.GREEN}誘拐中", "${ChatColor.RED}身代金1000p")
            }
            "大泥棒" -> {
                e.isCancelled = true
                val item = Random.nextInt(1, 3) == 1
                val point = Random.nextInt(1, 6) == 1
                if (item) {
                    takeItem(zombie, entity)
                } else if (point) {
                    takeAway(zombie, entity)
                }
            }
        }
    }
    fun takeAway(zombie: Zombie, entity: Entity) {
        val owner = GET().owner(zombie) ?: return
        if (entity !is Player) { return }
        val removeCoin: Int = Scoreboard().getValue("point", entity.name) / 2
        Point().remove(entity, removeCoin)
        entity.sendTitle("", "${ChatColor.RED}${zombie.customName}に${removeCoin}p盗まれた")
        Point().add(owner, removeCoin, false)
    }
    fun takeItem(zombie: Zombie, entity: Entity) {
        val owner = GET().owner(zombie) ?: return
        if (entity !is Player) { return }
        val item = entity.inventory.itemInMainHand.clone()
        entity.inventory.setItemInMainHand(ItemStack(Material.AIR))
        owner.inventory?.addItem(item)
        entity.sendTitle("", "${ChatColor.RED}${zombie.customName}にアイテムを盗まれた")
        owner.sendTitle("", "${ChatColor.GREEN}${zombie.customName}がアイテムを盗んできてくれた")
        entity.playSound(entity, Sound.ITEM_TRIDENT_RETURN, 1f, 1f)
    }
    fun offhandSet(player: Player) {
        val mainHandItem = player.inventory.itemInMainHand.clone()
        val offhandItem = player.inventory.itemInOffHand.clone()
        player.inventory.setItemInMainHand(offhandItem)
        player.inventory.setItemInOffHand(mainHandItem)
    }
    fun ransom(player: Player, zombie: Entity, e: EntityDismountEvent) {
        if (zombie !is Zombie) { return }
        if (Random.nextInt(1, 31) == 1) {
            player.sendTitle("${ChatColor.GREEN}脱出成功", "${ChatColor.AQUA}うまく逃げた")
            zombie.remove()
            return
        }
        if (Scoreboard().getValue("point", player.name) <1000) {
            player.sendTitle("${ChatColor.RED}脱出失敗", "${ChatColor.RED}お金が足りず うまく逃げ切れなかった")
            e.isCancelled = true
        } else {
            Point().remove(player, 1000)
            zombie.remove()
            player.sendTitle("${ChatColor.GREEN}脱出", "${ChatColor.YELLOW}身代金を払った")
        }
    }
}
