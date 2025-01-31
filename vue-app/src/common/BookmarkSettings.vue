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

  <el-dialog
    v-model="showCopyModal"
    :modal="true"
    :show-close="true"
    destroy-on-close
    append-to-body
    class="bookmark-copy-dialog"
  >
    <BookmarkCopyShared @close="showCopyModal = false" />
  </el-dialog>

  <el-dialog
    v-model="showMoveModal"
    :modal="true"
    :show-close="true"
    destroy-on-close
    append-to-body
    class="bookmark-move-dialog"
  >
    <BookmarkMovePersonal 
      :bookmark-id="bookmarkId"
      @close="showMoveModal = false"
      @move-complete="handleMoveComplete"
    />
  </el-dialog>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import BookmarkCopyShared from '@/modal/BookmarkCopyShared.vue'
import BookmarkMovePersonal from '@/modal/BookmarkMovePersonal.vue'

const emit = defineEmits(['delete'])
const props = defineProps({
  isImportant: {
    type: Boolean,
    default: false
  }
})

const isVisible = ref(false)
const showCopyModal = ref(false)
const showMoveModal = ref(false)

const toggleImportant = () => {
  // TODO: Implement toggle important
}

const copyToSharedCollection = () => {
  isVisible.value = false
  showCopyModal.value = true
}

const showMoveDialog = () => {
  isVisible.value = false
  showMoveModal.value = true
}

const showTagManagement = () => {
  // TODO: Implement tag management
}

const openDeleteModal = () => {
  isVisible.value = false
  emit('delete')
}

const handleMoveComplete = () => {
  ElMessage.success('북마크가 성공적으로 이동되었습니다.')
  // TODO: 필요한 경우 부모 컴포넌트에 이동 완료 이벤트 발생
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

:deep(.bookmark-copy-dialog) {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

:deep(.bookmark-copy-dialog .el-dialog) {
  margin: 0 !important;
  width: 500px;
}

:deep(.bookmark-move-dialog .el-dialog) {
  margin: 0 !important;
  width: 500px;
}
</style>
