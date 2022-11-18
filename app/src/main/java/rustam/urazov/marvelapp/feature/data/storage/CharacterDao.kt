package rustam.urazov.marvelapp.feature.data.storage

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import javax.inject.Singleton

@Singleton
@Dao
interface CharacterDao {

    @Insert
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Update
    suspend fun updateCharacters(characters: List<CharacterEntity>)

    @Delete
    suspend fun deleteCharacters(characters: List<CharacterEntity>)

    @Query("SELECT * FROM Character WHERE characterId > :offset AND characterId <= (:offset + 1) * 20")
    suspend fun getCharacters(offset: Int): List<CharacterEntity>

    @Query("SELECT * FROM Character WHERE chId = :id")
    suspend fun getCharacter(id: Int): CharacterEntity

}