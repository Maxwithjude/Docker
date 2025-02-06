<template>
  <div class="collection-card" @click="$emit('click')">
    <div class="collection-header">
      <h3>{{ collection.name }}</h3>
      <button class="delete-button" @click.stop="$emit('delete', collection.collection_id)">
        <i class="fas fa-trash"></i>
      </button>
    </div>
    <div class="collection-type">
      <i :class="['collection-icon', collection.isPersonal ? 'fas fa-user' : 'fas fa-users']"></i>
      {{ collection.isPersonal ? '개인' : '공유' }} 컬렉션
    </div>
  </div>
</template>

<script setup>
defineProps({
  collection: {
    type: Object,
    required: true
  }
});

defineEmits(['delete', 'click']);
</script>

<style scoped>
.collection-card {
  background: white;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  transition: all 0.3s ease;
  cursor: pointer;
}

.collection-card:hover {
  box-shadow: 0 4px 8px rgba(0,0,0,0.3);
}

.collection-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8px;
}

.collection-header h3 {
  margin: 0;
  font-size: 1.1rem;
}

.delete-button {
  background: none;
  border: none;
  color: #dc3545;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
}

.delete-button:hover {
  background-color: #ffebee;
}

.collection-type {
  font-size: 0.9rem;
  color: #666;
  display: flex;
  align-items: center;
  gap: 6px;
}

.collection-icon {
  color: #888;
}

.fa-user {
  color: #2196F3;  /* 개인 컬렉션 아이콘 색상 */
}

.fa-users {
  color: #4CAF50;  /* 공유 컬렉션 아이콘 색상 */
}
</style>
