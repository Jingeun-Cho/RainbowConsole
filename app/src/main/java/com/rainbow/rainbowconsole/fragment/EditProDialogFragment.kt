package com.rainbow.rainbowconsole.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.rainbow.rainbowconsole.R
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.config.AppConfig.convertDateToTimestamp
import com.rainbow.rainbowconsole.config.AppConfig.convertSimpleTimeToTimestamp
import com.rainbow.rainbowconsole.config.AppConfig.convertTimeStampToSimpleTime
import com.rainbow.rainbowconsole.config.AppConfig.convertTimestamp
import com.rainbow.rainbowconsole.config.AppConfig.createTimeTableRowTime
import com.rainbow.rainbowconsole.controller.ProController
import com.rainbow.rainbowconsole.databinding.FragmentEdieProBinding
import com.zzoin.temprainbow.model.ManagerDTO
import com.zzoin.temprainbow.model.ManagerScheduleDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProDialogFragment : DialogFragment(){

    private var binding : FragmentEdieProBinding? = null
    private val proController : ProController = AppConfig.proController
    private var previousPosition = 0
    private var isNotFirst = false

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = FragmentEdieProBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val proUid = arguments?.getString("proUid", "")!!
        initView(proUid)


    }
    private fun updateProfile(proItem : ManagerDTO, proSchedule: ManagerScheduleDTO){
        proController.updatePro(proItem, proSchedule)!!
            .addOnSuccessListener { dismiss() }
            .addOnFailureListener {
                Snackbar.make(binding!!.root, "수정에 실패 했습니다, 잠시 후 다 시도해주세요.", Snackbar.LENGTH_SHORT ).show()
            }
    }

    private fun initView(proUid : String){
        CoroutineScope(Dispatchers.Main).launch {
            val proItem = proController.searchProByUid(proUid).await()
            val proSchedule = proController.getProSchedule(proUid).await()
            val editBranchItems = resources.getStringArray(R.array.edit_branch)
            val dateOfWeek = resources.getStringArray(R.array.date_of_week)
            val branchSpinnerAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, editBranchItems)
            val dateSelectAdapter = ArrayAdapter(requireContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, dateOfWeek)
            if(proItem != null){

                Glide
                    .with(this@EditProDialogFragment)
                    .load(proItem.profileImg)
                    .placeholder(R.drawable.default_profile)
                    .circleCrop()
                    .into(binding!!.imgPro)

                binding?.inputProName?.setText(proItem.name)
                binding?.inputProPhone?.setText(proItem.phone)

                Log.d("spinner", "onViewCreated: ${proItem.branch} / ${editBranchItems.indexOf(proItem.branch + "점") }")
                binding?.inputProWebpage?.setText(proItem.proUrl)
                binding?.inputProBranch?.apply {
                    adapter = branchSpinnerAdapter
                    setSelection(editBranchItems.indexOf(proItem.branch + "점"))
                    onItemSelectedListener = object : OnItemSelectedListener {
                        override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                            proItem.branch = editBranchItems[position]
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            Log.d("spinner", "onViewCreated1: ${editBranchItems} ")
                        }
                    }
                }

                binding?.inputProStartTime?.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int ) {
                    }

                    override fun onTextChanged( string: CharSequence?, start: Int, before: Int, count: Int ) {
                        inputTextFormatTime(binding?.inputProStartTime!!, string!!, before,count)

                    }

                    override fun afterTextChanged(s: Editable?) {
                    }
                })

                binding?.inputProEndTime?.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int ) {
                    }

                    override fun onTextChanged( string: CharSequence?, start: Int, before: Int, count: Int ) {
                        inputTextFormatTime(binding?.inputProEndTime!!, string!!, before,count)

                    }

                    override fun afterTextChanged(s: Editable?) {
                    }
                })

                binding?.inputProRestStartTime?.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int ) {
                    }

                    override fun onTextChanged( string: CharSequence?, start: Int, before: Int, count: Int ) {
                        inputTextFormatTime(binding?.inputProRestStartTime!!, string!!, before,count)

                    }

                    override fun afterTextChanged(s: Editable?) {
                    }
                })

                binding?.inputProRestEndTime?.addTextChangedListener(object : TextWatcher{
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int ) {
                    }

                    override fun onTextChanged( string: CharSequence?, start: Int, before: Int, count: Int ) {
                        inputTextFormatTime(binding?.inputProRestEndTime!!, string!!, before,count)

                    }

                    override fun afterTextChanged(s: Editable?) {
                    }
                })




                binding?.inputProScheduleDate?.apply {
                    adapter = dateSelectAdapter
                    onItemSelectedListener = object : OnItemSelectedListener{
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                            updateEditScheduleView(proSchedule, position )
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                        }
                    }
                }
                binding?.btnUpdate?.setOnClickListener {
                    val beforeWorkStart = binding?.inputProStartTime?.text.toString().convertTimestamp()
                    val beforeWorkEnd = binding?.inputProEndTime?.text.toString().convertTimestamp()
                    val beforeRestStart = binding?.inputProRestStartTime?.text.toString().convertTimestamp()
                    val beforeRestEnd = binding?.inputProRestEndTime?.text.toString().convertTimestamp()
                    proSchedule.schedule[previousPosition].apply {
                        workingStart = beforeWorkStart
                        workingFinish = beforeWorkEnd
                        restStart = beforeRestStart
                        restFinish = beforeRestEnd
                    }
                    //ProItem update
                    proItem.name = binding?.inputProName?.text.toString()
                    proItem.phone = binding?.inputProPhone?.text.toString()
                    proItem.proUrl = binding?.inputProWebpage?.text.toString()
                    updateProfile(proItem, proSchedule)
                }

                binding?.btnCancel?.setOnClickListener {
                    dismiss()
                }
            }
            else{
                //Alert 추가
                dismiss()
            }
        }
    }

    private fun inputTextFormatTime(edieText : EditText, string : CharSequence, before : Int, count : Int){
        if (string.length == 2 && count == 1)
            edieText.append(":")
        else if(string.length == 2 && before == 1)
            edieText.apply{
                setText(string.substring(0,1))
                setSelection(1)
            }
    }
    private fun updateEditScheduleView(proSchedule : ManagerScheduleDTO, position : Int){
        if(isNotFirst) {
            val beforeWorkStart = binding?.inputProStartTime?.text.toString().convertTimestamp()
            val beforeWorkEnd = binding?.inputProEndTime?.text.toString().convertTimestamp()
            val beforeRestStart = binding?.inputProRestStartTime?.text.toString().convertTimestamp()
            val beforeRestEnd = binding?.inputProRestEndTime?.text.toString().convertTimestamp()


            proSchedule.schedule[previousPosition].apply {
                workingStart = beforeWorkStart
                workingFinish = beforeWorkEnd
                restStart = beforeRestStart
                restFinish = beforeRestEnd
            }
            isNotFirst = true
        }
        Log.d("updateEditScheduleView", "updateEditScheduleView: ${proSchedule.schedule[position].workingStart}")
        binding?.inputProStartTime?.setText( proSchedule.schedule[position].workingStart.createTimeTableRowTime() )
        binding?.inputProEndTime?.setText( proSchedule.schedule[position].workingFinish.createTimeTableRowTime() )
        binding?.inputProRestStartTime?.setText( proSchedule.schedule[position].restStart.createTimeTableRowTime() )
        binding?.inputProRestEndTime?.setText( proSchedule.schedule[position].restFinish.createTimeTableRowTime() )

        previousPosition = position
    }
    override fun onResume() {
        super.onResume()
        val width = (resources.displayMetrics.widthPixels / 1.5).toInt()
        val height : Int = (resources.displayMetrics.heightPixels * 0.9).toInt()

        dialog?.window?.setLayout(width, height)
    }


}