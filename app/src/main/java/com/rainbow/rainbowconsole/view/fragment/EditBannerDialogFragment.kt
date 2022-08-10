package com.rainbow.rainbowconsole.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.rainbow.rainbowconsole.R
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.databinding.FragmentEditBannerDialogBinding
import com.rainbow.rainbowconsole.model.data_class.BannerDTO
import com.rainbow.rainbowconsole.view_model.fragment.EditBannerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditBannerDialogFragment : DialogFragment(){
    private var binding : FragmentEditBannerDialogBinding? = null
    private lateinit var editBannerViewModel: EditBannerViewModel

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        editBannerViewModel = ViewModelProvider(this).get(EditBannerViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_banner_dialog, container, false)
        binding?.lifecycleOwner = viewLifecycleOwner
        binding?.editBannerViewModel = editBannerViewModel
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 0 -> Add
        // 1 -> Edit

        val dialogType = arguments?.getInt("type", 0)!!
        binding?.textTitle?.apply {
            text = when(dialogType){
                0 -> "배너 추가"
                1 -> "배너 수정"
                else -> {""}
            }
        }
        Log.d("dialog", "onViewCreated: ${dialogType} ")
        if(dialogType == 1){
            val documentId = arguments?.getString("documentId", "")!!

            editBannerViewModel.getBanner(documentId)
            editBannerViewModel.observeBannerItem().observe(viewLifecycleOwner, object : Observer<BannerDTO>{
                override fun onChanged(bannerItem: BannerDTO?) {
                    binding?.inputDescription?.setText(bannerItem?.description)
                    binding?.inputDialogTitle?.setText(bannerItem?.dialogTitle)
                    binding?.inputImageUrl?.setText(bannerItem?.imageUrl)
                    binding?.inputRedirectUrl?.setText(bannerItem?.redirectUrl)

                    binding?.btnConfirm?.setOnClickListener {
                        editBanner(bannerItem!!, documentId)
                    }
                    editBannerViewModel.observeBannerItem().removeObserver(this)

                }
            })


//            CoroutineScope(Dispatchers.IO).launch {
//
//                bannerItem = bannerController.getBanner(documentId).await()
//
//                requireActivity().runOnUiThread {
//                    binding?.inputDescription?.setText(bannerItem?.description)
//                    binding?.inputDialogTitle?.setText(bannerItem?.dialogTitle)
//                    binding?.inputImageUrl?.setText(bannerItem?.imageUrl)
//                    binding?.inputRedirectUrl?.setText(bannerItem?.redirectUrl)
//
//
//                    binding?.btnConfirm?.setOnClickListener {
//                        editBanner(bannerItem!!, documentId)
//                    }
//                }
//            }
        }
        else{
            binding?.btnConfirm?.setOnClickListener {
                val bannerItem = BannerDTO()
                addBanner(bannerItem)
            }
        }

        binding?.btnBtnCancel?.setOnClickListener {
            dismiss()
        }

    }

    private fun addBanner(bannerItem : BannerDTO){
        val currentTime = System.currentTimeMillis()
        val description = binding?.inputDescription?.text.toString()
        val dialogTitle = binding?.inputDialogTitle?.text.toString()
        val imageUrl = binding?.inputImageUrl?.text.toString()
        val redirectionUrl = binding?.inputRedirectUrl?.text.toString()
        val documentId = "banner_${currentTime}"
        bannerItem.apply {
            this.description = description
            this.dialogTitle = dialogTitle
            this.documentId = documentId
            this.imageUrl = imageUrl
            this.redirectUrl = redirectionUrl
        }
        editBannerViewModel.addBanner(bannerItem, documentId)
            .addOnSuccessListener {
                dismiss()
            }
            .addOnFailureListener {
                Snackbar.make(binding!!.root, "배너 등록에 실패 했습니다.", Snackbar.LENGTH_SHORT).show()
            }

    }

    private fun editBanner(bannerItem : BannerDTO, documentId : String){
        val description = binding?.inputDescription?.text.toString()
        val dialogTitle = binding?.inputDialogTitle?.text.toString()
        val imageUrl = binding?.inputImageUrl?.text.toString()
        val redirectionUrl = binding?.inputRedirectUrl?.text.toString()
        bannerItem.apply {
            this.description = description
            this.dialogTitle = dialogTitle
            this.documentId = documentId
            this.imageUrl = imageUrl
            this.redirectUrl = redirectionUrl
        }

        editBannerViewModel.editBanner(bannerItem, documentId)
            .addOnSuccessListener {
                dismiss()
            }
            .addOnFailureListener {
                Snackbar.make(binding!!.root, "배너 수정에 실패 했습니다. 다시 시도 해주세요.", Snackbar.LENGTH_SHORT).show()
            }

    }

    override fun onResume() {
        super.onResume()
        val width = resources.displayMetrics.widthPixels * 0.6
        val height = resources.displayMetrics.heightPixels * 0.7

        dialog?.window?.setLayout(width.toInt(), height.toInt())
        binding?.inputDescription?.setText("")
        binding?.inputDialogTitle?.setText("")
        binding?.inputImageUrl?.setText("")
        binding?.inputRedirectUrl?.setText("")

    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}