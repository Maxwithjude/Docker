import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useCounterStore = defineStore('counter', () => {
  const user = ref({
    "success": true,
    "message": "some message",
    "results": {
      "userId": 123,
      "email": "user@example.com",
      "nickname": "userNickname",
      "createdAt": "2025-01-01T12:00:00",
      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    }
  })

  const personalCollections = ref(
    {
      "success" : true,
      "message" : "some message",
      "results": [
        {
          "collection_id" : 1,
          "name" : "웹서핑",
          "created_at" : "2024-01-01",
          "updated_at" : "2024-01-02"
        }, 
        {
          "collection_id" : 2,
          "name" : "자바",
          "created_at" : "2024-01-01",
          "updated_at" : "2024-01-02"
        },
        {
          "collection_id" : 3,
          "name" : "파이썬",
          "created_at" : "2024-01-01",
          "updated_at" : "2024-01-02"
        }
      ] 
    }
  );
  

  const personalCollectionsBookmarks = ref(
    {
      "success":true,
      "message":"some message",
      "results": {
        "name" : "웹서핑",
        "bookmarks" : [
          {
              "bookmark_id" : 1,
              "url" : "https://naver.com",
              "img" : "https://naver.com/image.jpg",
              "title" : "네이버 메인 - 검색, 뉴스, 쇼핑",
              "description" : "네이버는 다양한 정보를 ...",
              "priority" : true,
              "created_at" : "2024-01-01",
              "updated_at" : "2024-01-02",
              "tag" : ["서핑", "웹"],
              "isPersonal" : true
          }, 
          {
              "bookmark_id" : 2,
              "url" : "https://edu.ssafy.com",
              "img" : "https://edu.ssafy.com/image.jpg",
              "title" : "싸피 메인 페이지",
              "description" : "대한민국 청년 삼성 ...",
              "priority" : true,
              "created_at" : "2024-01-01",
              "updated_at" : "2024-01-02",
              "tag" : ["싸피", "IT"],
              "isPersonal" : true
          },
        ]
      }
    }
  )

  const sharedCollections = ref(
    {
      "success":true,
      "message":"some message",
      "results": [
        {
          "collection_id" : 1,
          "name" : "일본여행",
          "users" : [
            {
              "user_id" : 1,
              "email" : "example@naver.com",
              "nickname" : "홍길동",
              "profile_img" : "https://example.com/"
            }, 
            {
              "user_id" : 2,
              "email" : "example222@naver.com",
              "nickname" : "저팔계",
              "profile_img" : "https://example.com/"
            },
          ]
        }, 
        {
          "collection_id" : 2,
          "name" : "알고리즘스터디",
          "users" : [
            {
              "user_id" : 1,
              "email" : "example@naver.com",
              "nickname" : "홍길순",
              "profile_img" : "https://example.com/"
            }, 
            {
              "user_id" : 2,
              "email" : "example222@naver.com",
              "nickname" : "저팔갱",
              "profile_img" : "https://example.com/"
            }
          ]
        }
      ]
    }
  )

  const sharedCollectionsBookmarks = ref(
    {
      "success":true,
      "message":"some message",
      "results": {
        "collection_id" : 1,
        "name" : "일본여행",
        "created_at" : "2024-01-01",
        "updated_at" : "2024-01-02",
        "users" : [
          {
            "user_id": 1,
            "email": "example@naver.com",
            "nickname": "사용자1",
            "user_profile_url" : "www.example.com"
          },
          {
            "user_id": 2,
            "email": "example222@naver.com",
            "nickname": "사용자2",
            "user_profile_url" : "www.example.com"
          },
        ],
        "bookmarks" : [
          {   
            "bookmark_id" : 1,
            "url" : "https://naver.com",
            "priority" : true,
            "created_at" : "2024-01-01",
            "updated_at" : "2024-01-02",
            "tag" : ["서핑", "웹"],
            "isPersonal" : false,
            "img" : "https://naver.com/image.jpg",
            "title" : "네이버 메인 - 검색, 뉴스, 쇼핑",
            "description" : "네이버는 다양한 정보를 ...",
          }, 
          {
            "bookmark_id" : 2,
            "url" : "https://edu.ssafy.com",
            "priority" : true,
            "created_at" : "2024-01-01",
            "updated_at" : "2024-01-02",
            "tag" : ["웹", "IT"],
            "isPersonal" : false,
            "img" : "https://edu.ssafy.com/image.jpg",
            "title" : "싸피 메인 페이지",
            "description" : "대한민국 청년 삼성 ...",
          },
        ]
      }
    }
  )

  return { user, personalCollections, personalCollectionsBookmarks, sharedCollections, sharedCollectionsBookmarks }
})


