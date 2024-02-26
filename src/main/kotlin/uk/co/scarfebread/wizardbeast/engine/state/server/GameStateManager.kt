package uk.co.scarfebread.wizardbeast.engine.state.server

import uk.co.scarfebread.wizardbeast.engine.state.publishable.PublishableState
import uk.co.scarfebread.wizardbeast.engine.state.publishable.action.ConnectAction
import uk.co.scarfebread.wizardbeast.engine.state.publishable.action.DisconnectAction
import uk.co.scarfebread.wizardbeast.engine.state.publishable.action.MoveAction
import uk.co.scarfebread.wizardbeast.engine.state.publishable.action.NoAction
import uk.co.scarfebread.wizardbeast.entity.Player
import uk.co.scarfebread.wizardbeast.entity.PlayerRepository

class GameStateManager() {
    private val players = mutableListOf<Player>()
    private val enemies = mutableListOf<String>()
    private val projectiles = mutableListOf<String>()

    private val gameState = GameState.initialState()
    private val stateSnapshots = mutableMapOf<Long, GameState>()

    fun createSnapshot(stateId: Long) {
        stateSnapshots[stateId] = GameState(
            players.toList(),
            enemies.toList(),
            projectiles.toList(),
        )

        stateSnapshots.remove(stateId - 64)
    }

    fun createPlayerRepository(): PlayerRepository {
        return PlayerRepository(players)
    }

    fun publishState(snapshotId: Long, player: Player): PublishableState {
        // TODO only send clients info they need to know (should be interesting to implement!)
        // TODO only send clients player data that they can see
        // TODO use compression https://stackoverflow.com/questions/13477321/fastest-way-to-gzip-and-udp-a-large-amount-of-strings-in-java

        val snapshot = stateSnapshots[snapshotId]!!
        val previousSnapshot = stateSnapshots.getOrDefault(player.lastConfirmedState, GameState.initialState())
        val previousPlayerState = previousSnapshot.players
            .firstOrNull { it.id == player.id }

        val playerActions = snapshot.players
            .filter { it.id != player.id }
            .map { newPlayerState ->
            val oldPlayerState = previousSnapshot.players.firstOrNull { it.id == newPlayerState.id }

            if (oldPlayerState == null) {
                ConnectAction(newPlayerState.name, newPlayerState.x, newPlayerState.y)
            } else {
                if (oldPlayerState.x != newPlayerState.x || oldPlayerState.y != newPlayerState.y) {
                    when(true) {
                        player.isCloseTo(newPlayerState) -> MoveAction(newPlayerState.name, newPlayerState.x, newPlayerState.y)
                        (previousPlayerState != null && previousPlayerState.isCloseTo(oldPlayerState)) -> MoveAction(newPlayerState.name, newPlayerState.x, newPlayerState.y)
                        else -> NoAction
                    }
                } else {
                    NoAction
                }
            }
        }.toMutableList()

        playerActions.addAll(
            previousSnapshot.players
                .filter { it.id != player.id }
                .map { oldPlayerState ->
                val newPlayerState = snapshot.players.firstOrNull { it.id == oldPlayerState.id }

                if (newPlayerState == null) {
                    DisconnectAction(oldPlayerState.name)
                } else {
                    NoAction
                }
            }
        )

        return PublishableState(
            player.toState(),
            playerActions.filter { it != NoAction },
            snapshot.projectiles,
            snapshot.enemies,
            snapshot.timestamp
        )
    }
}
