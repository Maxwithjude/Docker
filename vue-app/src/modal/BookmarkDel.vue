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
const emit = defineEmits(['close', 'confirm'])

const handleDelete = () => {
  emit('confirm')
  emit('close')
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
