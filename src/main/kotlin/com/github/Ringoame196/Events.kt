package com.github.Ringoame196

import com.github.Ringoame196.Entity.Zombie
import com.github.Ringoame196.Game.GameSystem
import com.github.Ringoame196.Game.Point
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Golem
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockDamageEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.block.SignChangeEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin

class Events(private val plugin: Plugin) : Listener {

    @EventHandler
    fun onPlayerInteractEntity(e: PlayerInteractEntityEvent) {
        // ショップGUIを開く
        val player = e.player
        val entity = e.rightClicked
        val teamName = GET().teamName(player) ?: return
        if (GET().shop(entity)) {
            if (!entity.scoreboardTags.contains("center")) { Shop().open(e, player, entity as Villager, teamName) }
        }
    }

    @EventHandler
    fun onInventoryClickEvent(e: InventoryClickEvent) {
        // GUIクリック
        val player = e.whoClicked as Player
        val item = e.currentItem ?: return
        val guiName = e.view.title
        GUI().clickSystem(plugin, e, player, guiName, item)
    }

    @EventHandler
    fun onInventoryCloseEvent(e: InventoryCloseEvent) {
        // インベントリを閉じる時のイベント
        val player = e.player as Player
        val title = e.view.title
        val inventory = e.inventory
        when (title) {
            "${ChatColor.DARK_GREEN}チームチェスト" -> player.playSound(player, Sound.BLOCK_CHEST_CLOSE, 1f, 1f)
            "${ChatColor.DARK_GREEN}金床" -> Anvil().returnItem(player, inventory)
            "${ChatColor.DARK_GREEN}召喚の杖" -> Hoe().exclusion(player, inventory)
            else -> return
        }
    }

    @EventHandler
    fun onEntityDamageByEntityEvent(e: EntityDamageByEntityEvent) {
        val entity = e.entity
        val damager = e.damager
        val damage = e.finalDamage.toInt()
        if (damager is org.bukkit.entity.Zombie) {
            Zombie().attack(damager, entity, e)
        }
        when (entity) {
            is Villager -> Shop().attack(e, damager, entity)
            is org.bukkit.entity.Zombie -> Zombie().damage(entity)
            is Player -> Player().showdamage(damager, entity, damage)
            is Golem -> com.github.Ringoame196.Entity.Golem().guardPlayerAttack(damager, e)
            else -> {}
        }
    }

    @EventHandler
    fun onPlayerInteractEvent(e: PlayerInteractEvent) {
        // インベントリアイテムクリック
        val player = e.player
        val item = e.item
        val block = e.clickedBlock
        val action = e.action
        if (item?.itemMeta?.displayName == "${ChatColor.YELLOW}[召喚の杖]") {
            Hoe().clickEvent(player, e)
        } else if ((action == Action.RIGHT_CLICK_AIR) || (action == Action.RIGHT_CLICK_BLOCK)) {
            Item().clickSystem(player, item, block, e, plugin)
        } else if (block != null) {
            Block().operationBlock(block, player, e)
        }
    }

    @EventHandler
    fun onBlockBreakEvent(e: BlockBreakEvent) {
        // ブロックを破壊したとき
        val player = e.player
        val worldName = player.world.name
        if (worldName != "BATTLE" && worldName != "jikken") { return }
        GameSystem().adventure(e, player)
        val block = e.block
        when (block.type) {
            Material.OAK_LOG -> Wood().blockBreak(e, player, block, plugin)
            else -> Point().ore(e, player, block, plugin)
        }
    }

    @EventHandler
    fun onBlockPlaceEvent(e: BlockPlaceEvent) {
        if (e.player.world.name != "BATTLE") { return }
        GameSystem().adventure(e, e.player)
    }

    @EventHandler
    fun onEntityDeathEvent(e: EntityDeathEvent) {
        // キル
        val killer = e.entity.killer
        val mob = e.entity
        if (GET().status() && killer is Player) {
            Point().add(killer, 1, true)
            Data.DataManager.gameData.goldenGolem.remove(mob)
            Data.DataManager.gameData.zombie.remove(mob)
        }
        if (mob.scoreboardTags.contains("redPet")) {
            Data.DataManager.teamDataMap["red"]?.petCount = Data.DataManager.teamDataMap["red"]?.petCount!! - 1
        } else if (mob.scoreboardTags.contains("bluePet")) {
            Data.DataManager.teamDataMap["blue"]?.petCount = Data.DataManager.teamDataMap["blue"]?.petCount!! - 1
        } else if (GET().shop(mob)) {
            Shop().kill(mob as Villager)
        }
    }

    @EventHandler
    fun onEntityRegainHealthEvent(e: EntityRegainHealthEvent) {
        // ショップが回復したときにHP反映させる
        if (e.entity !is Villager) { return }
        val shop = e.entity as Villager
        val amout = e.amount
        if (GET().shop(shop)) {
            Shop().recovery(shop, amout)
        }
    }

    @EventHandler
    fun onEntityTargetEvent(e: EntityTargetEvent) {
        // 敵対化
        val entity = e.entity
        var radius = 0.0
        val target: EntityType?
        when {
            entity.scoreboardTags.contains("targetshop") -> {
                target = EntityType.VILLAGER
                radius = 100.0
            }
            entity.scoreboardTags.contains("targetPlayer") -> {
                target = EntityType.PLAYER
                radius = 20.0
            }
            entity.scoreboardTags.contains("targetZombie") -> {
                target = EntityType.ZOMBIE
                radius = 100.0
            }
            entity.scoreboardTags.contains("friendship") -> {
                target = null
            }
            else -> { return }
        }
        e.target = GET().getNearestEntityOfType(entity.location, target, radius)
    }
    @EventHandler
    fun onSignChangeEvent(e: SignChangeEvent) {
        // 看板に文字を決定したとき
        val block = e.block.type == Material.OAK_WALL_SIGN
        if (block) { Sign().make(e) }
    }

    @EventHandler
    fun onPlayerQuitEvent(e: PlayerQuitEvent) {
        // プレイヤーが抜けたとき
        if (Data.DataManager.gameData.participatingPlayer.contains(e.player)) { Team().inAndout(e.player) }
    }
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        if (GET().joinTeam(e.player)) { Data.DataManager.gameData.participatingPlayer.add(e.player) }
    }
    @EventHandler
    fun onPlayerTookDamage(e: EntityDamageEvent) {
        // ダメージを受けたプレイヤー
        val player = e.entity
        if (player !is Player) { return }

        if (player.scoreboardTags.contains("invincible")) {
            e.isCancelled = true
        } else if (e.damage > 0 && player.health <= e.damage) {
            Player().death(e, player, plugin)
        }
    }
    @EventHandler
    fun onBlockDamage(e: BlockDamageEvent) {
        // ブロックにダメージを与えたときの処理
        Block().notAppropriate(e.player.inventory.itemInMainHand, e.block, e)
    }
}
