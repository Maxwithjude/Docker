<template>
  <div class="collection-card" @click="!showSharedSettings && !showPersonalSettings && $emit('click')">
    <div class="collection-header">
      <h3>{{ collection.name }}</h3>
      <div class="header-buttons">
        <button class="settings-button" @click.stop="openSettings">
          <i class="fas fa-cog"></i>
        </button>
        <button class="delete-button" @click.stop="handleDelete">
          <i class="fas fa-trash"></i>
        </button>
      </div>
    </div>
    <div class="collection-type">
      <i :class="['collection-icon', collection.isPersonal ? 'fas fa-user' : 'fas fa-users']"></i>
      {{ collection.isPersonal ? '개인' : '공유' }} 컬렉션
    </div>

    <SharedCollectionSettings 
      v-if="showSharedSettings && !collection.isPersonal" 
      :current-name="collection.name"
      :collection-id="collection.collectionId
      "
      @close="showSharedSettings = false"
      @update="handleNameUpdate"
    />
    <PersonalCollectionSettings 
      v-if="showPersonalSettings && collection.isPersonal"
      :current-name="collection.name"
      :collection-id="collection.collectionId"
      @close="showPersonalSettings = false"
      @update="handleNameUpdate"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useCollectionStore } from '@/stores/collection';
import SharedCollectionSettings from '../component/SharedCollectionSettings.vue';
import PersonalCollectionSettings from '../component/PersonalCollectionSettings.vue';

const showSharedSettings = ref(false);
const showPersonalSettings = ref(false);

const props = defineProps({
  collection: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['delete', 'click', 'update']);

const collectionStore = useCollectionStore();

const openSettings = () => {
  if (props.collection.isPersonal) {
    showPersonalSettings.value = true;
  } else {
    showSharedSettings.value = true;
  }
};

const handleNameUpdate = (newName) => {
  emit('update', {
    collectionId: props.collection.collectionId,
    newName: newName
  });
};

const handleDelete = async () => {
  try {
    console.log("삭제하려는 컬렉션:", props.collection);
    console.log("컬렉션 ID:", props.collection.collectionId);
    
    if (props.collection.isPersonal) {
      await collectionStore.deletePersonalCollection(props.collection.collectionId);
    } else {
      await collectionStore.deleteSharedCollection(props.collection.collectionId);
    }
    
    await collectionStore.fetchAllCollection();
    emit('delete', props.collection.collectionId);
  } catch (error) {
    console.error('컬렉션 삭제 중 오류 발생:', error);
  }
};
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

.header-buttons {
  display: flex;
  gap: 8px;
  align-items: center;
}

.settings-button {
  background: none;
  border: none;
  color: #666;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
}

.settings-button:hover {
  background-color: #f5f5f5;
  color: #1a73e8;
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
