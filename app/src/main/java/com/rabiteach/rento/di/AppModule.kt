package com.rabiteach.rento.di

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

//    @Provides
//    @Singleton
//    fun provideTenantRepository(
//        firestore: FirebaseFirestore
//    ): TenantRepository = TenantRepositoryImpl(firestore)
}