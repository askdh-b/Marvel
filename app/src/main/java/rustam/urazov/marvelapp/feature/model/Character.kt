package rustam.urazov.marvelapp.feature.model

data class Character(
    val id: Int,
    private val name: String,
    private val description: String,
    private val thumbnail: String,
    private val resourceURI: String
) {

    fun toCharacterView(): CharacterView = CharacterView(
        id = id,
        name = name,
        description = description,
        thumbnail = thumbnail,
        resourceURI = resourceURI
    )
}