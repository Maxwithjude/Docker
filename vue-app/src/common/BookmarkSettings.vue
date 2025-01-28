<template>
    <div class="bookmark-settings" :style="positionStyle">
        <div class="settings-menu">
            <button class="menu-item" @click="handleDelete">
                <i class="fas fa-trash"></i>
                북마크 삭제
            </button>
            
            <button class="menu-item" @click="handleTags">
                <i class="fas fa-tags"></i>
                북마크 태그 관리
            </button>
            
            <button class="menu-item" @click="handleImportant">
                <i :class="isImportant ? 'fas fa-star' : 'far fa-star'"></i>
                {{ isImportant ? '중요 북마크 해제' : '중요 북마크 설정' }}
            </button>
            
            <button class="menu-item" @click="handleCopyToShared">
                <i class="fas fa-share-alt"></i>
                공유 컬렉션으로 복사
            </button>
            
            <button class="menu-item" @click="handleMove">
                <i class="fas fa-folder-open"></i>
                다른 개인 컬렉션으로 이동
            </button>
        </div>
    </div>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
    position: {
        type: Object,
        required: true,
        // { x: number, y: number }
    },
    isImportant: {
        type: Boolean,
        default: false
    }
});

const emit = defineEmits([
    'delete',
    'manageTags',
    'toggleImportant',
    'copyToShared',
    'move',
    'close'
]);

const positionStyle = computed(() => ({
    left: `${props.position.x}px`,
    top: `${props.position.y}px`
}));

const handleDelete = () => emit('delete');
const handleTags = () => emit('manageTags');
const handleImportant = () => emit('toggleImportant');
const handleCopyToShared = () => emit('copyToShared');
const handleMove = () => emit('move');
</script>

<style scoped>
.bookmark-settings {
    position: absolute;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.15);
    width: 240px;
    z-index: 1000;
    overflow: hidden;
}

.settings-menu {
    display: flex;
    flex-direction: column;
}

.menu-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 16px;
    border: none;
    background: none;
    width: 100%;
    text-align: left;
    cursor: pointer;
    color: #333;
    font-size: 0.9rem;
    transition: background-color 0.2s;
}

.menu-item:hover {
    background-color: #f5f5f5;
}

.menu-item i {
    width: 16px;
    color: #666;
}

.menu-item:hover i {
    color: #333;
}
</style>
