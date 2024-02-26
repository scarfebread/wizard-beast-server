package uk.co.scarfebread.wizardbeast.event

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import uk.co.scarfebread.wizardbeast.client.UdpClient
import uk.co.scarfebread.wizardbeast.client.UdpClient.Companion.EVENT_REGISTERED
import uk.co.scarfebread.wizardbeast.entity.Player
import uk.co.scarfebread.wizardbeast.entity.PlayerRepository

class EventService(
    private val playerRepository: PlayerRepository,
    private val udpClient: UdpClient,
    private val json: Json = Json { ignoreUnknownKeys = true }
) {
    fun register(event: Event, requestId: String) {
        when (event) {
            is RegisterEvent -> {
                playerRepository.addPlayer(
                    Player(
                        name = event.request.name,
                        address = event.address
                    )
                ).run {
                    udpClient.send(EVENT_REGISTERED, json.encodeToString(this.toState()), event.address, requestId)
                }
            }
            is DeregisterEvent -> {
                playerRepository.removePlayer(event.request.id)
            }
            is PlayerActionEvent -> { // TODO should this be move event?
                playerRepository.getPlayer(event.request.id)?.run {
                    event.request.actions.forEach {
                        when (it.action) {
                            "x" -> this.x = it.value.toFloat()
                            "y" -> this.y = it.value.toFloat()
                        }
                    }
                }
            }
            is AcknowledgeEvent -> {
                playerRepository.getPlayer(event.request.player)?.acknowledge(event.request.stateId)
            }
        }
    }
}
