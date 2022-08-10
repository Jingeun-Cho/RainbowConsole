package com.rainbow.rainbowconsole.model.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Transaction
import com.google.firebase.firestore.ktx.toObjects
import com.rainbow.rainbowconsole.model.data_class.UserDTO
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class MemberRepositoryImpl(private val firestore : FirebaseFirestore) : MemberRepository{

    override fun findByUid(uid: String): Query =
           firestore
                .collection("user")
                .whereEqualTo("uid", uid)





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



    override fun findByProUid(uid: String):  Query =
                firestore
                    .collection("user")
                    .whereEqualTo("proUid", uid)



    override fun findAll(): CollectionReference =
        firestore.collection("user")

    override fun finByBranch(branch: String): Query =
        firestore.collection("user").whereEqualTo("branch", branch)






    override fun updateUser(userItem: UserDTO, documentId : String) : Task<Transaction> {
        val target = firestore.collection("user").document(documentId)
        return firestore.runTransaction { transaction ->
            transaction.set(target, userItem)
        }
    }


}