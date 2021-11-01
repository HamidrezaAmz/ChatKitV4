package ir.vasl.chatkitv4

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import ir.vasl.chatkitv4.helpers.LocaleHelper

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initApplication()
    }

    private fun initApplication() {
        LocaleHelper.setApplicationLanguage(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleHelper.setLocale(applicationContext, LocaleHelper.getPersistedLocale(this))
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }
}