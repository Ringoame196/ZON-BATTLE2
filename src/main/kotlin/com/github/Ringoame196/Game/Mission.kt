package com.github.Ringoame196.Game

import com.github.Ringoame196.Entity.Shop
import com.github.Ringoame196.Give
import com.github.Ringoame196.data.GET
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random

class Mission {
    @Suppress("DEPRECATION")
    fun set(teamName: String, plugin: Plugin) {
        val clickCount = Random.nextInt(3, 14)
        val bossbar = Bukkit.createBossBar("ミッション", BarColor.RED, BarStyle.SEGMENTED_10)
        Scoreboard().set(GET().getTeamScoreName(teamName), "ミッション", clickCount)
        for (loopPlayer in Bukkit.getOnlinePlayers()) {
            if (GET().teamName(loopPlayer) == teamName) {
                loopPlayer.sendTitle("${ChatColor.AQUA}ミッション", "ビーコンの上で${clickCount}回シフト！")
                loopPlayer.playSound(loopPlayer, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1f, 1f)
                bossbar.addPlayer(loopPlayer)
            }
        }
        countTime(plugin, bossbar, teamName)
    }
    fun countTime(plugin: Plugin, bossBar: BossBar, teamName: String) {
        var countTime = 40
        object : BukkitRunnable() {
            override fun run() {
                if (countTime > 0) {
                    val count = Scoreboard().getValue(GET().getTeamScoreName(teamName), "ミッション")
                    countTime--
                    if (count == 0) {
                        bossBar.setTitle("${ChatColor.YELLOW}ミッションクリア")
                    } else {
                        bossBar.setTitle("${ChatColor.RED}ミッション:${count}個 (残り${countTime}秒)")
                    }
                } else {
                    bossBar.removeAll()
                    check(teamName)
                    this.cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, 20L)
    }
    fun check(teamName: String) {
        val clear = Scoreboard().getValue(GET().getTeamScoreName(teamName), "ミッション")
        for (loopPlayer in Bukkit.getOnlinePlayers()) {
            val loopPlayerTeam = GET().teamName(loopPlayer) ?: continue
            if (loopPlayerTeam != teamName) { continue }
            if (clear == 0) {
                loopPlayer.sendMessage("${ChatColor.YELLOW}クリア")
            } else {
                loopPlayer.sendMessage("${ChatColor.RED}失敗")
                GET().getTeamshop(teamName)?.let { Shop().attack((clear * 5).toDouble(), null, it) }
                Scoreboard().set(GET().getTeamScoreName(teamName), "ミッション", 0)
            }
        }
    }
    fun blockClick(player: Player, block: Block?, plugin: Plugin) {
        if (block == null) { return }

        val team = GET().teamName(player)
        if (Scoreboard().getValue(GET().getTeamScoreName(team), "ミッション") == 0) {
            return
        }
        Scoreboard().remove(GET().getTeamScoreName(team), "ミッション", 1)
        player.sendMessage("${ChatColor.GREEN}-1")
        player.inventory.addItem(Give().point(150))
        player.inventory.addItem(ItemStack(Material.STICK, 5))
        com.github.Ringoame196.Block().revival(plugin, block.location, 15, Material.BEACON, block.blockData)
    }
}
