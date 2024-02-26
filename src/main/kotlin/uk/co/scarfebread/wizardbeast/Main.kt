package uk.co.scarfebread.wizardbeast

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import uk.co.scarfebread.wizardbeast.client.UdpClient
import uk.co.scarfebread.wizardbeast.engine.GameStateEngine
import uk.co.scarfebread.wizardbeast.event.EventService
import uk.co.scarfebread.wizardbeast.state.PlayerState
import uk.co.scarfebread.wizardbeast.server.UdpServer

fun main() = runBlocking {
    val serverSocket = aSocket(SelectorManager(Dispatchers.IO))
        .udp()
        .bind(InetSocketAddress("127.0.0.1", 9002))
    val udpClient = UdpClient(serverSocket)

    val playerState = PlayerState()
    val eventService = EventService(playerState, udpClient)

    launch {
        UdpServer(serverSocket, eventService).start()
    }

    GameStateEngine(playerState).start()
}
