# RainbowConsole
RainbowConsole Application

Create by Jingeun_Cho

<h4 align="center"> Tech Spec <h4>
<p align="center">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logoColor=white&logo=android">
  <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logoColor=white&logo=kotlin">
  <img src="https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logoColor=white&logo=firebase">
</p>
  
<h3 align="center"> Description <h3>
<p align="center"> 매장의 예약 관리(추가 및 취소)와 회원 및 매니저들을 관리하는 태블릿 용 어플리케이션 </p>
<p align="center"> Landscape를 기준으로 하여 제작 </p>
<p align="center"> MVC패턴을 이용하여 기존 스파게티 코드를 해소하고, View에서 직접 Firebase 코드 호출을 최소화</p>
<br/>
<h3 align="center"> Problem <h3>
<p align="center"> 1. Firestore의 AddSnapshotListener를 추가 할 때 Controller에서 선언해야하는가 View에서 해야 하는가? </p> 
<p align="center"> Contoller의 return type을 Collection Reference 타입으로 반환 시 AddSnapshotListener 등록 가능 </p>
 
<p align="center"> 2. MVVM 패턴을 적용 할 수 있는가? </p> 
<p align="center"> MVVM 패턴을 적용하기 위하여 학습 중 </p>


<h3 align="center"> 실행 화면 <h4>
<h4 align="center">1. Login <h4>
<p align="center">매장 선택 또는 전체 매장 선택 후 매니저 로그인</p>
  
![rainbowConsole_login](https://user-images.githubusercontent.com/91510708/178640289-633b0d52-9f54-4930-987d-2334db9d17b0.jpeg)

<h4 align="center">2. Dashboard<h4>
<p align="center">로그인 후 오늘 매장 예약 현황 및 최근에 방문한 회원님 목록 표시</p>
<br/>
  
![rainbow_edit_member](https://user-images.githubusercontent.com/91510708/178640272-6ba516b1-c78a-48c9-8570-3d21029447f2.jpeg)

<h4 align="center">3. 회원 목록 <h4>
<p align="cenrer"> 선택 된 지점 또는 전체 회원 목록 및 정보 관리</p>
<br/>  

![rainbowConsole_member](https://user-images.githubusercontent.com/91510708/178640293-cbeda90d-d54e-439e-af63-a80fba84a469.jpeg)

![rainbow_edit_member](https://user-images.githubusercontent.com/91510708/178640272-6ba516b1-c78a-48c9-8570-3d21029447f2.jpeg)

<h4 align="center">4. 매니저 목록 <h4>
<p align="cenrer"> 선택 된 지점 또는 전체 매니저 목록 및 정보 관리</p>
<br/>
  
![rainbowConsole_manager](https://user-images.githubusercontent.com/91510708/178640291-2ce5652d-d043-4f2e-aa9b-2dd938e6b950.jpeg)
  
![rainbowConsole_edit_manager](https://user-images.githubusercontent.com/91510708/178640282-d979a774-65d0-4545-979d-96ffde6b784e.jpeg)
