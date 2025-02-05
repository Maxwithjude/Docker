<template>
    <div class="layout">
        <Header class="header"/>
        <div class="content-wrapper">
            <SideBar class="sidebar"/>
            <div class="main-content">
                <div class="detail-view">
                    <!-- URL 프리뷰 섹션 -->
                    <section class="preview-section">
                        <h2>URL 프리뷰</h2>
                        <div v-if="bookmark" class="content">
                            <div class="action-buttons">
                                <a :href="bookmark.url" target="_blank" class="original-link-btn">
                                    <i class="fas fa-external-link-alt"></i>
                                    원본 페이지로 이동
                                </a>
                            </div>
                            <iframe
                                :src="bookmark.url"
                                class="website-preview"
                                frameborder="0"
                                sandbox="allow-same-origin allow-scripts allow-popups allow-forms"
                                loading="lazy"
                            ></iframe>
                            <div class="iframe-fallback" v-if="iframeError">
                                <p>미리보기를 불러올 수 없습니다.</p>
                            </div>
                        </div>
                    </section>
            

                    <!-- 메모장 섹션 -->
                    <section class="memo-section">
                        <h2>메모장</h2>
                        <div class="memo-input">
                            <textarea 
                                v-model="newMemo" 
                                placeholder="메모를 입력하세요..."
                                rows="4"
                            ></textarea>
                            <button @click="addMemo" class="add-memo-btn">메모 추가</button>
                        </div>
                        <div class="memo-list">
                            <div v-for="memo in memos" :key="memo.id" class="memo-item">
                                <div class="memo-content">{{ memo.content }}</div>
                                <div class="memo-date">{{ memo.date }}</div>
                                <button @click="deleteMemo(memo.id)" class="delete-memo-btn">삭제</button>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useCounterStore } from '@/stores/counter';
import Header from '@/common/Header.vue';
import SideBar from '@/common/SideBar.vue';

const route = useRoute();
const store = useCounterStore();
const bookmark = ref(null);
const newMemo = ref('');
const memos = ref([]);
const iframeError = ref(false);

const addMemo = () => {
    if (newMemo.value.trim()) {
        memos.value.push({
            id: Date.now(),
            content: newMemo.value,
            date: new Date().toLocaleString()
        });
        newMemo.value = '';
    }
};

const deleteMemo = (id) => {
    memos.value = memos.value.filter(memo => memo.id !== id);
};

// iframe 로드 실패 시 처리
const handleIframeError = () => {
    iframeError.value = true;
};

onMounted(() => {
    const bookmarkId = parseInt(route.params.id);
    // store의 personalCollectionsBookmarks에서 북마크 찾기
    const foundBookmark = store.personalCollectionsBookmarks.results.bookmarks.find(
        b => b.bookmark_id === bookmarkId
    );
    
    if (foundBookmark) {
        bookmark.value = {
            id: foundBookmark.bookmark_id,
            image: foundBookmark.img,
            title: foundBookmark.title,
            description: foundBookmark.description,
            url: foundBookmark.url,
            hashtags: foundBookmark.tag,
            // readingTime: 15, // 이 값은 API에 없는 것 같습니다
        };
    }
});
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

.detail-view {
    flex: 1;
    padding: 20px;
    overflow-y: auto;
    display: grid;
    grid-template-columns: 7fr 3fr;
    gap: 20px;
}

.preview-section, .memo-section {
    background: white;
    border-radius: 12px;
    padding: 20px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

h2 {
    font-size: 1.5rem;
    margin-bottom: 20px;
    color: #333;
}

.main-image {
    width: 100%;
    height: 300px;
    object-fit: cover;
    border-radius: 8px;
    margin: 10px 0;
}

.description {
    font-size: 1.1rem;
    line-height: 1.6;
    margin: 10px 0;
}

.tag {
    background: #f0f0f0;
    padding: 4px 8px;
    border-radius: 20px;
    margin-right: 8px;
    font-size: 0.9rem;
}

.visit-link {
    display: inline-block;
    padding: 8px 16px;
    background: #007bff;
    color: white;
    text-decoration: none;
    border-radius: 6px;
    margin-top: 10px;
}

/* 메모장 스타일 */
.memo-input {
    margin-bottom: 20px;
}

.memo-section {
    background: white;
    border-radius: 12px;
    padding: 15px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    max-width: 400px;
}

textarea {
    width: 100%;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 8px;
    resize: vertical;
    margin-bottom: 8px;
    font-size: 0.9rem;
}

.add-memo-btn {
    padding: 8px 16px;
    background: #28a745;
    color: white;
    border: none;
    border-radius: 6px;
    cursor: pointer;
}

.memo-item {
    background: #f8f9fa;
    padding: 10px;
    border-radius: 6px;
    margin-bottom: 8px;
    position: relative;
    font-size: 0.9rem;
}

.memo-content {
    margin-bottom: 8px;
}

.memo-date {
    font-size: 0.8rem;
    color: #666;
}

.delete-memo-btn {
    position: absolute;
    top: 8px;
    right: 8px;
    padding: 4px 8px;
    background: #dc3545;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

.website-preview {
    width: 100%;
    height: 600px;
    border: 1px solid #ddd;
    border-radius: 8px;
    margin: 10px 0;
}

.iframe-fallback {
    text-align: center;
    padding: 20px;
    background: #f8f9fa;
    border-radius: 8px;
    margin: 10px 0;
}

.action-buttons {
    margin-bottom: 20px;
    text-align: right;
}

.original-link-btn {
    display: inline-flex;
    align-items: center;
    gap: 8px;
    padding: 12px 24px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 8px;
    text-decoration: none;
    font-weight: 500;
    transition: background-color 0.2s;
}

.original-link-btn:hover {
    background-color: #0056b3;
}

.original-link-btn i {
    font-size: 0.9em;
}
</style>