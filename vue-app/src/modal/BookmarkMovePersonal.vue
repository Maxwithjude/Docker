<template>
  <div class="bookmark-move-modal">
    <h2 class="modal-title">북마크 이동</h2>
    
    <div class="select-container">
      <label for="collection-select">개인 컬렉션 선택:</label>
      <select 
        id="collection-select" 
        v-model="selectedCollectionId"
        class="collection-select"
      >
        <option value="" selected>컬렉션을 선택해주세요</option>
        <option 
          v-for="collection in personalCollections" 
          :key="collection.id" 
          :value="collection.id"
        >
          {{ collection.name }}
        </option>
      </select>
    </div>

    <div class="modal-actions">
      <button @click="$emit('close')" class="btn-cancel">취소</button>
      <button 
        @click="moveBookmark" 
        :disabled="!selectedCollectionId"
        class="btn-move"
      >
        이동
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  bookmarkId: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['close', 'moveComplete'])

const personalCollections = ref([]) // API로부터 받아올 개인 컬렉션 목록
const selectedCollectionId = ref('')

const selectCollection = (collectionId) => {
  selectedCollectionId.value = collectionId
}

const moveBookmark = async () => {
  try {
    // TODO: API 호출하여 북마크 이동 처리
    // await moveBookmarkToCollection(props.bookmarkId, selectedCollectionId.value)
    emit('moveComplete')
    emit('close')
  } catch (error) {
    console.error('북마크 이동 중 오류 발생:', error)
  }
}
</script>

<style scoped>
.bookmark-move-modal {
  padding: 20px;
}

.modal-title {
  font-size: 1.5rem;
  margin-bottom: 20px;
}

.select-container {
  margin-bottom: 20px;
}

.collection-select {
  width: 100%;
  padding: 8px;
  margin-top: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.btn-cancel, .btn-move {
  padding: 8px 16px;
  border-radius: 4px;
  cursor: pointer;
}

.btn-cancel {
  background-color: #f5f5f5;
  border: 1px solid #ddd;
}

.btn-move {
  background-color: #4CAF50;
  color: white;
  border: none;
}

.btn-move:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}
</style>