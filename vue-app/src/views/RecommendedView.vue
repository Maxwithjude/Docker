<template>
    <div class="layout">
        <Header class="header"/>
        <div class="content-wrapper">
            <SideBar class="sidebar"/>
            <div class="main-content">
                <div class="body">
                    <div class="page-header">
                        <div class="header-content">
                            <div class="title-section">
                                <i class="fas fa-thumbs-up title-icon"></i>
                                <h2 class="title">추천 북마크</h2>
                            </div>
                            <p class="description">사용자의 관심사에 맞는 추천 북마크를 확인할 수 있습니다</p>
                        </div>
                    </div>

                    <!-- 태그가 없는 경우 -->
                    <div v-if="!hasUserTags" class="no-tags-container">
                        <div class="no-tags-content">
                            <i class="fas fa-tags no-tags-icon"></i>
                            <h3>관심사 태그가 없습니다</h3>
                            <p>관심 태그를 추가하면 관련된 추천 콘텐츠를 받아볼 수 있습니다.</p>
                        </div>
                    </div>

                    <!-- 태그가 있는 경우 -->
                    <template v-else>
                        <div class="tabs-container">
                            <div class="tabs">
                                <button 
                                    v-for="tag in userTags" 
                                    :key="tag.id"
                                    :class="['tab', { active: selectedTag === tag.id }]"
                                    @click="handleTagSelect(tag.id)"
                                >
                                    {{ tag.name }}
                                </button>
                            </div>
                        </div>
                        
                        <div class="bookmarks-grid" v-if="!isLoading">
                            <CardUnbookmarked 
                                v-for="bookmark in recommendedBookmarksList" 
                                :key="bookmark.id"
                                :url="bookmark.url"
                                :img="bookmark.img"
                                :title="bookmark.title"
                                :description="bookmark.description"
                            />
                        </div>
                        <div v-else class="loading">
                            <i class="fas fa-spinner fa-spin"></i>
                        </div>
                    </template>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { storeToRefs } from 'pinia';
import Header from '@/common/Header.vue';
import SideBar from '@/common/SideBar.vue';
import CardUnbookmarked from '@/common/CardUnbookmarked.vue';
import { useBookmarkStore } from '@/stores/bookmark';

const router = useRouter();
const bookmarkStore = useBookmarkStore();
const { userDefineTags, recommendedBookmarks } = storeToRefs(bookmarkStore);
const { getUserDefineTags, getRecommendedBookmarks } = bookmarkStore;


const isLoading = ref(true);
const recommendedBookmarksList = ref([]);
const selectedTag = ref(null);
const userTags = ref([]);
const page = ref(1);
const hasMore = ref(true);
const observer = ref(null);

const hasUserTags = computed(() => userTags.value && userTags.value.length > 0);

const fetchUserTags = async () => {
    try {
        await getUserDefineTags();
        // 실제 API 준비될 때까지 예시 데이터 사용
        const tagList = userDefineTags.value.results.tagList;
        console.log('태그 리스트:', tagList);
        

        if (tagList && tagList.length > 0) {
            userTags.value = tagList.map((tagName, index) => ({
                id: index + 1,
                name: tagName
            }));
            if (userTags.value.length > 0) {
                selectedTag.value = userTags.value[0].id;
                await fetchRecommendedBookmarks(userTags.value[0].name);
            }
        }
    } catch (error) {
        console.error('태그 로딩 실패:', error);
        userTags.value = [];
    }
};

const fetchRecommendedBookmarks = async (tagName = null) => {
    try {
        isLoading.value = true;
        await getRecommendedBookmarks(tagName);
        
        // 실제 API 준비될 때까지 예시 데이터 사용
        const recommendedList = recommendedBookmarks.value.result.recommendedUrlList;
        

        if (recommendedList) {
            recommendedBookmarksList.value = recommendedList.map((bookmark, index) => ({
                id: `${page.value}-${index}`,
                url: bookmark.url,
                title: bookmark.title,
                description: bookmark.description,
                img: bookmark.img
            }));
        }
    } catch (error) {
        console.error('추천 북마크 로딩 실패:', error);
    } finally {
        isLoading.value = false;
    }
};

const handleTagSelect = async (tagId) => {
    selectedTag.value = tagId;
    page.value = 1;
    const selectedTagName = userTags.value.find(tag => tag.id === tagId)?.name;
    console.log('선택된 태그:', selectedTagName);
    await fetchRecommendedBookmarks(selectedTagName);
};

const setupIntersectionObserver = () => {
    observer.value = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting && hasMore.value && !isLoading.value) {
            page.value++;
            const selectedTagName = userTags.value.find(tag => tag.id === selectedTag.value)?.name;
            fetchRecommendedBookmarks(selectedTagName);
        }
    });

    const target = document.querySelector('.bookmarks-grid');
    if (target) observer.value.observe(target);
};

const goToBookmarks = () => {
    router.push('/personal-collection');  // 북마크 관리 페이지로 이동
};

onMounted(async () => {
    await fetchUserTags();
    setupIntersectionObserver();
});

onUnmounted(() => {
    if (observer.value) observer.value.disconnect();
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
    margin-top: 60px;
    height: calc(100vh - 60px);
}

.sidebar {
    position: fixed;
    left: 0;
    top: 60px;
    bottom: 0;
    width: 240px;
    background: white;
    z-index: 99;
}

.main-content {
    flex: 1;
    margin-left: 240px;
    overflow-y: auto;
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

.page-header {
    background: linear-gradient(to right, #f8f9fa, #ffffff);
    padding: 16px 24px;
    border-radius: 12px;
    margin-bottom: 24px;
}

.header-content {
    max-width: 800px;
}

.title-section {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 8px;
}

.title-icon {
    font-size: 1.5rem;
    color: #007bff;
}

.title {
    font-size: 1.5rem;
    font-weight: 600;
    color: #2c3e50;
    margin: 0;
}

.description {
    font-size: 0.95rem;
    color: #666;
    margin: 0;
    line-height: 1.4;
}

.tabs-container {
    margin-bottom: 24px;
    overflow-x: hidden;
    width: 100%;
}

.tabs {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
    padding: 4px;
}

.tab {
    padding: 8px 16px;
    border: none;
    border-radius: 20px;
    background-color: #f8f9fa;
    color: #666;
    cursor: pointer;
    transition: all 0.3s ease;
    white-space: nowrap;
    margin: 4px 0;
}

.tab.active {
    background-color: #007bff;
    color: white;
}

.bookmarks-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 24px;
    padding: 20px 0;
}

.loading {
    display: flex;
    justify-content: center;
    padding: 24px;
    font-size: 24px;
    color: #007bff;
}

.no-tags-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 400px;
    margin: 20px 0;
}

.no-tags-content {
    text-align: center;
    padding: 40px;
    background: #f8f9fa;
    border-radius: 12px;
    max-width: 500px;
}

.no-tags-icon {
    font-size: 48px;
    color: #007bff;
    margin-bottom: 20px;
}

.no-tags-content h3 {
    font-size: 1.5rem;
    color: #333;
    margin-bottom: 12px;
}

.no-tags-content p {
    color: #666;
    margin-bottom: 24px;
    line-height: 1.5;
}

.add-tags-button {
    padding: 10px 20px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 6px;
    font-size: 1rem;
    cursor: pointer;
    transition: background-color 0.2s;
}

.add-tags-button:hover {
    background-color: #0056b3;
}
</style>