package ir.vasl.chatkitv4core.repository.local.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object MigrationCore {

    const val message_table_name = "table_messages"

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE $message_table_name ADD COLUMN 'fileName' TEXT DEFAULT ''")
            database.execSQL("ALTER TABLE $message_table_name ADD COLUMN 'fileSize' TEXT DEFAULT ''")
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE $message_table_name ADD COLUMN 'userId' TEXT DEFAULT ''")
        }
    }

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE $message_table_name ADD COLUMN 'serverMessageId' TEXT DEFAULT ''")
        }
    }

}