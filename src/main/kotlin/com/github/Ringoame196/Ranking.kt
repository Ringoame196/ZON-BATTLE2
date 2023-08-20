package com.github.Ringoame196

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Score

class Ranking {
    fun addScore(playerName: String) {
        val scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard
        val objective: Objective = scoreboard?.getObjective("RankingData_win") ?: return

        val score: Score = objective.getScore(playerName)
        score.score += 1
    }

    fun updateRankingScoreboard() {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard players reset * ranking")
        val onlinePlayers = Bukkit.getOnlinePlayers()
        val topPlayers = onlinePlayers.sortedByDescending { player ->
            val score = Bukkit.getScoreboardManager()?.mainScoreboard
                ?.getObjective("RankingData_win")
                ?.getScore(player.name)
            score?.score ?: 0
        }.take(3)

        for ((index, player) in topPlayers.withIndex()) {
            val name = "${ChatColor.AQUA}${index + 1}位${player.name}"
            val command = "scoreboard players operation $name ranking = ${player.name} RankingData_win"
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
        }
    }
}
