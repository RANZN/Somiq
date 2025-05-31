package com.ranjan.smartcents.data.repository

import com.ranjan.smartcents.domain.model.AuthResult
import com.ranjan.smartcents.domain.model.User
import com.ranjan.smartcents.domain.model.UserProfile
import com.ranjan.smartcents.domain.repository.AuthRepo
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.toDuration

class AuthRepoImpl(
    private val userDatabase: CollectionReference,
) : AuthRepo {
    override suspend fun loginUser(email: String, password: String): AuthResult {
        val result = try {
            Firebase.auth.signInWithEmailAndPassword(email, password)
        } catch (e: Exception) {
            return AuthResult.Failure.fromException(e)
        }

        val userId = result.user?.uid ?: return AuthResult.Failure.Unknown("Missing user ID")

        val userProfile = getUserProfile(userId)
        if (userProfile == null) {
            return AuthResult.Failure.Unknown("User profile not found")
        }

        val user = User(
            name = userProfile.name,
            email = userProfile.email,
            uid = userId,
            createdAt = userProfile.createdAt
        )

        return AuthResult.Success(user)
    }

    private suspend fun getUserProfile(userId: String): UserProfile? {
        val snapshot = userDatabase.document(userId).get()
        return if (snapshot.exists) snapshot.data(UserProfile.serializer()) else null
    }

    override suspend fun signUpUser(name: String, email: String, password: String): AuthResult {
        val result = try {
            Firebase.auth.createUserWithEmailAndPassword(email, password)
        } catch (e: Exception) {
            return AuthResult.Failure.fromException(e)
        }

        val userId = result.user?.uid ?: return AuthResult.Failure.Unknown("Sign-up failed")

        val createdAt = Timestamp.Companion.now()

        val profileData = mapOf(
            "name" to name,
            "email" to email,
            "createdAt" to createdAt
        )

        try {
            userDatabase.document(userId).set(profileData)
        } catch (e: Exception) {
            return AuthResult.Failure.Unknown("Failed to store user profile: ${e.message}")
        }

        return AuthResult.Success(
            User(
                name = name,
                email = email,
                uid = userId,
                createdAt = createdAt.toDuration().toString()
            )
        )
    }

    override suspend fun logoutUser(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun isUserLoggedIn(): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrentUser(): User? {
        TODO("Not yet implemented")
    }
}