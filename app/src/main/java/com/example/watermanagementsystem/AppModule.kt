package com.example.watermanagementsystem

import android.content.Context
import androidx.work.WorkManager
import com.example.watermanagementsystem.api.apiInterface
import com.example.watermanagementsystem.api.mlApiInterface
import com.example.watermanagementsystem.constant.API
import com.example.watermanagementsystem.repository.WaterRepository
import com.example.watermanagementsystem.repository.WaterRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiInterface(retrofit: Retrofit): apiInterface {
        return retrofit.create(apiInterface::class.java)
    }

    @Provides
    @Singleton
    fun provideApiRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMlApiInterface(@Named("ML_RETROFIT") retrofit : Retrofit): mlApiInterface {
        return retrofit.create(mlApiInterface::class.java)
    }

    @Provides
    @Singleton
    @Named("ML_RETROFIT")
    fun provideMlApiRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API.ML_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideWaterRepository(mlApi : mlApiInterface , api : apiInterface , workManager: WorkManager) : WaterRepository{
        return WaterRepositoryImpl(mlApi , api, workManager)
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context) : WorkManager{
        return WorkManager.getInstance(context)
    }
}