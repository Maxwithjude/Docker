import { defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore("user", () => {
  
  const userId = ref(null);
  const setUserId = (id) => {
    userId.value = id;
  };

  return { userId, setUserId };
});