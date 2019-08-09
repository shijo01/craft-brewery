package com.shijo.beercraft.di

import android.app.Application
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.shijo.beercraft.data.db.BeerCraftDao
import com.shijo.beercraft.data.db.BeerCraftDatabase
import com.shijo.beercraft.data.db.CartDao
import com.shijo.beercraft.utils.BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton


/**
 * All the application level dependencies are provided through this module
 */
@Module
class AppModule {
    @Module
    companion object {

        @Singleton
        @JvmStatic
        @Provides
        fun provideBeerCraftDatabase(application: Application): BeerCraftDatabase {
            return Room.databaseBuilder(application, BeerCraftDatabase::class.java, "beercraft-db").build()
        }

        @Singleton
        @JvmStatic
        @Provides
        fun provideBeerCraftDao(beerCraftDatabase: BeerCraftDatabase): BeerCraftDao {
            return beerCraftDatabase.beerCraftDao()
        }

        @Singleton
        @JvmStatic
        @Provides
        fun provideCartDao(beerCraftDatabase: BeerCraftDatabase): CartDao {
            return beerCraftDatabase.cartDao()
        }

        @Singleton
        @JvmStatic
        @Provides
        fun provideRequestOptions(): RequestOptions {
            return RequestOptions
                .placeholderOf(com.shijo.beercraft.R.drawable.white_background)
                .error(com.shijo.beercraft.R.drawable.white_background)
        }

        @Singleton
        @JvmStatic
        @Provides
        fun provideRequestManager(
            application: Application,
            requestOptions: RequestOptions
        ): RequestManager {
            return Glide.with(application).setDefaultRequestOptions(requestOptions)
        }


        @Singleton
        @JvmStatic
        @Provides
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
        }

        @Singleton
        @JvmStatic
        @Provides
        fun provideOkHttpClient(): OkHttpClient {
            val logging = HttpLoggingInterceptor()
            logging.level = Level.BODY
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)
            return httpClient.build()
        }

    }


}