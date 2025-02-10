<template>
    <div class="collection-container">
        <div class="collection-header">
            <h2 class="collection-title">{{ collectionInfo.name }}</h2>
            <div class="shared-users">
                공유 멤버:
                <span v-for="(user, index) in collectionInfo.users" :key="user.user_id" class="user-name">
                    {{ user.nickname }}{{ index < collectionInfo.users.length - 1 ? ', ' : '' }}
                </span>
            </div>
        </div>
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
                :isPersonal="false"
                :createdAt="bookmark.created_at"
                :updatedAt="bookmark.updated_at"
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
import { storeToRefs } from 'pinia';
import { useBookmarkStore } from '@/stores/bookmark';

const bookmarkStore = useBookmarkStore();
const { sharedCollectionBookmarks } = storeToRefs(bookmarkStore);


const props = defineProps({
    collectionInfo: {
        type: Object,
        required: true
    }
});

// collection_id에 해당하는 북마크들을 찾아서 반환
const bookmarks = computed(() => {
    return sharedCollectionBookmarks.value?.results?.bookmarks || [];
});



</script>

<style scoped>
.collection-container {
    margin-bottom: 30px;
}

.collection-header {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-bottom: 20px;
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

.shared-users {
    color: #666;
    font-size: 0.9rem;
}

.user-name {
    color: #333;
    font-weight: 500;
}
</style>