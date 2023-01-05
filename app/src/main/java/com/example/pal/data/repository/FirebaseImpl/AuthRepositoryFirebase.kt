package com.example.pal.data.repository.FirebaseImpl

import com.example.pal.data.models.User
import com.example.pal.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import il.co.syntax.myapplication.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import safeCall

class AuthRepositoryFirebase : AuthRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val userRef = FirebaseFirestore.getInstance().collection("user")

    // check if you was logged in to the app
    override suspend fun currentUser(): Resource<User> {
        return withContext(Dispatchers.IO){
            safeCall {
                val user=userRef.document(firebaseAuth.currentUser!!.uid).get().await().toObject(User::class.java)
                Resource.Success(user!!)
            }
        }
    }

    override suspend fun login(email: String, password: String): Resource<User> {
        return withContext(Dispatchers.IO){
            safeCall {
                val result=firebaseAuth.signInWithEmailAndPassword(email,password).await()
                val user =userRef.document(result.user?.uid!!).get().await().toObject(User::class.java)!!
                Resource.Success(user)

            }
        }
    }

    override suspend fun createUser(
        userName: String,
        userEmail: String,
        userPhone: String,
        userLoginPass: String,
    )  :Resource<User>{

        return withContext(Dispatchers.IO){ // inside coroutine scope because of the suspend fun
            safeCall {
                val registrationResult = firebaseAuth.createUserWithEmailAndPassword(userEmail,userLoginPass).await()
                val userId = registrationResult.user?.uid!!
                val newUser = User(userName,userEmail,userPhone)
                userRef.document(userId).set(newUser).await()

                Resource.Success(newUser)
            }

        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}