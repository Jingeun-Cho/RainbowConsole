package com.rainbow.rainbowconsole.model.repository

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO
import com.rainbow.rainbowconsole.model.data_class.ManagerScheduleDTO
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class ProRepositoryImpl(private val firestore : FirebaseFirestore) : ProRepository {
    override fun findByUid(uid: String): Deferred<ManagerDTO?> =
        CoroutineScope(Dispatchers.IO).async {
            try {
            firestore
                .collection("manager")
                .document(uid)
                .get()
                .await()
                .toObject(ManagerDTO::class.java)
            }
            catch (e : FirebaseException){
                Log.e("FirebaseProRepository", "findByUid: ${e.message}" )
                null
            }
        }

    override fun findByName(name: String): Deferred<ManagerDTO?> =
        CoroutineScope(Dispatchers.IO).async {
            try {
                firestore
                    .collection("manager")
                    .whereEqualTo("name", name)
                    .get()
                    .await()
                    .documents[0]
                    .toObject(ManagerDTO::class.java)
            }
            catch (e : FirebaseException){
                Log.e("FirebaseProRepository", "findByUid: ${e.message}" )
                null
            }
        }

    override fun findByEmail(email: String): Query =
        firestore
            .collection("manager")
            .whereEqualTo("email", email)


    override fun findByBranch(branch: String): Query =
            firestore
                .collection("manager")
                .whereEqualTo("branch", branch)



    override fun findAllByBranch(): Query =
            firestore
                .collection("manager")



    override fun findProScheduleByUid(uid: String): Deferred<ManagerScheduleDTO> =
        CoroutineScope(Dispatchers.IO).async {
            try {
                val item = firestore
                    .collection("manager_schedule")
                    .document(uid)
                    .get()
                    .await()
                    .toObject(ManagerScheduleDTO::class.java)
                if(item == null){
                    return@async ManagerScheduleDTO()
                }
                else
                    return@async item

            }
            catch (e : FirebaseException){
                Log.e("FirebaseProRepository", "findByUid: ${e.message}" )
                ManagerScheduleDTO()
            }
        }

    override fun updatePro(pro: ManagerDTO, proSchedule: ManagerScheduleDTO) : Task<Transaction>?{
        return try {
            val targetProInfo = firestore.collection("manager").document(pro.uid!!)
            val targetSchedule = firestore.collection("manager_schedule").document(pro.uid!!)
            Log.d("updatePro", "updatePro: ${pro}")
            Log.d("updatePro", "updatePro: ${proSchedule}")
            firestore.runTransaction { transaction ->
                transaction.set(targetProInfo, pro)
                transaction.set(targetSchedule, proSchedule)

            }
        }
        catch (e : FirebaseException){
            Log.e("updatePro", "updatePro: ${e.message}", )
            null
        }

    }
}