<!-- <div v-if="oldBookmarks.length === 0" class="empty-state">
                    <i class="fas fa-history empty-icon"></i>
                    <p class="empty-text">30일이 지난 북마크가 없습니다.</p>
                    <p class="empty-description">
                        추가한지 30일이 지난 북마크들이 이곳에 표시됩니다.<br>
                        오래된 북마크들을 정리하고 관리해보세요. -->

<template>
    <div class="layout">
        <Header />
        <div class="content-wrapper">
            <SideBar />
            <div class="body">
                <h1 class="page-title">30일 이상 읽지않은 북마크</h1>
                
                <div v-if="!bookmarkResults.length" class="empty-state">
                    <i class="fas fa-star empty-icon"></i>
                    <p class="empty-text">오랜기간 읽지않은 북마크가 없습니다.</p>
                    <p class="empty-description">[예시]북마크에 별표를 클릭하여 중요 북마크로 지정할 수 있습니다.</p>
                </div>
                
                <div v-else class="cards-grid">
                    <Card
                        v-for="bookmark in bookmarkResults"
                        :bookmarkId="bookmark.bookmark_id"
                        :url="bookmark.url"
                        :img="bookmark.img"
                        :title="bookmark.title"
                        :description="bookmark.description"
                        :tag="bookmark.tag"
                        :priority="bookmark.priority"
                        :isPersonal="bookmark.isPersonal"
                        :createdAt="bookmark.created_at"
                        :updatedAt="bookmark.updated_at"
                        @togglePriority="togglePriority(bookmark)"
                    />
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import Header from '@/common/Header.vue';
import SideBar from '@/common/SideBar.vue';
import Card from '@/common/Card.vue';

const togglePriority = (bookmark) => {
    bookmark.priority = !bookmark.priority;
}

// 여기에 실제 북마크 데이터를 가져오는 로직이 들어갈 예정
const bookmarks = ref([
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
                "tag" : ["서핑", "웹"]
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
                "tag" : ["싸피", "IT"]
            }
        ]
    }
]);

const bookmarkResults = computed(() => bookmarks.value[0]?.results || []);
</script>

<style scoped>
.layout {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
    overflow: hidden;
}

.content-wrapper {
    display: flex;
    flex: 1;
    position: fixed;
    top: 60px; /* 헤더 높이 */
    bottom: 0;
    left: 0;
    right: 0;
}

.body {
    flex: 1;
    padding: 20px;
    overflow-y: auto;
}

.page-title {
    font-size: 1.5rem;
    font-weight: 600;
    color: #333;
    margin-bottom: 24px;
}

.empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 60px 20px;
    background-color: #f8f9fa;
    border-radius: 12px;
    text-align: center;
}

.empty-icon {
    font-size: 3rem;
    color: #ddd;
    margin-bottom: 16px;
}

.empty-text {
    font-size: 1.2rem;
    color: #666;
    margin-bottom: 8px;
}

.empty-description {
    font-size: 0.9rem;
    color: #888;
}

.cards-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 24px;
    padding: 20px 0;
}
</style>