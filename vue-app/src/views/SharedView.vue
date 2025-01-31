<template>
    <div class="layout">
        <Header />
        <div class="content-wrapper">
            <SideBar />
            <div class="body">
                <h1 class="page-title">공유 컬렉션</h1>

                <div v-if="sharedCollections.length === 0" class="empty-state">
                    <i class="fas fa-share-alt empty-icon"></i>
                    <p class="empty-text">공유 컬렉션이 존재하지 않습니다.</p>
                    <p class="empty-description">다른 사람과 공유하고 싶은 북마크들을 모아 공유 컬렉션을 만들어보세요!</p>
                    <button class="create-collection-button" @click="showCreateModal = true">
                        <i class="fas fa-plus"></i>
                        공유 컬렉션 만들기
                    </button>
                </div>

                <div v-else class="collections-grid">
                    <!-- 공유 컬렉션이 있을 때의 UI -->
                </div>
            </div>
        </div>

        <!-- 컬렉션 생성 모달 -->
        <div v-if="showCreateModal" class="modal-overlay" @click="showCreateModal = false">
            <div class="modal-content" @click.stop>
                <CreateCollection @close="showCreateModal = false" />
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import Header from '@/common/Header.vue';
import SideBar from '@/common/SideBar.vue';
import CreateCollection from '@/modal/CreateCollection.vue';

const sharedCollections = ref([]);
const showCreateModal = ref(false);
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
    margin-bottom: 24px;
    max-width: 400px;
    line-height: 1.5;
}

.create-collection-button {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 12px 24px;
    background-color: #4CAF50;
    color: white;
    border: none;
    border-radius: 8px;
    font-size: 0.95rem;
    cursor: pointer;
    transition: background-color 0.2s;
}

.create-collection-button:hover {
    background-color: #45a049;
}

.create-collection-button i {
    font-size: 1rem;
}

.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

.modal-content {
    background: white;
    border-radius: 12px;
    padding: 0;
    max-width: 90%;
    max-height: 90%;
    overflow-y: auto;
}

.collections-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 24px;
    padding: 20px 0;
}
</style>