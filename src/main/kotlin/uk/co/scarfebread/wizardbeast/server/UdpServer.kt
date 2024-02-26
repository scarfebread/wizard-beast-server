package uk.co.scarfebread.wizardbeast.server

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.aSocket
import io.ktor.utils.io.core.readUTF8Line
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import uk.co.scarfebread.wizardbeast.event.EventService
import uk.co.scarfebread.wizardbeast.event.RegisterEvent
import uk.co.scarfebread.wizardbeast.server.request.RegisterRequest
import uk.co.scarfebread.wizardbeast.server.request.Request
import uk.co.scarfebread.wizardbeast.state.PlayerState

class UdpServer(
    private val eventService: EventService,
    private val json: Json = Json { ignoreUnknownKeys = true }
) {
    suspend fun start() = runBlocking {
        val serverSocket = aSocket(SelectorManager(Dispatchers.IO))
            .udp()
            .bind(InetSocketAddress("127.0.0.1", 9002))

        while (true) {
            val datagram = serverSocket.receive()
            val message = datagram.packet.readUTF8Line() ?: continue

            runCatching {
                when (message.deserialise<Request>().action) {
                    "register" -> eventService.register(RegisterEvent(message.deserialise<RegisterRequest>(), datagram.address))
                    "deregister" -> {
                        println("player deregistered")
                    }
                    "update" -> {
                        // or something more specific?
                        // create player event
                    }
                    else -> println("unknown event")
                }
            }.onFailure {
                println(it.message)
            }
        }
    }

    private inline fun <reified T> String.deserialise() = json.decodeFromString<T>(this)
}
