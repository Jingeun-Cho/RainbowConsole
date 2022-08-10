package com.rainbow.rainbowconsole.view.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rainbow.rainbowconsole.R
import com.rainbow.rainbowconsole.config.AppConfig.convertTimestampToFullTime
import com.rainbow.rainbowconsole.databinding.LayoutRecentlyMemberBinding
import com.rainbow.rainbowconsole.model.data_class.LessonDTO
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO
import com.rainbow.rainbowconsole.model.data_class.UserDTO

class RecentLessonRecyclerViewAdapter: RecyclerView.Adapter<RecentLessonRecyclerViewAdapter.ViewHolder>() {

    private val recentItems : ArrayList<Triple<LessonDTO, UserDTO, ManagerDTO>> = arrayListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun getData(data : ArrayList<Triple<LessonDTO, UserDTO, ManagerDTO>>){
        Log.d("test", "getData: call ")
        recentItems.clear()
        recentItems.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: LayoutRecentlyMemberBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutRecentlyMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val data = recentItems[position]
        Log.d("test", "onBindViewHolder: ${data}")
        setView(binding, data)
    }

    override fun getItemCount(): Int = recentItems.size

    private fun setView(binding : LayoutRecentlyMemberBinding, data : Triple<LessonDTO, UserDTO, ManagerDTO>){
        val lesson = data.first
        val user = data.second
        val pro = data.third

        binding.textMemberName.text = user.name
        binding.textProName.text = pro.name
        //진짜 시간 적용 필요
        binding.textLessonTime.text = lesson.lessonDateTime?.convertTimestampToFullTime()
        Glide
            .with(binding.root.context)
            .load(user.profileImg)
            .placeholder(R.drawable.default_profile)
            .circleCrop()
            .into(binding.imgRecent)
    }
}