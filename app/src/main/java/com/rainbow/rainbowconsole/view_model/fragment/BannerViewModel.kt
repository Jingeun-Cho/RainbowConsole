package com.rainbow.rainbowconsole.view_model.fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Transaction
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.model.data_class.BannerDTO

class BannerViewModel : ViewModel(){
    private val bannerRepository = AppConfig.bannerRepository
    private val bannerItems : MutableLiveData<QuerySnapshot> = MutableLiveData()


    fun observeBannerList() : MutableLiveData<QuerySnapshot> = bannerItems
    fun getBanner(){
        bannerRepository.getBannerList()
            .addSnapshotListener { value, error ->
                if(value == null || error != null ) return@addSnapshotListener
                bannerItems.postValue(value)
            }
    }

    fun deleteBanner(documentId: String) : Task<Transaction> =
        bannerRepository.deleteBanner(documentId)

}