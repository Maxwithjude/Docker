// store/errorStore.js
import { defineStore } from "pinia";
import axios from "axios";


export const useErrorStore = defineStore("error", {
  actions: {
    async throwRefreshToken() {
      try {
        const refreshToken = sessionStorage.getItem("refreshToken");
        
        const res = await axios.post("/api/auth/refresh", { refreshToken });

        sessionStorage.setItem("accessToken", res.data["accessToken"]);
        sessionStorage.setItem("refreshToken", res.data["refreshToken"]);
        
        return res.data["access-token"];
      } catch (error) {
        console.log("refresh-token도 유효하지 않습니다!");
        return null;
      }
    }
  }
});
