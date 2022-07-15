package com.rainbow.rainbowconsole.view.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.rainbow.rainbowconsole.R.*
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.config.AppConfig.convertTimestampToDate
import com.rainbow.rainbowconsole.model.controller.LessonController
import com.rainbow.rainbowconsole.model.controller.MemberController
import com.rainbow.rainbowconsole.model.controller.ProController
import com.rainbow.rainbowconsole.databinding.DialogLessonReservationBinding
import com.rainbow.rainbowconsole.model.data_class.LessonDTO
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO
import com.rainbow.rainbowconsole.model.data_class.UserDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LessonReserveDialogFragment : DialogFragment(){
    private var binding : DialogLessonReservationBinding? = null
    private val proController : ProController = AppConfig.proController
    private val memberController : MemberController = AppConfig.memberController
    private  val lessonController : LessonController = AppConfig.lessonController
    private var selectedMember : UserDTO? = null
    private var selectedLessonType : String = "레슨"
    private var lessonDuration : Long = 15 * 60 * 1000
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogLessonReservationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val proUid = arguments?.getString("proUid", "")
        val selectedTime = arguments?.getLong("lessonDateTime", 0)!!

        CoroutineScope(Dispatchers.IO).launch {
            val pro : ManagerDTO? = getProInformation(proUid)
            val memberList = getMemberList(proUid)
            requireActivity().runOnUiThread {
            initView(pro, memberList, selectedTime)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView(pro : ManagerDTO?, memberList : ArrayList<UserDTO>, selectedTime : Long){
        val selectMemberArray : ArrayList<String> = arrayListOf()
        memberList.forEach { selectMemberArray.add(it.name!!) }
        binding?.spinnerMember?.apply {
            adapter = ArrayAdapter(requireContext(), layout.design_center_text_dropdown, selectMemberArray)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                    selectedMember = memberList[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?){}
            }
        }

        val lessonTypes = resources.getStringArray(array.lesson_type)
        binding?.spinnerType?.apply {
            adapter = ArrayAdapter(requireContext(), layout.design_center_text_dropdown, lessonTypes)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                    selectedLessonType = lessonTypes[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
        val lessonTimeLists = resources.getStringArray(array.lesson_time)
        binding?.spinnerLessonTime?.apply {
            adapter = ArrayAdapter(requireContext(), layout.design_center_text_dropdown, lessonTimeLists)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                    lessonDuration = (lessonTimeLists[position].slice(0..1)  .toLong()) * 60 * 1000
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }
        binding?.textProName?.text = pro?.name + " 프로님"
        binding?.textLessonTime?.text = selectedTime.convertTimestampToDate()

        binding?.btnConfirm?.setOnClickListener {
            val alertDialog = AlertDialog.Builder(requireContext())
            val title = "레슨 예약"
            val lessonMemo = binding?.inputLessonMemo?.text.toString()
            val message = "${pro?.name}프로님 ${selectedMember?.name}레슨을 예약 하시겠습니까?"
            alertDialog.setTitle(title)
                .setMessage(message)
                .setNegativeButton("취소"){ _, _ -> }
                .setPositiveButton("예약") { _, _ ->
                    val reserveLesson = LessonDTO(
                        coachUid = pro?.uid,
                        uid = selectedMember?.uid,
                        lessonDateTime = selectedTime,
                        lessontime = lessonDuration,
                        lessonMemo = lessonMemo,
                        type = selectedLessonType )

                    val documentId = "${pro?.name}_${selectedTime}"
                    lessonController.reserveLesson(documentId, reserveLesson)
                        .addOnSuccessListener {
                            //완료 화면 추가
                            dismiss()
                        }
                        .addOnFailureListener {
                            //실패 화면 추가
                            dismiss()
                        }
                }.show()
        }
        binding?.btnCancel?.setOnClickListener {
            dismiss()
        }
    }

    private suspend fun getProInformation(uid : String?) : ManagerDTO?{
        if(uid.isNullOrBlank()) return null
        return proController.searchProByUid(uid).await()
    }
    private suspend fun getMemberList(uid : String?) : ArrayList<UserDTO>{
        if(uid.isNullOrBlank()) return arrayListOf()
        return memberController.searchByProUid(uid).await()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}