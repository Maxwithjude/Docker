<template>
    <div>
        {{ collectionInfo.name }}
        <Card
            v-for="bookmark in personalCollectionsBookmarks[0].results.bookmarks"
            :bookmarkId="bookmark.bookmark_id"
            :url="bookmark.url"
            :img="bookmark.img"
            :title="bookmark.title"
            :description="bookmark.description"
            :tag="bookmark.tag"
            :priority="bookmark.priority"
            :isPersonal="bookmark.isPersonal"
            :createdAt="bookmark.created_at"
            :updatedAt="bookmark.updated_at"
            @togglePriority="togglePriority(bookmark)"
        />
    </div>
</template>

<script setup>
import Card from '@/common/Card.vue';
import { ref,defineProps } from 'vue';

const props = defineProps({
    collectionInfo: {
        type: Object,
        required: true
    }
});
const { collectionInfo } = props;

const togglePriority = (bookmark) => {
    bookmark.priority = !bookmark.priority;
}

const personalCollectionsBookmarks = ref([
    {
        "success":true,
        "message":"some message",
        "results": {
            "name" : "웹서핑",
            "bookmarks" : [
                {
                    "bookmark_id" : 1,
                    "url" : "https://naver.com",
                    "img" : "https://naver.com/image.jpg",
                    "title" : "네이버 메인 - 검색, 뉴스, 쇼핑",
                    "description" : "네이버는 다양한 정보를 ...",
                    "priority" : true,
                    "created_at" : "2024-01-01",
                    "updated_at" : "2024-01-02",
                    "tag" : ["서핑", "웹"]
                }, 
                {
                    "bookmark_id" : 2,
                    "url" : "https://edu.ssafy.com",
                    "img" : "https://edu.ssafy.com/image.jpg",
                    "title" : "싸피 메인 페이지",
                    "description" : "대한민국 청년 삼성 ...",
                    "priority" : true,
                    "created_at" : "2024-01-01",
                    "updated_at" : "2024-01-02",
                    "tag" : ["싸피", "IT"]
                },
            ]
        }
    }
])
</script>

<style scoped>

</style>