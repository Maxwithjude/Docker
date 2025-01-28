<template>
    <header class="header">
        <div class="logo">
            <h1>별담</h1>
        </div>
        <div class="auth-section">
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
}

.logo h1 {
    margin: 0;
    font-size: 1.5rem;
    color: #333;
}

.auth-section {
    display: flex;
    align-items: center;
}

.profile-image {
    width: 40px;
    height: 40px;
    border-radius: 50%;
    object-fit: cover;
}
</style>