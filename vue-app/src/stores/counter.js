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
          "name" : "개발",
          "isPersonal" : true
        }, 
        {
          "collection_id" : 2,
          "name" : "자바",
          "isPersonal" : true
        },
        {
          "collection_id" : 3,
          "name" : "웹서핑",
          "isPersonal" : true
        }
      ] 
    }
  );

  
  const personalCollectionsBookmarks = ref(
    {
      "success":true,
      "message":"some message",
      "results": {
        "name" : "개발",
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
          "isPersonal" : false
        }, 
        {
          "collection_id" : 2,
          "name" : "알고리즘스터디",
          "isPersonal" : false
        }, 
        {
          "collection_id" : 3,
          "name" : "영어공부",
          "isPersonal" : false
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
  const importantBookmarks = ref(
    {
      "success":true,
      "message":"some message",
      "results": [
        {
          "bookmark_id" : 1,
          "url" : "https://naver.com",
          "img" : "https://edu.ssafy.com/image.jpg",
          "title" : "네이버 메인 페이지",
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
          "isPersonal" : false
        }
      ]
    }
  );
  
  const oldBookmarks = ref(
    {
      "success":true,
      "message":"some message",
      "results": [
        {
          "bookmark_id" : 1,
          "url" : "https://naver.com",
          "img" : "https://edu.ssafy.com/image.jpg",
          "title" : "네이버 메인 페이지",
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
          "isPersonal" : false
        }
      ]
    }
  );

  const searchedBookmarks = ref(
    {
      "success":true,
      "message":"some message",
      "result": {
        "userBookmarkList": [
          {
            "bookmark_id": 1,
            "url": "https://naver.com",
            "img": "https://naver.com/image.jpg",
            "title": "네이버 메인 - 검색, 뉴스, 쇼핑",
            "description": "네이버는 다양한 정보를 ...",
            "priority": true,
            "created_at": "2024-01-01",
            "updated_at": "2024-01-02",
            "tag": ["서핑", "웹"],
            "isPersonal": true
          },
          {
            "bookmark_id": 2,
            "url": "https://edu.ssafy.com",
            "img": "https://edu.ssafy.com/image.jpg",
            "title": "싸피 메인 페이지",
            "description": "대한민국 청년 삼성 ...",
            "priority": true,
            "created_at": "2024-01-01",
            "updated_at": "2024-01-02",
            "tag": ["싸피", "IT"],
            "isPersonal": true
          },
          {
            "bookmark_id": 3,
            "url": "https://example.com/some-page-3",
            "img": "https://example.com/image3.jpg",
            "title": "제목 3",
            "description": "설명 3",
            "priority": true,
            "created_at": "2024-01-01",
            "updated_at": "2024-01-02",
            "tag": ["싸피", "IT"],
            "isPersonal": true
          },
          {
            "bookmark_id": 4,
            "url": "https://example.com/some-page-4",
            "img": "https://example.com/image4.jpg",
            "title": "제목 4",
            "description": "설명 4",
            "priority": true,
            "created_at": "2024-01-01",
            "updated_at": "2024-01-02",
            "tag": ["싸피", "IT"],
            "isPersonal": true
          },
          {
            "bookmark_id": 5,
            "url": "https://example.com/some-page-5",
            "img": "https://example.com/image5.jpg",
            "title": "제목 5",
            "description": "설명 5",
            "priority": true,
            "created_at": "2024-01-01",
            "updated_at": "2024-01-02",
            "tag": ["싸피", "IT"],
            "isPersonal": true
          },
          {
            "bookmark_id": 6,
            "url": "https://example.com/some-page-6",
            "img": "https://example.com/image6.jpg",
            "title": "제목 6",
            "description": "설명 6",
            "priority": true,
            "created_at": "2024-01-01",
            "updated_at": "2024-01-02",
            "tag": ["싸피", "IT"],
            "isPersonal": true
          }
        ],
        "recommendedBookmarkList": [
          {
            "url": "https://example.com/some-page-1",
            "title": "제목 1",
            "description": "설명 1",
            "image": "https://example.com/image1.jpg"
          },
          {
            "url": "https://example.com/some-page-2",
            "title": "제목 2",
            "description": "설명 2",
            "image": "https://example.com/image2.jpg"
          },
          {
            "url": "https://example.com/some-page-3",
            "title": "제목 3",
            "description": "설명 3",
            "image": "https://example.com/image3.jpg"
          },
          {
            "url": "https://example.com/some-page-4",
            "title": "제목 4",
            "description": "설명 4",
            "image": "https://example.com/image4.jpg"
          },
          {
            "url": "https://example.com/some-page-5",
            "title": "제목 5",
            "description": "설명 5",
            "image": "https://example.com/image5.jpg"
          },
          {
            "url": "https://example.com/some-page-6",
            "title": "제목 6",
            "description": "설명 6",
            "image": "https://example.com/image6.jpg"
          }
        ]
      }
    }
  )
//검색페이지에서 사용할 검색 함수
  const searchBookmarksByTag = async (tag, cursorId = null, size = 6) => {
    try {
      // 임의의 검색결과를 searchedBookmarks 라 하자.
      return searchedBookmarks.value;
      
      // 백엔드 준비되면 아래 코드로 교체
      // const response = await fetch(
      //   `/api/tags/my-data/search?cursorId=${cursorId}&size=${size}&tag=${tag}`
      // );
      // const data = await response.json();
      // return data;
    } catch (error) {
      console.error('북마크 검색 중 오류 발생:', error);
      throw error;
    }
  }

  const rssSubscriptions = ref({
    "success":true,
    "message":"some message",
    "results": [
      {
        "rss_id": 1,
        "name": "TED",
        "is_read": false
      },
      {
        "rss_id": 2,
        "name": "연합뉴스TV",
        "is_read": false
      },
      {
        "rss_id": 3,
        "name": "BBC",
        "is_read": false
      }
    ]
  })

  const rssSubscriptionsItems = ref(
    {
      "success":true,
      "message":"some message",
      "results": {
        "rss_id": 1,
        "name": "TED",
        "latest_posts": [
          {
            "title": "The Power of Innovation",
            "url": "https://example.com/articles/power-of-innovation",
            "is_read": false
          },
          {
            "title": "Future of Education",
            "url": "https://example.com/articles/future-education",
            "is_read": false	     
          }, 
        ],
        "totalPages": 5,
        "currentPage": 0,
        "hasNext": true
      }
    }
  )

  const getRssSubscriptionItems = async (rssId) => {
    try {
      // 임시로 하드코딩된 데이터 반환
      return rssSubscriptionsItems.value;
      
      // 백엔드 준비되면 아래 코드로 교체
      // const response = await fetch(`/api/rss/subscriptions/${rssId}`);
      // if (!response.ok) {
      //   throw new Error('RSS 데이터를 가져오는데 실패했습니다.');
      // }
      // const data = await response.json();
      // return data;
    } catch (error) {
      console.error('RSS 구독 정보 조회 중 오류 발생:', error);
      throw error;
    }
  }

  return { 
    user, 
    personalCollections, 
    personalCollectionsBookmarks, 
    sharedCollections, 
    sharedCollectionsBookmarks, 
    importantBookmarks, 
    oldBookmarks, 
    searchedBookmarks,
    searchBookmarksByTag,
    rssSubscriptions,
    rssSubscriptionsItems,
    getRssSubscriptionItems,
  }
})


