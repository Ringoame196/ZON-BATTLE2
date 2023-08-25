package com.github.Ringoame196.Game

import com.github.Ringoame196.Block
import com.github.Ringoame196.Entity.Blaze
import com.github.Ringoame196.Entity.Golem
import com.github.Ringoame196.Entity.Zombie
import com.github.Ringoame196.ParticipatingPlayer
import com.github.Ringoame196.RandomChest
import com.github.Ringoame196.data.Data
import com.github.Ringoame196.data.GET
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.boss.BarColor
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import kotlin.random.Random

class Timer {
    private var timerTask: BukkitTask? = null
    fun GameTimer(plugin: Plugin) {
        timerTask = object : BukkitRunnable() {
            override fun run() {
                if (!GET().status()) {
                    this.cancel()
                    return
                }
                Data.DataManager.gameData.time += 1
                regularly(plugin)
                val randomChestTime = 300 - (Data.DataManager.gameData.time % 300)
                Data.DataManager.gameData.randomChestTitle?.customName = "${ChatColor.AQUA}${GET().minutes(randomChestTime)}"
            }
        }.runTaskTimer(plugin, 0L, 20L)
    }
    fun regularly(plugin: Plugin) {
        val time = Data.DataManager.gameData.time
        if (time == 1200) {
            ParticipatingPlayer().message("${ChatColor.RED}20分経ったためポイントが2倍になりました")
            ParticipatingPlayer().sound(Sound.BLOCK_ANVIL_USE)
            Data.DataManager.gameData.magnification = 2
        }
        if (Data.DataManager.gameData.fever) {
            Data.DataManager.gameData.bossBar.setTitle("${ChatColor.YELLOW}フィーバータイム！！")
        } else {
            if (time <= 300) {
                val remaining = 300 - time
                Data.DataManager.gameData.bossBar.setTitle("${ChatColor.YELLOW}ゾンビ解放まで${GET().minutes(remaining)}")
            } else if (time <= 1200) {
                val remaining = 1200 - time
                Data.DataManager.gameData.bossBar.setTitle("${ChatColor.AQUA}ポイント2倍まで${GET().minutes(remaining)}")
            }
        }
        if (time == 300) { ParticipatingPlayer().message("${ChatColor.YELLOW}ゾンビ解放!") }
        if (time % 300 == 0) { RandomChest().set() }
        if (time % 17 == 0) { Zombie().summonner("§5エンペラー", "shield", "soldier") }
        if (time % 5 == 0) {
            Golem().golden()
            Blaze().attack()
        }
        if (time % 7 == 0) { Zombie().summonner("§5ネクロマンサー", "normal", "normal") }
        if (time == Data.DataManager.gameData.feverTime) { Timer().feverActivation(plugin) }
    }
    fun feverSet() {
        val feverSetTime: MutableList<Int> = mutableListOf(20, 30, 40)
        val r = Random.nextInt(0, feverSetTime.size)
        Data.DataManager.gameData.feverTime = feverSetTime.get(r)
    }
    fun feverActivation(plugin: Plugin) {
        ParticipatingPlayer().message("${ChatColor.YELLOW}フィーバータイム開始！")
        ParticipatingPlayer().sound(Sound.BLOCK_BELL_USE)
        Data.DataManager.gameData.fever = true
        Data.DataManager.gameData.magnification *= 2
        Block().deleteRevival()
        Data.DataManager.gameData.bossBar.color = BarColor.YELLOW
        object : BukkitRunnable() {
            override fun run() {
                ParticipatingPlayer().message("${ChatColor.RED}フィーバータイム終了！")
                ParticipatingPlayer().sound(Sound.BLOCK_ANVIL_USE)
                Data.DataManager.gameData.fever = false
                Data.DataManager.gameData.magnification /= 2
                Data.DataManager.gameData.bossBar.color = BarColor.BLUE
            }
        }.runTaskLater(plugin, 60 * 20L)
    }
}
