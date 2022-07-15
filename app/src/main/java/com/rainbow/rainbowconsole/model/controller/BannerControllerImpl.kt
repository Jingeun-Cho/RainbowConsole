package com.rainbow.rainbowconsole.model.controller

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Transaction

import com.rainbow.rainbowconsole.model.data_class.BannerDTO
import com.rainbow.rainbowconsole.model.repository.BannerRepository
import kotlinx.coroutines.Deferred

class BannerControllerImpl(private val bannerRepository: BannerRepository) : BannerController{
    override fun getBannerList(): CollectionReference? = bannerRepository.getBannerList()

    override fun getBanner(documentId: String): Deferred<BannerDTO?> = bannerRepository.getBanner(documentId)

    override fun addNewBanner(banner: BannerDTO, documentId: String): Task<Void> = bannerRepository.addBanner(banner, documentId)

    override fun deleteBanner(documentId: String): Task<Transaction> = bannerRepository.deleteBanner(documentId)

    override fun editBanner(banner: BannerDTO, documentId: String) = bannerRepository.editBanner(banner, documentId)
}