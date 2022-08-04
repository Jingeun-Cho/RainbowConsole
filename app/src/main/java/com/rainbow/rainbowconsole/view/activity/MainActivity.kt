package com.rainbow.rainbowconsole.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rainbow.rainbowconsole.R
import com.rainbow.rainbowconsole.view.adapter.RecentLessonRecyclerViewAdapter
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.config.AppConfig.auth
import com.rainbow.rainbowconsole.databinding.ActivityMainBinding
import com.rainbow.rainbowconsole.view_model.activity.MainViewModel
import com.rainbow.rainbowconsole.model.data_class.LessonDTO
import java.time.LocalDate
import java.time.ZoneOffset

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel : MainViewModel
    private val bundle = Bundle()
    private val proFragment = AppConfig.proFragment
    private val dashboardFragment = AppConfig.dashboardFragment
    private val memberFragment = AppConfig.memberFragment
    private val settingDialogFragment = AppConfig.settingDialogFragment
    private val bannerSettingFragment = AppConfig.bannerSettingFragment

    private val offset = ZoneOffset.systemDefault()
    private val today = LocalDate.now().atStartOfDay(offset).toInstant().toEpochMilli()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        binding.apply {
            lifecycleOwner = this@MainActivity
            mainViewModel = mainViewModel
        }

        val branch = intent.getStringExtra("branch")!!
        bundle.putString("branch", branch)
        setViewModel(branch)
        initView(branch)

    }

    private fun initView(branch : String){

        val arrayItem = resources.getStringArray(R.array.branch_status)


        binding.textBranch.text = if( branch == "전체" ) branch else branch + "점"
        binding.recyclerRecentMember.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)

        setBranchStatusSpinner(branch, arrayItem)
        setSideButton()
    }

    private fun setViewModel(branch : String){
        mainViewModel.getTodayLesson(today)
        mainViewModel.getBranchStatus(branch)

        mainViewModel.observeLessonData().observe(this@MainActivity){
            initRecentRecyclerView(it)
        }
    }

    private fun setSideButton(){
        //First initialize
        supportFragmentManager.beginTransaction().apply {
            dashboardFragment.arguments = bundle
            replace(R.id.view_fragment,dashboardFragment)
            commit()
        }

        binding.btnDashboard.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                dashboardFragment.arguments = bundle
                replace(R.id.view_fragment, dashboardFragment)
                commit()
            }
        }
        binding.btnPro.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                proFragment.arguments = bundle
                replace(R.id.view_fragment,proFragment)
                commit()
            }
        }
        binding.btnSetting.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                proFragment.arguments = bundle
                replace(R.id.view_fragment,settingDialogFragment)
                commit()
            }
//           settingDialogFragment.show(supportFragmentManager, "settingDialog")
        }
        binding.btnMember.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                memberFragment.arguments = bundle
                replace(R.id.view_fragment,memberFragment)
                commit()
            }
        }
        binding.btnBanner.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                memberFragment.arguments = bundle
                replace(R.id.view_fragment,bannerSettingFragment)
                commit()
            }
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut().let {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        }
    }

    private fun initRecentRecyclerView(lessonItems : ArrayList<LessonDTO>){
        binding.recyclerRecentMember.adapter = RecentLessonRecyclerViewAdapter(lessonItems)

    }

    private fun setBranchStatusSpinner(branch: String, branchArray : Array<String>){
        val branchStatusItems = resources.getStringArray(R.array.branch_status_value)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, branchArray)
        binding.spinnerBranchStatus.apply {
            adapter = spinnerAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                    val selectStatus = branchStatusItems[position]
                    if(branch == "전체"){
                        val isSuccess = mainViewModel.updateBranchStatus(branchArray, selectStatus)
                        if(isSuccess)
                            Snackbar.make(binding.root, "전체 지점의 상태가 변경 되었습니다.", Snackbar.LENGTH_SHORT).show()
                        else
                            Snackbar.make(binding.root, "전체 지점의 상태가 변경되지 않았습니다. 잠시 후 다시 시도해주세요", Snackbar.LENGTH_SHORT).show()
                    }
                    else{
                        val isSuccess =  mainViewModel.updateBranchStatus(branch, selectStatus)
                        if(isSuccess)
                            Snackbar.make(binding.root, "${branch}점 상태가 변경 되었습니다.", Snackbar.LENGTH_SHORT).show()
                        else
                            Snackbar.make(binding.root, "${branch}점 상태가 변경되지 않았습니다. 잠시 후 다시 시도해주세요", Snackbar.LENGTH_SHORT).show()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }
        }

        mainViewModel.observeBranchStatusData().observe(this){
            val index = branchStatusItems.indexOf(it?.status)
            binding.spinnerBranchStatus.setSelection(index)
        }
    }
}