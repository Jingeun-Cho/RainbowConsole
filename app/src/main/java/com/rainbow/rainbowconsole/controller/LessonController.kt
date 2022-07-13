package com.rainbow.rainbowconsole.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import com.zzoin.temprainbow.model.LessonDTO
import com.zzoin.temprainbow.model.ManagerDTO
import com.zzoin.temprainbow.model.UserDTO
import kotlinx.coroutines.Deferred

interface LessonController {
    fun searchByUid(uid : String) : Deferred<ArrayList<LessonDTO>>
    fun searchByProUid(proUid : String) : Deferred<ArrayList<LessonDTO>>
    fun searchByPeriod(startTime : Long, endTime : Long) : Deferred<ArrayList<LessonDTO>>
    fun searchByUidWithPeriod(startTime: Long, endTime: Long, uid: ArrayList<String>) : Deferred<ArrayList<LessonDTO>>
    fun searchRecent() : Deferred<ArrayList<LessonDTO>>
    fun searchRecent(branch : String) : Deferred<ArrayList<LessonDTO>>
    fun deleteLesson(documentId : String): Task<Transaction>
    fun reserveLesson(documentId: String, lessonDTO: LessonDTO) : Task<Void>

}