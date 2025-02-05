<template>
    <div class="post-content">
      <iframe v-if="url" :src="url" frameborder="0"></iframe>
      <div v-else class="no-content">
        게시물을 선택해주세요
      </div>
    </div>
  </template>
  
  <script setup>
  defineProps({
    url: {
      type: String,
      default: null
    }
  })
  </script>
  
  <style scoped>
  .post-content {
    flex: 1;
    min-height: 500px;
  }
  
  iframe {
    width: 100%;
    height: 100%;
    border: none;
  }
  
  .no-content {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    color: #666;
  }
  </style>