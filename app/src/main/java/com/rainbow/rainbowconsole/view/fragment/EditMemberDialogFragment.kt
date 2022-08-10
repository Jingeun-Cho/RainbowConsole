package com.rainbow.rainbowconsole.view.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.rainbow.rainbowconsole.R
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.config.AppConfig.convertTimestampToSimpleDate
import com.rainbow.rainbowconsole.databinding.FragmentEditMemberDialogBinding
import com.rainbow.rainbowconsole.model.data_class.UserDTO
import com.rainbow.rainbowconsole.view_model.fragment.EditMemberViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditMemberDialogFragment : DialogFragment() {

    private var binding : FragmentEditMemberDialogBinding? = null
    private lateinit var editMemberViewModel : EditMemberViewModel

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {

        editMemberViewModel = ViewModelProvider(this).get(EditMemberViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_member_dialog, container,  false)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            editMemberViewModel = editMemberViewModel

        }
        return binding?.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val uid = arguments?.getString("uid", "")!!
        val documentId = arguments?.getString("documentId", "")!!
        editMemberViewModel.getMemberItem(uid)
        editMemberViewModel.observeMemberItem().observe(viewLifecycleOwner){ userItem ->
            if(userItem != null){
                //First Row
                binding?.textMemberName?.text = userItem.name
                binding?.inputMemberPhone?.setText(userItem.phone)
                binding?.inputMemberBirthday?.text = userItem.birth?.convertTimestampToSimpleDate()

                //Second Row
                binding?.inputMemberEmail?.setText(userItem.email)
                binding?.textMemberBranch?.text = userItem.branch
                binding?.textMemberGender?.text = userItem.gender

                //Third Row
                binding?.textLessonType?.text = userItem.lessonMembershipType
                binding?.textLessonTotal?.text = "${userItem.lessonMembership}회"
                binding?.textLessonUsed?.text = "${userItem.lessonMembershipUsed}회"
                binding?.textLessonCancel?.text = "${userItem.lessonCancelCount}회"

                //User Memo
                binding?.inputMemberMemo?.setText(userItem.memo)

                //User log
                binding?.inputMemberLog?.setText(userItem.log)


                binding?.btnUpdate?.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        //update
                        updateUser(userItem, documentId)
                    }
                }
            }
            else{
                binding?.btnUpdate?.visibility = View.GONE
            }
        }
        binding?.btnCancel?.setOnClickListener { dismiss() }
    }

    private fun updateUser(userItem : UserDTO, documentId : String){
        userItem.memo = binding?.inputMemberMemo?.text.toString()
        userItem.phone= binding?.inputMemberPhone?.text.toString()

        editMemberViewModel.updateMemberItem(userItem, documentId)
            .addOnSuccessListener {
                dismiss()
            }
            .addOnFailureListener {
                    Snackbar.make(binding?.root!!, "수정에 실패 했습니다, 잠시 후 다 시도해주세요.", Snackbar.LENGTH_SHORT ).show()
            }

    }

    override fun onResume() {
        super.onResume()
        val width = resources.displayMetrics.widthPixels * 0.8
        val height = resources.displayMetrics.heightPixels * 0.9

        dialog?.window?.setLayout(width.toInt(), height.toInt())
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}