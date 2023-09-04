package com.github.Ringoame196.Game

import org.bukkit.Bukkit
import org.bukkit.ChatColor

class Ranking {

    fun updateRankingScoreboard() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard players reset * ranking")
        val onlinePlayers = Bukkit.getOnlinePlayers()
        val topPlayers = onlinePlayers.sortedByDescending { player ->
            val score = Bukkit.getScoreboardManager()?.mainScoreboard
                ?.getObjective("RankingData_win")
                ?.getScore(player.uniqueId.toString())
            score?.score ?: 0
        }.take(3)

        for ((index, player) in topPlayers.withIndex()) {
            val name = "${ChatColor.AQUA}${index + 1}位${player.name}"
            val value = Scoreboard().getValue("RankingData_win", player.uniqueId.toString())
            Scoreboard().set("ranking", name, value)
        }
    }
}
