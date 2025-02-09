import { ref, computed } from "vue";
import { defineStore } from "pinia";
import api from "@/utils/api";  // 인터셉터가 적용된 axios 인스턴스
import { useErrorStore } from "@/stores/error";
const REST_API_URL = import.meta.env.VITE_API_BASE_URL;

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
        api.get('/api/collections/personal'),  // REST_API_URL 제거
        api.get('/api/collections/shared'),    // REST_API_URL 제거
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

  return {
    personalCollections,
    sharedCollections,
    allCollections,
    fetchAllCollection,
    exampleAllCollections
  };
});
