package edu.ucne.inventario.data.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.inventario.data.di.AppModule.BASE_URL
import edu.ucne.inventario.data.local.database.InventarioDb
import edu.ucne.inventario.data.remote.interfaces.CategoriaApi
import edu.ucne.inventario.data.remote.interfaces.ProductoApi
import edu.ucne.inventario.data.remote.interfaces.ProovedorApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    const val BASE_URL = "https://inventarioapi-f3c6fvgsh0f2aueq.eastus2-01.azurewebsites.net"


    @Provides
    @Singleton
    fun provideInventarioDb(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            InventarioDb::class.java,
            "Inventario.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideCategoriaDao(inventarioDb: InventarioDb) = inventarioDb.categoriaDao

    @Provides
    @Singleton
    fun provideProductoDao(inventarioDb: InventarioDb) = inventarioDb.productoDao

    @Provides
    @Singleton
    fun provideProovedorDao(inventarioDb: InventarioDb) = inventarioDb.proovedorDao



    @Singleton
    @Provides
    fun providesMoshi(): Moshi =
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    @Singleton
    fun providesProductosApi(moshi: Moshi): ProductoApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ProductoApi::class.java)
    }

    @Provides
    @Singleton
    fun providesCategoriaApi(moshi: Moshi): CategoriaApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(CategoriaApi::class.java)
    }

    @Provides
    @Singleton
    fun providesProovedorApi(moshi: Moshi): ProovedorApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ProovedorApi::class.java)
    }
}
