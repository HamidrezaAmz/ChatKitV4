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

}