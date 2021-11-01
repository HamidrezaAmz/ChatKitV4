package ir.vasl.chatkitv4core.repository.local.remoteKeys

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_remote_keys")
data class RemoteKeysEntity(
    @PrimaryKey val repoId: String,
    val prevKey: Int?,
    val nextKey: Int?
)
