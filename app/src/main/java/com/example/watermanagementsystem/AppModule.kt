package com.example.watermanagementsystem

import android.content.Context
import androidx.lifecycle.ViewModel
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
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

     val client : OkHttpClient =  OkHttpClient.Builder()
    .connectTimeout(2, TimeUnit.MINUTES)
    .writeTimeout(2, TimeUnit.MINUTES)
    .readTimeout(2, TimeUnit.MINUTES)
    .build()

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
            .client(client)
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
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWaterRepository(mlApi : mlApiInterface , api : apiInterface , workManager: WorkManager) : WaterRepository{
        return WaterRepositoryImpl(mlApi , api, workManager)
    }

    @Provides
    fun provideViewModel(repository: WaterRepository , @ApplicationContext context: Context) : MainViewModel {
        return MainViewModel(repository , context)
    }

    @Provides
    @Singleton
    fun provideWorkManager(@ApplicationContext context: Context) : WorkManager{
        return WorkManager.getInstance(context)
    }
}