package uk.co.scarfebread.wizardbeast.event

import io.ktor.network.sockets.SocketAddress
import uk.co.scarfebread.wizardbeast.server.request.DeregisterRequest

data class DeregisterEvent(val request: DeregisterRequest, val address: SocketAddress) : Event
