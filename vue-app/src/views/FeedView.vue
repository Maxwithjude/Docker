<template>
    <div class="layout">
        <Header class="header"/>
        <div class="content-wrapper">
            <SideBar class="sidebar"/>
            <div class="main-content">
                <div class="body">
                    <div class="rss-container">
                        <FeedTabs 
                            :feeds="rssStore.rssSubscriptions.results"
                            :selected-feed="selectedFeed"
                            @select-feed="selectFeed"
                        />
                        <div class="content-container">
                            <FeedPostList 
                                :posts="currentRssItems?.latest_posts"
                                @select-post="selectPost"
                            />
                            <FeedPostContent :url="selectedPostUrl" />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import Header from '@/common/Header.vue'
import SideBar from '@/common/SideBar.vue'
import FeedTabs from '@/component/FeedTabs.vue'
import FeedPostList from '@/component/FeedPostList.vue'
import FeedPostContent from '@/component/FeedPostContent.vue'
import { ref, onMounted } from 'vue'
import { useCounterStore } from '@/stores/counter'

const rssStore = useCounterStore()
const selectedFeed = ref(1)
const selectedPostUrl = ref(null)
const currentRssItems = ref(null)

// RSS 피드 아이템 로드
const loadRssItems = async (rssId) => {
  try {
    const data = await rssStore.getRssSubscriptionItems(rssId)
    currentRssItems.value = data.results
  } catch (error) {
    console.error('RSS 아이템 로드 실패:', error)
  }
}

// 피드 선택 시 해당 피드의 아이템 로드
const selectFeed = async (rssId) => {
  selectedFeed.value = rssId
  await loadRssItems(rssId)
}

// 컴포넌트 마운트 시 초기 데이터 로드
onMounted(async () => {
  await loadRssItems(selectedFeed.value)
})

const selectPost = (url) => {
  selectedPostUrl.value = url
}
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

.rss-container {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.content-container {
  display: flex;
  flex: 1;
  gap: 1rem;
  padding: 1rem;
}
</style>