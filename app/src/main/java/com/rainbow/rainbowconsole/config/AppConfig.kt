package com.rainbow.rainbowconsole.config

import android.util.DisplayMetrics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.rainbow.rainbowconsole.model.controller.*
import com.rainbow.rainbowconsole.model.repository.*
import com.rainbow.rainbowconsole.view.fragment.*
import java.text.SimpleDateFormat
import java.util.*

object AppConfig {
    val firestore: FirebaseFirestore by lazy { Firebase.firestore }
    val auth : FirebaseAuth by lazy { Firebase.auth }

    val proController : ProController = ProControllerImpl(ProRepositoryImpl(firestore))
    val memberController : MemberController = MemberControllerImpl(MemberRepositoryImpl(firestore))
    val lessonController : LessonController = LessonControllerImpl(LessonRepositoryImpl(firestore))

    var loginRepository : LoginRepository = LoginRepositoryImpl(auth)
    val proRepository = ProRepositoryImpl(firestore)
    val memberRepository = MemberRepositoryImpl(firestore)
    val lessonRepository = LessonRepositoryImpl(firestore)
    val bannerRepository = BannerRepositoryImpl(firestore)
    val branchRepository = BranchRepositoryImpl(firestore)

    val proFragment  by lazy { ProFragment() }
    val dashboardFragment by lazy{ DashboardFragment() }
    val memberFragment by lazy{ MemberFragment() }
    val lessonDeleteDialogFragment by lazy { LessonDeleteDialogFragment() }
    val lessonReserveDialogFragment by lazy { LessonReserveDialogFragment() }
    val editProDialogFragment by lazy { EditProDialogFragment() }
    val editMemberFragment by lazy { EditMemberDialogFragment() }
    val settingDialogFragment by lazy { SettingDialogFragment() }
    val bannerSettingFragment by lazy { BannerSettingFragment() }
    val editBannerDialogFragment by lazy { EditBannerDialogFragment() }

    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun String.convertDateToTimestamp(): Long = SimpleDateFormat("yyyy-MM-dd", Locale("ko","KR")).parse(this).time
    fun String.convertSimpleTimeToTimestamp(): Long = SimpleDateFormat("HH:mm", Locale("ko","KR")).parse(this)!!.time
    fun Long.convertTimestampToDate(): String = SimpleDateFormat("yy년 MM월 dd일 aa hh시 mm분", Locale("ko", "KR")).format(this)
    fun Long.convertTimestampToSimpleDate(): String = SimpleDateFormat("yy년 MM월 dd일", Locale("ko", "KR")).format(this)
    fun Long.convertTimeStampToSimpleTime(): String = SimpleDateFormat("HH:mm", Locale("ko", "KR")).format(this)
    fun Long.convertTimestampToFullTime(): String = SimpleDateFormat("M월 dd일 HH:mm", Locale("ko", "KR")).format(this)
    fun Long.countDay() : String? = SimpleDateFormat("dd일 ", Locale("ko", "KR")).format(this)
    private fun Long.doubleLengthString() : String = if(this < 10L) "0${this}" else this.toString()
    fun String.convertTimestamp() : Long {
        val hour = this.substring(0,2).toLong()
        val minute = this.substring(3,5).toLong()
        return (hour * 60 * 60 * 1000) + (minute * 60 * 1000)

    }
    fun Long.createTimeTableRowTime() : String {
        val millisecond = this
        val hour = millisecond / (60 * 60 * 1000)
        val minute = (millisecond % (60 * 60 * 1000)) / (60 * 1000)
        return "${hour.doubleLengthString()}:${minute.doubleLengthString()}"
    }

    fun Float.dpToPixel() : Int = (this * DisplayMetrics().density).toInt()
}