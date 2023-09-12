package com.github.Ringoame196.Game

import com.github.Ringoame196.Block
import com.github.Ringoame196.Entity.Blaze
import com.github.Ringoame196.Entity.Golem
import com.github.Ringoame196.Entity.Minion
import com.github.Ringoame196.Entity.PotionShop
import com.github.Ringoame196.Entity.Zombie
import com.github.Ringoame196.ParticipatingPlayer
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
                Scoreboard().add("gameData", "time", 1)
                regularly(plugin)
                val randomChestTime = 300 - (GET().gameTime() % 300)
                for (title in Data.DataManager.gameData.randomChestTitle) {
                    title.customName = "${ChatColor.AQUA}${GET().minutes(randomChestTime)}"
                }
            }
        }.runTaskTimer(plugin, 0L, 20L)
    }
    fun regularly(plugin: Plugin) {
        val time = GET().gameTime()
        val timeLimit = Scoreboard().getValue("gameData", "timeLimit")
        Point().fountain()
        if (timeLimit != 0) {
            Scoreboard().set("RedTeam", "${ChatColor.YELLOW}制限時間", time - timeLimit)
            Scoreboard().set("BlueTeam", "${ChatColor.YELLOW}制限時間", time - timeLimit)
            if (time == timeLimit) {
                GameSystem().gameEndSystem("${ChatColor.AQUA}村人を守り抜いた", "red")
            }
        }
        if (time == 1200) {
            ParticipatingPlayer().message("${ChatColor.RED}20分経ったためポイントが2倍になりました")
            ParticipatingPlayer().sound(Sound.BLOCK_ANVIL_USE)
            Scoreboard().set("gameData", "magnification", 2)
        }
        if (Scoreboard().getValue("gameData", "fever") == 1) {
            Data.DataManager.gameData.bossBar.setTitle("${ChatColor.YELLOW}フィーバータイム！！")
        } else {
            if (time <= 300) {
                val remaining = 300 - time
                Data.DataManager.gameData.bossBar.setTitle("${ChatColor.YELLOW}ゾンビ解放まで${GET().minutes(remaining)}")
            } else if (time <= 1200) {
                val remaining = 1200 - time
                if (Scoreboard().getValue("gameData", "map") == 3) {
                    Data.DataManager.gameData.bossBar.setTitle("${ChatColor.RED}制限時間${GET().minutes(timeLimit - time)}")
                } else {
                    Data.DataManager.gameData.bossBar.setTitle("${ChatColor.AQUA}ポイント2倍まで${GET().minutes(remaining)}")
                }
            }
        }
        if (time == 300) {
            ParticipatingPlayer().message("${ChatColor.YELLOW}ゾンビ解放!")
            if (Scoreboard().getValue("gameData", "map") == 3) {
                Scoreboard().set("RedTeamSystem", "magnification", 2)
            }
        }
        if (time % 300 == 0) {
            Map().randomChest()
        }
        if (time % 17 == 0) { Zombie().summonner("§5エンペラー", "シールドゾンビ", "ゾンビソルジャー") }
        if (time % 5 == 0) {
            Golem().golden()
        }
        if (time % 3 == 0) {
            Blaze().attack()
        }
        if (time % 14 == 0) {
            PotionShop().give()
            Minion().loopMinion(plugin)
        }
        if (time % 7 == 0) { Zombie().summonner("§5ネクロマンサー", "ノーマルゾンビ", "ノーマルゾンビ") }
        if (time == Scoreboard().getValue("gameData", "feverTime")) { Timer().feverActivation(plugin) }
    }
    fun feverSet() {
        val feverSetTime: MutableList<Int> = mutableListOf(420, 600, 780)
        val r = Random.nextInt(0, feverSetTime.size)
        Scoreboard().set("gameData", "feverTime", feverSetTime.get(r))
    }
    fun feverActivation(plugin: Plugin) {
        ParticipatingPlayer().message("${ChatColor.YELLOW}フィーバータイム開始！")
        ParticipatingPlayer().sound(Sound.BLOCK_BELL_USE)
        Scoreboard().set("gameData", "fever", 1)
        Block().deleteRevival()
        Data.DataManager.gameData.bossBar.color = BarColor.YELLOW
        object : BukkitRunnable() {
            override fun run() {
                ParticipatingPlayer().message("${ChatColor.RED}フィーバータイム終了！")
                ParticipatingPlayer().sound(Sound.BLOCK_ANVIL_USE)
                Scoreboard().set("gameData", "fever", 0)
                Data.DataManager.gameData.bossBar.color = BarColor.BLUE
            }
        }.runTaskLater(plugin, 30 * 20L)
    }
}
