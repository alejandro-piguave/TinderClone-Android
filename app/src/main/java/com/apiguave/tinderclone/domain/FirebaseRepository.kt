package com.apiguave.tinderclone.domain

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object FirebaseRepository {
    private const val USERS = "users"
    val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser


    suspend fun userExists(): Result<Boolean>{
        return suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance().collection(USERS).document(currentUser!!.uid).get().addOnSuccessListener { document ->
                continuation.resume(Result.success(document != null))
            }.addOnFailureListener { exception ->
                Result.failure<Boolean>(exception)
            }
        }
    }
}