<template>
    <div class="card">
        <div class="card-header">
            <div class="importance">
                <span v-if="props.isImportant" class="star-icon">★</span>
                <span v-else class="star-icon empty">☆</span>
            </div>
            <div class="settings">
                <el-popover
                    placement="bottom-end"
                    :width="200"
                    trigger="click"
                    popper-class="bookmark-settings-popover"
                    v-model:visible="isSettingsVisible"
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
                            
                            <el-button link class="delete-button" @click="confirmDelete">
                                북마크 삭제
                            </el-button>
                        </el-button-group>
                    </div>
                </el-popover>
            </div>
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
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';

const isSettingsVisible = ref(false);

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

const toggleImportant = () => {
    // TODO: Implement toggle important
};

const copyToSharedCollection = () => {
    // TODO: Implement copy to shared collection
};

const showMoveDialog = () => {
    // TODO: Implement move dialog
};

const showTagManagement = () => {
    // TODO: Implement tag management
};

const confirmDelete = () => {
    // TODO: Implement delete confirmation
};
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

.settings {
    position: relative;
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