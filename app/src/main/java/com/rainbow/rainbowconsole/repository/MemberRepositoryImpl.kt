package com.rainbow.rainbowconsole.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.toObjects
import com.zzoin.temprainbow.model.UserDTO
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class MemberRepositoryImpl(private val firestore : FirebaseFirestore) : MemberRepository{

    override fun findByUid(uid: String): Deferred<Pair<UserDTO?, String>> =
        CoroutineScope(Dispatchers.IO).async {
            try {
                val result = firestore
                    .collection("user")
                    .whereEqualTo("uid", uid)
                    .get()
                    .await()
                    .documents[0]
                Pair(result.toObject(UserDTO::class.java), result.id)
            }
            catch (e : FirebaseException){
                Log.e("FirebaseMemberRepository", "findByUid: ${e.message}")
                Pair(null, "")
            }
            catch (e : IndexOutOfBoundsException){
                Log.e("FirebaseMemberRepository", "findByUid: ${e.message}")
                Pair(null, "")
            }
        }




    override fun findByName(name: String): Deferred<UserDTO?> =
        CoroutineScope(Dispatchers.IO).async {
            try{
                firestore
                    .collection("user")
                    .whereEqualTo("name", name)
                    .get()
                    .await()
                    .documents[0]
                    .toObject(UserDTO::class.java)

            }
            catch (e : FirebaseException){
                Log.e("FirebaseMemberRepository", "findByUid: ${e.message}")
                null
            }
        }

    override fun finByBranch(branch: String): Deferred<ArrayList<UserDTO>> =
        CoroutineScope(Dispatchers.IO).async {
            try{
                firestore
                    .collection("user")
                    .whereEqualTo("branch", branch)
                    .get()
                    .await()
                    .toObjects(UserDTO::class.java) as ArrayList

            }
            catch (e : FirebaseException){
                Log.e("FirebaseMemberRepository", "findByUid: ${e.message}")

                arrayListOf()
            }
        }

    override fun findByProName(name: String): Deferred<ArrayList<UserDTO>> =
        CoroutineScope(Dispatchers.IO).async {
            try{
                firestore
                    .collection("user")
                    .whereEqualTo("proName", name)
                    .get()
                    .await()
                    .toObjects<UserDTO>() as ArrayList

            }
            catch (e : FirebaseException){
                Log.e("FirebaseMemberRepository", "findByUid: ${e.message}")

                arrayListOf()
            }
        }



    override fun findByProUid(uid: String): Deferred<ArrayList<UserDTO>> =
        CoroutineScope(Dispatchers.IO).async {
            try{
                firestore
                    .collection("user")
                    .whereEqualTo("proUid", uid)
                    .get()
                    .await()
                    .toObjects<UserDTO>() as ArrayList

            }
            catch (e : FirebaseException){
                Log.e("FirebaseMemberRepository", "findByUid: ${e.message}")
                arrayListOf()
            }
        }

    override fun findAll(): Deferred<ArrayList<UserDTO>> =
        CoroutineScope(Dispatchers.IO).async {
            try{
                firestore
                    .collection("user")
                    .get()
                    .await()
                    .toObjects<UserDTO>() as ArrayList

            }
            catch (e : FirebaseException){
                Log.e("FirebaseMemberRepository", "findByUid: ${e.message}")
                arrayListOf()
            }
        }

    override fun updateUser(userItem: UserDTO, documentId : String) : Task<Transaction> {
        val target = firestore.collection("user").document(documentId)
        return firestore.runTransaction { transaction ->
            transaction.set(target, userItem)
        }
    }


}