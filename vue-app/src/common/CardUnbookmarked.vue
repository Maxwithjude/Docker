<template>
    <div class="card">
        <div class="card-header">
            <button class="save-button" @click="showSaveModal = true">
                Save
            </button>
        </div>
        <a 
            :href="props.url" 
            target="_blank" 
            rel="noopener noreferrer" 
            class="image-link"
        >
            <img 
                :src="imageSrc" 
                :alt="props.title" 
                class="card-image"
                style="cursor: pointer"
            >
        </a>
        <div class="card-content">
            <h2 class="card-title">{{ props.title }}</h2>
            <p class="card-description">{{ props.description }}</p>
            
            <div class="card-url">
                {{ props.url }}
            </div>
        </div>
    </div>

    <!-- 북마크 저장 모달 추가 -->
    <BookmarkSave 
        v-if="showSaveModal" 
        :key="props.url"
        :url="props.url"
        :title="props.title"
        :description="props.description"
        :img="props.img"
        @close="showSaveModal = false"
        @save="showSaveModal = false"
    />
</template>

<script setup>
import { ref, computed } from 'vue';
import BookmarkSave from '@/modal/BookmarkSave.vue';

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

// 모달 상태 관리
const showSaveModal = ref(false);

const imageSrc = computed(() => {
    return props.img && props.img.startsWith('http') 
        ? props.img 
        : 'https://images.unsplash.com/photo-1481627834876-b7833e8f5570?q=80&w=2128&auto=format&fit=crop';
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
    position: relative;
}

.card:hover {
    transform: translateY(-4px);
}

.card-image {
    width: 100%;
    height: 140px;
    object-fit: cover;
}

.card-content {
    padding: 12px;
    height: 140px;
    display: flex;
    flex-direction: column;
}

.card-title {
    margin: 0 0 8px 0;
    font-size: 1.1rem;
    color: #333;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.card-description {
    color: #666;
    font-size: 0.9rem;
    line-height: 1.4;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    margin-bottom: 6px;
}

.card-url {
    font-size: 0.8rem;
    color: #666;
    margin-bottom: 6px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.card-header {
    display: flex;
    justify-content: flex-end;
    align-items: center;
    padding: 8px 12px;
}

.save-button {
    cursor: pointer;
    padding: 6px 12px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 0.9rem;
    transition: background-color 0.2s;
}

.save-button:hover {
    background-color: #0056b3;
}

.image-link {
    display: block;
    text-decoration: none;
}
</style>
