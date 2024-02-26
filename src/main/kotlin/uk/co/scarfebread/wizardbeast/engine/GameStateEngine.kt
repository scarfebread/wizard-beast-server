package uk.co.scarfebread.wizardbeast.engine

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import uk.co.scarfebread.wizardbeast.client.UdpClient
import uk.co.scarfebread.wizardbeast.event.EventService
import uk.co.scarfebread.wizardbeast.entity.PlayerRepository
import uk.co.scarfebread.wizardbeast.engine.state.server.GameStateManager
import java.lang.System.currentTimeMillis

class GameStateEngine(
    private val playerRepository: PlayerRepository,
    private val gameStateManager: GameStateManager,
    private val udpClient: UdpClient,
    private val json: Json = Json { ignoreUnknownKeys = true }
) {
    fun start() = runBlocking {
        val networkTick = 1000 / 64
        var snapshotId = 0L

        while (true) {
            val startTime = currentTimeMillis()

            gameStateManager.createSnapshot(snapshotId)

            playerRepository.getPlayers().forEach { player ->
                udpClient.send(
                    json.encodeToString(
                        gameStateManager.publishState(snapshotId, player)
                    ),
                    player.address
                )
            }

            val elapsed = currentTimeMillis() - startTime

            if (elapsed < networkTick) {
                delay(networkTick - elapsed)
            }

            snapshotId++
        }
    }
}
