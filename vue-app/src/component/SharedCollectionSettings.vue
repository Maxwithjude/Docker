<template>
  <div class="modal-overlay" @click.stop>
    <div class="modal">
      <!-- 컬렉션 이름변경 영역 -->
      <div class="modal-header">
        <h2>공유 컬렉션 설정</h2>
        <button class="close-btn" @click.stop="$emit('close')">×</button>
      </div>

      <!-- 이름 입력 영역 -->
      <div class="name-section">
        <h3>이름 변경</h3>
        <div class="name-input-section">
          <input 
            type="text" 
            v-model="collectionName"
            placeholder="컬렉션 이름을 입력하세요"
            class="name-input"
          />
          <button class="change-btn" @click="changeCollectionName">변경</button>
        </div>
      </div>

      <!-- 공유 컬렉션 멤버 설정 영역 -->
      <div class="members-section">
        <h2>공유 컬렉션 멤버 설정</h2>
        <div class="email-input-wrapper">
          <input 
            type="email" 
            v-model="newMemberEmail"
            placeholder="초대할 사람의 이메일 정보를 입력해주세요"
            class="email-input"
          />
          <button class="add-btn" @click="addMember">
            <i class="fas fa-plus"></i>
          </button>
        </div>

        <!-- 멤버 목록 -->
        <div class="members-list">
          <h4>Members</h4>
          <div v-for="member in members" :key="member.id" class="member-item">
            <div class="member-info">
              <i class="fas fa-user member-icon"></i>
              <span>{{ member.name }}</span>
            </div>
            <button class="remove-btn" @click="removeMember(member.id)">×</button>
          </div>
        </div>
      </div>

      <!-- 하단 버튼 -->
      <div class="modal-footer">
        <!-- 완료 버튼 제거 -->
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from 'axios';

const collectionName = ref('');
const newMemberEmail = ref('');
const members = ref([
  { id: 1, name: '이은지' },
  { id: 2, name: '김진영' },
  { id: 3, name: '박태현' }
]);

const emit = defineEmits(['close']);

// 이름 변경 함수
const changeCollectionName = async () => {
  try {
    await axios.put('/api/collections/name', {
      name: collectionName.value
    });
    alert('컬렉션 이름이 변경되었습니다.');
  } catch (error) {
    alert('이름 변경 중 오류가 발생했습니다.');
    console.error(error);
  }
};

// 멤버 추가 함수
const addMember = async () => {
  try {
    await axios.post('/api/collections/members', {
      email: newMemberEmail.value
    });
    newMemberEmail.value = ''; // 입력 필드 초기화
    // TODO: 멤버 목록 새로고침 로직 추가
    alert('멤버가 추가되었습니다.');
  } catch (error) {
    alert('멤버 추가 중 오류가 발생했습니다.');
    console.error(error);
  }
};

// 멤버 삭제 함수
const removeMember = async (memberId) => {
  try {
    await axios.delete(`/api/collections/members/${memberId}`);
    // TODO: 멤버 목록 새로고침 로직 추가
    alert('멤버가 삭제되었습니다.');
  } catch (error) {
    alert('멤버 삭제 중 오류가 발생했습니다.');
    console.error(error);
  }
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal {
  background: white;
  padding: 24px;
  border-radius: 12px;
  width: 480px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.modal-header h2 {
  font-size: 20px;
  font-weight: 600;
  margin: 0;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #666;
}

.name-section {
  margin-bottom: 24px;
}

.name-section h3 {
  font-size: 16px;
  margin-bottom: 16px;
}

.name-input-section {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
}

.name-input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.change-btn {
  padding: 8px 16px;
  background: #1a73e8;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.members-section {
  margin-bottom: 24px;
}

.members-section h3 {
  font-size: 16px;
  margin-bottom: 16px;
}

.email-input-wrapper {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
}

.email-input {
  flex: 1;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
}

.add-btn {
  padding: 8px;
  background: #1a73e8;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.members-list {
  background: #f8f9fa;
  padding: 16px;
  border-radius: 8px;
}

.members-list h4 {
  margin: 0 0 12px 0;
  font-size: 14px;
  color: #666;
}

.member-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
}

.member-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.member-icon {
  color: #666;
}

.remove-btn {
  background: none;
  border: none;
  color: #666;
  cursor: pointer;
  font-size: 18px;
}

.modal-footer {
  text-align: center;
}

button:hover {
  opacity: 0.9;
}
</style>
