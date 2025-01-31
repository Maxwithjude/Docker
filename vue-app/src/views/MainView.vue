<template>
    <div class="layout">
        <Header />
        <div class="content-wrapper">
            <SideBar />
            <div class="body">
                <div class="collection-controls">
                    <button class="control-button create" @click="showCreateModal = true">
                        <i class="fas fa-plus"></i>
                        컬렉션 생성
                    </button>
                    <button 
                        class="control-button delete" 
                        @click="toggleDeleteMode"
                        :class="{ 'active': isDeleteMode }"
                    >
                        <i class="fas fa-trash"></i>
                        {{ isDeleteMode ? '삭제 완료' : '컬렉션 삭제' }}
                    </button>
                </div>
                
                <div v-if="collections.length === 0" class="empty-state">
                    <i class="fas fa-folder-open empty-icon"></i>
                    <p class="empty-text">컬렉션이 존재하지 않습니다.</p>
                    <p class="empty-description">
                        상단의 '컬렉션 생성' 버튼을 클릭하여<br>
                        새로운 컬렉션을 만들어보세요!
                    </p>
                </div>
                
                <div v-else class="collections-grid">
                    <Collection 
                        v-for="collection in collections"
                        :key="collection.id"
                        :title="collection.title"
                        :type="collection.type"
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
import Collection from '@/common/Collection.vue';
import CreateCollection from '@/modal/CreateCollection.vue';

const isDeleteMode = ref(false);
const collections = ref([
    // {
    //     id: 1,
    //     title: "영어 단어장",
    //     type: 'personal'
    // },
    // {
    //     id: 2,
    //     title: "프로그래밍 용어",
    //     type: 'shared'
    // },
    // {
    //     id: 3,
    //     title: "일본어 문법",
    //     type: 'personal'
    // },
    // {
    //     id: 4,
    //     title: "수학 공식",
    //     type: 'shared'
    // },
    // {
    //     id: 5,
    //     title: "한국사 연대표",
    //     type: 'personal'
    // }
]);
const showCreateModal = ref(false);

const toggleDeleteMode = () => {
    isDeleteMode.value = !isDeleteMode.value;
};
</script>

<style scoped>
.layout {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
}

.content-wrapper {
    display: flex;
    flex: 1;
}

.body {
    flex: 1;
    padding: 20px;
    overflow-y: auto;
}

.collection-controls {
    display: flex;
    gap: 12px;
    margin-bottom: 24px;
}

.control-button {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 10px 20px;
    border: none;
    border-radius: 8px;
    font-size: 0.95rem;
    cursor: pointer;
    transition: all 0.2s;
}

.control-button i {
    font-size: 1rem;
}

.control-button.create {
    background-color: #4CAF50;
    color: white;
}

.control-button.create:hover {
    background-color: #45a049;
}

.control-button.delete {
    background-color: #f8f9fa;
    color: #dc3545;
    border: 1px solid #dc3545;
}

.control-button.delete:hover {
    background-color: #dc3545;
    color: white;
}

.control-button.delete.active {
    background-color: #dc3545;
    color: white;
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
    margin-top: 20px;
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
}

.collections-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 24px;
    padding: 20px 0;
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
</style>