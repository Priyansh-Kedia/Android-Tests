package com.kedia.androidtestproject.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.kedia.androidtestproject.data.local.ShoppingItemsDatabase
import com.kedia.androidtestproject.other.Constants.BASE_URL
import com.kedia.androidtestproject.other.Constants.DATABASE_NAME
import com.kedia.androidtestproject.remote.PixabayAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {



    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ShoppingItemsDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideShoppindDao(
        database: ShoppingItemsDatabase
    ) = database.shoppingDao()

    @Singleton
    @Provides
    fun providePixabayAPI(): PixabayAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixabayAPI::class.java)
    }
}