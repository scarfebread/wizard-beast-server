package uk.co.scarfebread.wizardbeast

import io.ktor.network.selector.SelectorManager
import io.ktor.network.sockets.InetSocketAddress
import io.ktor.network.sockets.aSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import uk.co.scarfebread.wizardbeast.client.UdpClient
import uk.co.scarfebread.wizardbeast.engine.GameStateEngine
import uk.co.scarfebread.wizardbeast.engine.state.server.GameStateManager
import uk.co.scarfebread.wizardbeast.event.EventService
import uk.co.scarfebread.wizardbeast.server.UdpServer

fun main() = runBlocking {
    val serverSocket = aSocket(SelectorManager(Dispatchers.IO))
        .udp()
        .bind(InetSocketAddress("127.0.0.1", 9002))
    val udpClient = UdpClient(serverSocket)

    val gameStateManager = GameStateManager()
    val playerRepository = gameStateManager.createPlayerRepository()
    val eventService = EventService(playerRepository, udpClient)

    launch {
        UdpServer(
            serverSocket,
            eventService
        ).start()
    }

    GameStateEngine(
        playerRepository,
        gameStateManager,
        udpClient,
    ).start()
}
