package com.rainbow.rainbowconsole.model.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.model.data_class.UserDTO
import kotlinx.coroutines.Deferred

interface MemberRepository {

    fun findByUid(uid : String) : Query

    fun findByName(name : String) : Deferred<UserDTO?>

    fun finByBranch(branch : String) : Query

    fun findByProName(name : String) : Deferred<ArrayList<UserDTO>>

    fun findByProUid(uid : String) : Query

    fun findAll() : CollectionReference

    fun updateUser(userItem : UserDTO, documentId : String) : Task<Transaction>

}