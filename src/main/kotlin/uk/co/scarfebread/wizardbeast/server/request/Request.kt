package uk.co.scarfebread.wizardbeast.server.request

import kotlinx.serialization.Serializable

@Serializable
data class Request(val action: String)
