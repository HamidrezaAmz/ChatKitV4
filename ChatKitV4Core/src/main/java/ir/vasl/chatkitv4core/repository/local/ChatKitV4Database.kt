package ir.vasl.chatkitv4core.repository.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.repository.local.messages.MessageDao
import ir.vasl.chatkitv4core.repository.local.migrations.MigrationCore
import ir.vasl.chatkitv4core.repository.local.remoteKeys.RemoteKeysDao
import ir.vasl.chatkitv4core.repository.local.remoteKeys.RemoteKeysEntity
import ir.vasl.chatkitv4core.util.PublicValue

@Database(
    entities = [MessageModel::class, RemoteKeysEntity::class],
    version = PublicValue.DATABASE_VERSION,
    exportSchema = false
)
abstract class ChatKitV4Database : RoomDatabase() {

    abstract fun getMessageDao(): MessageDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao

    companion object {

        @Volatile
        private var INSTANCE: ChatKitV4Database? = null

        fun getDatabase(context: Context): ChatKitV4Database = INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                ChatKitV4Database::class.java,
                PublicValue.DATABASE_NAME
            ).allowMainThreadQueries()
                .addMigrations(MigrationCore.MIGRATION_1_2)
                .build()
            INSTANCE = instance
            instance
        }
    }
}