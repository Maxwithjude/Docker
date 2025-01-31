<template>
    <div class="card">
        <div class="card-header">
            <div class="importance">
                <span v-if="props.isImportant" class="star-icon">★</span>
                <span v-else class="star-icon empty">☆</span>
            </div>
            <button class="settings-button" @click="showSettings">
                ⋮
            </button>
        </div>
        <img 
            :src="props.image || defaultImage" 
            :alt="props.title" 
            class="card-image"
        >
        <div class="card-content">
            <h2 class="card-title">{{ props.title }}</h2>
            <p class="card-description">{{ props.description }}</p>
            
            <div class="card-url">
                {{ props.url }}
            </div>
            
            <div class="card-footer">
                <div class="tags-container">
                    <span 
                        v-for="tag in visibleTags" 
                        :key="tag"
                        class="tag"
                    >
                        #{{ tag }}
                    </span>
                    <span 
                        v-if="remainingTagsCount > 0" 
                        class="remaining-count"
                    >
                        +{{ remainingTagsCount }}
                    </span>
                </div>
                <span class="read-time">
                    {{ props.readingTime }}분
                </span>
            </div>
        </div>

        <BookmarkSettings
            v-if="isSettingsVisible"
            :position="settingsPosition"
            :isImportant="props.isImportant"
            @delete="handleDelete"
            @manageTags="handleTags"
            @toggleImportant="handleImportant"
            @copyToShared="handleCopyToShared"
            @move="handleMove"
            @close="isSettingsVisible = false"
        />
    </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue';
import BookmarkSettings from './BookmarkSettings.vue';

const showTooltip = ref(false);

const defaultImage = 'https://images.unsplash.com/photo-1481627834876-b7833e8f5570?q=80&w=2128&auto=format&fit=crop';

const props = defineProps({
    image: {
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
    url: {
        type: String,
        required: true
    },
    hashtags: {
        type: Array,
        default: () => []
    },
    readingTime: {
        type: Number,
        required: true
    },
    isImportant: {
        type: Boolean,
        default: false
    }
});

const visibleTags = computed(() => props.hashtags.slice(0, 2));
const remainingTags = computed(() => props.hashtags.slice(2));
const remainingTagsCount = computed(() => Math.max(0, props.hashtags.length - 2));

const emit = defineEmits(['delete', 'manageTags', 'toggleImportant', 'copyToShared', 'move']);

const isSettingsVisible = ref(false);
const settingsPosition = ref({ x: 0, y: 0 });

const showSettings = (event) => {
    // 이벤트 전파 중단
    event.stopPropagation();
    
    // 버튼의 위치 계산
    const rect = event.target.getBoundingClientRect();
    settingsPosition.value = {
        x: rect.right + 5,
        y: rect.top
    };
    
    isSettingsVisible.value = true;
};

// 설정 메뉴의 각 액션 핸들러
const handleDelete = () => {
    emit('delete');
    isSettingsVisible.value = false;
};

const handleTags = () => {
    emit('manageTags');
    isSettingsVisible.value = false;
};

const handleImportant = () => {
    emit('toggleImportant');
    isSettingsVisible.value = false;
};

const handleCopyToShared = () => {
    emit('copyToShared');
    isSettingsVisible.value = false;
};

const handleMove = () => {
    emit('move');
    isSettingsVisible.value = false;
};

// 설정 메뉴 외부 클릭 시 닫기
const closeSettings = (event) => {
    if (isSettingsVisible.value) {
        isSettingsVisible.value = false;
    }
};

// 컴포넌트 마운트 시 이벤트 리스너 추가
onMounted(() => {
    document.addEventListener('click', closeSettings);
});

// 컴포넌트 언마운트 시 이벤트 리스너 제거
onUnmounted(() => {
    document.removeEventListener('click', closeSettings);
});
</script>

<style scoped>
.card {
    border-radius: 8px;
    background: white;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    overflow: hidden;
    transition: transform 0.2s;
    width: 240px;
    position: relative; /* BookmarkSettings의 절대 위치 기준점 */
}

.card:hover {
    transform: translateY(-4px);
}

.card-image {
    width: 100%;
    height: 180px;
    object-fit: cover;
}

.card-content {
    padding: 16px;
    height: 200px;
    display: flex;
    flex-direction: column;
}

.card-title {
    margin: 0 0 12px 0;
    font-size: 1.1rem;
    color: #333;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
}

.card-description {
    color: #666;
    font-size: 0.9rem;
    line-height: 1.5;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
    flex: 1;
    margin-bottom: 8px;
}

.card-url {
    font-size: 0.8rem;
    color: #666;
    margin-bottom: 8px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.card-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: auto;
}

.tags-container {
    display: flex;
    align-items: center;
    gap: 4px;
    flex-wrap: nowrap;
    overflow: hidden;
    max-width: 70%;
}

.tag {
    padding: 2px 6px;
    background-color: #f0f0f0;
    border-radius: 12px;
    white-space: nowrap;
}

.remaining-count {
    color: #888;
    font-size: 0.8rem;
    position: relative;
    cursor: pointer;
}

.read-time {
    white-space: nowrap;
    color: #666;
    font-size: 0.8rem;
}

.tooltip {
    position: absolute;
    bottom: 100%;
    left: 50%;
    transform: translateX(-50%);
    background-color: #333;
    color: white;
    padding: 8px;
    border-radius: 4px;
    font-size: 0.8rem;
    white-space: nowrap;
    z-index: 1000;
    margin-bottom: 8px;
    box-shadow: 0 2px 4px rgba(0,0,0,0.2);
}

.tooltip::after {
    content: '';
    position: absolute;
    top: 100%;
    left: 50%;
    transform: translateX(-50%);
    border-width: 4px;
    border-style: solid;
    border-color: #333 transparent transparent transparent;
}

.tooltip-tag {
    margin-right: 8px;
}

.tooltip-tag:last-child {
    margin-right: 0;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 8px 12px;
}

.importance {
    display: flex;
    align-items: center;
}

.star-icon {
    color: #FFD700;
    font-size: 1.2rem;
    cursor: pointer;
}

.star-icon.empty {
    color: #ccc;
}

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
</style>