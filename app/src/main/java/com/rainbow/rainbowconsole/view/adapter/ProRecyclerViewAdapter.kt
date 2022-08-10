package com.rainbow.rainbowconsole.view.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rainbow.rainbowconsole.R
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.config.AppConfig.createTimeTableRowTime
import com.rainbow.rainbowconsole.databinding.LayoutProProfileBinding
import com.rainbow.rainbowconsole.model.data_class.ManagerDTO
import com.rainbow.rainbowconsole.model.data_class.ManagerScheduleDTO
import java.util.*
import kotlin.collections.ArrayList

class ProRecyclerViewAdapter() : RecyclerView.Adapter<ProRecyclerViewAdapter.ViewHolder>() {

    private val fragmentEditProDialog = AppConfig.editProDialogFragment
    private val calendar = Calendar.getInstance()
    private val dateOfWeek = if(calendar.get(Calendar.DAY_OF_WEEK) == 0) 6 else calendar.get(Calendar.DAY_OF_WEEK) - 2
    private val proItems : ArrayList<Pair<ManagerDTO, ManagerScheduleDTO>> = arrayListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun getData(data : ArrayList<Pair<ManagerDTO, ManagerScheduleDTO>>){
        proItems.clear()
        proItems.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = LayoutProProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    inner class ViewHolder(val binding : LayoutProProfileBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.binding
        val data = proItems[position]
        setView(view, data)
    }

    override fun getItemCount(): Int = proItems.size

    private fun setView(binding: LayoutProProfileBinding, data : Pair<ManagerDTO, ManagerScheduleDTO>){
        val pro = data.first
        val schedule = data.second

        Glide.with(binding.root.context)
            .load(pro.profileImg)
            .placeholder(R.drawable.default_profile)
            .circleCrop()
            .into(binding.imgProfile)
        binding.textProName.text = pro.name
        binding.viewWorkStart.title.text = "근무 시작"

        //진짜 시간 적용 필요
//        binding.viewWorkStart.time.text = schedule.workingStart.convertTimeStampToSimpleTime()
        binding.viewWorkEnd.title.text = "근무 종료"

        binding.btnInfo.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("proUid", pro.uid)
            fragmentEditProDialog.arguments = bundle
            fragmentEditProDialog.show((binding.root.context as AppCompatActivity).supportFragmentManager, "editProDialogFragment")
        }

        schedule.schedule[dateOfWeek].apply {
        binding.viewWorkStart.time.text = workingStart.createTimeTableRowTime()
        binding.viewWorkEnd.time.text = workingFinish.createTimeTableRowTime()
        if(working == "work")
            binding.btnInfo.text = "근무 중"
        else
            binding.btnInfo.text = "휴무"
        }


        //진짜 시간 적용 필요
//        binding.viewWorkEnd.time.text = schedule.workingFinish.convertTimeStampToSimpleTime()

    }


}