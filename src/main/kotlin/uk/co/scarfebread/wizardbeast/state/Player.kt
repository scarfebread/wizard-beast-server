package uk.co.scarfebread.wizardbeast.state

import io.ktor.network.sockets.SocketAddress
import java.util.*

data class Player(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    var x: String = "",
    var y: String = "",
    val address: SocketAddress
)
