package uk.co.scarfebread.wizardbeast.server

import io.ktor.network.sockets.BoundDatagramSocket
import io.ktor.network.sockets.Datagram
import io.ktor.network.sockets.SocketAddress
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUTF8Line
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import uk.co.scarfebread.wizardbeast.event.Event
import uk.co.scarfebread.wizardbeast.event.EventService
import uk.co.scarfebread.wizardbeast.event.PlayerActionEvent
import uk.co.scarfebread.wizardbeast.event.RegisterEvent
import uk.co.scarfebread.wizardbeast.server.request.PlayerActionRequest
import uk.co.scarfebread.wizardbeast.server.request.RegisterRequest
import uk.co.scarfebread.wizardbeast.server.request.Request

class UdpServer(
    private val serverSocket: BoundDatagramSocket,
    private val eventService: EventService,
    private val json: Json = Json { ignoreUnknownKeys = true }
) {
    suspend fun start() = runBlocking {
        while (true) {
            // TODO authorisation
            val datagram = serverSocket.receive()
            val message = datagram.packet.readUTF8Line() ?: continue

            runCatching {
                when (message.deserialise<Request>().action) {
                    "register" -> eventService.register(RegisterEvent(message.deserialise<RegisterRequest>(), datagram.address))
                    "deregister" -> eventService.register(Dereeg(message.deserialise<RegisterRequest>(), datagram.address))
                    "update" -> eventService.register(PlayerActionEvent(message.deserialise<PlayerActionRequest>(), datagram.address))
                    else -> send("unknown event", datagram.address)
                }
            }.onFailure {
                println(it.message)
                println(it.stackTrace)
                send("invalid event", datagram.address)
            }
        }
    }

    private inline fun <reified T> String.deserialise() = json.decodeFromString<T>(this)

    private suspend fun send(message: String, address: SocketAddress) = serverSocket.send(Datagram(ByteReadPacket(message.encodeToByteArray()), address))
}
