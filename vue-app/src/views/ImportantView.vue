<template>
    <div class="layout">
        <Header />
        <div class="content-wrapper">
            <SideBar />
            <div class="body">
                <h1 class="page-title">중요 북마크</h1>
                
                <div v-if="!bookmarkResults.length" class="empty-state">
                    <i class="fas fa-star empty-icon"></i>
                    <p class="empty-text">중요 표시된 북마크가 없습니다.</p>
                    <p class="empty-description">북마크에 별표를 클릭하여 중요 북마크로 지정할 수 있습니다.</p>
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
import { ref, computed, watch } from 'vue';
import { storeToRefs } from 'pinia';
import Header from '@/common/Header.vue';
import SideBar from '@/common/SideBar.vue';
import Card from '@/common/Card.vue';
import { useCounterStore } from '@/stores/counter';


const togglePriority = (bookmark) => {
    bookmark.priority = !bookmark.priority;
}

const { importantBookmarks } = storeToRefs(useCounterStore());
const bookmarkResults = computed(() => importantBookmarks.value.results || []);

// bookmarkResults의 변화를 감지하여 콘솔에 출력
watch(bookmarkResults, (newResults) => {
    console.log('북마크 결과:', newResults);
});
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