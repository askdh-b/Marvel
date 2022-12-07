package rustam.urazov.marvelapp.feature.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CharacterEntity::class], version = 1)
abstract class CharactersDatabase : RoomDatabase() {
    abstract fun getCharacterDao(): CharacterDao
}