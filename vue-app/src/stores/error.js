// store/errorStore.js
import { defineStore } from "pinia";
import axios from "axios";
import { useUserStore } from "./userStore";

export const useErrorStore = defineStore("error", {
  actions: {
    async throwRefreshToken() {
      try {
        const token = sessionStorage.getItem("refresh-token");
        
        const res = await axios.post("/api/auth/refresh", { token });

        sessionStorage.setItem("access-token", res.data["access-token"]);
        sessionStorage.setItem("refresh-token", res.data["refresh-token"]);
        
        return res.data["access-token"];
      } catch (error) {
        console.log("refresh-token도 유효하지 않습니다!");
        return null;
      }
    }
  }
});
