package com.rainbow.rainbowconsole.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import com.zzoin.temprainbow.model.UserDTO
import kotlinx.coroutines.Deferred

interface MemberController {
    fun searchByUid(uid : String) : Deferred<Pair<UserDTO? ,String>>
    fun searchByName(name : String) : Deferred<UserDTO?>
    fun searchByProUid(proUid : String) : Deferred<ArrayList<UserDTO>>
    fun searchByBranch(branch : String) : Deferred<ArrayList<UserDTO>>
    fun searchAll() : Deferred<ArrayList<UserDTO>>
    fun updateUser(userItem : UserDTO, documentId : String) : Task<Transaction>

}