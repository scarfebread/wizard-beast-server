package uk.co.scarfebread.wizardbeast.server

import io.ktor.network.sockets.BoundDatagramSocket
import io.ktor.utils.io.core.readUTF8Line
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import uk.co.scarfebread.wizardbeast.client.UdpClient
import uk.co.scarfebread.wizardbeast.client.UdpClient.Companion.EVENT_INVALID
import uk.co.scarfebread.wizardbeast.event.DeregisterEvent
import uk.co.scarfebread.wizardbeast.event.EventService
import uk.co.scarfebread.wizardbeast.event.PlayerActionEvent
import uk.co.scarfebread.wizardbeast.event.RegisterEvent
import uk.co.scarfebread.wizardbeast.server.request.DeregisterRequest
import uk.co.scarfebread.wizardbeast.server.request.PlayerActionRequest
import uk.co.scarfebread.wizardbeast.server.request.RegisterRequest
import uk.co.scarfebread.wizardbeast.server.request.Request

class UdpServer(
    private val serverSocket: BoundDatagramSocket,
    private val udpClient: UdpClient,
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
                    "deregister" -> eventService.register(DeregisterEvent(message.deserialise<DeregisterRequest>(), datagram.address))
                    "update" -> eventService.register(PlayerActionEvent(message.deserialise<PlayerActionRequest>(), datagram.address))
                    else -> udpClient.send(EVENT_INVALID, "unknown request", datagram.address)
                }
            }.onFailure {
                println(it.message)
                println(it.stackTrace)
                udpClient.send(EVENT_INVALID, "invalid request", datagram.address)
            }
        }
    }

    private inline fun <reified T> String.deserialise() = json.decodeFromString<T>(this)
}
