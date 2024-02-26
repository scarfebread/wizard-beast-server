package uk.co.scarfebread.wizardbeast.event

import io.ktor.network.sockets.SocketAddress
import uk.co.scarfebread.wizardbeast.server.request.PlayerActionRequest

data class PlayerActionEvent(val request: PlayerActionRequest, val address: SocketAddress) : Event
