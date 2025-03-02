package dev.korryr.mpesaapi.ui.componenets.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.korryr.mpesaapi.ui.componenets.mpesa.model.MpesaApiService
import dev.korryr.mpesaapi.ui.componenets.mpesa.model.MpesaRepository
import dev.korryr.mpesaapi.ui.componenets.mpesa.repo.MpesaRepositoryImpl
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MpesaModule {
    
    @Provides
    @Singleton
    fun provideMpesaApiService(retrofit: Retrofit): MpesaApiService {
        return retrofit.create(MpesaApiService::class.java)
    }
    
    @Provides
    @Singleton
    fun provideMpesaRepository(
        mpesaApiService: MpesaApiService,
        @ApplicationContext context: Context
    ): MpesaRepository {
        return MpesaRepositoryImpl(mpesaApiService, context)
    }
}