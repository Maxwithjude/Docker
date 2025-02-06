<template>
    <div class="layout">
        <Header class="header"/>
        <div class="content-wrapper">
            <SideBar class="sidebar"/>
            <div class="main-content">
                <div class="body">
                    <h1 class="page-title">추천 북마크</h1>

                
                    <div v-if="isLoading" class="loading-state">
                        <i class="fas fa-spinner fa-spin loading-icon"></i>
                        <p>추천 북마크를 불러오는 중입니다...</p>
                    </div>

                    <div v-else-if="interestTags.length === 0" class="empty-state">
                        <i class="fas fa-lightbulb empty-icon"></i>
                        <p class="empty-text">관심 태그를 설정하고 맞춤 추천을 받아보세요!</p>
                        <p class="empty-description">관심 태그를 설정하면 회원님의 관심사에 맞는 북마크를 추천해드립니다.</p>
                        <button class="set-tags-button" @click="goToTagSettings">
                            관심 태그 설정하기
                        </button>
                    </div>

                    <div v-else-if="recommendedBookmarks.length === 0" class="empty-state">
                        <i class="fas fa-thumbs-up empty-icon"></i>
                        <p class="empty-text">추천할 북마크가 없습니다.</p>
                        <p class="empty-description">
                            {{ selectedTag 
                                ? `'${selectedTag}' 태그와 관련된 북마크가 없습니다.` 
                                : '북마크를 더 저장하면 관련된 추천을 받을 수 있습니다.' 
                            }}
                        </p>
                    </div>
                
                    <template v-else>
                        <div class="recommendation-info">
                            <p>
                                {{ selectedTag 
                                    ? `'${selectedTag}' 태그와 관련된 ${recommendedBookmarks.length}개의 북마크를 찾았습니다.`
                                    : `회원님의 관심사와 관련된 ${recommendedBookmarks.length}개의 북마크를 찾았습니다.`
                                }}
                            </p>
                        </div>
                        
                        <div class="cards-grid">
                            <Card
                                v-for="bookmark in recommendedBookmarks"
                                :key="bookmark.id"
                                :image="bookmark.image"
                                :title="bookmark.title"
                                :description="bookmark.description"
                                :url="bookmark.url"
                                :hashtags="bookmark.hashtags"
                                :readingTime="bookmark.readingTime"
                                :isImportant="false"
                            />
                        </div>
                    </template>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import Header from '@/common/Header.vue';
import SideBar from '@/common/SideBar.vue';
import Card from '@/common/Card.vue';

const router = useRouter();
const isLoading = ref(true);
const recommendedBookmarks = ref([]);
const interestTags = ref([]); // 사용자의 관심 태그 목록
const selectedTag = ref(null);

const goToTagSettings = () => {
    // 태그 설정 페이지로 이동
    router.push('/settings/tags');
};

// 관심 태그 목록을 가져오는 함수
const fetchInterestTags = async () => {
    try {
        // API 호출 로직
        // const response = await api.getInterestTags();
        // interestTags.value = response.data;
    } catch (error) {
        console.error('관심 태그 로딩 실패:', error);
    }
};

// 추천 북마크를 가져오는 함수
const fetchRecommendedBookmarks = async (tag = null) => {
    try {
        isLoading.value = true;
        // API 호출 로직
        // const response = await api.getRecommendedBookmarks({ tag });
        // recommendedBookmarks.value = response.data;
    } catch (error) {
        console.error('추천 북마크 로딩 실패:', error);
    } finally {
        isLoading.value = false;
    }
};

// 태그 선택 핸들러
const selectTag = (tag) => {
    if (selectedTag.value === tag) {
        selectedTag.value = null;
        fetchRecommendedBookmarks();
    } else {
        selectedTag.value = tag;
        fetchRecommendedBookmarks(tag);
    }
};

onMounted(() => {
    fetchInterestTags();
    fetchRecommendedBookmarks();
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

.page-title {
    font-size: 1.5rem;
    font-weight: 600;
    color: #333;
    margin-bottom: 24px;
}

.loading-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 60px 20px;
    color: #666;
}

.loading-icon {
    font-size: 2rem;
    margin-bottom: 16px;
    color: #4CAF50;
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

.interest-tags-section {
    margin-bottom: 24px;
}

.section-title {
    font-size: 1.1rem;
    font-weight: 600;
    color: #333;
    margin-bottom: 12px;
}

.tags-container {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
}

.tag-button {
    padding: 6px 16px;
    border-radius: 20px;
    border: 1px solid #ddd;
    background: white;
    color: #666;
    font-size: 0.9rem;
    cursor: pointer;
    transition: all 0.2s;
}

.tag-button:hover {
    background: #f5f5f5;
}

.tag-button.active {
    background: #4CAF50;
    color: white;
    border-color: #4CAF50;
}

.recommendation-info {
    background-color: #e8f5e9;
    padding: 12px 20px;
    border-radius: 8px;
    margin-bottom: 20px;
    color: #2e7d32;
}

.cards-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 24px;
    padding: 20px 0;
}

.no-tags-message {
    text-align: center;
    padding: 20px;
    background-color: #f8f9fa;
    border-radius: 8px;
    color: #666;
}

.no-tags-message i {
    font-size: 1.5rem;
    margin-bottom: 8px;
    color: #999;
}

.set-tags-button {
    margin-top: 16px;
    padding: 8px 20px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 20px;
    font-size: 0.9rem;
    cursor: pointer;
    transition: background-color 0.2s;
}

.set-tags-button:hover {
    background-color: #0056b3;
}
</style>