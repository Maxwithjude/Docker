<template>
    <div class="modal-overlay" @click="closeModal">
        <div class="modal-content" @click.stop>
            <div class="modal-header">
                <h2>북마크 저장하기</h2>
                <button class="close-button" @click="closeModal">×</button>
            </div>
            
            <div class="modal-body">
                <div class="collection-select">
                    <p>저장할 개인 컬렉션을 선택하세요.</p>
                    <select v-model="selectedCollection" class="select-input">
                        <option value="" disabled>컬렉션을 선택하세요</option>
                        <option v-for="collection in collections" :key="collection.id" :value="collection.id">
                            {{ collection.name }}
                        </option>
                    </select>
                </div>

                <div class="tags-section">
                    <p>태그</p>
                    <div class="selected-tags">
                        <span 
                            v-for="tag in selectedTags" 
                            :key="tag"
                            class="tag"
                        >
                            # {{ tag }}
                            <button class="remove-tag" @click="removeTag(tag)">×</button>
                        </span>
                    </div>
                    <div class="tag-input-container">
                        <input 
                            v-model="newTag"
                            type="text"
                            class="tag-input"
                            placeholder="태그를 입력하세요"
                            @keyup.enter="addTag"
                        >
                        <button class="add-button" @click="addTag">+</button>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button class="save-button" @click="handleSave">저장</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useCollectionStore } from '@/stores/collection';
import { useBookmarkStore } from '@/stores/bookmark';

const props = defineProps({
    key: {
        type: String,
        required: true
    },
    url: {
        type: String,
        required: true
    },
    title: {
        type: String,
        required: true
    },
    description: {
        type: String,
        required: true
    },
    img: {
        type: String,
        required: true
    }
});

const emit = defineEmits(['close', 'save']);
const collectionStore = useCollectionStore();
const bookmarkStore = useBookmarkStore();

// 컴포넌트가 마운트될 때 컬렉션 데이터를 가져옵니다
await collectionStore.fetchAllCollection();

const selectedCollection = ref('');
// personalCollections 대신 allCollections 사용
const collections = computed(() => collectionStore.allCollections);

const selectedTags = ref([]);
const newTag = ref('');

const addTag = () => {
    if (newTag.value.trim() && !selectedTags.value.includes(newTag.value.trim())) {
        selectedTags.value.push(newTag.value.trim());
        newTag.value = '';
    }
};

const removeTag = (tag) => {
    selectedTags.value = selectedTags.value.filter(t => t !== tag);
};

const closeModal = () => {
    emit('close');
};

const handleSave = async () => {
    try {
        if (!selectedCollection.value) {
            alert('컬렉션을 선택해주세요.');
            return;
        }

        // 선택된 컬렉션이 개인 컬렉션인지 확인
        const collection = collectionStore.allCollections.find(
            c => c.collection_id === selectedCollection.value
        );
        
        // 태그 형식 변환
        const formattedTags = selectedTags.value.map(tag => ({
            tagName: tag,
            tagColor: "#111111" // 기본 색상 설정
        }));

        await bookmarkStore.saveBookmark(
            props.url, // props로 전달받은 URL
            selectedCollection.value,
            collection.isPersonal,
            formattedTags
        );

        emit('save'); // 저장 성공 이벤트 발생
        closeModal();
    } catch (error) {
        console.error('북마크 저장 중 오류 발생:', error);
        alert('북마크 저장에 실패했습니다.');
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
    border-radius: 8px;
    width: 90%;
    max-width: 500px;
    padding: 20px;
}

.modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
}

.modal-header h2 {
    margin: 0;
    font-size: 1.5rem;
}

.close-button {
    background: none;
    border: none;
    font-size: 1.5rem;
    cursor: pointer;
    padding: 0;
}

.select-input {
    width: 100%;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 4px;
    margin-top: 8px;
}

.tags-section {
    margin-top: 20px;
}

.selected-tags {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    margin: 10px 0;
}

.tag {
    display: flex;
    align-items: center;
    padding: 4px 8px;
    background: #e9ecef;
    border-radius: 16px;
    font-size: 0.9rem;
}

.tag.redis {
    background: #e3f2fd;
}

.tag.smtp {
    background: #fff3e0;
}

.tag.spring {
    background: #fce4ec;
}

.remove-tag {
    background: none;
    border: none;
    margin-left: 4px;
    cursor: pointer;
    padding: 0 4px;
}

.tag-input-container {
    display: flex;
    gap: 8px;
}

.tag-input {
    flex: 1;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
}

.add-button {
    background: #4285f4;
    color: white;
    border: none;
    border-radius: 4px;
    padding: 0 16px;
    cursor: pointer;
}

.modal-footer {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
}

.save-button {
    background: #4285f4;
    color: white;
    border: none;
    border-radius: 4px;
    padding: 8px 24px;
    cursor: pointer;
}

.save-button:hover {
    background: #3367d6;
}
</style>
