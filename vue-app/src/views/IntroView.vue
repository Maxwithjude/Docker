<template>
    <div class="min-h-screen bg-gray-100">
        <!-- 헤더 -->
        <header class="flex justify-between items-center bg-white shadow-md px-6 py-4">
            <h1 class="text-2xl font-bold text-blue-600">별담</h1>
            <div>
                <button class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600 mr-2">
                    <RouterLink :to="{ name: 'register' }">회원가입</RouterLink>
                </button>
                <button class="px-4 py-2 border border-blue-500 text-blue-500 rounded-lg hover:bg-blue-500 hover:text-white">
                    <RouterLink :to="{ name: 'login' }">로그인</RouterLink>
                </button>
            </div>
        </header>
        
        <!-- 히어로 섹션 -->
        <section class="bg-white py-24 px-6 shadow-lg rounded-lg mx-6 mt-8 min-h-[300px]">

            <h2 class="text-xl font-bold text-gray-900">별담</h2>
            <p class="text-gray-600 mt-2">
    원하던 페이지를 북마크하고 별도로 알림을 받을 수 있는 서비스입니다.<br>
    북마크한 정보를 쉽게 인식해보세요.
</p>
<button class="mt-8 px-6 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600">
    익스텐션 설치하러 가기
</button>

        </section>

        <!-- 기능 소개 섹션 -->
        <section class="mt-12 text-center">
            <h3 class="text-lg font-bold text-gray-900">프로젝트의 대표적인 7개의 기능 소개</h3>
            <div class="grid grid-cols-2 md:grid-cols-4 gap-6 mt-6 px-6">
                <div class="p-4 bg-white shadow-md rounded-lg">
                    <h4 class="font-semibold">기능 1</h4>
                    <p class="text-gray-600 text-sm">특정 페이지를 저장하고 원하는 정보를 쉽게 찾아볼 수 있습니다.</p>
                </div>
                <div class="p-4 bg-white shadow-md rounded-lg">
                    <h4 class="font-semibold">기능 2</h4>
                    <p class="text-gray-600 text-sm">개인 맞춤형 알림을 통해 중요 업데이트를 받을 수 있습니다.</p>
                </div>
                <div class="p-4 bg-white shadow-md rounded-lg">
                    <h4 class="font-semibold">기능 3</h4>
                    <p class="text-gray-600 text-sm">간단한 UI로 누구나 쉽게 사용할 수 있습니다.</p>
                </div>
                <div class="p-4 bg-white shadow-md rounded-lg">
                    <h4 class="font-semibold">기능 4</h4>
                    <p class="text-gray-600 text-sm">데이터를 안전하게 보관할 수 있습니다.</p>
                </div>
            </div>
        </section>

        <!-- 설치 과정 설명 -->
        <!-- <section class="mt-12 text-center px-6">
            <h3 class="text-lg font-bold text-gray-900">크롬 익스텐션 설치 방법 및 과정</h3>
            <img src="/path-to-your-image.png" alt="설치 설명" class="mx-auto mt-4 shadow-lg rounded-lg">
        </section> -->

        <!-- 푸터 -->
        <footer class="mt-12 text-center text-gray-500 py-4">
            Copyright© 별담. All rights reserved.
        </footer>
    </div>
</template>

<script setup>
import { RouterLink } from 'vue-router';
</script>

<style scoped>
</style>
