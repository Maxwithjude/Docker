import { defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore("user", () => {
  const userId = ref(null);
  const accessToken = ref(null);

  const setUser = (id, token) => {
    userId.value = id;
    accessToken.value = token;
  };

  return { userId, accessToken, setUser };
});
