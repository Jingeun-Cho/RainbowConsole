package com.rainbow.rainbowconsole.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.repository.MemberRepository
import com.zzoin.temprainbow.model.UserDTO
import kotlinx.coroutines.Deferred

class MemberControllerImpl(private val memberRepository: MemberRepository) : MemberController{
    override fun searchByUid(uid: String): Deferred<Pair<UserDTO?, String>> = memberRepository.findByUid(uid)

    override fun searchByName(name: String): Deferred<UserDTO?> = memberRepository.findByName(name)

    override fun searchByProUid(proUid: String) : Deferred<ArrayList<UserDTO>> = memberRepository.findByProUid(proUid)

    override fun searchByBranch(branch: String): Deferred<ArrayList<UserDTO>> = memberRepository.finByBranch(branch)

    override fun searchAll(): Deferred<ArrayList<UserDTO>> = memberRepository.findAll()
    override fun updateUser(userItem: UserDTO, documentId: String) : Task<Transaction> = memberRepository.updateUser(userItem, documentId)
}