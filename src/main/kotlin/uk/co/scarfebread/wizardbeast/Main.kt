package uk.co.scarfebread.wizardbeast

import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import uk.co.scarfebread.wizardbeast.engine.GameStateEngine
import uk.co.scarfebread.wizardbeast.event.EventService
import uk.co.scarfebread.wizardbeast.state.PlayerState
import uk.co.scarfebread.wizardbeast.server.UdpServer

fun main() = runBlocking {
    val playerState = PlayerState()
    val eventService = EventService(playerState)

    launch {
        UdpServer(eventService).start()
    }

    GameStateEngine(playerState).start()
}
