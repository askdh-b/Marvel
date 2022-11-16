package rustam.urazov.marvelapp.feature.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import dagger.Provides
import rustam.urazov.marvelapp.feature.model.CharacterEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@Database(entities = [CharacterEntity::class], version = 1)
abstract class CharactersDatabase : RoomDatabase() {
    abstract fun getCharacterDao(): CharacterDao
}