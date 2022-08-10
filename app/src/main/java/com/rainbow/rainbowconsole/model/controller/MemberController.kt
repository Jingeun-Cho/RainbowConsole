package com.rainbow.rainbowconsole.model.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.model.data_class.UserDTO
import kotlinx.coroutines.Deferred

interface MemberController {
    fun searchByUid(uid : String) : Query
    fun searchByName(name : String) : Deferred<UserDTO?>
    fun searchByProUid(proUid : String) : Query
    fun searchByBranch(branch : String) : Query
    fun searchAll() : CollectionReference
    fun updateUser(userItem : UserDTO, documentId : String) : Task<Transaction>

}