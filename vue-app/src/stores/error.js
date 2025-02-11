import { defineStore } from "pinia";
import axios from "axios";
const REST_API_URL = import.meta.env.VITE_API_BASE_URL;

export const useErrorStore = defineStore("error", {
  actions: {
    async throwRefreshToken() {
      try {
        const refreshToken = sessionStorage.getItem("refreshToken");

        if (!refreshToken) {
          console.log("🚨 리프레시 토큰이 없습니다!");
          return null;
        }

        const res = await axios.post(`${REST_API_URL}/users/refresh`, { refreshToken });

        if (res.data?.status && res.data?.results) {
          const { accessToken, refreshToken: newRefreshToken } = res.data.results;

          // 새로운 accessToken과 refreshToken 저장
          sessionStorage.setItem("accessToken", accessToken);
          sessionStorage.setItem("refreshToken", newRefreshToken);

          console.log("✅ 토큰 갱신 성공!");
          console.log("🔑 새 accessToken:", accessToken);
          console.log("🔄 새 refreshToken:", newRefreshToken);

          return accessToken;
        } else {
          console.log("🚨 토큰 갱신 실패: 서버 응답 오류");
          return null;
        }
      } catch (error) {
        console.error("🚨 refresh-token도 유효하지 않습니다!", error);
        return null;
      }
    }
  }
});
