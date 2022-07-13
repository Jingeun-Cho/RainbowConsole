package com.rainbow.rainbowconsole.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import com.zzoin.temprainbow.model.UserDTO
import kotlinx.coroutines.Deferred

interface MemberRepository {

    fun findByUid(uid : String) : Deferred<Pair<UserDTO?, String>>

    fun findByName(name : String) : Deferred<UserDTO?>

    fun finByBranch(branch : String) : Deferred<ArrayList<UserDTO>>

    fun findByProName(name : String) : Deferred<ArrayList<UserDTO>>

    fun findByProUid(uid : String) : Deferred<ArrayList<UserDTO>>

    fun findAll() : Deferred<ArrayList<UserDTO>>

    fun updateUser(userItem : UserDTO, documentId : String) : Task<Transaction>

}