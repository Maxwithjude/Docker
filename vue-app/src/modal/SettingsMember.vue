<template>
    <div class="modal-overlay" @click="closeModal">
        <div class="modal-content" @click.stop>
            <div class="modal-header">
                <h2>공유 컬렉션 멤버 설정</h2>
                <button class="close-button" @click="closeModal">×</button>
            </div>
            
            <div class="modal-body">
                <div class="member-section">
                    <h3>멤버 초대</h3>
                    <div class="invite-input-container">
                        <input 
                            type="text"
                            class="invite-input"
                            placeholder="이메일을 입력하세요"
                        >
                        <button class="add-button">
                            <span>+</span>
                        </button>
                    </div>
                </div>

                <div class="member-list-section">
                    <h3>멤버 목록</h3>
                    <div class="member-list">
                        <div v-for="member in members" :key="member.id" class="member-item">
                            <div class="member-info">
                                <span class="member-icon">👤</span>
                                <span class="member-name">{{ member.name }}</span>
                            </div>
                            <button class="remove-button" @click="removeMember(member.id)">
                                <span>-</span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button class="complete-button" @click="handleComplete">완료</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useCollectionStore } from '@/stores/collection'
import { storeToRefs } from 'pinia'
import { ElMessage } from 'element-plus'

const emit = defineEmits(['close'])
const collectionStore = useCollectionStore()
const { members } = storeToRefs(collectionStore)

const props = defineProps({
  collectionId: {
    type: String,
    required: true
  }
})

onMounted(async () => {
  try {
    await collectionStore.fetchCollectionMembers(props.collectionId)
  } catch (error) {
    console.error('멤버 목록 로드 실패:', error)
    ElMessage.error('멤버 목록을 불러오는데 실패했습니다')
  }
})

const removeMember = async (memberId) => {
  try {
    await collectionStore.removeMember(props.collectionId, memberId)
    ElMessage.success('멤버가 삭제되었습니다')
  } catch (error) {
    console.error('멤버 삭제 실패:', error)
    ElMessage.error('멤버 삭제에 실패했습니다')
  }
}

const closeModal = () => {
    emit('close');
};

const handleComplete = () => {
    // 완료 로직 구현
    closeModal();
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

.modal-content {
    background: white;
    border-radius: 12px;
    width: 90%;
    max-width: 500px;
    padding: 24px;
}

.modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
}

.modal-header h2 {
    margin: 0;
    font-size: 1.5rem;
    font-weight: bold;
}

.close-button {
    background: none;
    border: none;
    font-size: 1.5rem;
    cursor: pointer;
    padding: 0;
}

.member-section {
    margin-bottom: 24px;
}

.member-section h3 {
    margin-bottom: 12px;
    font-size: 1.1rem;
}

.invite-input-container {
    display: flex;
    gap: 8px;
}

.invite-input {
    flex: 1;
    padding: 12px;
    border: 1px solid #ddd;
    border-radius: 8px;
    font-size: 1rem;
}

.add-button {
    width: 40px;
    height: 40px;
    background: #0066ff;
    border: none;
    border-radius: 50%;
    color: white;
    font-size: 1.5rem;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
}

.member-list-section h3 {
    margin-bottom: 12px;
    font-size: 1.1rem;
}

.member-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

.member-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px;
    background: #f8f9fa;
    border-radius: 8px;
}

.member-info {
    display: flex;
    align-items: center;
    gap: 8px;
}

.member-icon {
    font-size: 1.2rem;
}

.member-name {
    font-size: 1rem;
}

.remove-button {
    width: 24px;
    height: 24px;
    background: #dc3545;
    border: none;
    border-radius: 50%;
    color: white;
    font-size: 1rem;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
}

.modal-footer {
    margin-top: 24px;
    display: flex;
    justify-content: center;
}

.complete-button {
    background: #0066ff;
    color: white;
    border: none;
    border-radius: 24px;
    padding: 12px 48px;
    font-size: 1rem;
    cursor: pointer;
}

.complete-button:hover {
    background: #0052cc;
}
</style>
