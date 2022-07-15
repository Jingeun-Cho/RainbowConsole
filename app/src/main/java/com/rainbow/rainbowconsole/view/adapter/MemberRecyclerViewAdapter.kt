package com.rainbow.rainbowconsole.view.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rainbow.rainbowconsole.R
import com.rainbow.rainbowconsole.view.adapter.MemberRecyclerViewAdapter.*
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.config.AppConfig.countDay
import com.rainbow.rainbowconsole.databinding.LayoutMemberProfileBinding
import com.rainbow.rainbowconsole.model.data_class.UserDTO
import java.time.LocalDate
import java.time.ZoneId
import kotlin.collections.ArrayList

class MemberRecyclerViewAdapter(private val memberItems : ArrayList<UserDTO>) : RecyclerView.Adapter<ViewHolder>(){
    private val today = LocalDate.now().atStartOfDay()
    private val editMemberDialogFragment = AppConfig.editMemberFragment
    inner class ViewHolder(val binding : LayoutMemberProfileBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutMemberProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        val data = memberItems[position]
        setView(binding, data)
    }

    @SuppressLint("SetTextI18n")
    private fun setView(binding : LayoutMemberProfileBinding, data : UserDTO){
        Glide
            .with(binding.root.context)
            .load(data.profileImg)
            .placeholder(R.drawable.default_profile)
            .circleCrop()
            .into(binding.imgProfile)
        binding.textMemberName.text = data.name
        binding.layoutRemainPeriod.textTitle.text = "남은 기간 : "
        binding.layoutRemainPeriod.textValue.text = (data.lessonMembershipEnd - today.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()).countDay()
        binding.layoutRemainCoupon.textTitle.text = "남은 레슨 횟수 : "
        binding.layoutRemainCoupon.textValue.text = "${data.lessonMembership - data.lessonMembershipUsed}회"
        binding.textMemberPhone.text = data.phone
        binding.textMembership.text = data.lessonMembershipType
        binding.layoutPro.textTitle.text = "담당 프로 : "
        binding.layoutPro.textValue.text = data.pro

        binding.btnInfo.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("uid", data.uid)
            editMemberDialogFragment.arguments = bundle
            editMemberDialogFragment.show((binding.root.context as AppCompatActivity).supportFragmentManager, "editMemberFragment")
        }

    }

    override fun getItemCount(): Int = memberItems.size
}