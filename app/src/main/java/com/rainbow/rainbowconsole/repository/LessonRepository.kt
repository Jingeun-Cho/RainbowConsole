package com.rainbow.rainbowconsole.repository


import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import com.zzoin.temprainbow.model.LessonDTO
import com.zzoin.temprainbow.model.ManagerDTO
import com.zzoin.temprainbow.model.UserDTO
import kotlinx.coroutines.Deferred

interface LessonRepository {
    fun findByUid(uid : String) : Deferred<ArrayList<LessonDTO>>
    fun findByProUid(proUid : String) : Deferred<ArrayList<LessonDTO>>
    fun findByPeriod(startTime : Long, endTime : Long) : Deferred<ArrayList<LessonDTO>>
    fun findByUidWithPeriod(startTime : Long, endTime : Long, uid : ArrayList<String>) : Deferred<ArrayList<LessonDTO>>
    fun findByRecent() : Deferred<ArrayList<LessonDTO>>
    fun findByRecentByBranch(branch : String) : Deferred<ArrayList<LessonDTO>>
    fun deleteLesson(documentId : String): Task<Transaction>
    fun reserveLesson(documentId : String, lessonDTO: LessonDTO): Task<Void>
}