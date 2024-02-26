package uk.co.scarfebread.wizardbeast.engine.state.server

import uk.co.scarfebread.wizardbeast.engine.state.publishable.PublishableState
import uk.co.scarfebread.wizardbeast.engine.state.publishable.action.NoAction
import uk.co.scarfebread.wizardbeast.engine.state.server.delta.ConnectionRule
import uk.co.scarfebread.wizardbeast.engine.state.server.delta.MoveRule
import uk.co.scarfebread.wizardbeast.engine.state.server.delta.PlayerStateChange
import uk.co.scarfebread.wizardbeast.entity.Player
import uk.co.scarfebread.wizardbeast.entity.PlayerRepository

class GameStateManager {
    private val players = mutableListOf<Player>()
    private val enemies = mutableListOf<String>()
    private val projectiles = mutableListOf<String>()

    private val stateSnapshots = mutableMapOf<Long, GameState>()

    fun createSnapshot(stateId: Long) {
        stateSnapshots[stateId] = GameState(
            players.toList().map { it.copy() },
            enemies.toList(), // TODO copy
            projectiles.toList(), // TODO copy
        )

        stateSnapshots.remove(stateId - 64)
    }

    fun createPlayerRepository(): PlayerRepository {
        return PlayerRepository(players)
    }

    fun publishState(snapshotId: Long, player: Player): PublishableState {
        val snapshot = stateSnapshots[snapshotId]!!
        val previousSnapshot = stateSnapshots.getOrDefault(player.lastConfirmedState, GameState.initialState())

        val playerStateChange = PlayerStateChange(
            previousSnapshot.players.firstOrNull { it.id == player.id },
            snapshot.players.firstOrNull { it.id == player.id },
        )

        return PublishableState(
            snapshotId,
            player.toState(),
            buildPlayerStateChange(player, snapshot, previousSnapshot)
                .map { otherPlayerStateChange ->
                    listOf(
                        MoveRule(),
                        ConnectionRule(),
                    ).map {
                        it.apply(
                            playerStateChange,
                            otherPlayerStateChange
                        )
                    }
                }
                .flatten()
                .filter { it != NoAction },
            snapshot.projectiles,
            snapshot.enemies,
            snapshot.timestamp
        )
    }

    private fun buildPlayerStateChange(player: Player, snapshot: GameState, previousSnapshot: GameState): List<PlayerStateChange> {
        val otherPlayerStateChanges = snapshot.players
            .filter { it.id != player.id }
            .map { newPlayerState ->
                PlayerStateChange(
                    previousSnapshot.players.firstOrNull { it.id == newPlayerState.id },
                    newPlayerState,
                )
            }
            .toMutableList()
        otherPlayerStateChanges.addAll(
            previousSnapshot.players
                .filter { it.id != player.id }
                .filter { previousSnapshotPlayer ->
                    otherPlayerStateChanges.firstOrNull { it.current?.id == previousSnapshotPlayer.id } != null
                }
                .map { oldPlayerState ->
                    PlayerStateChange(
                        oldPlayerState,
                        snapshot.players.firstOrNull { it.id == oldPlayerState.id },
                    )
                }
        )

        return otherPlayerStateChanges
    }
}
