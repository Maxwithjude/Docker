<template>
    <div class="layout">
        <Header />
        <div class="content-wrapper">
            <SideBar />
            <div class="body">
                기본 페이지
                <PersonalCollectionList
                    v-for="collectionInfo in personalCollections[0].results" 
                    :key="collectionInfo.collection_id" 
                    :collectionInfo="collectionInfo" 
                />
            </div>
        </div>
    </div>
</template>

<script setup>
import Header from '@/common/Header.vue'
import SideBar from '@/common/SideBar.vue'
import { ref } from 'vue';
import PersonalCollectionList from '@/component/PersonalCollectionList.vue';
// import { useCollectionStore } from '@/stores/collection';

// const collectionStore = useCollectionStore();


const personalCollections = ref([
    {
        "success" : true,
        "message" : "some message",
        "results": [
            {
                "collection_id" : 1,
                "name" : "웹서핑",
                "created_at" : "2024-01-01",
                "updated_at" : "2024-01-02"
            }, 
            {
                "collection_id" : 2,
                "name" : "자바",
                "created_at" : "2024-01-01",
                "updated_at" : "2024-01-02"
            }
        ] 
    }
]);

</script>

<style scoped>
.layout {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
}

.content-wrapper {
    display: flex;
    flex: 1;
}

.body {
    flex: 1;
    padding: 20px;
}
</style>

