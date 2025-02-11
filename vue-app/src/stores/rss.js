import { defineStore } from "pinia";
import { ref } from 'vue';
import api from "@/utils/api";

//북마크에 관한 함수 store
export const useRssStore = defineStore("rss", () => {
    //rss 구독 목록 조회 실제 response
    const rssList = ref({});
    //rss 구독 목록 조회 예시 response
    const exRssList = ref({

        success:true,
        message:"some message",
        results: [

              {
                "rss_id": 1,
                "name": "TED",
                "is_read": true
              },
              {
                "rss_id": 2,
                "name": "연합뉴스TV",
                "is_read": false
              },
            ]
    });
    //구독 중인 rss 최신 글 목록 조회 실제 response
    const rssArticles = ref({});
    //구독 중인 rss 최신 글 목록 조회 예시 response
    const exRssArticles = ref({
        "success":true,

        "message":"some message",
        "results": {
            "rss_id": 1,
            "name": "BBC",
            "latest_posts": [
                {
                "title": "New AI Breakthrough Announced",
                "url": "https://example.com/articles/ai-breakthrough", //글 url
                "is_read": true
                },
                {
                "title": "The Future of Quantum Computing",
                "url": "https://example.com/articles/quantum-computing",
                "is_read": false	     
                }, 
            ],
            "totalPages": 5,
            "currentPage": 0,
            "hasNext": true
        }
    });


    //rss 구독 목록 조회
    const getRss = async () => {
        const response = await api.get(`/subscriptions`);
        rssList.value = response.data;
    };


    

    //특정 rss 최신 글 목록 조회
    const getRssArticles = async (rssId) => {
        const response = await api.get(`/subscriptions/${rssId}`);
        rssArticles.value = response.data;
    };


    //rss 상태 갱신
    const updateRss = async () => {
        const response = await api.get(`/subscriptions/refresh`);
        console.log(response.data);
    };

    //rss 구독 삭제
    const deleteRss = async (rssId) => {
        const response = await api.delete(`/subscriptions/${rssId}`);
        console.log(response.data);
    };

    return {
        getRss,
        getRssArticles,
        updateRss,
        deleteRss,
        rssList,
        rssArticles,
        exRssList,
        exRssArticles,
    };

});