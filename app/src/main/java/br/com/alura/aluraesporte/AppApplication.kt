package br.com.alura.aluraesporte

import android.app.Application
import br.com.alura.aluraesporte.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppApplication)
            modules(
                listOf(
                    testeDatabaseModule,
                    daoModule,
                    uiModule,
                    viewModelModule,
                    firebaseModule
                )
            )
        }
    }
}