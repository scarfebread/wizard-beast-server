package uk.co.scarfebread.wizardbeast.client

import io.ktor.network.sockets.BoundDatagramSocket
import io.ktor.network.sockets.Datagram
import io.ktor.network.sockets.SocketAddress
import io.ktor.utils.io.core.ByteReadPacket
import kotlinx.coroutines.runBlocking

class UdpClient(private val serverSocket: BoundDatagramSocket) {
    fun send(message: String, address: SocketAddress) = runBlocking {
        serverSocket.send(Datagram(ByteReadPacket(message.encodeToByteArray()), address))
    }
}
