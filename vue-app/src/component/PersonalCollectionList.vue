<template>
    <div class="collection-container">
        <h2 class="collection-title">{{ collectionInfo.name }}</h2>
        <div v-if="bookmarks.length > 0" class="bookmarks-grid">
            <Card
                v-for="bookmark in bookmarks"
                :key="bookmark.bookmark_id"
                :bookmarkId="bookmark.bookmark_id"
                :url="bookmark.url"
                :img="bookmark.img"
                :title="bookmark.title"
                :description="bookmark.description"
                :tag="bookmark.tag"
                :priority="bookmark.priority"
                :isPersonal="true"
                :createdAt="bookmark.created_at"
                :updatedAt="bookmark.updated_at"
                @togglePriority="togglePriority(bookmark)"
            />
        </div>
        <div v-else class="empty-message">
            <p>이 컬렉션에는 아직 북마크가 없습니다.</p>
            <p>새로운 북마크를 추가해보세요!</p>
        </div>
    </div>
</template>

<script setup>
import Card from '@/common/Card.vue';
import { computed } from 'vue';
import { useCounterStore } from '@/stores/counter';

const counterStore = useCounterStore();

const props = defineProps({
    collectionInfo: {
        type: Object,
        required: true
    }
});

// collection_id에 해당하는 북마크들을 찾아서 반환
const bookmarks = computed(() => {
    const bookmarksData = counterStore.personalCollectionsBookmarks.results;
    return bookmarksData.name === props.collectionInfo.name ? bookmarksData.bookmarks : [];
});

const togglePriority = (bookmark) => {
    bookmark.priority = !bookmark.priority;
}
</script>

<style scoped>
.collection-container {
    margin-bottom: 30px;
}

.collection-title {
    margin-bottom: 20px;
    font-size: 1.5rem;
    color: #333;
}

.bookmarks-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 20px;
}

.empty-message {
    text-align: center;
    padding: 40px;
    background: #f9f9f9;
    border-radius: 8px;
    color: #666;
}

.empty-message p {
    margin: 5px 0;
}

.empty-message p:first-child {
    font-size: 1.1em;
    color: #333;
}
</style>