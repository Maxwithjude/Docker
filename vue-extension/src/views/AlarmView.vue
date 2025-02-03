<template>
  <div class="p-3">
    <div class="space-y-2">
      <div class="border rounded-md p-4 flex justify-between items-center">
        <div>
          <h2 class="font-semibold mb-2">GOD생 사는 현직자의 경제이야기</h2>
          <p class="text-gray-600">이 포스트를 추가한 지 7일이 지났습니다. 확인해보세요.</p>
        </div>
        <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
    <path stroke-linecap="round" stroke-linejoin="round" d="M15 17h5l-1.405-1.405A2.002 2.002 0 0018 14V8a6 6 0 10-12 0v6c0 .795-.316 1.56-.895 2.095L5 17h5m5 0a2 2 0 11-4 0" />
  </svg>
      </div>

      <div class="border rounded-md p-4 flex justify-between items-center">
        <div>
          <h2 class="font-semibold mb-2">알고리즘 마스터하기</h2>
          <p class="text-gray-600">컬렉션에 초대되었습니다! 지금 바로 확인하고 참여해보세요.</p>
        </div>
        <button class="bg-blue-500 text-white px-3 py-1 rounded-md">수정</button>
      </div>

      <div class="border rounded-md p-4 flex justify-between items-center">
        <div>
          <h2 class="font-semibold mb-2">Redis를 활용한 이메일 인증하기</h2>
          <p class="text-gray-600">이 포스트를 추가한 지 7일이 지났습니다. 확인해보세요.</p>
        </div>
        <button class="bg-blue-500 text-white px-3 py-1 rounded-md">수정</button>
      </div>

      <div class="border rounded-md p-4 flex justify-between items-center">
        <div>
          <h2 class="font-semibold mb-2">안드로이드 스튜디오 시작하기</h2>
          <p class="text-gray-600">이 포스트를 추가한 지 7일이 지났습니다. 확인해보세요.</p>
        </div>
        <button class="bg-blue-500 text-white px-3 py-1 rounded-md">수정</button>
      </div>

      <div class="border rounded-md p-4 flex justify-between items-center">
        <div>
          <h2 class="font-semibold mb-2">오사카 여행 계획</h2>
          <p class="text-gray-600">컬렉션에 초대되었습니다! 지금 바로 확인하고 참여해보세요.</p>
        </div>
        <button class="bg-blue-500 text-white px-3 py-1 rounded-md">수정</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// 상태 관리
const notifications = ref([
  { title: 'GOD생 사는 현직자의 경제이야기', description: '이 포스트는 추가된 지 7일이 지났습니다.' },
  { title: '알고리즘 마스터하기', description: '이 포스트는 추가된 지 7일이 지났습니다.' },
  { title: 'Redis를 활용한 이메일 인증하기', description: '이 포스트는 추가된 지 7일이 지났습니다.' },
  { title: '안드로이드 스튜디오 시작하기', description: '이 포스트는 추가된 지 7일이 지났습니다.' },
  { title: '오사카 여행 계획', description: '이 포스트는 추가된 지 7일이 지났습니다.' },
]);

// 메서드
const editNotification = (index) => {
  // 수정 로직 추가 예정
  console.log(`Editing notification at index: ${index}`);
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