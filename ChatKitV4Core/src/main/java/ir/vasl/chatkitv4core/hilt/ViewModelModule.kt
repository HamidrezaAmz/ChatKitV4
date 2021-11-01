package ir.vasl.chatkitv4core.hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent
import ir.vasl.chatkitv4core.repository.ChatKitV4Repository
import ir.vasl.chatkitv4core.repository.local.ChatKitV4Database
import ir.vasl.chatkitv4core.viewmodel.ChatKitV4ViewModel
import javax.inject.Singleton
/*

@Module
@InstallIn(ActivityComponent::class)
object ViewModelModule {

    @Singleton
    @Provides
    fun provideRepository(chatKitV4Database: ChatKitV4Database): ChatKitV4Repository {
        return ChatKitV4Repository(chatKitV4Database)
    }

    @Singleton
    @Provides
    fun provideViewModel(repository: ChatKitV4Repository): ChatKitV4ViewModel {
        return ChatKitV4ViewModel(repository)
    }

}*/
