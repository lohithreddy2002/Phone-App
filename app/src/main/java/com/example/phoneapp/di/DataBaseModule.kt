package com.example.phoneapp

import android.content.Context
import androidx.room.Room
import com.example.phoneapp.data.ContactsDao
import com.example.phoneapp.data.DataBaseDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataBaseModule {
    @Provides
    @Singleton
    fun provideRoomDb(@ApplicationContext context: Context): DataBaseDB =
        Room.databaseBuilder(
            context.applicationContext,
            DataBaseDB::class.java,
            "db"
        ).build()

    @Provides
    @Singleton
    fun provideContactsRoomDao(readingRightDB: DataBaseDB): ContactsDao =
        readingRightDB.contactsDao()
}