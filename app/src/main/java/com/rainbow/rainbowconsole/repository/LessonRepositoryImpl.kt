package com.rainbow.rainbowconsole.repository

import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Transaction
import com.zzoin.temprainbow.model.LessonDTO
import com.zzoin.temprainbow.model.ManagerDTO
import com.zzoin.temprainbow.model.UserDTO
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class LessonRepositoryImpl(private val firestore : FirebaseFirestore) : LessonRepository{
    override fun findByUid(uid: String): Deferred<ArrayList<LessonDTO>> =
        CoroutineScope(Dispatchers.IO).async {
            try {
                firestore
                    .collection("lesson")
                    .whereEqualTo("uid", uid)
                    .orderBy("lessonDateTime", Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .toObjects(LessonDTO::class.java) as ArrayList
            }
            catch (e : FirebaseException){
                Log.e("LessonRepositoryImpl", "findByUid: ${e.message}", )
                arrayListOf()
            }
        }

    override fun findByProUid(proUid: String): Deferred<ArrayList<LessonDTO>> =
        CoroutineScope(Dispatchers.IO).async {
            try {
                firestore
                    .collection("lesson")
                    .whereEqualTo("proUid", proUid)
                    .orderBy("lessonDateTime", Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .toObjects(LessonDTO::class.java) as ArrayList
            }
            catch (e : FirebaseException){
                Log.e("LessonRepositoryImpl", "findByUid: ${e.message}", )
                arrayListOf()
            }
        }

    override fun findByPeriod(startTime: Long, endTime: Long): Deferred<ArrayList<LessonDTO>> =
        CoroutineScope(Dispatchers.IO).async {
            try {
                firestore
                    .collection("lesson")
                    .whereGreaterThanOrEqualTo("lessonDateTime", startTime)
                    .whereLessThanOrEqualTo("lessonDateTime", endTime)
                    .orderBy("lessonDateTime", Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .toObjects(LessonDTO::class.java) as ArrayList
            }
            catch (e : FirebaseException){
                Log.e("LessonRepositoryImpl", "findByUid: ${e.message}" )
                arrayListOf()
            }
        }

    override fun findByUidWithPeriod( startTime: Long, endTime: Long, uid: ArrayList<String> ): Deferred< ArrayList<LessonDTO>> =
        CoroutineScope(Dispatchers.IO).async {
            try {
                firestore
                    .collection("lesson")
                    .whereIn("coachUid", uid)
                    .whereGreaterThanOrEqualTo("lessonDateTime", startTime)
                    .whereLessThanOrEqualTo("lessonDateTime", endTime)
                    .orderBy("lessonDateTime", Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .toObjects(LessonDTO::class.java) as ArrayList
            }
            catch (e : FirebaseException){
                Log.e("LessonRepositoryImpl", "findByUid: ${e.message}" )
                arrayListOf()
            }
        }

    override fun findByRecent(): Deferred<ArrayList<LessonDTO>> =
        CoroutineScope(Dispatchers.IO).async {
            try {
                firestore
                    .collection("lesson")
                    .limit(10)
                    .orderBy("lessonDateTime", Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .toObjects(LessonDTO::class.java) as ArrayList
            }
            catch (e : FirebaseException){
                Log.e("LessonRepositoryImpl", "findByUid: ${e.message}" )
                arrayListOf()
            }
        }

    override fun findByRecentByBranch(branch: String): Deferred<ArrayList<LessonDTO>> =
        CoroutineScope(Dispatchers.IO).async {
            try {
                val branchPro =  firestore
                    .collection("manager")
                    .whereEqualTo("branch", branch)
                    .get()
                    .await()
                    .toObjects(ManagerDTO::class.java) as ArrayList
                val proUids : ArrayList<String> = arrayListOf()
                branchPro.forEach { proUids.add(it.uid!!)  }
//                Log.d("findByRecentByBranch", "findByRecentByBranch: $proUids")
                firestore
                    .collection("lesson")
                    .whereIn("coachUid", proUids)
                    .limit(20)
                    .orderBy("lessonDateTime", Query.Direction.DESCENDING)
                    .get()
                    .await()
                    .toObjects(LessonDTO::class.java) as ArrayList
            }
            catch (e : FirebaseException){
                Log.e("LessonRepositoryImpl", "findByUid: ${e.message}" )
                arrayListOf()
            }
        }

    override fun deleteLesson(documentId : String): Task<Transaction> {
        val target = firestore.collection("lesson").document(documentId)
        return firestore.runTransaction { transaction ->
            transaction.delete(target)
        }
    }

    override fun reserveLesson(documentId: String, lessonDTO: LessonDTO) : Task<Void>{
        return firestore.collection("lesson")
            .document(documentId)
            .set(lessonDTO)
    }
}