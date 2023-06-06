package md.hackaton.aasocialrecovery

import android.app.Application
import md.hackaton.aasocialrecovery.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        GlobalContext.startKoin {
            androidContext(this@App)
            androidLogger()
            modules(appModule)
        }
    }
}