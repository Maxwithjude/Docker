<template>
  <el-popover
    placement="bottom-end"
    :width="200"
    trigger="click"
    popper-class="bookmark-settings-popover"
    v-model:visible="isVisible"
  >
    <template #reference>
      <slot name="reference"></slot>
    </template>
    
    <div class="popover-content">
      <el-button-group vertical class="settings-menu">
        <el-button type="text" @click="toggleImportant">
          {{ isImportant ? '중요 북마크 해제' : '중요 북마크로 설정' }}
        </el-button>
        
        <el-button type="text" @click="copyToSharedCollection">
          공유 컬렉션으로 복사
        </el-button>
        
        <el-button type="text" @click="showMoveDialog">
          다른 컬렉션으로 이동
        </el-button>
        
        <el-button type="text" @click="showTagManagement">
          태그 관리
        </el-button>
        
        <el-button type="text" class="delete-button" @click="confirmDelete">
          북마크 삭제
        </el-button>
      </el-button-group>
    </div>
  </el-popover>
</template>

<script setup>
import { ref } from 'vue';

const isVisible = ref(false);
const isImportant = ref(false);

const toggleImportant = () => {
  isImportant.value = !isImportant.value;
  // TODO: API 호출하여 중요 상태 업데이트
};

const copyToSharedCollection = () => {
  // TODO: 공유 컬렉션 선택 다이얼로그 표시
};

const showMoveDialog = () => {
  // TODO: 이동할 컬렉션 선택 다이얼로그 표시
};

const showTagManagement = () => {
  // TODO: 태그 관리 다이얼로그 표시
};

const confirmDelete = () => {
  ElMessageBox.confirm(
    '이 북마크를 삭제하시겠습니까?',
    '경고',
    {
      confirmButtonText: '삭제',
      cancelButtonText: '취소',
      type: 'warning',
    }
  ).then(() => {
    // TODO: 북마크 삭제 API 호출
    ElMessage({
      type: 'success',
      message: '북마크가 삭제되었습니다.',
    });
  }).catch(() => {});
};
</script>

<style scoped>
.bookmark-actions {
  position: absolute;
  top: 8px;
  right: 8px;
}

.settings-button {
  padding: 4px;
  color: #909399;
}

.settings-button:hover {
  color: #409EFF;
}

.settings-menu {
  width: 100%;
}

.settings-menu .el-button {
  justify-content: flex-start;
  padding: 8px 16px;
  width: 100%;
}

.settings-menu .el-button:hover {
  background-color: #f5f7fa;
}

.settings-menu .delete-button {
  color: #f56c6c;
}

:deep(.bookmark-settings-popover) {
  padding: 0;
}
</style>
