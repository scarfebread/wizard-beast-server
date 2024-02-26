package uk.co.scarfebread.wizardbeast.event

import io.ktor.network.sockets.SocketAddress
import uk.co.scarfebread.wizardbeast.server.request.AcknowledgeRequest

data class AcknowledgeEvent(val request: AcknowledgeRequest, val address: SocketAddress) : Event
