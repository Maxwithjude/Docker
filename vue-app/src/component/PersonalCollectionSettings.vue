<template>
    <div class="modal-overlay" @click="closeModal">
        <div class="modal-content" @click.stop>
            <div class="modal-header">
                <h2>컬렉션 이름 변경</h2>
                <button class="close-button" @click="closeModal">×</button>
            </div>
            
            <div class="modal-body">
                <div class="input-section">
                    <input 
                        v-model="newCollectionName"
                        type="text"
                        class="name-input"
                        placeholder="새로운 컬렉션 이름을 입력하세요"
                        @keyup.enter="handleComplete"
                    >
                </div>
            </div>

            <div class="modal-footer">
                <button 
                    class="complete-button" 
                    @click="handleComplete"
                    :disabled="!isValidName"
                >
                    완료
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useCollectionStore } from '@/stores/collection';

const collectionStore = useCollectionStore();

const props = defineProps({
    currentName: {
        type: String,
        required: true
    },
    collectionId: {
        type: Number,
        required: true
    }
});

const emit = defineEmits(['close', 'update']);

const newCollectionName = ref(props.currentName);

const isValidName = computed(() => {
    return newCollectionName.value.trim().length > 0;
});

const closeModal = () => {
    emit('close');
};

const handleComplete = async () => {
    if (isValidName.value) {
        try {
            await collectionStore.updatePersonalCollectionName(
                props.collectionId, 
                newCollectionName.value.trim()
            );
            emit('update', newCollectionName.value.trim());
            closeModal();
        } catch (error) {
            console.error('컬렉션 이름 변경 실패:', error);
            // 여기에 에러 처리 로직 추가 (예: 사용자에게 알림 표시)
        }
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

.modal-content {
    background: white;
    border-radius: 12px;
    width: 90%;
    max-width: 400px;
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

.input-section {
    margin-bottom: 24px;
}

.name-input {
    width: 100%;
    padding: 12px;
    border: 1px solid #ddd;
    border-radius: 8px;
    font-size: 1rem;
    outline: none;
    transition: border-color 0.2s;
}

.name-input:focus {
    border-color: #0066ff;
}

.modal-footer {
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
    transition: background-color 0.2s;
}

.complete-button:hover:not(:disabled) {
    background: #0052cc;
}

.complete-button:disabled {
    background: #cccccc;
    cursor: not-allowed;
}
</style>
