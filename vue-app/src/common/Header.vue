<template>
    <header class="header">
        <div class="logo">
            <router-link :to="{ name: 'main'}" class="logo-link">
                <div class="logo-text">
                    <span class="logo-title">별담</span>
                    <span class="logo-subtitle">: 별을 담다</span>
                </div>
                <img src="@/assets/logo.png" alt="별담 로고" class="logo-image">
            </router-link>
        </div>
        
        <div class="auth-section">
            <div>
                <p>{{ userStore.userId }}</p>
            </div>
            <img 
                :src="userProfile || defaultProfileImage" 
                alt="프로필 사진" 
                class="profile-image"
            />
        </div>
    </header>
</template>

<script setup>
import { ref } from 'vue';

import { useUserStore } from '@/stores/user';

const userStore = useUserStore();
const defaultProfileImage = 'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png';
const userProfile = ref(null);
</script>

<style scoped>
.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 1rem 2rem;
    background-color: #f8f9fa;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    height: 60px;  /* 헤더 높이 고정 */
}

.logo {
    display: flex;
    align-items: center;
    gap: 10px;
}

.logo-image {
    width: 40px;  /* 로고 이미지 크기 조정 */
    height: 40px;  /* 로고 이미지 크기 조정 */
    object-fit: contain;  /* 이미지 비율 유지 */
}

.auth-section {
    display: flex;
    align-items: center;
    gap: 10px;
}

.profile-image {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    object-fit: cover;
}

.logo-link {
    display: flex;
    align-items: center;
    gap: 15px;
    text-decoration: none;
    color: inherit;
    white-space: nowrap;  /* 줄바꿈 방지 */
}

.logo-text {
    display: flex;
    align-items: center;  /* 세로 중앙 정렬 */
    gap: 5px;  /* 타이틀과 서브타이틀 사이 간격 */
}

.logo-title {
    font-size: 1.8rem;
    font-weight: 700;
    color: #2c3e50;
    letter-spacing: 0.5px;
}

.logo-subtitle {
    font-size: 1.2rem;  /* 크기 조정 */
    color: #666;
    letter-spacing: 0.5px;
}
</style>