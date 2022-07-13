package com.rainbow.rainbowconsole.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.*
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.rainbow.rainbowconsole.R
import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.databinding.ActivityLoginBinding
import com.rainbow.rainbowconsole.controller.LoginControllerImpl
import com.rainbow.rainbowconsole.controller.LoginController
import com.rainbow.rainbowconsole.controller.ProController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private var binding : ActivityLoginBinding? = null
    private lateinit var loginController : LoginController
    private lateinit var proController: ProController

    override fun onStart() {
        super.onStart()
        val preference = getPreferences(MODE_PRIVATE)
        val branch = preference.getString("branch", "전체")
        startActivity(AppConfig.auth.currentUser, branch!!)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initActivity()
        initView()
        setContentView(binding?.root)
    }

    private fun initActivity(){
        binding = ActivityLoginBinding.inflate(layoutInflater)
        loginController = LoginControllerImpl(AppConfig.auth)
        proController = AppConfig.proController
    }
    private fun initView(){
        val arrayItem = resources.getStringArray(R.array.branch)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, arrayItem)
        var branch = ""
        binding?.spinnerBranch.let {
            it?.adapter = spinnerAdapter
        }

        binding?.spinnerBranch!!.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                branch = arrayItem[position]
                if(position != 0)
                    branch = branch.substring(0, branch.length - 1 )

                Log.d("LoginActivity", "onItemSelected: $branch")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding?.btnLogin!!.setOnClickListener {
            val email = binding!!.inputEmail.text.toString()
            val password = binding!!.inputPassword.text.toString()

            login(branch, email, password)
        }

    }

    private fun login(branch : String, email : String, password : String){
        CoroutineScope(Dispatchers.IO).launch {
            val loginResult = loginController.loginByEmailAsync(email, password).await()
            if(loginResult != null){
                val checkBranch = proController.searchProByBranch(branch).await()
                val verified = checkBranch.find { item ->
                    item.email == email
                }
                if(verified != null || branch == "전체"){
                    startActivity(loginResult, branch)
                }
                else{
                    runOnUiThread {
                        Snackbar.make(binding!!.root, "해당 지점에 존재하지 않는 프로입니다", Snackbar.LENGTH_SHORT).show()
                    }
                    AppConfig.auth.signOut()
                }
            }
            else{
                runOnUiThread {
                    Snackbar.make(binding!!.root, "아이디 또는 비밀번호가 맞지 않습니다. 다시 시도해주세요", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun startActivity(user : FirebaseUser?, branch: String){
        if(user !== null){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)

            val sharedPreferences = getPreferences(MODE_PRIVATE)
            val prefEdit = sharedPreferences.edit()
            prefEdit.putString("branch", branch)
            prefEdit.apply()

            intent.putExtra("branch", branch)
            startActivity(intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}