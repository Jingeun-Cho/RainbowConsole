package com.rainbow.rainbowconsole.model.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.model.data_class.LessonDTO
import kotlinx.coroutines.Deferred

interface LessonController {

    fun searchByUid(uid : String) : Deferred<ArrayList<LessonDTO>>

    fun searchByProUid(proUid : String) : Deferred<ArrayList<LessonDTO>>

    fun searchByPeriod(startTime : Long, endTime : Long) : Query

    fun searchByUidWithPeriod(startTime: Long, endTime: Long, uid: ArrayList<String>) : Deferred<ArrayList<LessonDTO>>

    fun searchRecent() : Deferred<ArrayList<LessonDTO>>

    fun searchRecent(branch : String) : Deferred<ArrayList<LessonDTO>>

    fun deleteLesson(documentId : String): Task<Transaction>

    fun reserveLesson(documentId: String, lessonDTO: LessonDTO) : Task<Void>


}