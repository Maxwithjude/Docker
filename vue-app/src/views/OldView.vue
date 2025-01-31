<template>
    <div class="layout">
        <Header />
        <div class="content-wrapper">
            <SideBar />
            <div class="body">
                <h1 class="page-title">오래된 북마크</h1>

                <div v-if="oldBookmarks.length === 0" class="empty-state">
                    <i class="fas fa-history empty-icon"></i>
                    <p class="empty-text">30일이 지난 북마크가 없습니다.</p>
                    <p class="empty-description">
                        추가한지 30일이 지난 북마크들이 이곳에 표시됩니다.<br>
                        오래된 북마크들을 정리하고 관리해보세요.
                    </p>
                </div>

                <div v-else class="bookmarks-grid">
                    <!-- 북마크가 있을 때의 UI -->
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import Header from '@/common/Header.vue';
import SideBar from '@/common/SideBar.vue';

const oldBookmarks = ref([]);
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
    font-size: 0.95rem;
    color: #888;
    line-height: 1.5;
    max-width: 400px;
}

.bookmarks-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 24px;
    padding: 20px 0;
}
</style>