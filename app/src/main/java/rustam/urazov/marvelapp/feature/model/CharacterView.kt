package rustam.urazov.marvelapp.feature.model

data class CharacterView(
    val id: Int,
    val name: String,
    val description: String,
    val thumbnail: String,
    val resourceURI: String
)