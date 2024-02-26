package uk.co.scarfebread.wizardbeast.event

import uk.co.scarfebread.wizardbeast.client.UdpClient
import uk.co.scarfebread.wizardbeast.state.Player
import uk.co.scarfebread.wizardbeast.state.PlayerState

class EventService(
    private val playerState: PlayerState,
    private val udpClient: UdpClient
) {
    fun register(event: Event) {
        when(event) {
            is RegisterEvent -> {
                playerState.addPlayer(
                    Player(
                        name = event.request.name,
                        address = event.address
                    )
                ).run {
                    udpClient.send(this.id, event.address)
                }
            }
            is PlayerActionEvent -> {
                playerState.getPlayer(event.request.id)?.run {
                    event.request.actions.forEach {
                        when(it.action) {
                            "x" -> this.x = it.value
                            "y" -> this.y = it.value
                        }
                    }
                }
                udpClient.send("acknowledged", event.address)
            }
        }
    }
}
