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
                :disabled="isEmailVerified"
                class="flex-1 p-2 border border-gray-300 rounded-l-md focus:ring focus:ring-blue-200"
                placeholder="이메일"
                required
              />
              <button
                type="button"
                @click="sendVerificationCode"
                :disabled="isEmailVerified"
                class="bg-blue-600 text-white px-4 rounded-r-md hover:bg-blue-700 transition"
                :class="{ 'opacity-50 cursor-not-allowed': isEmailVerified }"
              >
                {{ isEmailSent ? '재발송' : '인증번호 발송' }}
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
                :disabled="isEmailVerified"
                class="flex-1 p-2 border border-gray-300 rounded-l-md focus:ring focus:ring-blue-200"
                placeholder="6자리 숫자를 입력하세요"
                maxlength="6"
                pattern="[0-9]{6}"
                inputmode="numeric"
                required
              />
              <button
                type="button"
                @click="verifyCode"
                :disabled="isEmailVerified"
                class="bg-blue-600 text-white px-4 rounded-r-md hover:bg-blue-700 transition"
                :class="{ 'opacity-50 cursor-not-allowed': isEmailVerified }"
              >
                {{ isEmailVerified ? '인증완료' : '인증하기' }}
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
            :disabled="!isFormValid"
            :class="[
              'w-full py-2 rounded-md transition duration-300',
              isFormValid 
                ? 'bg-blue-600 hover:bg-blue-700 text-white' 
                : 'bg-gray-300 text-gray-500 cursor-not-allowed'
            ]"
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
import { useUserStore } from "@/stores/user";;
import { ref, computed } from "vue";
import { useRouter } from "vue-router";
  
  const userStore = useUserStore();
  const router = useRouter();
  

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

  const isEmailVerified = ref(false);
  const isEmailSent = ref(false);
///////////////////////////////////////////////////////////////////////////////////////////
  // 폼 유효성 검사를 위한 computed 속성 추가
  // const isFormValid = computed(() => {
  //   return email.value &&
  //     verificationCode.value &&
  //     nickname.value &&
  //     password.value &&
  //     confirmPassword.value &&
  //     isEmailVerified.value &&
  //     password.value === confirmPassword.value;
  // });
  const isFormValid = computed(() => {
    return email.value &&
      nickname.value &&
      password.value &&
      confirmPassword.value &&
      // isEmailVerified.value && // 이메일 인증 체크 제거
      password.value === confirmPassword.value;
  });
///////////////////////////////////////////////////////////////////////////////////////////
  // 인증번호 발송
  const sendVerificationCode = async () => {
    try {
      const response = await userStore.emailVerification(email.value);
      if (response.success) {
        isEmailSent.value = true;
        modal.value = { 
          visible: true, 
          success: true, 
          message: response.message || "인증번호 발송이" 
        };
      }
    } catch (error) {
      modal.value = { 
        visible: true, 
        success: false, 
        message: error.response?.data?.message || "이메일 발송이"
      };
    }
  };
  
  // 인증 코드 확인
  const verifyCode = async () => {
    if (!isEmailSent.value) {
      modal.value = {
        visible: true,
        success: false,
        message: "먼저 인증번호를 발송해주세요"
      };
      return;
    }

    try {
      const response = await userStore.checkCode(verificationCode.value);
      if (response.success) {
        isEmailVerified.value = true;
        modal.value = {
          visible: true,
          success: true,
          message: response.message || "이메일 인증이"
        };
      }
    } catch (error) {
      isEmailVerified.value = false;
      modal.value = {
        visible: true,
        success: false,
        message: error.response?.data?.message || "이메일 인증이"
      };
    }
  };
  ///////////////////////////////////////////////////////////////////////////////////////////
  // 회원가입
  const handleRegister = async () => {
    // 이메일 인증 확인
    // if (!isEmailVerified.value) {
    //   modal.value = { 
    //     visible: true, 
    //     success: false, 
    //     message: "이메일 인증을 완료해주세요" 
    //   };
    //   return;
    // }
///////////////////////////////////////////////////////////////////////////////////////////
    // 비밀번호 확인
    if (password.value !== confirmPassword.value) {
      modal.value = { 
        visible: true, 
        success: false, 
        message: "비밀번호가 일치하지 않습니다" 
      };
      return;
    }

    const payload = {
      email: email.value,
      password: password.value,
      nickname: nickname.value,
    };

    try {
      await userStore.signup(payload);
      router.push({ name: 'login' });
    } catch (error) {
      modal.value = { 
        visible: true, 
        success: false, 
        message: error.response?.data?.message || "회원가입에 실패했습니다" 
      };
    }
  };
  </script>
  
  <style scoped>
  </style>
  