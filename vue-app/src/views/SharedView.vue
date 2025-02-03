<template>
    <div class="layout">
        <Header />
        <div class="content-wrapper">
            <SideBar />
            <div class="body">
                <div class="header-section">
                    <h1 class="page-title">공유 컬렉션</h1>
                    <div class="collection-controls">
                        <button class="create-collection-button" @click="showCreateModal = true">
                            <i class="fas fa-plus"></i>
                            새 컬렉션
                        </button>
                    </div>
                </div>
                <div class="collection-buttons">
                    <button 
                        class="collection-button"
                        :class="{ active: selectedCollectionId === 'all' }"
                        @click="selectedCollectionId = 'all'"
                    >
                        전체 컬렉션
                    </button>
                    <button 
                        v-for="collection in sharedCollections"
                        :key="collection.id"
                        class="collection-button"
                        :class="{ active: selectedCollectionId === collection.id }"
                        @click="selectedCollectionId = collection.id"
                    >
                        {{ collection.name }}
                    </button>
                </div>

                <div v-if="displayCollections.length === 0" class="empty-state">
                    <i class="fas fa-folder-open empty-icon"></i>
                    <p class="empty-text">컬렉션이 존재하지 않습니다.</p>
                    <p class="empty-description">새로운 컬렉션을 추가해보세요!</p>
                </div>

                <div v-else v-for="collection in displayCollections" :key="collection.id">
                    <h2 class="collection-title">{{ collection.name }}</h2>
                    <div class="cards-grid">
                        <Card
                            v-for="bookmark in sortedBookmarks(collection.bookmarks)"
                            :key="bookmark.id"
                            v-bind="bookmark"
                            :is-personal="false"
                            :bookmark-id="bookmark.id"
                            @delete="handleDelete(collection.id, bookmark.id)"
                            @manageTags="handleManageTags"
                            @toggleImportant="handleToggleImportant(collection.id, bookmark.id)"
                            @copyToShared="handleCopyToShared"
                            @move="handleMove"
                        />
                    </div>
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
import { ref, computed } from 'vue';
import Header from '@/common/Header.vue';
import SideBar from '@/common/SideBar.vue';
import CreateCollection from '@/modal/CreateCollection.vue';
import Card from '@/common/Card.vue';

const sharedCollections = ref([
    {
        id: 1,
        name: '공유 컬렉션 1',
        bookmarks: [
            {
                id: 1,
                image: 'https://images.unsplash.com/photo-1481627834876-b7833e8f5570?q=80&w=2128&auto=format&fit=crop',
                title: '프로그래밍 입문자를 위한 자바스크립트 기초 가이드',
                description: '자바스크립트를 처음 시작하는 분들을 위한 완벽 가이드. 기초 문법부터 실전 예제까지 모두 담았습니다.',
                url: 'https://example.com/docker-kubernetes-guide',
                hashtags: ['JavaScript', 'Programming', 'WebDev', 'Tutorial'],
                readingTime: 15,
                isImportant: false
            },
            {
                id: 2,
                image: 'https://images.unsplash.com/photo-1551288049-bebda4e38f71?q=80&w=2070&auto=format&fit=crop',
                title: 'Docker와 Kubernetes로 시작하는 컨테이너 오케스트레이션',
                description: '현대적인 애플리케이션 배포와 관리를 위한 컨테이너 기술 완벽 가이드',
                url: 'https://example.com/docker-kubernetes-guide',
                hashtags: ['Docker', 'Kubernetes', 'DevOps', 'Container'],
                readingTime: 35,
                isImportant: true
            }
        ]
    },
    {
        id: 2,
        name: '공유 컬렉션 2',
        bookmarks: [
            {
                id: 3,
                image: 'https://images.unsplash.com/photo-1555066931-4365d14bab8c?q=80&w=2070&auto=format&fit=crop',
                title: 'Python으로 시작하는 데이터 분석',
                description: 'Python과 주요 데이터 분석 라이브러리를 활용한 실무 데이터 분석 방법론',
                url: 'https://example.com/python-data-analysis',
                hashtags: ['Python', 'DataScience', 'Analytics'],
                readingTime: 20,
                isImportant: false
            },
            {
                id: 4,
                image: 'https://images.unsplash.com/photo-1498050108023-c5249f4df085?q=80&w=2072&auto=format&fit=crop',
                title: 'React와 TypeScript로 만드는 현대적인 웹 애플리케이션',
                description: '실전 프로젝트로 배우는 React와 TypeScript 완벽 가이드. 컴포넌트 설계부터 상태관리까지 상세히 다룹니다.',
                url: 'https://example.com/react-typescript-guide',
                hashtags: ['React', 'TypeScript', 'Frontend', 'WebDev'],
                readingTime: 25,
                isImportant: true
            },
            {
                id: 5,
                image: 'https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?q=80&w=2070&auto=format&fit=crop',
                title: '사이버 보안 입문: 웹 해킹과 방어 기초',
                description: '웹 보안의 기초부터 실제 취약점 분석까지, 화이트해커가 되기 위한 첫걸음을 시작해보세요.',
                url: 'https://example.com/cyber-security-basics',
                hashtags: ['Security', 'Hacking', 'WebSecurity', 'Cybersecurity'],
                readingTime: 30,
                isImportant: false
            }
        ]
    }
]);

const showCreateModal = ref(false);
const selectedCollectionId = ref('all');
const displayCollections = computed(() => {
    if (selectedCollectionId.value === 'all') {
        return sharedCollections.value;
    }
    return sharedCollections.value.filter(
        collection => collection.id === Number(selectedCollectionId.value)
    );
});

const handleCollectionChange = () => {
    console.log('선택된 컬렉션:', selectedCollectionId.value);
};

// 이벤트 핸들러 함수들
const handleDelete = (collectionId, bookmarkId) => {
    const collection = sharedCollections.value.find(c => c.id === collectionId);
    if (collection) {
        collection.bookmarks = collection.bookmarks.filter(bookmark => bookmark.id !== bookmarkId);
    }
};

const handleManageTags = (id) => {
    console.log('태그 관리:', id);
};

const handleToggleImportant = (collectionId, bookmarkId) => {
    const collection = sharedCollections.value.find(c => c.id === collectionId);
    if (collection) {
        const bookmark = collection.bookmarks.find(b => b.id === bookmarkId);
        if (bookmark) {
            bookmark.isImportant = !bookmark.isImportant;
        }
    }
};

const handleCopyToShared = (id) => {
    console.log('공유 컬렉션으로 복사:', id);
};

const handleMove = (id) => {
    console.log('다른 컬렉션으로 이동:', id);
};

// 새로운 메서드 추가
const sortedBookmarks = (bookmarks) => {
    return bookmarks.sort((a, b) => {
        return (b.isImportant ? 1 : 0) - (a.isImportant ? 1 : 0);
    });
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

.collection-title {
    font-size: 1.2rem;
    font-weight: 600;
    color: #333;
    margin: 20px 0 16px 0;
}

.header-section {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
}

.collection-controls {
    display: flex;
    gap: 16px;
    align-items: center;
}

.collection-buttons {
    display: flex;
    gap: 12px;
    padding: 0 20px;
    margin-bottom: 24px;
    flex-wrap: wrap;
}

.collection-button {
    padding: 8px 16px;
    background-color: #f1f1f1;
    border: none;
    border-radius: 20px;
    color: #333;
    font-size: 0.95rem;
    cursor: pointer;
    transition: all 0.2s;
}

.collection-button:hover {
    background-color: #e0e0e0;
}

.collection-button.active {
    background-color: #4CAF50;
    color: white;
}

.create-collection-button {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 16px;
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
    font-size: 0.9rem;
}
</style>