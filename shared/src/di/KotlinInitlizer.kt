package di

import com.ranjan.somiq.core.di.networkModule
import com.ranjan.somiq.core.di.platformModules
import com.ranjan.somiq.di.sharedModules
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    appDeclaration: KoinAppDeclaration = {},
) = startKoin {
    appDeclaration()
    modules(platformModules)
    modules(networkModule)
    modules(sharedModules)
}
