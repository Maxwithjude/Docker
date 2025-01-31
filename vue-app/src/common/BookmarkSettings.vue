<template>
  <el-popover
    placement="bottom-end"
    :width="200"
    trigger="click"
    popper-class="bookmark-settings-popover"
    v-model:visible="isVisible"
  >
    <template #reference>
      <button class="settings-button">⋮</button>
    </template>
    
    <div class="popover-content">
      <el-button-group vertical class="settings-menu">
        <el-button link @click="toggleImportant">
          {{ isImportant ? '중요 북마크 해제' : '중요 북마크로 설정' }}
        </el-button>
        
        <el-button link @click="copyToSharedCollection">
          공유 컬렉션으로 복사
        </el-button>
        
        <el-button link @click="showMoveDialog">
          다른 컬렉션으로 이동
        </el-button>
        
        <el-button link @click="showTagManagement">
          태그 관리
        </el-button>
        
        <el-button link class="delete-button" @click="openDeleteModal">
          북마크 삭제
        </el-button>
      </el-button-group>
    </div>
  </el-popover>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'

const emit = defineEmits(['delete'])
const props = defineProps({
  isImportant: {
    type: Boolean,
    default: false
  }
})

const isVisible = ref(false)

const toggleImportant = () => {
  // TODO: Implement toggle important
}

const copyToSharedCollection = () => {
  // TODO: Implement copy to shared collection
}

const showMoveDialog = () => {
  // TODO: Implement move dialog
}

const showTagManagement = () => {
  // TODO: Implement tag management
}

const openDeleteModal = () => {
  isVisible.value = false
  emit('delete')
}
</script>

<style scoped>
.settings-button {
  background: none;
  border: none;
  font-size: 1.2rem;
  color: #666;
  cursor: pointer;
  padding: 4px 8px;
  border-radius: 4px;
}

.settings-button:hover {
  background-color: #f0f0f0;
}

.settings-menu {
  width: 100%;
}

.settings-menu .el-button {
  justify-content: flex-start;
  padding: 8px 16px;
  width: 100%;
}

.settings-menu .delete-button {
  color: #f56c6c;
}

:deep(.bookmark-settings-popover) {
  padding: 0;
}
</style>
