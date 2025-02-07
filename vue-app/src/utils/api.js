// 로그인 후 모든 요청이 access-token을 요청하므로 반복해서 쓰지 않기 위해
//만듬 axios 인터셉터라고 보면 된다

import axios from "axios";
import { useErrorStore } from "@/stores/error";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
});

// 요청 인터셉터 (Access Token 자동 추가)
api.interceptors.request.use(
  (config) => {
    const accessToken = sessionStorage.getItem("access-token");
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 응답 인터셉터 (Access Token 만료 시 처리)
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response && error.response.status === 404) {
      console.log("🔄 Access Token 만료됨! Refresh Token 시도");

      const errorStore = useErrorStore();
      const newAccessToken = await errorStore.throwRefreshToken();

      if (newAccessToken) {
        // 새 토큰을 헤더에 설정 후 실패한 요청 재시도
        error.config.headers.Authorization = `Bearer ${newAccessToken}`;
        return api.request(error.config);
      } else {
        console.log("🚨 재로그인 필요!");
        return Promise.reject(error);
      }
    }
    return Promise.reject(error);
  }
);

export default api;
