package com.rainbow.rainbowconsole.model.data_class

import java.io.Serializable

data class UserDTO(
    var uid : String? = "",
    var name : String? = "",             // 회원명 매니저
    var gender : String? = "",           // 성별 매니저
    var birth : Long? = 0,                  // 생년월일 매니저
    var phone : String? = "",            // 회원 전화번호 매니저
    var email : String? = "",            // 회원 이메일 유저
    var profileImg : String? = "",          // 프로필 이미지

    var lessonAvailable : Boolean = false, // 레슨 유무

    var lessonMembershipType : String = "",
    var lessonMembershipStart : Long = 0,   //레슨권 시작일
    var lessonMembershipEnd : Long = 0,     //레슨권 종료일
    var lessonMembership : Long = 0,        //레슨권 총 갯수
    var lessonMembershipUsed: Long = 0,     //사용한 레슨권 갯수
    var lessonCancelCount : Long = 0,       //레슨 취소 횟수

    var branch : String? = "",           // 가입 지점
    var pro : String = "",         // 담당 프로코치
    var proUid : String = "",
    var point : Long = 0,                  // 포인트
    var memo : String? = "",             // 회원 메모
    var log : String? = "",             // 회원 로그
    var available : Boolean = false,        // 앱 가입 여부

    var ableReservation : String? = "false" //회원 예약 가능여부

): Serializable
