<template>
  <div class="p-3">
    <!-- 가이드라인 -->
    <div class="guide-text-with-icon mb-5">
      <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4">
        <path stroke-linecap="round" stroke-linejoin="round" d="M11.25 11.25l.041-.02a.75.75 0 011.063.852l-.708 2.836a.75.75 0 001.063.853l.041-.021M21 12a9 9 0 11-18 0 9 9 0 0118 0zm-9-3.75h.008v.008H12V8.25z" />
      </svg>
      <span class="guide-text">북마크를 저장하려면 컬렉션과 태그를 선택하거나 직접 입력해주세요.</span>
    </div>

    <!-- 컬렉션 선택 -->
    <div class="mb-4">
      <label class="block text-sm font-medium mb-2 mt-5">컬렉션</label>
      <div class="flex gap-2">
        <select class="flex-1 p-2 border rounded-md focus:outline-none focus:border-gray-400">
          <option value="" disabled selected>컬렉션 선택</option>
          <option value="" >공통프로젝트</option>
          <option value="" >A208</option>
        </select>
        <input 
          type="text" 
          placeholder="새로운 컬렉션 만들기" 
          class="flex-1 p-2 border rounded-md focus:outline-none focus:border-gray-400"
        >
      </div>
    </div>

    <!-- 태그 선택 -->
    <div class="mb-4">
    <label class="block text-sm font-medium mb-2">태그</label>
    <div class="flex flex-wrap gap-2 mb-2" id="tag-container">
      <span v-for="(tag, index) in tags" :key="index" :class="tagColors[index % tagColors.length] + ' px-3 py-1 rounded-full text-sm flex items-center'">
        {{ '# ' + tag }} <!-- '#' 기호 추가 -->
        <button class="ml-2 text-gray-600 hover:text-gray-800" @click="removeTag(index)">&times;</button>
      </span>
    </div>
    <div class="flex gap-2">
      <input 
        type="text" 
        v-model="newTag" 
        placeholder="북마크를 설명할 태그를 입력해보세요."
        class="flex-1 p-2 border rounded-md focus:outline-none focus:border-gray-400"
      >
      <button 
      class="flex items-center justify-center bg-blue-500 text-white w-10 h-10 rounded-md hover:bg-blue-600 transition-colors duration-200" 
      @click="addTag"
    >
        <span class="text-lg font-bold">+</span>
      </button>
    </div>
  </div>
  
    <!-- RSS 피드 구독 선택 -->
    <label class="block text-sm font-medium mb-2">RSS 피드 구독</label>
    <div class="flex items-center justify-between mb-4">
      <div :class="isRssFeed ? 'flex items-center gap-2 bg-blue-50 text-blue-700 px-3 py-2 rounded-md' : 'flex items-center gap-2 bg-red-100 text-red-700 px-3 py-2 rounded-md'">
        <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-4 h-4">
          <path stroke-linecap="round" stroke-linejoin="round" d="M12.75 19.5v-.75a7.5 7.5 0 00-7.5-7.5H4.5m0-6.75h.75c7.87 0 14.25 6.38 14.25 14.25v.75M6 18.75a.75.75 0 11-1.5 0 .75.75 0 011.5 0z" />
        </svg>
        <span class="text-sm">
          {{ isRssFeed ? '이 포스트는 RSS 피드 구독이 가능합니다.' : '이 포스트는 RSS 피드 구독이 불가능합니다.' }}
        </span>
      </div>
      <label class="relative inline-flex items-center cursor-pointer">
        <input type="checkbox" class="sr-only peer" v-model="isRssFeed">
        <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
      </label>
    </div>

    <!-- 저장 버튼 -->
    <div class="flex justify-end">
      <button 
        @click="saveBookmark"
        class="px-6 py-2 bg-gradient-to-r from-blue-500 to-blue-600 text-white rounded-md hover:from-blue-600 hover:to-blue-700 transition-all duration-200 flex items-center gap-2"
      >
        <span>저장</span>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/userStore'
import axios from 'axios'

const userStore = useUserStore()
const url = ref('')

onMounted(async () => {
  try {
    // background.js에서 URL 정보 가져오기
    chrome.runtime.sendMessage({ action: 'getCurrentUrl' }, async (response) => {
      if (response && response.url) {
        url.value = response.url
        
        // userId가 있을 때만 서버에 요청
        if (userStore.userId) {
          const response = await axios.post('API_URL/storage/init', {
            userId: userStore.userId,
            url: url.value
          })
          
          // 응답 데이터 처리
          // ...
        }
      }
    })
  } catch (error) {
    console.error('데이터 로드 실패:', error)
  }
})

// 상태 관리
const newTag = ref('')
const tags = ref(['Spring', 'Redis']) // GPT 태그 배열

// 태그 색상 배열
const tagColors = [
  'border border-green-500 bg-green-50 text-green-800',
  'border border-yellow-500 bg-yellow-50 text-yellow-800',
  'border border-blue-500 bg-blue-50 text-blue-800',
  'border border-red-500 bg-red-50 text-red-800',
  'border border-purple-500 bg-purple-50 text-purple-800',
];

// 메서드
const addTag = () => {
  if (newTag.value.trim()) {
    tags.value.push(newTag.value.trim());
    newTag.value = '';
  }
}

const removeTag = (index) => {
  tags.value.splice(index, 1);
}

const saveBookmark = async () => {
  try {
    // 북마크 저장 API 호출 로직
    
    // 저장 성공 후 창 닫기
    window.close()
  } catch (error) {
    console.error('저장 실패:', error)
    // 에러 처리
  }
}
</script>

<style scoped>
/* 스타일 1: 심플한 회색 텍스트 */
.guide-text-simple {
  color: #666;
  font-size: 13px;
  line-height: 1.4;
  margin: -4px 0 12px;
}

/* 스타일 2: 정보 아이콘이 있는 스타일 */
.guide-text-with-icon {
  display: flex;
  align-items: center;
  gap: 6px;
  margin: -4px 0 12px;
}

.guide-text-with-icon svg {
  color: #1a73e8;
  flex-shrink: 0;
  transform: translateY(-1px);
  width: 16px;
  height: 16px;
}

.guide-text {
  color: #5f6368;
  font-size: 13px;
  line-height: 1.4;
}

/* 스타일 3: 연한 배경색이 있는 스타일 */
.guide-text-with-bg {
  background-color: #f8f9fa;
  border-radius: 4px;
  padding: 8px 12px;
  margin: -4px 0 12px;
}

.guide-text-with-bg p {
  color: #5f6368;
  font-size: 13px;
  line-height: 1.4;
  margin: 0;
}
</style>