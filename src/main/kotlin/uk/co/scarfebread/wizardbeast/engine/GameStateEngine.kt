package uk.co.scarfebread.wizardbeast.engine

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import uk.co.scarfebread.wizardbeast.state.PlayerState
import java.lang.System.currentTimeMillis

class GameStateEngine(private val playerState: PlayerState) {
    fun start() = runBlocking {
        val networkTick = 1000 / 64

        while (true) {
            val startTime = currentTimeMillis()

            playerState.getPlayers().forEach {
                // TODO send delta (not the state)
//                serverSocket.send(Datagram(ByteReadPacket("Hello".encodeToByteArray()), InetSocketAddress("127.0.0.1", 9002)))
            }

            // TODO only send clients info they need to know (should be interesting to implement!)
            // TODO only send clients player data that they can see
            // TODO client to ack state events so the server knows it will understand subsequent events
            // - see page 205 - the server knowing packet 207 has been acknowledged, packet 209 can be a delta between 207 and 209
            // TODO use compression https://stackoverflow.com/questions/13477321/fastest-way-to-gzip-and-udp-a-large-amount-of-strings-in-java

            val elapsed = currentTimeMillis() - startTime

            if (elapsed < networkTick) {
                delay(networkTick - elapsed)
            }
        }
    }
}
