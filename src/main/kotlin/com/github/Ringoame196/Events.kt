package com.github.Ringoame196

import com.github.Ringoame196.Entity.Shop
import com.github.Ringoame196.Entity.Zombie
import com.github.Ringoame196.Game.GameSystem
import com.github.Ringoame196.Game.Point
import com.github.Ringoame196.Game.Scoreboard
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.EntityType
import org.bukkit.entity.Fireball
import org.bukkit.entity.Player
import org.bukkit.entity.Shulker
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
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.plugin.Plugin
import org.spigotmc.event.entity.EntityDismountEvent

class Events(private val plugin: Plugin) : Listener {

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEntityEvent) {
        // ショップGUIを開く
        val player = e.player
        val entity = e.rightClicked
        val teamName = GET().teamName(player) ?: return
        if (GET().shop(entity)) {
            if (!entity.scoreboardTags.contains("center")) {
                Shop().open(e, player, entity as Villager, teamName)
            }
        }
    }

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        // GUIクリック
        val player = e.whoClicked as Player
        val item = e.currentItem ?: return
        val guiName = e.view.title
        GUI().clickSystem(plugin, e, player, guiName, item)
    }

    @EventHandler
    fun onInventoryClose(e: InventoryCloseEvent) {
        // インベントリを閉じる時のイベント
        val player = e.player as Player
        val title = e.view.title
        val inventory = e.inventory
        when (title) {
            "${ChatColor.DARK_GREEN}チームチェスト" -> player.playSound(player, Sound.BLOCK_CHEST_CLOSE, 1f, 1f)
            "${ChatColor.DARK_GREEN}金床" -> Anvil().returnItem(player, inventory)
            else -> return
        }
    }

    @EventHandler
    fun onEntityDamageByEntity(e: EntityDamageByEntityEvent) {
        val entity = e.entity
        val damager = e.damager
        val damage = e.finalDamage.toInt()
        if (entity.scoreboardTags.contains("invincible")) {
            // 無敵タグ
            e.isCancelled = true
        } else if (damager is org.bukkit.entity.Zombie) {
            // ゾンビの特殊能力
            Zombie().attack(damager, entity, e)
        } else if (damager is Shulker && entity !is org.bukkit.entity.Zombie) {
            // シュルカー 仲間にダメージを与えない
            e.isCancelled = true
        } else if (damager is Fireball && entity !is org.bukkit.entity.Zombie) {
            // ファイヤーボール ゾンビ以外にダメージを与えない
            e.isCancelled = true
        } else if (damager is Player && entity.scoreboardTags.contains("friend")) {
            // friendというタグをつけていると プレイヤーからのダメージを無効にする
            if (damager.gameMode != GameMode.CREATIVE) {
                e.isCancelled = true
                return
            }
        }
        when (entity) {
            is Villager -> Shop().attack(e, damager, entity)
            is org.bukkit.entity.Zombie -> Zombie().damage(entity)
            is Player -> Player().showdamage(damager, entity, damage)
            else -> {}
        }
    }

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {
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
    fun onBlockBreak(e: BlockBreakEvent) {
        // ブロックを破壊したとき
        val player = e.player
        val worldName = player.world.name
        if (worldName != "BATTLE" && worldName != "jikken") {
            return
        }
        GameSystem().adventure(e, player)
        val block = e.block
        when (block.type) {
            Material.OAK_LOG -> Wood().blockBreak(e, player, block, plugin)
            else -> Point().ore(e, player, block, plugin)
        }
    }

    @EventHandler
    fun onBlockPlace(e: BlockPlaceEvent) {
        if (e.player.world.name != "BATTLE") {
            return
        }
        GameSystem().adventure(e, e.player)
    }

    @EventHandler
    fun onEntityDeath(e: EntityDeathEvent) {
        // キル
        val killer = e.entity.killer
        val mob = e.entity
        if (GET().status() && killer is Player) {
            Point().add(killer, 1, true)
            Data.DataManager.gameData.goldenGolem.remove(mob)
            Data.DataManager.gameData.zombie.remove(mob)
        }
        if (mob.scoreboardTags.contains("redPet")) {
            Scoreboard().remove("RedTeamSystem", "petCount", 1)
        } else if (mob.scoreboardTags.contains("bluePet")) {
            Scoreboard().remove("BlueTeamSystem", "petCount", 1)
        } else if (GET().shop(mob)) {
            Shop().kill(mob as Villager)
        }
    }

    @EventHandler
    fun onEntityRegainHealth(e: EntityRegainHealthEvent) {
        // ショップが回復したときにHP反映させる
        if (e.entity !is Villager) {
            return
        }
        val shop = e.entity as Villager
        val amout = e.amount
        if (GET().shop(shop)) {
            Shop().recovery(shop, amout)
        }
    }

    @EventHandler
    fun onEntityTarget(e: EntityTargetEvent) {
        // 敵対化
        val entity = e.entity
        var radius = 0.0
        val target: EntityType?
        var tag: String? = null
        val team = when {
            entity.scoreboardTags.contains("red") -> "red"
            entity.scoreboardTags.contains("blue") -> "blue"
            else -> ""
        }
        when {
            entity.scoreboardTags.contains("targetshop") -> {
                target = EntityType.VILLAGER
                radius = 150.0
                tag = GET().opposingTeamname(team)
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

            else -> {
                return
            }
        }
        e.target = GET().getNearestEntityOfType(entity.location, target, radius, tag)
    }

    @EventHandler
    fun onSignChange(e: SignChangeEvent) {
        // 看板に文字を決定したとき
        val block = e.block.type == Material.OAK_WALL_SIGN
        if (block) {
            Sign().make(e)
        }
    }

    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        // プレイヤーが抜けたとき
        Scoreboard().getValue("participatingPlayer", e.player.name)
        Scoreboard().set("participatingPlayer", e.player.name, 0)
    }

    @EventHandler
    fun onPlayerTookDamage(e: EntityDamageEvent) {
        // 殺したときにポイントを与える
        val player = e.entity
        if (player !is Player) {
            return
        }
        if (e.damage > 0 && player.health <= e.damage) {
            Player().death(e, player, plugin)
        }
    }

    @EventHandler
    fun onBlockDamage(e: BlockDamageEvent) {
        // ブロックにダメージを与えたときの処理
        Block().notAppropriate(e.player.inventory.itemInMainHand, e.block, e)
    }

    @EventHandler
    fun onPlayerToggleSneak(e: PlayerToggleSneakEvent) {
        val isSneak = e.isSneaking
        val player = e.player
        val item = e.player.inventory.itemInMainHand
        if (isSneak && item.itemMeta?.displayName?.contains("[ゾンビ召喚]") == true) {
            Zombie().offhandSet(player)
        } else if (player.location.add(0.0, -1.0, 0.0).block.type == Material.BEACON) {
            Mission().blockClick(player, player.location.add(0.0, -1.0, 0.0).block, plugin)
        }
    }
    @EventHandler
    fun ona(e: EntityDismountEvent) {
        val player = e.entity
        val entity = e.dismounted
        if (player !is Player) { return }
        when (entity.customName) {
            "${ChatColor.DARK_RED}誘拐犯" -> Zombie().ransom(player, entity, e)
        }
    }
}
