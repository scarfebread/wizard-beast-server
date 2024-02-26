package uk.co.scarfebread.wizardbeast.client

import io.ktor.network.sockets.BoundDatagramSocket
import io.ktor.network.sockets.Datagram
import io.ktor.network.sockets.SocketAddress
import io.ktor.utils.io.core.ByteReadPacket
import kotlinx.coroutines.runBlocking
import java.util.*

class UdpClient(private val serverSocket: BoundDatagramSocket) {
    fun send(
        event: String,
        message: String,
        address: SocketAddress,
        requestId: String = requestId()
    ) = runBlocking {
        serverSocket.send(Datagram(ByteReadPacket(
            message
                .toEvent(event, requestId)
                .encodeToByteArray()
        ), address))
    }

    private fun String.toEvent(event: String, requestId: String) = "$event--$this--$requestId"

    private fun requestId() = UUID.randomUUID().toString()

    companion object {
        const val EVENT_STATE = "state"
        const val EVENT_REGISTERED = "registered"
        const val EVENT_INVALID = "invalid"
    }
}
