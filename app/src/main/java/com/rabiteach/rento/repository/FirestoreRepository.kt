package com.rabiteach.rento.repository

import android.annotation.SuppressLint
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.rabiteach.rento.model.UserRole
import kotlinx.coroutines.tasks.await


object FirestoreRepository {
    @SuppressLint("StaticFieldLeak")
    private val db = Firebase.firestore

    suspend fun checkPasscodeAndGetRole(code: String): UserRole? {
        val collections = listOf(
            "landlords" to UserRole.LANDLORD,
            "assistants" to UserRole.ASSISTANT,
            "tenants" to UserRole.TENANT
        )

        for ((collection, role) in collections) {
            val result = db.collection(collection)
                .whereEqualTo("passcode", code)
                .limit(1)
                .get()
                .await()

            if (!result.isEmpty) return role
        }

        return null
    }
}
