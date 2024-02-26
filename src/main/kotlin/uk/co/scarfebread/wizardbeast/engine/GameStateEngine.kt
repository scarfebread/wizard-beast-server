package uk.co.scarfebread.wizardbeast.engine

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import uk.co.scarfebread.wizardbeast.client.UdpClient
import uk.co.scarfebread.wizardbeast.client.UdpClient.Companion.EVENT_STATE
import uk.co.scarfebread.wizardbeast.engine.state.server.GameStateManager
import uk.co.scarfebread.wizardbeast.entity.PlayerRepository
import java.lang.System.currentTimeMillis

class GameStateEngine(
    private val playerRepository: PlayerRepository,
    private val gameStateManager: GameStateManager,
    private val udpClient: UdpClient,
    private val json: Json = Json { ignoreUnknownKeys = true }
) {
    fun start() = runBlocking {
        runCatching {
            val networkTick = 1000 / 64
            var snapshotId = 0L

            while (true) {
                val startTime = currentTimeMillis()

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
                    delay(networkTick - elapsed)
                }

                snapshotId++
            }
        }.onFailure {
            println(it.message)
            it.printStackTrace()
        }
    }
}
