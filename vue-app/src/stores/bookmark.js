import { defineStore } from "pinia";
import api from "@/utils/api";

//북마크에 관한 함수 store
export const useBookmarkStore = defineStore("bookmark", () => {
    
    //북마크 모달들에 대한 함수 store
    //북마크 중요도 수정 함수
    const changePiority = async (bookmarkId, currentPiority) => {
        try {
          const response = await api.put(`/bookmarks/${bookmarkId}/priority`, currentPiority); // 사용자 생성 API 엔드포인트
          console.log(response.data); // 응답 데이터 처리
        } catch (error) {
          console.error("사용자 생성 중 오류 발생:", error);
        }
    };

    //북마크 이동 및 복사 함수
    const moveToOtherCollection = async (bookmarkId, isPersonal, targetCollectionId) => {
        const request = {
            targetCollectionId: targetCollectionId,
            isPersonal: isPersonal
        };
        const response = await api.post(`/bookmarks/${bookmarkId}/move`, request);
        console.log(response.data);
    };

    //북마크 삭제 함수
    const deleteBookmark = async (bookmarkId) => {
        const response = await api.delete(`/bookmarks/${bookmarkId}`);
        console.log(response.data);
    };

    //북마크 태그 관리 함수
    const manageTags = async (bookmarkId, tag) => {
        const request = {
            tags: tag
        };
        const response = await api.put(`/bookmarks/${bookmarkId}/tags`, request);
        console.log(response.data);
    };

    ///////////////////////////////////////////////////////////////////////////////////////////////
    //북마크 상세 페이지에 사용할 함수
    //북마크 메모목록 조회
    const getMemo = async (bookmarkId) => {
        const response = await api.get(`/bookmarks/${bookmarkId}/memos`);
        console.log(response.data);
    };  

    //북마크 메모 생성
    const createMemo = async (bookmarkId, memo) => {
        const request = {
            content: memo
        };
        const response = await api.post(`/bookmarks/${bookmarkId}/memos`, request);
        console.log(response.data);
    };

    //북마크 메모 수정
    const updateMemo = async (bookmarkId, memoId, memo) => {
        const request = {
            content: memo
        };
        const response = await api.put(`/bookmarks/${bookmarkId}/memos/${memoId}`, request);
        console.log(response.data);
    };
    
    //북마크 메모 삭제
    const deleteMemo = async (bookmarkId, memoId) => {
        const response = await api.delete(`/bookmarks/${bookmarkId}/memos/${memoId}`);
        console.log(response.data);
    };

    return {
        changePiority,
        moveToOtherCollection,
        deleteBookmark,
        manageTags,
        getMemo,
        createMemo,
        updateMemo,
        deleteMemo,
        
    };
});