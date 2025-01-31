<template>
    <div class="layout">
        <Header />
        <div class="content-wrapper">
            <SideBar />
            <div class="body">
                <h1 class="page-title">개인 컬렉션</h1>

                <div v-if="bookmarks.length === 0" class="empty-state">
                    <i class="fas fa-folder-open empty-icon"></i>
                    <p class="empty-text">북마크가 존재하지 않습니다.</p>
                    <p class="empty-description">새로운 북마크를 추가해보세요!</p>
                </div>

                <div v-else class="cards-grid">
                    <Card
                        v-for="bookmark in bookmarks"
                        :key="bookmark.id"
                        v-bind="bookmark"
                        @delete="handleDelete"
                        @manageTags="handleManageTags"
                        @toggleImportant="handleToggleImportant"
                        @copyToShared="handleCopyToShared"
                        @move="handleMove"
                    />
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
import Card from '@/common/Card.vue';

const bookmarks = ref([
    {
        id: 1,
        image: 'https://images.unsplash.com/photo-1481627834876-b7833e8f5570?q=80&w=2128&auto=format&fit=crop',
        title: '프로그래밍 입문자를 위한 자바스크립트 기초 가이드',
        description: '자바스크립트를 처음 시작하는 분들을 위한 완벽 가이드. 기초 문법부터 실전 예제까지 모두 담았습니다.',
        url: 'https://example.com/javascript-guide',
        hashtags: ['JavaScript', 'Programming', 'WebDev', 'Tutorial'],
        readingTime: 15,
        isImportant: true
    },
    {
        id: 2,
        image: 'https://images.unsplash.com/photo-1498050108023-c5249f4df085?q=80&w=2072&auto=format&fit=crop',
        title: 'React와 Vue.js 비교 분석',
        description: '현대 웹 개발의 양대 산맥, React와 Vue.js의 특징과 차이점을 상세히 비교 분석합니다.',
        url: 'https://example.com/react-vs-vue',
        hashtags: ['React', 'Vue', 'Frontend', 'Comparison'],
        readingTime: 10,
        isImportant: false
    },
    {
        id: 3,
        image: 'https://images.unsplash.com/photo-1555066931-4365d14bab8c?q=80&w=2070&auto=format&fit=crop',
        title: 'Python으로 시작하는 데이터 분석',
        description: 'Python과 주요 데이터 분석 라이브러리를 활용한 실무 데이터 분석 방법론',
        url: 'https://example.com/python-data-analysis',
        hashtags: ['Python', 'DataScience', 'Analytics'],
        readingTime: 20,
        isImportant: false
    }
]);

const showCreateModal = ref(false);

// 이벤트 핸들러 함수들
const handleDelete = (id) => {
    bookmarks.value = bookmarks.value.filter(bookmark => bookmark.id !== id);
};

const handleManageTags = (id) => {
    console.log('태그 관리:', id);
};

const handleToggleImportant = (id) => {
    const bookmark = bookmarks.value.find(b => b.id === id);
    if (bookmark) {
        bookmark.isImportant = !bookmark.isImportant;
    }
};

const handleCopyToShared = (id) => {
    console.log('공유 컬렉션으로 복사:', id);
};

const handleMove = (id) => {
    console.log('다른 컬렉션으로 이동:', id);
};
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

.cards-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 24px;
    padding: 20px 0;
}
</style>