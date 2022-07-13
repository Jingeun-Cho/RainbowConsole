package com.rainbow.rainbowconsole.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.repository.LessonRepository
import com.zzoin.temprainbow.model.LessonDTO
import com.zzoin.temprainbow.model.ManagerDTO
import com.zzoin.temprainbow.model.UserDTO
import kotlinx.coroutines.Deferred

class LessonControllerImpl(private val lessonRepository : LessonRepository) : LessonController{
    override fun searchByUid(uid: String): Deferred<ArrayList<LessonDTO>> = lessonRepository.findByUid(uid)

    override fun searchByProUid(proUid: String): Deferred<ArrayList<LessonDTO>> = lessonRepository.findByProUid(proUid)

    override fun searchByPeriod(startTime: Long, endTime: Long): Deferred<ArrayList<LessonDTO>> = lessonRepository.findByPeriod(startTime, endTime)

    override fun searchByUidWithPeriod( startTime: Long, endTime: Long, uid: ArrayList<String> ): Deferred<ArrayList<LessonDTO>> = lessonRepository.findByUidWithPeriod(startTime, endTime, uid)

    override fun searchRecent(): Deferred<ArrayList<LessonDTO>> = lessonRepository.findByRecent()
    override fun searchRecent(branch : String): Deferred<ArrayList<LessonDTO>> = lessonRepository.findByRecentByBranch(branch)
    override fun deleteLesson(documentId: String) : Task<Transaction> {
        return lessonRepository.deleteLesson(documentId)
    }

    override fun reserveLesson(documentId: String, lessonDTO: LessonDTO): Task<Void> {
        return lessonRepository.reserveLesson(documentId, lessonDTO)
    }

}