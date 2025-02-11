<template>
  <div class="bookmark-delete-modal">
    <h2 class="modal-title">북마크 삭제</h2>
    <p class="warning-text">삭제된 북마크는 되돌릴 수 없습니다.</p>
    <div class="button-group">
      <el-button @click="$emit('close')">아니오</el-button>
      <el-button type="danger" @click="handleDelete">예, 삭제하겠습니다</el-button>
    </div>
  </div>
</template>

<script setup>
import { useBookmarkStore } from '@/stores/bookmark'
import { ElMessage } from 'element-plus'

const emit = defineEmits(['close', 'confirm'])
const bookmarkStore = useBookmarkStore()
const props = defineProps({
  bookmarkId: {
    type: Number,
    required: true
  }
})

const handleDelete = async () => {
  try {
    const success = await bookmarkStore.deleteBookmark(props.bookmarkId)
    if (success) {
      ElMessage.success('북마크가 삭제되었습니다')
      emit('confirm')
      emit('close')
    } else {
      ElMessage.error('북마크 삭제에 실패했습니다')
    }
  } catch (error) {
    console.error('북마크 삭제 중 오류 발생:', error)
    ElMessage.error('북마크 삭제에 실패했습니다')
  }
}
</script>

<style scoped>
.bookmark-delete-modal {
  padding: 20px;
  text-align: center;
}

.modal-title {
  font-size: 20px;
  margin-bottom: 16px;
  color: #303133;
}

.warning-text {
  color: #606266;
  margin-bottom: 24px;
}

.button-group {
  display: flex;
  justify-content: center;
  gap: 12px;
}
</style>
