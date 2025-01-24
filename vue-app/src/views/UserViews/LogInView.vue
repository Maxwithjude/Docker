<template>
    <div>
        로그인 페이지
        <RouterLink :to="{ name: 'main', params: { id: user.id } }">메인</RouterLink>
    </div>
</template>

<script setup>
import { ref } from "vue";

const user = ref({
  id: 1,
  name: "test",
  email: "test@test.com",
});
</script>

<style scoped>

</style>