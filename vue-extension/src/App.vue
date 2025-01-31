<template>
  <div class="container">
    <nav>
      <RouterLink :to="{ name: 'storage' }">저장</RouterLink>
      <RouterLink :to="{ name: 'alarm' }">알림</RouterLink>
      <RouterLink :to="{ name: 'feed' }">피드</RouterLink>
    </nav>
    <RouterView />
  </div>
</template>

<script setup>
import { RouterView } from "vue-router";
</script>

<style scoped>
.container {
  width: 400px;
  min-height: 400px;
  padding: 16px;
  border-radius: 12px;
}

nav {
  display: flex;
  position: relative;
  margin-bottom: 16px;
  text-align: center;
}

/* 하단 언더라인 */
nav::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 1px;
  background-color: #eee;
}

nav a {
  flex: 1;
  text-decoration: none;
  color: #666;
  padding: 8px 16px;
  text-align: center;
  position: relative;
  z-index: 1;
  transition: background-color 0.2s ease; /* hover 애니메이션 효과 */
}

nav a.router-link-active {
  color: #4169e1;
  font-weight: bold;
  background-color: transparent; /* 활성 탭의 배경색 제거 */
  border-radius: 4px 4px 0 0;
}

/* 활성 탭의 하단 파란색 라인 */
nav a.router-link-active::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 2px;
  background-color: #4169e1;
  z-index: 2;
}
</style>
