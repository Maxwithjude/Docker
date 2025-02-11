import { defineStore } from "pinia";
import api from "@/utils/api";
import { ref } from "vue";

//북마크에 관한 함수 store
export const useBookmarkStore = defineStore("bookmark", () => {
    //북마크 모달들에 대한 함수 store

    //북마크 생성(저장)에 대한 함수(미완성)
    // const saveBookmark = async (bookmarkUrl, collectionId, isPersonal, tags) => {
    //     const request = {
    //         bookmark_url: bookmarkUrl,
    //         collectionId: collectionId,
    //         isPersonal: isPersonal,
    //         tags: tags
    //     };
    //     const response = await api.post("/bookmarks", request);
    //     console.log(response.data);
    // };

    //북마크 중요도 수정 함수
    const changePriority = async (bookmarkId, priority) => {
        try {
            const response = await api.put(`/api/bookmarks/${bookmarkId}/priority`, {
                priority: priority
            });
            
            if (response.data.success) {
                console.log('북마크 중요도 변경 완료');
                return true;
            } else {
                console.error('북마크 중요도 변경 실패:', response.data.message);
                return false;
            }
        } catch (error) {
            console.error("북마크 중요도 변경 중 오류 발생:", error);
            throw error;
        }
    };


    //북마크 이동 및 복사 함수
    const moveToOtherCollection = async (bookmarkId, isPersonal, targetCollectionId) => {
        const request = {
            targetCollectionId: targetCollectionId,
            isPersonal: isPersonal
        };
        const response = await api.post(`/api/bookmarks/${bookmarkId}/move`, request);
        console.log(response.data);
    };

    //북마크 삭제 함수
    const deleteBookmark = async (bookmarkId) => {
        const response = await api.delete(`/api/bookmarks/${bookmarkId}`);
        console.log(response.data);
    };

    //북마크 태그 관리 함수
    const manageTags = async (bookmarkId, tag) => {
        const request = {
            tags: tag
        };
        const response = await api.put(`/api/bookmarks/${bookmarkId}/tags`, request);
        console.log(response.data);
    };

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //북마크 상세 페이지에 사용할 함수
    //북마크 메모목록 조회
    const getMemo = async (bookmarkId) => {
        const response = await api.get(`/api/bookmarks/${bookmarkId}/memos`);
        console.log(response.data);
    };  

    //북마크 메모 생성
    const createMemo = async (bookmarkId, memo) => {
        const request = {
            content: memo
        };
        const response = await api.post(`/api/bookmarks/${bookmarkId}/memos`, request);
        console.log(response.data);
    };

    //북마크 메모 수정
    const updateMemo = async (bookmarkId, memoId, memo) => {
        const request = {
            content: memo
        };
        const response = await api.put(`/api/bookmarks/${bookmarkId}/memos/${memoId}`, request);
        console.log(response.data);
    };
    
    //북마크 메모 삭제
    const deleteMemo = async (bookmarkId, memoId) => {
        const response = await api.delete(`/api/bookmarks/${bookmarkId}/memos/${memoId}`);
        console.log(response.data);
    };
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //중요 북마크 실제 response
    const importantBookmarks = ref({});


    //중요 북마크 불러오는 함수
    const getImportantBookmarks = async () => {
        const response = await api.get('/api/collections/personal/priority');
        importantBookmarks.value = response.data;
        console.log(response.data);
    };

    //중요 북마크 예시 response
    const exampleImportantBookmarks = ref(
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
      ///////////////////////////////////////////////////////////////////////////////////////////////
      //사용자 정의 태그 실제 response
      const userDefineTags = ref({});



      //사용자 정의 태그 조회
      const getUserDefineTags = async () => {
        try {
            const response = await api.get('/api/tags/recommend');

            userDefineTags.value = response.data;
            return userDefineTags.value;

        } catch (error) {
            console.error('태그 로딩 실패:', error);
            userDefineTags.value = userDefineTags.value;
            return userDefineTags.value;


        }
      };

      //사용자 정의 태그 예시 response
        const exampleUserDefineTags = ref({
            "success":true,
            "message":"some message",
            "results": {

                "tagList": [
                    "서핑",
                    "웹",
                    "싸피",
                    "IT",
                    "프로그래밍",
                    "교육",
                    "Python",
                    "FastAPI",
                    "Spring",
                    "Java"
                ]
            }
        })
      
      //추천 태그 기반 북마크 실제 response
      const recommendedBookmarks = ref({});

      //추천 태그 기반 북마크 불러오는 함수
      const getRecommendedBookmarks = async (cursorId, size, tagName) => {
        try {
            const response = await api.get(`/api/tags/search?cursorId=${cursorId}&size=${size}&tag=${tagName}`);
            recommendedBookmarks.value = response.data;
            return recommendedBookmarks.value;
        } catch (error) {
            console.error('북마크 로딩 실패:', error);
            return recommendedBookmarks.value;

        }
      };


      //추천 북마크 예시 response
      const exampleRecommendedBookmarks = ref({
        "success":true,
        "message":"some message",
        "result": {

          "recommendedUrlList": [
            {
              "url": "https://example.com/some-page-1",
              "title": "추천 페이지 제목 1",
              "description": "추천 페이지 설명 1",
              "img": "https://example.com/image1.jpg"
            },
            {
              "url": "https://example.com/some-page-2",
              "title": "추천 페이지 제목 2",
              "description": "추천 페이지 설명 2",
              "img": "https://example.com/image2.jpg"
            }
          ]
        }
      })
      ///////////////////////////////////////////////////////////////////////////////////////////////
      //개인 컬렉션 별 북마크 실제 response
      const personalCollectionBookmarks = ref({});

      //개인 컬렉션 별 북마크 불러오는 함수
      const getPersonalCollectionBookmarks = async (personalCollectionId) => {
        const response = await api.get(`/api/collections/personal/${personalCollectionId}`);
        personalCollectionBookmarks.value = response.data;
        console.log(response.data);
      };


      //개인 컬렉션 별 북마크 예시 response
      const examplePersonalCollectionBookmarks = ref(
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
      ///////////////////////////////////////////////////////////////////////////////////////////////
      //공유 컬렉션 별 북마크 실제 response
      const sharedCollectionBookmarks = ref({});

      //공유 컬렉션 별 북마크 불러오는 함수
      const getSharedCollectionBookmarks = async (sharedCollectionId) => {  
        const response = await api.get(`/api/collections/shared/${sharedCollectionId}`);
        sharedCollectionBookmarks.value = response.data;
        console.log(response.data);
      };

      //공유 컬렉션 별 북마크 예시 response
      const exampleSharedCollectionBookmarks = ref(
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
      ///////////////////////////////////////////////////////////////////////////////////////////////
      //태그 기반 검색 실제 response
      const searchBookmarksByTag = ref({});

      //태그 기반 검색 불러오는 함수
      const getSearchBookmarksByTag = async (tagName, cursorId = 1, size = 10) => {
        const response = await api.get(`/api/tags/my-data/search?cursorId=${cursorId}&size=${size}&tag=${tagName}`);
        searchBookmarksByTag.value = response.data;
        console.log(response.data);
      };


      //태그 기반 검색 예시 response
      const exampleSearchedBookmarksByTag = ref({
        "success": true,
        "message": "태그 기반 검색 성공",
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
                    "img": "https://example.com/image1.jpg"
                },
                {
                    "url": "https://example.com/some-page-2",
                    "title": "제목 2",
                    "description": "설명 2",
                    "img": "https://example.com/image2.jpg"
                },
                {
                    "url": "https://example.com/some-page-3",
                    "title": "제목 3",
                    "description": "설명 3",
                    "img": "https://example.com/image3.jpg"
                },
                {
                    "url": "https://example.com/some-page-4",
                    "title": "제목 4",
                    "description": "설명 4",
                    "img": "https://example.com/image4.jpg"
                },
                {
                    "url": "https://example.com/some-page-5",
                    "title": "제목 5",
                    "description": "설명 5",
                    "img": "https://example.com/image5.jpg"
                },
                {
                    "url": "https://example.com/some-page-6",
                    "title": "제목 6",
                    "description": "설명 6",
                    "img": "https://example.com/image6.jpg"
                }
            ]
        }
    });
    ///////////////////////////////////////////////////////////////////////////////////////////////
    //오래된 북마크 실제 response
    const oldBookmarks = ref({});

    //오래된 북마크 불러오는 함수
    const getOldBookmarks = async () => {
        const response = await api.get('/api/collections/personal/long-unread');
        oldBookmarks.value = response.data;
        console.log(response.data);
    };  

    //오래된 북마크 예시 response
    const exampleOldBookmarks = ref(
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
    

    return {
        changePriority,
        moveToOtherCollection,
        deleteBookmark,
        manageTags,
        getMemo,
        createMemo,
        updateMemo,
        deleteMemo,
        getImportantBookmarks,
        getUserDefineTags,
        getRecommendedBookmarks,
        getPersonalCollectionBookmarks,
        getSharedCollectionBookmarks,
        getSearchBookmarksByTag,
        getOldBookmarks,
        exampleImportantBookmarks,
        importantBookmarks,
        exampleUserDefineTags,
        userDefineTags,
        exampleRecommendedBookmarks,
        recommendedBookmarks,
        examplePersonalCollectionBookmarks,
        personalCollectionBookmarks,
        exampleSharedCollectionBookmarks,
        sharedCollectionBookmarks,
        exampleSearchedBookmarksByTag,
        searchBookmarksByTag,
        exampleOldBookmarks,
        oldBookmarks
    };
});