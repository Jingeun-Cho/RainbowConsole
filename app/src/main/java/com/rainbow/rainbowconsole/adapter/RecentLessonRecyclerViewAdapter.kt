package com.rainbow.rainbowconsole.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rainbow.rainbowconsole.R
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.config.AppConfig.convertTimestampToDate
import com.rainbow.rainbowconsole.config.AppConfig.convertTimestampToFullTime
import com.rainbow.rainbowconsole.controller.MemberController
import com.rainbow.rainbowconsole.controller.ProController
import com.rainbow.rainbowconsole.databinding.LayoutRecentlyMemberBinding
import com.zzoin.temprainbow.model.LessonDTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecentLessonRecyclerViewAdapter(private val recentItems : ArrayList<LessonDTO>) : RecyclerView.Adapter<RecentLessonRecyclerViewAdapter.ViewHolder>() {
    private val memberController : MemberController = AppConfig.memberController
    private val proController : ProController = AppConfig.proController

    inner class ViewHolder(val binding: LayoutRecentlyMemberBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutRecentlyMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val data = recentItems[position]
        setView(binding, data)
    }

    override fun getItemCount(): Int = recentItems.size

    private fun setView(binding : LayoutRecentlyMemberBinding, data : LessonDTO){
        CoroutineScope(Dispatchers.Main).launch {
            val (member, documentId) = memberController.searchByUid(data.uid!!).await()
            val pro = proController.searchProByUid(data.coachUid!!).await()
            Log.d("RecentLessonRecyclerViewAdapter", "${data.uid} \n ${data.lessonDateTime?.convertTimestampToDate()}")
            Glide
                .with(binding.root.context)
                .load(member?.profileImg)
                .placeholder(R.drawable.default_profile)
                .circleCrop()
                .into(binding.imgRecent)


            binding.textMemberName.text = member?.name
            binding.textProName.text = pro?.name
            //진짜 시간 적용 필요
            binding.textLessonTime.text = data.lessonDateTime?.convertTimestampToFullTime()

        }
    }
}