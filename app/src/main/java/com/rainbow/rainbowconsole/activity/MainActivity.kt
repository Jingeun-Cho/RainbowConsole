package com.rainbow.rainbowconsole.activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.Query
import com.rainbow.rainbowconsole.R
import com.rainbow.rainbowconsole.adapter.RecentLessonRecyclerViewAdapter
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.config.AppConfig.auth
import com.rainbow.rainbowconsole.databinding.ActivityMainBinding
import com.zzoin.temprainbow.model.LessonDTO
import io.grpc.Context
import java.time.LocalDate
import java.time.ZoneOffset

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val bundle = Bundle()
    private val proFragment = AppConfig.proFragment
    private val dashboardFragment = AppConfig.dashboardFragment
    private val memberFragment = AppConfig.memberFragment
    private val settingDialogFragment = AppConfig.settingDialogFragment
    private val firestore = AppConfig.firestore
    private val bannerSettingFragment = AppConfig.bannerSettingFragment

    private val offset = ZoneOffset.systemDefault()
    private val today = LocalDate.now().atStartOfDay(offset).toInstant().toEpochMilli()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val branch = intent.getStringExtra("branch")
        bundle.putString("branch", branch)
        initView(branch!!)
        setContentView(binding.root)

    }

    private fun initView(branch : String){

        val arrayItem = resources.getStringArray(R.array.branch_status)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayItem)

//        CoroutineScope(Dispatchers.IO).launch {
//            val lessonItems = if(branch == "전체") lessonController.searchRecent().await() else lessonController.searchRecent(branch).await()

//        }

        updateRecentMember()
        binding.textBranch.text = if( branch == "전체" ) branch else branch + "점"

        binding.spinnerBranchStatus .apply {
            adapter = spinnerAdapter
        }
        binding.spinnerBranchStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected( parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

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
        binding.recyclerRecentMember.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = RecentLessonRecyclerViewAdapter(lessonItems)
        }
    }
    private fun updateRecentMember(){
        firestore.collection("lesson")
            .whereGreaterThanOrEqualTo("lessonDateTime", today)
            .whereLessThanOrEqualTo("lessonDateTime", (today + 24 * 60 * 60 * 1000 -1))
            .orderBy("lessonDateTime", Query.Direction.DESCENDING)
            .addSnapshotListener{ querySnapshot, error ->
                if(querySnapshot == null || error != null){
                    Log.e("MainActivity", "initView: ${error?.message}", )
                    return@addSnapshotListener
                }
                val lessonItems = querySnapshot.toObjects(LessonDTO::class.java) as ArrayList
                initRecentRecyclerView(lessonItems)
            }
    }
}