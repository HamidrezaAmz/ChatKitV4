package ir.vasl.chatkitv4core.repository.local.remoteKeys

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKeyEntity: List<RemoteKeysEntity>)

    @Query("SELECT * FROM table_remote_keys WHERE repoId = :imdbId")
    suspend fun remoteKeysById(imdbId: String): RemoteKeysEntity?

    @Query("DELETE FROM table_remote_keys")
    fun clearAll()

}