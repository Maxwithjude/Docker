<template>
    <div class="layout">
        <Header class="header"/>
        <div class="content-wrapper">
            <SideBar class="sidebar"/>
            <div class="main-content">
                <div class="body">
                    <div class="search-page">
                        <div class="search-container">
                            <input 
                                v-model="searchTag" 
                                @keyup.enter="handleSearch"
                                placeholder="태그를 입력하세요"
                                class="search-input"
                            />
                            <button @click="handleSearch" class="search-button">
                                검색
                            </button>
                        </div>
                        
                        <!-- 검색 전 안내 메시지 -->
                        <div v-if="!hasSearched" class="initial-message">
                            <div class="message-box">
                                <i class="fas fa-search search-icon"></i>
                                <h3>태그로 북마크를 검색해보세요</h3>
                                <p>원하는 태그를 입력하고 검색하면 관련된 북마크들을 찾을 수 있습니다.</p>
                            </div>
                        </div>
                    
                        <!-- 검색 결과 -->
                        <template v-else>
                            <div class="search-results-container">
                                <!-- 왼쪽: 검색 결과 -->
                                <div class="main-results">
                                    <h2 class="section-title">검색결과</h2>
                                    <div class="bookmarks-grid">
                                        <Card
                                            v-for="bookmark in bookmarks"
                                            :key="bookmark.bookmarkId"
                                            :bookmark-id="bookmark.bookmarkId"
                                            :img="bookmark.image"
                                            :title="bookmark.title"
                                            :description="bookmark.description"
                                            :url="bookmark.url"
                                            :tag="bookmark.tags"
                                            :priority="bookmark.priority"
                                            :created-at="bookmark.createdAt"
                                            :updated-at="bookmark.updatedAt"
                                            :is-personal="true"
                                            @toggle-priority="() => handleTogglePriority(bookmark.bookmarkId)"
                                        />
                                    </div>
                                    
                                    <div v-if="loading" class="loading">
                                        데이터를 불러오는 중...
                                    </div>
                                    
                                    <div v-if="!hasMore && bookmarks.length > 0" class="no-more">
                                        모든 북마크를 불러왔습니다.
                                    </div>
                                    
                                    <div v-if="hasSearched && bookmarks.length === 0 && !loading" class="no-results">
                                        검색 결과가 없습니다.
                                    </div>
                                </div>

                                <!-- 오른쪽: 추천 북마크 -->
                                <div v-if="recommendedBookmarks.length > 0" class="recommended-section">
                                    <h2 class="section-title">관련 북마크</h2>
                                    <div class="recommended-list">
                                        <div 
                                            v-for="bookmark in recommendedBookmarks" 
                                            :key="bookmark.url"
                                            class="recommended-item"
                                            @click="goToUrl(bookmark.url)"
                                        >
                                            <img :src="bookmark.image" :alt="bookmark.title" class="recommended-image">
                                            <div class="recommended-content">
                                                <h3>{{ bookmark.title }}</h3>
                                                <button class="save-button">save</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </template>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useCounterStore } from '@/stores/counter'
import Header from '@/common/Header.vue'
import SideBar from '@/common/SideBar.vue'
import Card from '@/common/Card.vue'

const store = useCounterStore()
const searchTag = ref('')
const bookmarks = ref([])
const recommendedBookmarks = ref([])
const loading = ref(false)
const hasMore = ref(true)
const lastCursorId = ref(null)
const hasSearched = ref(false)

const handleSearch = async () => {
    if (!searchTag.value.trim()) return
    
    hasSearched.value = true
    bookmarks.value = []
    recommendedBookmarks.value = []
    lastCursorId.value = null
    hasMore.value = true
    await loadMoreBookmarks()
}

const loadMoreBookmarks = async () => {
    if (loading.value || !hasMore.value) return
    
    try {
        loading.value = true
        const response = await store.searchBookmarksByTag(
            searchTag.value,
            lastCursorId.value,
            6
        )

        const newBookmarks = response.result.userBookmarkList
        
        // 새로운 북마크가 6개 미만이면 더 이상 불러올 데이터가 없음
        if (newBookmarks.length < 6) {
            hasMore.value = false
        }

        if (newBookmarks.length > 0) {
            // 중복 제거: 이미 있는 bookmarkId는 제외
            const uniqueNewBookmarks = newBookmarks.filter(newBookmark => 
                !bookmarks.value.some(existingBookmark => 
                    existingBookmark.bookmarkId === newBookmark.bookmarkId
                )
            );

            // 중복이 제거된 새로운 북마크만 추가
            if (uniqueNewBookmarks.length > 0) {
                bookmarks.value.push(...uniqueNewBookmarks)
                lastCursorId.value = uniqueNewBookmarks[uniqueNewBookmarks.length - 1].bookmarkId
            } else {
                // 새로운 고유 북마크가 없다면 더 이상 로드하지 않음
                hasMore.value = false
            }
        } else {
            hasMore.value = false
        }

        // 첫 검색 시에만 추천 북마크 설정
        if (!recommendedBookmarks.value.length) {
            recommendedBookmarks.value = response.result.recommendedBookmarkList
        }
    } catch (error) {
        console.error('북마크 로딩 중 오류 발생:', error)
    } finally {
        loading.value = false
    }
}

const handleTogglePriority = async (bookmarkId) => {
    // TODO: 북마크 우선순위 토글 API 구현
    console.log('Toggle priority for bookmark:', bookmarkId)
}

const goToUrl = (url) => {
    window.open(url, '_blank')
}

const handleScroll = () => {
    const mainContent = document.querySelector('.main-content')
    if (!mainContent) return
    
    const { scrollTop, scrollHeight, clientHeight } = mainContent
    if (scrollTop + clientHeight >= scrollHeight - 300) {
        loadMoreBookmarks()
    }
}

onMounted(() => {
    const mainContent = document.querySelector('.main-content')
    if (mainContent) {
        mainContent.addEventListener('scroll', handleScroll)
    }
})

onUnmounted(() => {
    const mainContent = document.querySelector('.main-content')
    if (mainContent) {
        mainContent.removeEventListener('scroll', handleScroll)
    }
})
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
.search-page {
    padding: 20px;
    max-width: 1200px;
    margin: 0 auto;
}

.search-container {
    margin-bottom: 30px;
    display: flex;
    gap: 10px;
}

.search-input {
    flex: 1;
    padding: 12px;
    border: 2px solid #ddd;
    border-radius: 8px;
    font-size: 16px;
}

.search-button {
    padding: 0 24px;
    background-color: #3182ce;
    color: white;
    border: none;
    border-radius: 8px;
    cursor: pointer;
    font-weight: bold;
}

.search-results-container {
    display: flex;
    gap: 40px;
}

.main-results {
    flex: 1;
}

.recommended-section {
    width: 300px;
    flex-shrink: 0;
}

.section-title {
    font-size: 1.2rem;
    font-weight: bold;
    margin-bottom: 20px;
    color: #2d3748;
}

.bookmarks-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 20px;
}

.recommended-list {
    display: flex;
    flex-direction: column;
    gap: 16px;
}

.recommended-item {
    display: flex;
    gap: 12px;
    padding: 12px;
    border: 1px solid #e2e8f0;
    border-radius: 8px;
    cursor: pointer;
    transition: transform 0.2s;
    height: 100px;
    width: 100%;
}

.recommended-item:hover {
    transform: translateY(-2px);
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.recommended-image {
    width: 60px;
    height: 60px;
    object-fit: cover;
    border-radius: 4px;
}

.recommended-content {
    flex: 1;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.recommended-content h3 {
    font-size: 0.9rem;
    margin: 0;
    color: #2d3748;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

.save-button {
    padding: 4px 12px;
    background-color: transparent;
    border: 1px solid #e2e8f0;
    border-radius: 4px;
    color: #718096;
    cursor: pointer;
    font-size: 0.8rem;
}

.save-button:hover {
    background-color: #f7fafc;
}

.loading {
    text-align: center;
    padding: 20px;
    color: #718096;
}

.no-more {
    text-align: center;
    padding: 20px;
    color: #718096;
    font-style: italic;
}

/* .initial-message {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 400px;
} */
.initial-message {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 60px 20px;
    background-color: #f8f9fa;
    border-radius: 12px;
    text-align: center;
}

.message-box {
    text-align: center;
    padding: 20px;
    background-color: #f8fafc;
    border-radius: 12px;
    max-width: 500px;
}

.search-icon {
    font-size: 48px;
    margin-bottom: 20px;
    color: #ddd;
    display: block;
}

.message-box h3 {
    color: #2d3748;
    font-size: 24px;
    margin-bottom: 12px;
}

.message-box p {
    color: #718096;
    font-size: 16px;
    line-height: 1.5;
}

.no-results {
    text-align: center;
    padding: 40px;
    color: #718096;
    font-style: italic;
}
</style>



