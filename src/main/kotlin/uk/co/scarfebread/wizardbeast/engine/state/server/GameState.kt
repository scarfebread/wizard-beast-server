package uk.co.scarfebread.wizardbeast.engine.state.server

import uk.co.scarfebread.wizardbeast.entity.Player
import java.lang.System.currentTimeMillis

data class GameState(
    val players: List<Player>,
    val projectiles: List<String>, // TODO
    val enemies: List<String>, // TODO
    val timestamp: Long = currentTimeMillis()
) {
    companion object {
        fun initialState() = GameState(listOf(), listOf(), listOf())
    }
}
