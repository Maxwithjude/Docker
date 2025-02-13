<template>
    <div class="wrapper">
      <!-- max-w-lg를 max-w-xl로, height를 vh 단위로 설정 -->
      <div class="max-w-xl mx-auto p-8 bg-white shadow-lg rounded-lg h-[80vh] flex flex-col">
        <h2 class="text-2xl font-bold text-center mb-4">당신만의 태그로 더 나은 추천을 받아보세요</h2>
        <p class="text-gray-600 text-center mb-8">태그를 추가하면 관심사에 맞는 포스트를 추천받을 수 있어요.</p>
  
        <!-- 추천 태그 목록 -->
        <div class="flex flex-wrap gap-3 mb-8">
          <span v-for="tag in recommendedTags" :key="tag"
                class="px-3 py-1.5 text-sm rounded-lg bg-gray-200 cursor-pointer hover:bg-gray-300"
                @click="addTag(tag)">
            # {{ tag.tagName }}
          </span>
        </div>
  
        <!-- 태그 입력 -->
        <div class="flex items-center gap-3 mb-8">
          <input 
              v-model="newTag" 
              type="text" 
              placeholder="나만의 관심 태그를 입력하고 엔터를 눌러주세요"
              class="flex-1 px-4 py-2.5 border rounded-lg text-base text-center"
              @keyup.enter="addTag(newTag)"
          >
          <!-- <button 
              @click="addTag(newTag)" 
              class="px-5 py-2.5 bg-blue-500 text-white rounded-lg hover:bg-blue-600">
            추가
          </button> -->
        </div>
  
        <!-- 선택된 태그 목록 -->
        <div class="flex flex-wrap gap-3 mb-8">
          <span v-for="(tag, index) in selectedTags" 
                :key="index"
                class="px-3 py-1.5 text-sm rounded-lg bg-blue-500 text-white flex items-center gap-2">
            # {{ tag.tagName }}
            <button @click="removeTag(tag)" class="text-white text-xs font-bold">✕</button>
          </span>
        </div>
  
        <div class="flex justify-between mt-auto">
          <!-- 이전 버튼 -->
          <router-link :to="{ name: 'collectionSelect' }">
            <button class="px-6 py-2.5 bg-gray-300 rounded-lg hover:bg-gray-400">이전</button>
          </router-link>
  
          <!-- 완료 버튼 -->
          <router-link :to="{ name: 'main' }">
            <button 
              @click="saveSelectedTags"
              class="px-6 py-2.5 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
            >
              완료
            </button>
          </router-link>
        </div>
      </div>
    </div>
  </template>
  
  

<script setup>
import { ref, onMounted } from "vue";
import { RouterLink, useRouter } from "vue-router";
import { useCollectionStore } from "@/stores/collection";
import { useBookmarkStore } from "@/stores/bookmark";

const router = useRouter();
const collectionStore = useCollectionStore();
const bookmarkStore = useBookmarkStore();

const recommendedTags = ref([]);
const selectedTags = ref([]);
const newTag = ref("");

// 컴포넌트 마운트 시 추천 태그 로드
onMounted(async () => {
    try {
        await collectionStore.getTop10Tags();
        const tagList = collectionStore.top10Tags.results.tagList;
        recommendedTags.value = tagList.map(tagName => ({
            tagName: tagName
        }));
    } catch (error) {
        alert('추천 태그를 불러오는데 실패했습니다.');
    }
});

const addTag = (tag) => {
    if (typeof tag === 'object' && tag.tagName) {
        if (!selectedTags.value.some(t => t.tagName === tag.tagName)) {
            selectedTags.value.push(tag);
        }
    } else if (typeof tag === 'string' && tag.trim()) {
        const newTagObj = {
            tagName: tag.trim()
        };
        if (!selectedTags.value.some(t => t.tagName === newTagObj.tagName)) {
            selectedTags.value.push(newTagObj);
        }
    }
    newTag.value = "";
};

const removeTag = (tag) => {
    selectedTags.value = selectedTags.value.filter(t => t.tagName !== tag.tagName);
};

// 완료 버튼 클릭 시 선택된 태그 저장
const saveSelectedTags = async () => {
    try {
        if (selectedTags.value.length === 0) {
            alert('최소 한 개 이상의 태그를 선택해주세요.');
            return;
        }
        
        // 태그 목록만 추출하여 전송
        const tagList = selectedTags.value.map(tag => tag.tagName);
        await collectionStore.addTag(tagList);
        
        // 태그 저장 후 메인 페이지로 이동
        router.push({ name: 'main' });
    } catch (error) {
        alert('태그 저장에 실패했습니다: ' + error.message);
    }
};
</script>

<style scoped>
.wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    padding: 2rem;
    background-color: #f9fafb;
}
</style>