# FuniBuni Android App

![퍼니버니_소개화면](https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-fe-android/assets/33805423/1ccbdc7b-41cb-426d-9867-702cc55320ef)

AI 기반 대형생활폐기물 간편 배출 서비스 안드로이드 앱 **FuniBuni** 입니다.

- 담당자: 조성록

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

## 주요 화면 소개

![퍼니버니_회원가입_로그인](https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-fe-android/assets/33805423/3815fe4a-2933-4107-9536-3bbb289b140d)
![퍼니버니_대형폐기물_배출신청](https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-fe-android/assets/33805423/9baf413f-1d6c-468e-bf03-c19afb2a9358)
![퍼니버니_나눔게시판](https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-fe-android/assets/33805423/8520303e-c041-48bd-b79d-fbe2a7465abd)

## 앱 시연 영상

[![퍼니버니_시연영상_유튜브썸네일2](https://kt-aivle-loggi-bucket.s3.ap-northeast-2.amazonaws.com/funibuni/github/funibuni_demo_video_thumbnail_for_github.png)](https://youtu.be/N-wBxebZWTY)

<p align="center" style="margin: 0">⬆️⬆️ 이미지를 클릭하면 유튜브로 연결됩니다 ⬆️⬆️</p>

## UI/UX

## Architecture

![clean_architecture](https://github.com/KT-AIVLE-3rd-AI-Team10/funibuni-fe-android/assets/33805423/e4f70f91-28b3-4380-a8a3-da7b62921af7)

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
