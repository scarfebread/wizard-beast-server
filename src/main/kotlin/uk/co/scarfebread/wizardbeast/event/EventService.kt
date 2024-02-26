package uk.co.scarfebread.wizardbeast.event

import uk.co.scarfebread.wizardbeast.state.Player
import uk.co.scarfebread.wizardbeast.state.PlayerState

class EventService(private val playerState: PlayerState) {
    fun register(event: Event) {
        when(event) {
            is RegisterEvent -> {
                playerState.addPlayer(Player(
                    name = event.request.name,
                    address = event.address
                ))
                println("player added")
            }
        }
    }
}
