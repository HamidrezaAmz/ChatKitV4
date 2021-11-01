package ir.vasl.chatkitv4core.hilt

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.vasl.chatkitv4core.repository.local.ChatKitV4Database
import ir.vasl.chatkitv4core.util.PublicValue
import javax.inject.Singleton
/*

@Module
@InstallIn(ActivityComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): ChatKitV4Database {
        return Room.databaseBuilder(
            appContext, ChatKitV4Database::class.java,
            PublicValue.DATABASE_NAME
        ).build()
    }
}
*/
