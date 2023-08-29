# FuniBuni Android App

![퍼니버니_소개화면](https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-fe-android/assets/33805423/1ccbdc7b-41cb-426d-9867-702cc55320ef)

> - 프로젝트 명: AI 기반 대형폐기물 간편 배출 서비스 모바일 앱 **FuniBuni(퍼니버니)**
> - 프로젝트 기간: 2023.05.31 ~ 2023.07.11
> - 프로젝트 인원: 6명 (AI/MLOps/FE 4명, Android 1명, BE 1명)
> - 앱 개발 담당자: 조성록

| Job                   | Team                                                                                       | Repository                                                                               |         |
|-----------------------|--------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|---------|
| Introduce             | [FuniBuni Team](https://github.com/orgs/KT-AIVLE-3rd-AI-Team10/teams/funibuni-team/teams)  | [funibuni-introduce](https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-introduce)       |
| AI: Modeling          | [AI Modeling Team](https://github.com/orgs/KT-AIVLE-3rd-AI-Team10/teams/ai-modeling)       | [funibuni-ai-modeling](https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-ai-modeling)   |
| AI: MLOps             | [MLOps Team](https://github.com/orgs/KT-AIVLE-3rd-AI-Team10/teams/mlops-enginner)          | [funibuni-ai-mlops](https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-ai-mlops)         |
| **Frontend: Android** | [**Android Team**](https://github.com/orgs/KT-AIVLE-3rd-AI-Team10/teams/android-developer) | [**funibuni-fe-android**](https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-fe-android) | **현위치** |
| Frontend: Web         | [Frontend Team](https://github.com/orgs/KT-AIVLE-3rd-AI-Team10/teams/frontend-developer)   | [funibuni-fe-web](https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-fe-web)             |
| Backend: Web Server   | [Backend Team](https://github.com/orgs/KT-AIVLE-3rd-AI-Team10/teams/backend-developer)     | [funibuni-be-webserver](https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-be-webserver) |

<br>


## Service Flow

<p align="center">
<img src="https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-fe-android/assets/33805423/a21fbb99-1fc7-4f1d-9015-f46caadcf1d3" width="100%"/>
</p>


1. 회원가입/로그인
   - 회원가입(휴대폰 번호 문자 인증, 이름, 주소(카카오 주소 검색 API) 입력)
   - 로그인(휴대폰 번호 문자 인증)
   - JWT 자동 로그인
2. 대형생활폐기물 배출 신청하기
   1. 폐기물 이미지 업로드(기본 카메라로 사진 촬영, 갤러리에서 불러오기)
   2. AI 모델의 이미지 분류 결과 보여주기(대분류 및 소분류 순위, 틀렸을 경우 재선택 팝업 화면)
   3. 분류 선택 완료 후 수수료 확인
   4. 배출 신청 상세정보 입력
3. 나눔게시판
   - 게시글 목록
   - 게시글 생성/수정/삭제 및 나눔 완료
   - 게시글 좋아요
   - 댓글 생성/수정/삭제/신고하기
   - 대댓글 생성/수정/삭제/신고하기
4. 마이페이지(나의 버니)
   - 목록
      - 배출 목록
      - 나눔 목록
      - 관심 목록
      - 활동 내역(댓글을 작성한 게시물 목록)
      - 최근 배출 목록
   - 내 정보 및 앱 설정
      - 내 정보 확인/수정
      - 퍼니버니 홈페이지 연결
      - 대형폐기물 배출 품목 분류표에서 검색하기
      - 무상 수거 홈페이지 연결
      - 개인정보 처리방침 확인
      - 오픈소스 라이선스 확인
      - 로그아웃/회원탈퇴

<br>


## 주요 화면 소개

![퍼니버니_회원가입_로그인](https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-fe-android/assets/33805423/3815fe4a-2933-4107-9536-3bbb289b140d)
![퍼니버니_대형폐기물_배출신청](https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-fe-android/assets/33805423/9baf413f-1d6c-468e-bf03-c19afb2a9358)
![퍼니버니_나눔게시판](https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-fe-android/assets/33805423/8520303e-c041-48bd-b79d-fbe2a7465abd)

<br>


## UI/UX & CustomView

|                                                                     GIF                                                                     | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |                                                                     GIF                                                                     | Description                                                                                                                                                                                                                                                                                                                                    |
|:-------------------------------------------------------------------------------------------------------------------------------------------:|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:-------------------------------------------------------------------------------------------------------------------------------------------:|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| <img width="150" src="https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-fe-android/assets/33805423/570b7035-3fee-4da0-b32b-1a2fc722c63b"/> | 📌 **간편 회원가입** 📌<br/><br/> **1. 휴대폰 번호 인증**<br/> - [Firebase Authentication](presentation/src/main/java/com/aivle/presentation/intro/firebase/FirebasePhoneAuthManager.kt)<br/> - [SMS 인증 코드 자동 입력 (SmsRetriever)](presentation/src/main/java/com/aivle/presentation/intro/firebase/SmsRetrieveHelper.kt) <br/> - [PhoneNumber Input CustomView](presentation-design/src/main/java/com/aivle/presentation_design/interactive/customView/FilterableMaterialAutoCompleteTextView.kt) <br/><br/> **2. 이름 입력**<br/><br/> **3. 주소 입력**<br/> - 카카오 주소 검색 API<br/> - 위치 정보 API (FusedLocationProviderClient) | <img width="150" src="https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-fe-android/assets/33805423/1ce23dfc-3e58-418b-99e0-a1d15890ee6b"/> | 📌 [**한글 초성 검색 알고리즘**](presentation/src/main/java/com/aivle/presentation/util/search/HangulJamoTextMatcher.kt) 📌<br/><br/> - 유니코드 한글 블럭 규칙 활용<br/> - `[(initial) × 588 + (medial) × 28 + (final)] + 44032` [(출처)](https://en.wikipedia.org/wiki/Korean_language_and_computers)<br/> - 초성, 초성+중성, 완성형 검색 구현<br/> - RecyclerView Item Animation |
| <img width="150" src="https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-fe-android/assets/33805423/f43ea238-e52b-4496-ae7a-82c7a5a957a5"/> | 📌 [**다이얼로그 커스텀뷰**](presentation-design/src/main/java/com/aivle/presentation_design/interactive/customView/BottomUpDialog.kt) 📌<br/><br/> - 앱 내 범용적으로 사용되는 다이얼로그<br/> - 2 Versions with `예/아니오` or `확인`<br/> - Overshoot Transition<br/> - Right Side Positive Button (UX)                                                                                                                                                                                                                                                                                                                            | <img width="150" src="https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-fe-android/assets/33805423/15b57a33-1856-4b05-b3ca-d41837aeee77"/> | 📌 **[User Touch Interaction](presentation-design/src/main/java/com/aivle/presentation_design/interactive/ui)** 📌<br/><br/> - 사용자의 터치 액션에 대한 시각적인 반응 효과<br/> - ViewPropertyAnimator (Scale Transition)<br/> - Ripple Effect                                                                                                                   |


<br>


## 앱 시연 영상

[![퍼니버니_시연영상_유튜브썸네일2](https://kt-aivle-loggi-bucket.s3.ap-northeast-2.amazonaws.com/funibuni/github/funibuni_demo_video_thumbnail_for_github.png)](https://youtu.be/N-wBxebZWTY)

<p align="center" style="margin: 0; font-weight: bold;">⬆️ 이미지를 클릭하면 유튜브로 연결됩니다 ⬆️</p>

<br>


## Architecture

![clean_architecture](https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-fe-android/assets/33805423/e4f70f91-28b3-4380-a8a3-da7b62921af7)

<br>


## Skills

| Category       | Skills                                                     |
|----------------|------------------------------------------------------------|
| Architecture   | Multi Module, Clean Architecture, MVVM, Repository Pattern |
| Jetpack(AAC)   | ViewModel, DataBinding, LiveData, Lifecycles, Navigation   |
| DI             | Dagger Hilt                                                |
| Network        | Retrofit2, ApiResponse                                     |
| DB             | Room, Datastore                                            |
| Async          | Coroutines Flow                                            |
| Image          | Glide                                                      |
| Authentication | Firebase                                                   |
| Others         | -                                                          |
