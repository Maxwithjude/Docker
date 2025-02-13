import { ref } from "vue";
import { defineStore } from "pinia";
import api from "@/utils/api";

export const useCollectionStore = defineStore("collection", () => {
  const personalCollections = ref([]); 
  const sharedCollections = ref([]); 
  const allCollections = ref([]);
  const membersByCollectionId = ref({});
  const top10Tags = ref({});

  // 로그인 후 개인 & 공유 컬렉션 모두 불러오기
  const fetchAllCollection = async () => {
    try {
      const [personalRes, sharedRes] = await Promise.all([
        api.get('/collections/personal'),
        api.get('/collections/shared'),
      ]);

      personalCollections.value = personalRes.data.results || [];
      sharedCollections.value = sharedRes.data.results || [];
      allCollections.value = [...personalCollections.value, ...sharedCollections.value];

      return { personalCollections, sharedCollections, allCollections };
    } catch (error) {
      console.error("컬렉션 로드 중 오류 발생:", error);
      throw error;
    }
  };

  // 공유 컬렉션 id로 멤버 조회
  const getMembersByCollectionId = async (collectionId) => {
    try {
      const response = await api.get(`/collections/shared/${collectionId}/users`);
      membersByCollectionId.value = response.data;
    } catch (error) {
      console.error('공유컬렉션 멤버 조회 중 오류 발생:', error);
      throw error;
    }
  };

  // 개인 컬렉션 생성
  const createPersonalCollection = async (name) => {
    try {
      const request = {
        "name": name
      } 
      const response = await api.post('/collections/personal', request)
      console.log('컬렉션 생성 성공:')
    } catch (error) {
      console.error('개인 컬렉션 생성 중 오류 발생:', error);
      throw error;
    }
  };

  // 공유 컬렉션 생성
  const createSharedCollection = async (name) => {
    try {
      const request = {
        "name": name
      } 
      const response = await api.post('/collections/shared', request)
      console.log('컬렉션 생성 성공:')

    } catch (error) {
      console.error('공유 컬렉션 생성 중 오류 발생:', error);
      throw error;
    }
  };

  // 개인컬렉션 이름 변경
  const updatePersonalCollectionName = async (collectionId, newName) => {
    try {
      const request = {
        "name": newName
      }
      const response = await api.put(`/collections/personal/${collectionId}`, request)
      console.log('컬렉션 이름 변경 성공:')

      // 변경 후 컬렉션 목록 갱신
      await fetchAllCollection();

    } catch (error) {
      console.error('컬렉션 이름 변경 중 오류 발생:', error);
      throw error;
    }
  };

  // 공유컬렉션 이름 변경
  const updateSharedCollectionName = async (collectionId, newName) => {
    try {
      const request = {
        "name": newName
      }
      const response = await api.put(`/collections/shared/${collectionId}`, request)
      console.log('컬렉션 이름 변경 성공:')

      // 변경 후 컬렉션 목록 갱신
      await fetchAllCollection();

    } catch (error) {
      console.error('컬렉션 이름 변경 중 오류 발생:', error);
      throw error;
    }
  };

  // 개인컬렉션 삭제
  const deletePersonalCollection = async (collectionId) => {
    try {
        console.log("삭제할 컬렉션 ID:", collectionId); // 디버깅용 로그 추가
        if (!collectionId) {
            throw new Error("컬렉션 ID가 유효하지 않습니다.");
        }
        const response = await api.delete(`/collections/personal/${collectionId}`);
        console.log('개인컬렉션 삭제 성공:');
        
        // 삭제 후 컬렉션 목록 갱신
        await fetchAllCollection();
    } catch (error) {
        console.error('개인컬렉션 삭제 중 오류 발생:', error);
        throw error;
    }
  };
  //공유컬렉션 삭제
  const deleteSharedCollection = async (collectionId) => {
    try { 
      if (!collectionId) {
        throw new Error("컬렉션 ID가 유효하지 않습니다.");
      }
      
      const response = await api.delete(`/collections/shared/${collectionId}`)
      console.log('공유컬렉션 삭제 성공:')

      // 삭제 후 컬렉션 목록 갱신
      await fetchAllCollection();
    } catch (error) {
      console.error('공유컬렉션 삭제 중 오류 발생:', error);
      throw error;
    }
  };

  // 공유 컬렉션 인원 초대
  const addMemberToSharedCollection = async (collectionId, memberEmail) => {
    try {
      const response = await api.post(`/collections/shared/${collectionId}/invite`, {
        email: memberEmail
      });
      return response.data;
    } catch (error) {
      console.error('공유컬렉션 인원 추가 중 오류 발생:', error);
      throw error;
    }
  };

  // 공유 컬렉션 인원 강퇴
  const removeMemberFromSharedCollection = async (collectionId, userId) => {
    try {
      const response = await api.delete(`/collections/shared/${collectionId}/members/${userId}`);
      return response.data;
    } catch (error) {
      console.error('공유컬렉션 인원 강퇴 중 오류 발생:', error);
      throw error;
    }
  };

  // 사용자들의 상위 10개 태그 조회
  const getTop10Tags = async () => {
    try {
      const response = await api.get('/tags/recommend');
      top10Tags.value = response.data;
      return response.data;
    } catch (error) {
      console.error('사용자들의 상위 10개 태그 조회 중 오류 발생:', error);
      throw error;
    }
  };

  // 사용자들의 관심 태그 추가
  const addTag = async (tagList) => {
    try {
      const response = await api.post('/tags', { tagList });
      return response.data;
    } catch (error) {
      console.error('태그 추가 중 오류 발생:', error);
      throw error;
    }
  };

  return {
    personalCollections,
    sharedCollections,
    allCollections,
    membersByCollectionId,
    top10Tags,
    fetchAllCollection,
    getMembersByCollectionId,
    createPersonalCollection,
    createSharedCollection,
    updatePersonalCollectionName,
    updateSharedCollectionName,
    deletePersonalCollection,
    deleteSharedCollection,
    addMemberToSharedCollection,
    removeMemberFromSharedCollection,
    getTop10Tags,
    addTag
  };
});



