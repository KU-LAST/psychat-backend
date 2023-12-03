# psychat-backend

정신건강 케어 앱 서비스 PsyChat

내 감정 기록을 한눈에, 필요할 때 간편하게, 내 상황에 알맞게

## Main Features

|이전 채팅 목록 화면|챗봇 채팅 화면|감정 판단 결과 화면|
|:-----:|:-----:|:-----:|
|<img width="240" src="https://github.com/KU-LAST/psychat-android/assets/51016231/44c32ebe-05bf-4a5f-8d06-5ed8b1a7706a">|<img width="240" src="https://github.com/KU-LAST/psychat-android/assets/51016231/6233b39d-3779-416e-a301-905e80201950">|<img width="240" src="https://github.com/KU-LAST/psychat-android/assets/51016231/b7bc7040-319f-4986-8ee6-ba57d062a193">|

## System Architecture
### Back-End 

<img width="856" alt="image" src="https://github.com/KU-LAST/psychat-backend/assets/86451540/f1b2f1cc-6917-48d0-ae7e-9dd460bd9d41">

## AI Model 
### Kobert, KoGPT2 모델 학습 과정
![image](https://github.com/KU-LAST/psychat-backend/assets/86451540/fca26669-ae97-424b-a07a-9fee81fcfe54)

- 학습 데이터를 사용자의 발화, 모델의 응답, 감정 카테고리로 분류하여 전처리
- Kobert, KoGPT2 학습을 위해 각각 Tokenizing, Masking 작업을 실시해 학습 진행

### 채팅 시 감정 분석 및 챗봇 응답 과정
![image](https://github.com/KU-LAST/psychat-backend/assets/86451540/35b8ec60-725a-4901-92aa-59b314e6bab2)

- Kobert를 통해 감정 분류 후 KoGPT2의 감정에 따른 적절한 답변 생성

## Challenges
### 챗봇의 응답 속도 개선

#### Issue
![image](https://github.com/KU-LAST/psychat-backend/assets/86451540/a337faf6-61ae-4800-bf32-2caa99878353)

Solution 1 ) 감정 분석 요청의 비동기 처리
![image](https://github.com/KU-LAST/psychat-backend/assets/86451540/7fc785e3-1728-4c05-a5ac-ac208dbaaa64)

- 감정은 사용자가 최종적인 결과를 볼 때 필요한 정보로 비동기적으로 처리
- **평균적으로 25초**의 응답 속도로 개선
  <img width="958" alt="image" src="https://github.com/KU-LAST/psychat-backend/assets/86451540/e56d53db-2b1c-41f8-8cda-bcc977ac015b">

Solution 2 ) 감정 분류 모델 개선
![image](https://github.com/KU-LAST/psychat-backend/assets/86451540/4a371726-b037-42af-af9e-98726ea2c446)
- 감정의 대분류 60개 -> 6개 축소 (분노, 기쁨, 불안, 당황, 슬픔, 상처)
- 감정의 소분류 359개 -> 58개 축소 
- 감정 분석 모델의 처리 속도 향상 및 결과의 신뢰도를 위한 변경
- 1~2 문장의 채팅에 대해서는 **5초 이내**의 응답 가능

## Development
- Java 11
- Spring / Data JPA
