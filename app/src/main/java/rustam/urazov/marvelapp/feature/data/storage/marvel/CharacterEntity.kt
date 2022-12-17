package rustam.urazov.marvelapp.feature.data.storage.marvel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import rustam.urazov.marvelapp.core.extention.empty

@Entity(tableName = CharacterEntity.TABLE_NAME)
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "characterId") val id: Int = 0,
    @ColumnInfo(name = "chId") val chId: Int = 0,
    @ColumnInfo(name = "name") val name: String = String.empty(),
    @ColumnInfo(name = "description") val description: String = String.empty(),
    @ColumnInfo(name = "thumbnail") val thumbnail: String = String.empty()
) {

    companion object {
        const val TABLE_NAME = "Character"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CharacterEntity

        if (id != other.id) return false
        if (chId != other.chId) return false
        if (name != other.name) return false
        if (description != other.description) return false
        if (thumbnail != other.thumbnail) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + thumbnail.hashCode()
        return result
    }

}
