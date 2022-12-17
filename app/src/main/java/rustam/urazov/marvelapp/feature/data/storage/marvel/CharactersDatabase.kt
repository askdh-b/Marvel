package rustam.urazov.marvelapp.feature.data.storage.marvel

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CharacterEntity::class], version = 1)
abstract class CharactersDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "charactersDb"
    }

    abstract fun getCharacterDao(): CharacterDao

}