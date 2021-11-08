package ir.vasl.chatkitv4

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import ir.vasl.chatkitv4.helpers.ContextWrapper
import java.util.*

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // initApplication()
    }

    private fun initApplication() {
        // LocaleHelper.setApplicationLanguage(this)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // LocaleHelper.setLocale(applicationContext, LocaleHelper.getPersistedLocale(this))
    }

    override fun attachBaseContext(base: Context?) {
        val newLocale = Locale("fa")
        val context: Context = ContextWrapper.wrap(base, newLocale)
        super.attachBaseContext(context)
    }
}