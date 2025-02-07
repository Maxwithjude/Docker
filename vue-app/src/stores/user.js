import { ref, computed } from "vue";
import { defineStore } from "pinia";
import api from "@/utils/api";
import axios from "axios";
import router from "@/router";

const REST_API_URL = import.meta.env.VITE_API_BASE_URL;
//testtest
export const useUserStore = defineStore("user", () => {
    const loginUser = ref(null);
    const userId = computed(() => loginUser.value);

  
    const user = ref(null);

    // const username = ref(null)
    // const password1 = ref(null)
    // const password2 = ref(null)
    // const nickname = ref(null)
    // const age = ref(null)
    // const email = ref(null)
    // const profile_img = ref(null)

    // const payload = {
    //   username: username.value,
    //   password1: password1.value,
    //   password2: password2.value,
    //   nickname: nickname.value,
    //   age: age.value,
    //   email: email.value,
    //   profile_img: profile_img.value
    // }

    //api 경로 : `http:localhost:8080/api/users/login`
//   임시 로그인 함수

  //로그인 함수 헤더가 필요없어 직접 axios 요청
  const userLogin = async (email, password) => {
    try {
      const res = await axios.post(`${REST_API_URL}/users/login`, { email, password });
  
      if (res.data.success) {
        const { userId, email, nickname, accessToken, refreshToken } = res.data.results;
  
        // JWT 토큰 저장
        sessionStorage.setItem("accessToken", accessToken);
        sessionStorage.setItem("refreshToken", refreshToken);
  
        // 사용자 정보 업데이트
        user.value = { userId, email, nickname };
  
        // 로그인 성공 시 main 페이지로 이동
        router.push({ name: "main" });
      } else {
        throw new Error(res.data.message || "로그인 실패");
      }
    } catch (err) {
      console.error("🚨 로그인 실패:", err);
      useErrorStore().setError(err.response?.data?.message || "ID/PW 정보가 맞지 않습니다.");
    }
  };

  //로그인 하면 토큰 익스텐션에 보낼 함수

//   const sendInfoExtension = async (params) => {
//     try {
        
//     } catch (error) {
        
//     }
//   }
const logout = async () => {
    try {
      await api.post(`${REST_API_URL}/users/logout`); // 백엔드에 로그아웃 요청
  
      // 익스텐션에 로그아웃 알림
      window.postMessage({ type: "LOGOUT" }, window.location.origin);
  
      // 세션 스토리지 및 유저 상태 초기화
      sessionStorage.removeItem("accessToken");
      sessionStorage.removeItem("refreshToken");
      user.value = null;
  
      alert("정상적으로 로그아웃 처리되었습니다.");
      router.push({ name: "intro" });
    } catch (error) {
      console.error("🚨 로그아웃 실패:", error);
      useErrorStore().setError("로그아웃에 실패했습니다. 다시 시도해주세요.");
    }
  };
  
  



  // 이메일 인증 요청 함수
  const emailVerification = async (email) => {
    try {
        await axios.post(`${REST_API_URL}/users/email/send`, {
            email: email
        })
    } catch (error) {
        console.error("이메일 인증에 실패하였습니다.")
    }
  }

  //이메일 응답코드 검증 함수
  // userStore.js
const checkCode = async (params) => {
    try {
      // API 호출 부분 (실제 엔드포인트와 메서드는 백엔드 명세에 맞게 수정 필요)
      const response = await axios.post(`${REST_API_URL}/users/email/verify`, {
        verifyCode: params
      });
      
      return response.data; // {success: true/false, message: string, results: null} 형태로 반환
    } catch (error) {
      throw error;
    }
  };
  // 회원가입 함수
  const signup = async (form) => {
    try {
      const response = await axios.post(`${REST_API_URL}/users/register`, {
        email: form.email,
        password: form.password,
        nickname: form.nickname,
      });
  
      if (response.data.success) {
        alert("회원가입이 성공적으로 완료되었습니다.");
        router.push({ name: "login" });
      } else {
        alert(response.data.message || "회원가입에 실패했습니다.");
      }
    } catch (err) {
      console.error("회원가입 요청 실패:", err);
      alert(err.response?.data?.message || "회원가입에 실패했습니다. 다시 시도해주세요.");
    }
  };

  //마이페이지 조회 밑에 리턴 함수명 주석 해제해야 사용 가능능
const getMyPage = async () => {
  try {
    const res = await api.get("/users/mypage"); // `api` 인스턴스로 요청 보내기
    return res.data; // 필요하면 데이터 반환
  } catch (error) {
    console.error("🚨 마이페이지 조회 실패:", error);
  }
};


  //마이페이지 수정
  const putMyPage = async (params) => {
    try {
        const res = await axios.put(`${REST_API_URL}/users/mypage`)
    } catch (error) {
        
    }    
  }

  //회원 탈퇴
  const withdrawalOfMembership = async (params) => {
    try {
            const res = await axios.delete(`${REST_API_URL}/users/mypage`)
    } catch (error) {
        console.log('회원 탈퇴에 실패하였습니다:', error);
    }
  };

  return {
    user,
    // payload,
    
    loginUser,
    // currentUser, // 사용자 정보 추가
    userLogin,
    emailVerification,
    logout,
    signup,
    userId,
    checkCode,
//     getMyPage,
//     withdrawalOfMembership,
//     putMyPage,
//     getMyPage,
  };
});