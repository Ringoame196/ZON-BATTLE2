package com.github.Ringoame196.Game

import com.github.Ringoame196.ParticipatingPlayer
import com.github.Ringoame196.data.Data
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import kotlin.random.Random

class Timer {
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
        object : BukkitRunnable() {
            override fun run() {
                ParticipatingPlayer().message("${ChatColor.RED}フィーバータイム終了！")
                ParticipatingPlayer().sound(Sound.BLOCK_ANVIL_USE)
                Data.DataManager.gameData.fever = false
                Data.DataManager.gameData.magnification /= 2
            }
        }.runTaskLater(plugin, 60 * 20L)
    }
}
