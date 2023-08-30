package com.github.Ringoame196.Game

import org.bukkit.Bukkit

class Scoreboard {
    val scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard
    fun make(id: String, name: String) {
        if (scoreboard?.getObjective(id) != null) {
            delete(id)
        }
        scoreboard?.registerNewObjective(id, "dummy", name)
    }
    fun set(scoreName: String, name: String, value: Int) {
        val objective = scoreboard?.getObjective(scoreName) ?: return
        val score = objective.getScore(name)
        score.score = value
    }
    fun deleteValue(scoreName: String, name: String) {
        val objective = scoreboard?.getObjective(scoreName) ?: return
        val score = objective.getScore(name)
        scoreboard.resetScores(name)
    }
    fun getSize(scoreName: String): Int {
        val objective = scoreboard?.getObjective(scoreName) ?: return 0
        var scoreCount = 0
        for (entry in scoreboard.entries) {
            val score = objective.getScore(entry)
            if (score.isScoreSet) {
                scoreCount++
            }
        }
        return scoreCount
    }
    fun getValue(score: String, name: String): Int? {
        val player = Bukkit.getPlayer(name)
        val scoreboard = player?.scoreboard
        val objective = scoreboard?.getObjective(score) ?: return null
        val scoreObject = objective.getScore(name)
        return scoreObject.score
    }
    fun delete(score: String) {
        val objective = scoreboard?.getObjective(score) ?: return
        objective.unregister()
    }
}
