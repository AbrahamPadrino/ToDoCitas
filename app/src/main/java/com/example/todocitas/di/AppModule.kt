package com.example.todocitas.di

import android.content.Context
import com.example.todocitas.data.local.dao.CitasDao
import com.example.todocitas.data.local.dao.ClientesDao
import com.example.todocitas.data.local.dao.ServiciosDao
import com.example.todocitas.data.local.repository.CitasRepository
import com.example.todocitas.data.local.repository.ClientesRepository
import com.example.todocitas.data.local.repository.ServiciosRepository
import com.example.todocitas.data.local.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Le dice a Hilt que estas dependencias vivirán mientras la app viva.
object AppModule {

    @Provides
    @Singleton // Garantiza que solo haya una instancia de la base de datos (como tu Singleton manual).
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstanceDatabase(context)
    }

    // Provee los DAOs y los Repositorios.

    // --- Clientes ---
    @Provides
    @Singleton // El DAO también será Singleton porque la DB lo es.
    fun provideClientesDao(db: AppDatabase): ClientesDao {
        return db.clientesDao()
    }
    @Provides
    @Singleton // El Repositorio también.
    fun provideClientesRepository(dao: ClientesDao): ClientesRepository {
        return ClientesRepository(dao)
    }

    // --- Servicios ---
    @Provides
    @Singleton // El DAO también será Singleton porque la DB lo es.
    fun provideServiciosDao(db: AppDatabase): ServiciosDao {
        return db.serviciosDao()
    }
    @Provides
    @Singleton // El Repositorio también.
    fun provideServiciosRepository(dao: ServiciosDao): ServiciosRepository {
        return ServiciosRepository(dao)
    }

    // --- Citas ---
    @Provides
    @Singleton // El DAO también será Singleton porque la DB lo es.
    fun provideCitasDao(db: AppDatabase): CitasDao {
        return db.citasDao()
    }
    @Provides
    @Singleton // El Repositorio también.
    fun provideCitasRepository(dao: CitasDao): CitasRepository {
        return CitasRepository(dao)
    }

}