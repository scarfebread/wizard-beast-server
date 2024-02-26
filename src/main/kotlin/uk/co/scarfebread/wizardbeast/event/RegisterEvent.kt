package uk.co.scarfebread.wizardbeast.event

import io.ktor.network.sockets.SocketAddress
import uk.co.scarfebread.wizardbeast.server.request.RegisterRequest

data class RegisterEvent(val request: RegisterRequest, val address: SocketAddress) : Event
