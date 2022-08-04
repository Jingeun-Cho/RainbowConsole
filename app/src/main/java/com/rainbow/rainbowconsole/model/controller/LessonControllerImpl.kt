package com.rainbow.rainbowconsole.model.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.model.repository.LessonRepository
import com.rainbow.rainbowconsole.model.data_class.LessonDTO
import kotlinx.coroutines.Deferred

class LessonControllerImpl(private val lessonRepository : LessonRepository) : LessonController{
    override fun searchByUid(uid: String): Deferred<ArrayList<LessonDTO>> = lessonRepository.findByUid(uid)

    override fun searchByProUid(proUid: String): Deferred<ArrayList<LessonDTO>> = lessonRepository.findByProUid(proUid)

    override fun searchByPeriod(startTime: Long, endTime: Long): Query = lessonRepository.findByPeriod(startTime, endTime)

    override fun searchByUidWithPeriod( startTime: Long, endTime: Long, uid: ArrayList<String> ): Query
    = lessonRepository.findByUidWithPeriod(startTime, endTime, uid)

    override fun searchRecent(): Deferred<ArrayList<LessonDTO>> = lessonRepository.findByRecent()
    override fun searchRecent(branch : String): Deferred<ArrayList<LessonDTO>> = lessonRepository.findByRecentByBranch(branch)
    override fun deleteLesson(documentId: String) : Task<Transaction> {
        return lessonRepository.deleteLesson(documentId)
    }

    override fun reserveLesson(documentId: String, lessonDTO: LessonDTO): Task<Void> {
        return lessonRepository.reserveLesson(documentId, lessonDTO)
    }

}