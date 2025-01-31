<template>
  <div class="collection-container" :class="{ 'personal': type === 'personal', 'shared': type === 'shared' }">
    <div class="collection-header">
      <h2>{{ title }}</h2>
    </div>
  </div>
</template>

<script setup>
defineProps({
  title: { 
    type: String,
    required: true
  },
  type: {
    type: String,
    required: true
  }
});
</script>

<style scoped>
.collection-container {
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 16px;
  background-color: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.personal {
  border-left: 4px solid #4CAF50;  /* 초록색 */
}

.shared {
  border-left: 4px solid #2196F3;  /* 파란색 */
}

.collection-header {
  margin-bottom: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.collection-header h2 {
  margin: 0;
  color: #333;
  font-size: 1.5rem;
}

.collection-content {
  color: #666;
}
</style>
