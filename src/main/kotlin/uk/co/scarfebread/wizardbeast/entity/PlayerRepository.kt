package uk.co.scarfebread.wizardbeast.entity

class PlayerRepository(private val players: MutableList<Player>) {
    fun addPlayer(player: Player) = player.also { players.add(it) }

    fun removePlayer(userId: String) = players.removeIf { it.id == userId }

    fun getPlayers() = players.toList()

    fun getPlayer(id: String) = getPlayers().firstOrNull { it.id == id }
}
