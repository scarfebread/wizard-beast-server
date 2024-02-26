package uk.co.scarfebread.wizardbeast.client

import io.ktor.network.sockets.BoundDatagramSocket
import io.ktor.network.sockets.Datagram
import io.ktor.network.sockets.SocketAddress
import io.ktor.utils.io.core.ByteReadPacket
import kotlinx.coroutines.runBlocking

class UdpClient(private val serverSocket: BoundDatagramSocket) {
    fun send(event: String, message: String, address: SocketAddress) = runBlocking {
        serverSocket.send(Datagram(ByteReadPacket(
            message
                .prefixEvent(event)
                .encodeToByteArray()
        ), address))
    }

    private fun String.prefixEvent(event: String) = "$event-$this"

    companion object {
        const val EVENT_STATE = "state"
        const val EVENT_REGISTERED = "registered"
        const val EVENT_INVALID = "invalid"
    }
}
