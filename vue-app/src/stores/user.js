import { ref, computed } from "vue";
import { defineStore } from "pinia";
import axios from "axios";
import router from "@/router";
const REST_API_URL = `http://localhost:8080/api`;

export const useUserStore = defineStore("user", () => {
  const loginUser = ref(null);
//   const currentUser = ref(null);
    const userId = computed(() => loginUser.value);
  const user = ref({
    "success": true,
    "message": "some message",
    "results": {
      "userId": 123,
      "email": "user@example.com",
      "nickname": "userNickname",
      "createdAt": "2025-01-01T12:00:00",
      "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
      "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
  })

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

  //로그인 함수
  const userLogin = async (email, password) => {
    try {
      const res = await axios.post(`${REST_API_URL}/users/login`, { email: email, password: password });
    //세션 스토리지에 jwt 토큰 저장
      sessionStorage.setItem("access-token", res.data["access-token"]);

      const token = res.data["access-token"].split(".");
        //첫 토큰 페이로드에 id가 담겨있으면 1은 언제든 백과 맞추어 변경 가능
      const id = JSON.parse(atob(token[1]))["id"];

      loginUser.value = id;
      sessionStorage.setItem("userId", id); // 세션에도 저장
      router.push({ name: "main" });
    } catch (err) {
      console.error(err);
      throw new Error(err.response?.data?.message || "ID/PW 정보가 맞지 않습니다.");
    }
  };

  // 로그아웃 함수 로그아웃하면 세션 스토리지에서 정보 빼고, 인트로로
  const logout = () => {
    loginUser.value = null;
    sessionStorage.removeItem("access-token");
    alert("정상적으로 로그아웃 처리되었습니다.");
    router.push({ name: "intro" });
  };
  
  // 이메일 인증 함수
  const emailVerification = async (email) => {
    try {
        await axios.post(`${REST_API_URL}/users/send-verification-code`, {
            email: email
        })
    } catch (error) {
        console.error("이메일 인증에 실패하였습니다.")
    }
  }

  // 회원가입 함수
  const signup = async (form) => {
    try {
      await axios.post(`${REST_API_URL}/users/register`, {
        userId: form.id,
        userPassword: form.password,
        userName: form.name,
        userNickname: form.nickname,
        userEmail: form.email,
      });
      alert("회원가입이 성공적으로 완료되었습니다.");
      router.push({ name: "login" });
    } catch (err) {
      console.error("회원가입 요청 실패:", err);
      alert(err.response?.data?.message || "회원가입에 실패했습니다. 다시 시도해주세요.");
    }
  };

  // **추가된 함수: 사용자 정보 가져오기**
  const fetchUserInfo = async () => {
    try {
      const response = await axios.get(`${REST_API_URL}/userinfo`, {
        headers: { Authorization: `Bearer ${sessionStorage.getItem("access-token")}` },
      });
      return response.data;
    } catch (error) {
      console.error("사용자 정보 가져오기 실패:", error);
      throw new Error("사용자 정보를 불러오지 못했습니다.");
    }
  };
  
  const loadCurrentUser = async () => {
    try {
        const userInfo = await fetchUserInfo();
        currentUser.value = userInfo;
    } catch (error) {
        console.error("사용자 정보 로드 실패:", error);
    }
  };


  //마이페이지 조회 밑에 리턴 함수명 주석 해제해야 사용 가능능
  const getMyPage = async () => {
    try {
        const res = await axios.get(`${REST_API_URL}/users/mypage`)
    } catch (error) {
        
    }
  }

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
    // userLogin,
    emailVerification,
    logout,
    signup,
    userId,
//     getMyPage,
//     withdrawalOfMembership,
//     putMyPage,
//     getMyPage,
    fetchUserInfo, // 추가된 부분
    loadCurrentUser, // 사용자 정보 로드 추가
  };
});