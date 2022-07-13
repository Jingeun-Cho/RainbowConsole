package com.rainbow.rainbowconsole.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.*
import androidx.recyclerview.widget.RecyclerView
import com.rainbow.rainbowconsole.R
import com.rainbow.rainbowconsole.activity.MainActivity
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.config.AppConfig.convertTimeStampToSimpleTime
import com.rainbow.rainbowconsole.config.AppConfig.createTimeTableRowTime
import com.rainbow.rainbowconsole.config.AppConfig.dpToPixel
import com.rainbow.rainbowconsole.controller.MemberController
import com.rainbow.rainbowconsole.databinding.LayoutTimeTableRowBinding
import com.zzoin.temprainbow.model.LessonDTO
import com.zzoin.temprainbow.model.ManagerDTO
import com.zzoin.temprainbow.model.UserDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TodayScheduleRecyclerViewAdapter(
    private val pro: ArrayList<ManagerDTO>,
    private val todayLessonItems: ArrayList<LessonDTO>,
    private val documentIds: ArrayList<String>,
    private val today: Long
) : RecyclerView.Adapter<TodayScheduleRecyclerViewAdapter.ViewHolder>() {
    private val tableLength = pro.size

    //Constructor
    /**
     * private val todaySchedule: ArrayList<LessonDTO>, private val pro : ArrayList<ManagerDTO>
     */
    private val startTime : Long = 6
    private val interval : Long = 15
    private val ticHour  = 18
    private val startTimeMilli = startTime * 60 * 60 * 1000
    private val intervalMilli : Long = interval * 60 * 1000
    private val memberController : MemberController = AppConfig.memberController


    inner class ViewHolder(val binding : LayoutTimeTableRowBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutTimeTableRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        pro.forEachIndexed { index, _ ->
            addTextView(view, index)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rowTime = startTimeMilli + (intervalMilli * position)
//        Log.d("TodayScheduleRecyclerViewAdapter", "onBindViewHolder: ${rowTime} ")
        setRow(holder.binding, rowTime)
    }

    private fun setRow(binding : LayoutTimeTableRowBinding, time : Long){
        binding.textTime.text = time.createTimeTableRowTime()
        pro.forEachIndexed{ index, _ ->
            updateTableRow(binding, index, time)
        }
    }
    private fun addTextView(binding : LayoutTimeTableRowBinding, index : Int){

        val textView = TextView(binding.root.context)
        val layoutParams = LinearLayout.LayoutParams( 250, LinearLayout.LayoutParams.MATCH_PARENT)
        layoutParams.setMargins(10)

        textView.textSize = 18f
        textView.gravity = Gravity.CENTER
        textView.background = ContextCompat.getDrawable(binding.root.context, R.drawable.design_rounded_rectangle)
        textView.setTextColor(ContextCompat.getColor(binding.root.context ,R.color.white))
        textView.setTypeface(null, Typeface.NORMAL)
        textView.id = index

        binding.layoutRoot.addView(textView, layoutParams)

    }

    private fun updateTableRow(binding: LayoutTimeTableRowBinding,  viewIndex : Int, time : Long,){
        val textView = binding.layoutRoot[viewIndex] as TextView
        val (lesson, index) = getMatchLesson(todayLessonItems,time, pro[viewIndex].uid!!)

        if(lesson == null){
            textView.background.setTint(ContextCompat.getColor(binding.root.context ,R.color.disable_gray))
            textView.text = time.createTimeTableRowTime()
            textView.setOnClickListener {
                showLessonReserveDialog(binding, pro[viewIndex].uid!!, today + time)
            }
            textView.setOnLongClickListener { return@setOnLongClickListener true }
            return
        }

        CoroutineScope(Dispatchers.Main).launch {
            val (member, documentId) = memberController.searchByUid(lesson.uid!!).await()
            textView.text = member?.name
            if(lesson.lessonNote!!.isNotEmpty()) textView.background.setTint( ContextCompat.getColor(binding.root.context, R.color.lesson_green) )

            else {
                textView.background.setTint(ContextCompat.getColor(binding.root.context, R.color.available_purple) )
                textView.setOnClickListener {  }
                textView.setOnLongClickListener {
                    showLessonCancelDialog(binding, documentIds[index])
                    return@setOnLongClickListener false
                }
            }
        }
    }

    override fun getItemCount(): Int = ticHour * 4 + 1

    private fun getMatchLesson(lessonItems : ArrayList<LessonDTO>, time : Long, proUid : String) : Pair<LessonDTO?, Int> {

        var lesson : LessonDTO? = null
        var selectedIndex : Int = -1
        lessonItems.forEachIndexed { index, item ->
            if( today + time in  item.lessonDateTime!! until  (item.lessonDateTime!! + item.lessontime!!) && proUid == item.coachUid){
                lesson = item
                selectedIndex = index
                return@forEachIndexed
            }
        }

        return Pair(lesson, selectedIndex)
    }

    private fun showLessonReserveDialog(binding: LayoutTimeTableRowBinding, proUid : String, selectedTime : Long){
        val lessonReserveDF = AppConfig.lessonReserveDialogFragment
        val bundle = Bundle()
        bundle.putLong("lessonDateTime", selectedTime)
        bundle.putString("proUid", proUid)

        lessonReserveDF.arguments = bundle
        lessonReserveDF.show((binding.root.context as AppCompatActivity).supportFragmentManager, "reserveDialog")
    }

    private fun showLessonCancelDialog(binding: LayoutTimeTableRowBinding, documentId : String){
        val lessonDeleteDialogFragment = AppConfig.lessonDeleteDialogFragment
        val title = "레슨 취소"
        val message = "해당 레슨을 취소하시겠습니까?"
        val bundle = Bundle()
        bundle.putString("title", title)
        bundle.putString("message", message)
        bundle.putString("documentId", documentId)
        lessonDeleteDialogFragment.arguments = bundle
        lessonDeleteDialogFragment.show((binding.root.context as AppCompatActivity).supportFragmentManager, "cancelDialog")
    }


}