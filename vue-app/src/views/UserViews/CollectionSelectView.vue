<template>
    <div class="page-container">
      <h2 class="headline">나만의 컬렉션 만들기</h2>
      <p class="paragraph">
        추천 컬렉션을 선택하거나, 나만의 개성있는 컬렉션명을 입력해보세요. <br />
        설정한 컬렉션은 자동으로 생성됩니다.
      </p>
  
      <div class="folders">
        <div
          v-for="(folder, index) in folders"
          :key="index"
          class="folder"
          :class="{ selected: selectedFolders.includes(folder.name) }"
          @click="toggleFolder(folder.name)"
        >
          <img src="@/assets/folder-icon.png" alt="폴더" />
          <span>{{ folder.name }}</span>
        </div>
      </div>
  
      <input
        type="text"
        v-model="customCollection"
        class="collection-input"
        placeholder="생성할 컬렉션명을 입력하세요."
        @keyup.enter="addCustomCollection"
      />
  
      <div class="selected-collections">
        <span v-for="(collection, index) in selectedFolders" :key="index" class="tag">
          {{ collection }}
          <button @click="removeCollection(collection)">x</button>
        </span>
      </div>
  
      <button class="next-button">다음</button>
    </div>
  </template>
  
  <script setup>
  import { ref } from "vue";
  
  // 폴더 리스트
  const folders = ref([
    { name: "바로가기" },
    { name: "쇼핑" },
    { name: "영화" },
    { name: "음악" },
    { name: "콘텐츠" },
    { name: "광고" },
    { name: "스터디" },
    { name: "맛집" },
    { name: "IT" },
    { name: "사진" },
  ]);
  
  // 선택된 폴더들
  const selectedFolders = ref([]);
  
  // 직접 입력한 컬렉션
  const customCollection = ref("");
  
  // 폴더 선택 토글
  const toggleFolder = (name) => {
    if (selectedFolders.value.includes(name)) {
      selectedFolders.value = selectedFolders.value.filter((item) => item !== name);
    } else {
      selectedFolders.value.push(name);
    }
  };
  
  // 직접 입력한 컬렉션 추가
  const addCustomCollection = () => {
    if (customCollection.value && !selectedFolders.value.includes(customCollection.value)) {
      selectedFolders.value.push(customCollection.value);
      customCollection.value = "";
    }
  };
  
  // 컬렉션 제거
  const removeCollection = (name) => {
    selectedFolders.value = selectedFolders.value.filter((item) => item !== name);
  };
  </script>
  
  <style scoped>
  .page-container {
    width: 680px;
    height: auto;
    background: #ffffff;
    border: 1px solid #dde1e6;
    padding: 20px;
    text-align: center;
  }
  
  .headline {
    font-size: 24px;
    font-weight: bold;
    color: #21272a;
  }
  
  .paragraph {
    font-size: 14px;
    color: #666;
    margin-bottom: 20px;
  }
  
  .folders {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    justify-content: center;
  }
  
  .folder {
    width: 100px;
    height: 120px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    border: 2px solid transparent;
    border-radius: 8px;
  }
  
  .folder img {
    width: 64px;
    height: 64px;
  }
  
  .folder span {
    margin-top: 8px;
    font-size: 14px;
  }
  
  .folder.selected {
    border-color: #0f62fe;
    background: rgba(15, 98, 254, 0.1);
  }
  
  .collection-input {
    width: 80%;
    padding: 10px;
    margin-top: 20px;
    border: 1px solid #ccc;
    border-radius: 6px;
    text-align: center;
  }
  
  .selected-collections {
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
    justify-content: center;
    margin-top: 20px;
  }
  
  .tag {
    background: #0f62fe;
    color: #fff;
    padding: 6px 12px;
    border-radius: 12px;
    display: flex;
    align-items: center;
  }
  
  .tag button {
    background: none;
    border: none;
    color: white;
    font-weight: bold;
    margin-left: 8px;
    cursor: pointer;
  }
  
  .next-button {
    width: 550px;
    height: 50px;
    margin-top: 20px;
    background: #0f62fe;
    color: white;
    border: none;
    border-radius: 10px;
    font-size: 18px;
    cursor: pointer;
  }
  </style>
  