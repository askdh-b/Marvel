package rustam.urazov.marvelapp.feature.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import rustam.urazov.marvelapp.core.extention.empty

@Entity(tableName = "Character")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "characterId") val id: Int = 0,
    @ColumnInfo(name = "chId") val chId: Int = 0,
    @ColumnInfo(name = "name") val name: String = String.empty(),
    @ColumnInfo(name = "description") val description: String = String.empty(),
    @ColumnInfo(name = "thumbnail", typeAffinity = ColumnInfo.BLOB) val thumbnail: ByteArray = ByteArray(0)
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CharacterEntity

        if (id != other.id) return false
        if (chId != other.chId) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (!thumbnail.contentEquals(other.thumbnail)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + thumbnail.contentHashCode()
        return result
    }

    fun toCharacter() = CharacterModel(
        id = chId,
        name = name,
        description = description,
        thumbnail = toBitmap(thumbnail)
    )

    private fun toBitmap(image: ByteArray): Bitmap? =
        when (image.isEmpty()) {
            true -> null
            false -> BitmapFactory.decodeByteArray(image, 0, image.size)
        }
}
