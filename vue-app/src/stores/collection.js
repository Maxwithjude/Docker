import { ref, computed } from "vue";
import { defineStore } from "pinia";
import axios from "axios";
import router from "@/router";

const REST_API_URL_coll = `http://localhost:8080/api/collections`;


export const useCollectionStore = defineStore("collection", () => {
  const personalCollections = ref([]); // 개인 컬렉션
  const sharedCollections = ref([]); // 공유 컬렉션
  const allCollections = ref([]); // 전체 컬렉션 저장 (필요할 경우)

  // 로그인 후 개인 & 공유 컬렉션 모두 불러오기
  const fetchAllCollection = async () => {
    try {
      console.log("✅ 로그인 성공! 저장된 토큰:", sessionStorage.getItem("accessToken"));
      console.log("✅ 저장된 리프레시토큰:", sessionStorage.getItem("refreshToken"));
      
      const token = sessionStorage.getItem("accessToken");

      if (!token) {
        throw new Error("인증 정보가 없습니다. 로그인 후 다시 시도해주세요.");
      }

      // baseURL은 이미 설정되어 있으므로 경로만 지정
      const [personalRes, sharedRes] = await Promise.all([
        api.get('/collections/personal'),  // REST_API_URL 제거
        api.get('/collections/shared'),    // REST_API_URL 제거
      ]);

      if (personalRes.data.success && sharedRes.data.success) {
        personalCollections.value = personalRes.data.results;
        sharedCollections.value = sharedRes.data.results;
        allCollections.value = [...personalRes.data.results, ...sharedRes.data.results]; // 합치기
      } else {
        console.error("컬렉션 로드 실패:", personalRes.data.message, sharedRes.data.message);
      }
    } catch (error) {
      console.error("컬렉션 로드 중 오류 발생:", error);
      throw error;
    }
  };

  const exampleAllCollections = ref([
    { "collection_id": 1, "name": "개발", "isPersonal": true },
    { "collection_id": 2, "name": "자바", "isPersonal": true },
    { "collection_id": 3, "name": "웹서핑", "isPersonal": true },
    { "collection_id": 4, "name": "일본여행", "isPersonal": false },
    { "collection_id": 5, "name": "알고리즘스터디", "isPersonal": false },
    { "collection_id": 6, "name": "영어공부", "isPersonal": false }
  ]);
  ///////////////////////////////////////////////////////////////////////////////////////////////
  //개인 컬렉션 예시 response
  const examplePersonalCollections = ref(
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
  ///////////////////////////////////////////////////////////////////////////////////////////////
  //공유 컬렉션 예시 response
  const exampleSharedCollections = ref(
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
  //공유 컬렉션 id로 멤버 조회 response
  const membersByCollectionId = ref({})
  //공유 컬렉션 id로 멤버 조회
  const getMembersByCollectionId = async (collectionId) => {
    try {
      const response = await api.get(`/collections/shared/${collectionId}/users`)
      membersByCollectionId.value = response.data
    } catch (error) {
      console.error('공유컬렉션 멤버 조회 중 오류 발생:', error)
      throw error;
    }
  };
  // 공유 컬렉션 id로 멤버 조회 예시 response
  const exampleMembersByCollectionId = ref(
    {
      "success" : true,
      "message" : "공유컬렉션의 사용자를 조회합니다. ",
      "results" : [
        {
          "user_id" : 1,
          "email" : "example11@naver.com",
          "nickname" : "사용자1"
        }, 
        {
          "user_id" : 2,
          "email" : "ex222@google.com",
          "nickname" : "사용자2"
        }, 
      ]
    }
  )
  ///////////////////////////////////////////////////////////////////////////////////////////////
  // 개인 컬렉션 생성
  const createPersonalCollection = async (name) => {
    try {
      const request = {
        name: name
      } 
      const response = await api.post('/collections/personal', request)
      console.log('컬렉션 생성 성공:')
    } catch (error) {
      console.error('개인 컬렉션 생성 중 오류 발생:', error)
      throw error
    }
  }

  // 공유 컬렉션 생성
  const createSharedCollection = async (name) => {
    try {
      const request = {
        "sharedCollectionName": name
      } 
      const response = await api.post('/collections/shared', request)
      console.log('컬렉션 생성 성공:')

    } catch (error) {
      console.error('컬렉션 생성 중 오류 발생:', error)
      throw error
    }
  }
  // 개인컬렉션 이름 변경
  const updatePersonalCollectionName = async (collectionId, newName) => {
    try {
      const request = {
        "newName": newName
      }
      const response = await api.put(`/collections/personal/${collectionId}`, request)
      console.log('컬렉션 이름 변경 성공:')

    } catch (error) {
      console.error('컬렉션 이름 변경 중 오류 발생:', error)
      throw error
    }
  }
  // 공유컬렉션 이름 변경
  const updateSharedCollectionName = async (collectionId, newName) => {
    try {
      const request = {
        "newName": newName
      }
      const response = await api.put(`/collections/shared/${collectionId}`, request)
      console.log('컬렉션 이름 변경 성공:')

    } catch (error) {
      console.error('컬렉션 이름 변경 중 오류 발생:', error)
      throw error
    }
  }
  //개인컬렉션 삭제
  const deletePersonalCollection = async (collectionId) => {
    try {
      const response = await api.delete(`/collections/personal/${collectionId}`)
      console.log('개인컬렉션 삭제 성공:')
    } catch (error) {
      console.error('개인컬렉션 삭제 중 오류 발생:', error)
      throw error
    }

  }
  //공유컬렉션 삭제
  const deleteSharedCollection = async (collectionId) => {
    try { 
      const response = await api.delete(`/collections/shared/${collectionId}`)
      console.log('공유컬렉션 삭제 성공:')
    } catch (error) {
      console.error('공유컬렉션 삭제 중 오류 발생:', error)
      throw error
    }

  } 
  // 공유 컬렉션 인원 초대 요청 
  const addMemberToSharedCollection = async (collectionId, memberEmail) => {
    try {
      const request = {
        "email": memberEmail
      }
      const response = await api.post(`/collections/shared/${collectionId}/invite`, request)
      console.log('공유컬렉션 인원 추가 성공:')

    } catch (error) {
      console.error('공유컬렉션 인원 추가 중 오류 발생:', error)
      throw error
    }
  }

  //공유 컬렉션 인원 강퇴
  const removeMemberFromSharedCollection = async (collectionId, userId) => {
    try {
      const response = await api.delete(`/collections/shared/${collectionId}/members/${userId}`)
      console.log('공유컬렉션 인원 강퇴 성공:')
    } catch (error) {
      console.error('공유컬렉션 인원 강퇴 중 오류 발생:', error)
      throw error
    }
  }



    return{
        personalCollections,
        sharedCollections,
        allCollections,
        fetchAllCollection,
        exampleAllCollections,
        examplePersonalCollections,
        exampleSharedCollections,
        createPersonalCollection,
        createSharedCollection,
        updatePersonalCollectionName,
        updateSharedCollectionName,
        deletePersonalCollection,
        deleteSharedCollection,
        addMemberToSharedCollection,
        removeMemberFromSharedCollection,
        getMembersByCollectionId,
        exampleMembersByCollectionId,
        membersByCollectionId
    }
});



