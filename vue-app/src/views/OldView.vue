<template>
    <div class="layout">
        <Header class="header"/>
        <div class="content-wrapper">
            <SideBar class="sidebar"/>
            <div class="main-content">
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
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import Header from '@/common/Header.vue';
import SideBar from '@/common/SideBar.vue';
import Card from '@/common/Card.vue';
import { useCounterStore } from '@/stores/counter';

const store = useCounterStore();
const bookmarkResults = computed(() => store.oldBookmarks.results || []);

const togglePriority = (bookmark) => {
    bookmark.priority = !bookmark.priority;
}
</script>

<style scoped>
.layout {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
}

.header {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 100;
    background: white;
}

.content-wrapper {
    display: flex;
    margin-top: 60px; /* 헤더 높이만큼 여백 추가 */
    height: calc(100vh - 60px); /* 전체 높이에서 헤더 높이를 뺀 만큼 설정 */
}

.sidebar {
    position: fixed;
    left: 0;
    top: 60px; /* 헤더 높이만큼 떨어뜨림 */
    bottom: 0;
    width: 240px; /* 사이드바 너비 */
    background: white;
    z-index: 99;
}

.main-content {
    flex: 1;
    margin-left: 240px; /* 사이드바 너비만큼 여백 */
    overflow-y: auto; /* 본문 내용만 스크롤 가능하도록 */
    height: 100%;
}

.body {
    padding: 20px;
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