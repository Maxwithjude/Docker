<template>
    <div>
        <h1>소개페이지</h1>
        <button><RouterLink :to="{name: 'login'}">로그인</RouterLink></button>
        <button><RouterLink :to="{name: 'register'}">회원가입</RouterLink></button>
    </div>
</template>

<script setup>
import { RouterLink } from 'vue-router';
</script>

<style scoped>

</style>