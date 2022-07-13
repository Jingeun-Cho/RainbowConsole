package com.zzoin.temprainbow.model

data class ManagerScheduleDTO(
    var schedule : List<DailySchedule> = listOf(
        DailySchedule(dayOfWeek = "mon", kor = "월", ),
        DailySchedule(dayOfWeek = "tue", kor = "화"),
        DailySchedule(dayOfWeek = "wen", kor = "수"),
        DailySchedule(dayOfWeek = "thu", kor = "목"),
        DailySchedule(dayOfWeek = "fri", kor = "금"),
        DailySchedule(dayOfWeek = "sat", kor = "토"),
        DailySchedule(dayOfWeek = "sun", kor = "일"))
){
    data class DailySchedule(
        var dayOfWeek : String = "",
        var kor : String = "",
        var working : String = "",
        var workingStart : Long = 0,
        var workingFinish : Long = 0,
        var restStart : Long = 0,
        var restFinish : Long = 0

    )
}
