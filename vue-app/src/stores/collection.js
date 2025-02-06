import { ref, computed } from "vue";
import { defineStore } from "pinia";
import axios from "axios";
import router from "@/router";

const REST_API_URL = `http://localhost:8080/api/collections`;


export const useCollectionStore = defineStore("collection", () => {
    const personalCollections = ref([]); // 개인 컬렉션
    const sharedCollections = ref([]); // 공유 컬렉션
    const allCollections = ref([]); // 전체 컬렉션 저장 (필요할 경우)

    //로그인 후 개인 & 공유 컬렉션 모두 불러오기
    const fetchAllCollection = async () => {
        try {
            const token = sessionStorage.getItem("access-token");
            const userId = sessionStorage.getItem("userId");

            if (!token || !userId) {
                throw new Error("인증 정보가 없습니다. 로그인 후 다시 시도해주세요.");
              }
        
              const headers = {
                Authorization: `Bearer ${token}`,
                userId: userId,
              };
              // 두 개의 API 요청을 병렬로 처리
      const [personalRes, sharedRes] = await Promise.all([
        axios.get(`${REST_API_URL}/personal`, { headers }),
        axios.get(`${REST_API_URL}/shared`, { headers }),
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
    }
  };

  const exampleAllCollections = ref([
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
    },
    {
      "collection_id" : 4,
      "name" : "일본여행",
      "isPersonal" : false
    }, 
    {
      "collection_id" : 5,
      "name" : "알고리즘스터디",
      "isPersonal" : false
    },
    {
      "collection_id" : 6,
      "name" : "영어공부",
      "isPersonal" : false
    }
  ]);

    

//     //개인 컬렉션 불러오기
//     const getPersonalCollection = async (params) => {
//         try {
//             const res = await axios.get(`${REST_API_URL}/personal`)
//         } catch (error) {
            
//         }
//     }

//     //공유컬렉션
//     const getSharedCollection = async (id) => {
//         try {
//             const res = await axios.get(`${REST_API_URL}/shared`), params: { id: id }
//         } catch (error) {
            
//         }
//     }
    return{
        personalCollections,
        sharedCollections,
        allCollections,
        fetchAllCollection,
    //     getPersonalCollection,
    //     getSharedCollection,
        exampleAllCollections,
    }
});
    

