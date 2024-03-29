package uk.co.scarfebread.wizardbeast.engine

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import uk.co.scarfebread.wizardbeast.client.UdpClient
import uk.co.scarfebread.wizardbeast.client.UdpClient.Companion.EVENT_STATE
import uk.co.scarfebread.wizardbeast.engine.game.GameSimulator
import uk.co.scarfebread.wizardbeast.engine.state.server.GameStateManager
import uk.co.scarfebread.wizardbeast.entity.PlayerRepository
import java.lang.System.currentTimeMillis

class GameStateEngine(
    private val playerRepository: PlayerRepository,
    private val gameStateManager: GameStateManager,
    private val gameSimulator: GameSimulator,
    private val udpClient: UdpClient,
    private val json: Json = Json { ignoreUnknownKeys = true }
) {
    private var lastTick: Long = 0

    fun start() = runBlocking {
        runCatching {
            val networkTick = 1000f / 64f
            var snapshotId = 0L

            while (true) {
                val startTime = currentTimeMillis()

                gameSimulator.processMovement(
                    lastTick,
                    networkTick
                )
                gameStateManager.createSnapshot(snapshotId)

                playerRepository.getPlayers().forEach { player ->
                    // TODO use compression https://stackoverflow.com/questions/13477321/fastest-way-to-gzip-and-udp-a-large-amount-of-strings-in-java
                    udpClient.send(
                        EVENT_STATE,
                        json.encodeToString(
                            gameStateManager.publishState(snapshotId, player)
                            // TODO account for RTT
                        ),
                        player.address
                    )
                }

                val elapsed = currentTimeMillis() - startTime

                if (elapsed < networkTick) {
                    delay(networkTick.toLong() - elapsed)
                }

                snapshotId++
                lastTick = startTime
            }
        }.onFailure {
            println(it.message)
            it.printStackTrace()
        }
    }
}
