package uk.co.scarfebread.wizardbeast.event

import uk.co.scarfebread.wizardbeast.client.UdpClient
import uk.co.scarfebread.wizardbeast.entity.Player
import uk.co.scarfebread.wizardbeast.entity.PlayerRepository

class EventService(
    private val playerRepository: PlayerRepository,
    private val udpClient: UdpClient
) {
    private val events = mutableListOf<Event>()

    fun register(event: Event) {
        events.add(event)

        when(event) {
            is RegisterEvent -> {
                playerRepository.addPlayer(
                    Player(
                        name = event.request.name,
                        address = event.address
                    )
                ).run {
                    udpClient.send(this.id, event.address)
                }
            }
            is PlayerActionEvent -> { // TODO should this be move event?
                playerRepository.getPlayer(event.request.id)?.run {
                    event.request.actions.forEach {
                        when(it.action) {
                            "x" -> this.x = it.value
                            "y" -> this.y = it.value
                        }
                    }
                }
            }
            is AcknowledgeEvent -> {
                playerRepository.getPlayer(event.id)?.acknowledge(event.stateId)
            }
        }
    }

    fun consume() = events.let {
        val immutableCopy = events.toList()
        events.clear()
        immutableCopy
    }
}
