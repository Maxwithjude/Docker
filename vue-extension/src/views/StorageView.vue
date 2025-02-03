<template>
  <div class="p-4">
    <!-- 설명 텍스트 -->
    <p class="text-sm text-gray-600 mb-6">
      북마크를 저장하려면 컬렉션과 태그를 설정하거나 처음 입력해주세요.
    </p>

    <!-- 컬렉션 선택 -->
    <div class="mb-4">
      <label class="block text-sm font-medium mb-2">컬렉션</label>
      <div class="flex gap-2">
        <select class="flex-1 p-2 border rounded-md">
          <option>Select</option>
        </select>
        <input 
          type="text" 
          placeholder="공용 프로젝트" 
          class="flex-1 p-2 border rounded-md"
        >
      </div>
    </div>

    <!-- 태그 입력 -->
    <div class="mb-6">
      <label class="block text-sm font-medium mb-2">태그</label>
      <div class="flex flex-wrap gap-2 mb-2">
        <span class="bg-red-100 text-red-800 px-2 py-1 rounded-md text-sm flex items-center">
          # Redis
          <button class="ml-1">&times;</button>
        </span>
        <span class="bg-yellow-100 text-yellow-800 px-2 py-1 rounded-md text-sm flex items-center">
          # SMTP
          <button class="ml-1">&times;</button>
        </span>
        <span class="bg-green-100 text-green-800 px-2 py-1 rounded-md text-sm flex items-center">
          # Spring
          <button class="ml-1">&times;</button>
        </span>
      </div>
      <div class="flex gap-2">
        <input 
          type="text" 
          placeholder="Input Value"
          class="flex-1 p-2 border rounded-md"
        >
        <button class="bg-blue-500 text-white px-3 rounded-md">
          +
        </button>
      </div>
    </div>

    <!-- RSS 피드 토글 -->
    <div class="flex items-center justify-between mb-6">
      <div class="flex items-center gap-2">
        <span class="text-green-500">✓</span>
        <span class="text-sm">이 포스트는 RSS 피드 구독이 가능합니다.</span>
      </div>
      <label class="relative inline-flex items-center cursor-pointer">
        <input type="checkbox" class="sr-only peer">
        <div class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
      </label>
    </div>

    <!-- 하단 버튼 -->
    <div class="flex justify-end gap-2">
      <button class="px-4 py-2 border rounded-md">취소</button>
      <button class="px-4 py-2 bg-blue-500 text-white rounded-md">저장</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// 상태 관리
const collection = ref('')
const projectName = ref('')
const newTag = ref('')
const tags = ref(['Redis', 'SMTP', 'Spring'])
const isRssFeed = ref(false)

// 메서드
const addTag = () => {
  if (newTag.value.trim()) {
    tags.value.push(newTag.value.trim())
    newTag.value = ''
  }
}

const removeTag = (index) => {
  tags.value.splice(index, 1)
}

const saveBookmark = () => {
  // API 호출 로직 추가 예정
  console.log('저장 로직 구현 예정')
}
</script>

<style scoped>
/* 설명 텍스트 */
/* .text-sm {
  font-weight: 400; 
} */
</style>