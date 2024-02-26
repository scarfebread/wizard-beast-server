package uk.co.scarfebread.wizardbeast.entity

import kotlinx.serialization.Serializable

@Serializable
data class Input(
    var up: Boolean = false,
    var down: Boolean = false,
    var left: Boolean = false,
    var right: Boolean = false,
)
