<template>
    <div class="flex items-center justify-center min-h-screen bg-gray-100">
      <div class="w-full max-w-md p-8 bg-white shadow-md rounded-lg">
        <h2 class="text-2xl font-bold text-center mb-6">회원가입</h2>
  
        <form @submit.prevent="handleRegister">
          <!-- 이메일 입력 -->
          <div class="mb-4">
            <label for="email" class="block text-sm font-medium text-gray-700">이메일</label>
            <div class="flex">
              <input
                v-model="email"
                type="email"
                id="email"
                class="flex-1 p-2 border border-gray-300 rounded-l-md focus:ring focus:ring-blue-200"
                placeholder="이메일"
                required
              />
              <button
                type="button"
                @click="sendVerificationCode"
                class="bg-blue-600 text-white px-4 rounded-r-md hover:bg-blue-700 transition"
              >
                인증번호 발송
              </button>
            </div>
          </div>
  
          <!-- 인증 코드 입력 -->
          <div class="mb-4">
            <label for="verificationCode" class="block text-sm font-medium text-gray-700">인증코드</label>
            <div class="flex">
              <input
                v-model="verificationCode"
                type="text"
                id="verificationCode"
                class="flex-1 p-2 border border-gray-300 rounded-l-md focus:ring focus:ring-blue-200"
                placeholder="인증코드"
                required
              />
              <button
                type="button"
                @click="verifyCode"
                class="bg-blue-600 text-white px-4 rounded-r-md hover:bg-blue-700 transition"
              >
                인증하기
              </button>
            </div>
          </div>
  
          <!-- 닉네임 입력 -->
          <div class="mb-4">
            <label for="nickname" class="block text-sm font-medium text-gray-700">닉네임</label>
            <input
              v-model="nickname"
              type="text"
              id="nickname"
              class="w-full p-2 border border-gray-300 rounded-md focus:ring focus:ring-blue-200"
              placeholder="닉네임"
              required
            />
          </div>
  
          <!-- 비밀번호 입력 -->
          <div class="mb-4">
            <label for="password" class="block text-sm font-medium text-gray-700">비밀번호</label>
            <input
              v-model="password"
              type="password"
              id="password"
              class="w-full p-2 border border-gray-300 rounded-md focus:ring focus:ring-blue-200"
              placeholder="비밀번호"
              required
            />
          </div>
  
          <!-- 비밀번호 재확인 -->
          <div class="mb-4">
            <label for="confirmPassword" class="block text-sm font-medium text-gray-700">비밀번호 재확인</label>
            <input
              v-model="confirmPassword"
              type="password"
              id="confirmPassword"
              class="w-full p-2 border border-gray-300 rounded-md focus:ring focus:ring-blue-200"
              placeholder="비밀번호 재확인"
              required
            />
          </div>
  
          <!-- 가입하기 버튼 -->
          <button
            type="submit"
            class="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 transition duration-300"
          >
            가입하기
          </button>
        </form>
      </div>
  
      <!-- 인증 성공/실패 모달 -->
      <div v-if="modal.visible" class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-30">
        <div class="bg-white p-6 rounded-lg shadow-lg w-80">
          <p class="text-lg font-bold">
            <span v-if="modal.success" class="text-green-600">✔ {{ modal.message }} 완료되었습니다.</span>
            <span v-else class="text-red-600">⚠ {{ modal.message }} 실패했습니다.</span>
          </p>
          <p v-if="!modal.success" class="text-gray-500 text-sm mt-2">상세 에러 메시지 조회란</p>
          <button
            @click="modal.visible = false"
            class="w-full mt-4 bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 transition"
          >
            확인
          </button>
        </div>
      </div>
    </div>
  </template>
  
  <script setup>
  import { useUserStore } from "@/stores/user";
import { ref } from "vue";
  
  const userStore = useUserStore();

  const email = ref("");
  const verificationCode = ref("");
  const nickname = ref("");
  const password = ref("");
  const confirmPassword = ref("");
  
  const modal = ref({
    visible: false,
    success: false,
    message: "",
  });
  
  //로그인 핸들러
  const handleLogin = async () => {
    try {
        await userstore.userLogin(email.value, password.value)
        router.push({ name: 'main' })
    } catch (error) {
        alert('로그인 실패: ' + error.message)
    }}


  // 인증번호 발송
  const sendVerificationCode = async () => {
    try{
        await userStore.emailVerification(email.value);
        modal.value = { visible: true, success: true, message: "인증번호 발송이" };
    }catch (error) {
        alert('올바르지 않은 이메일 입니다')
    }};
  
  // 인증 코드 확인
  const verifyCode = async () => {
    const isSuccess = Math.random() > 0.5; // 랜덤 성공/실패 처리 (실제 API 로직 필요)
    userStore.ve
    modal.value = {
      visible: true,
      success: isSuccess,
      message: "인증이",
    };
  };
  
  // 회원가입
  const handleRegister = () => {
    if (password.value !== confirmPassword.value) {
      modal.value = { visible: true, success: false, message: "비밀번호 확인이" };
      return;
    }
  
    console.log("회원가입 시도:", email.value, nickname.value);
    modal.value = { visible: true, success: true, message: "회원가입이" };
  };
  </script>
  
  <style scoped>
  </style>
  