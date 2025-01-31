import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useCounterStore = defineStore('counter', () => {
  const count = ref(0)
  const doubleCount = computed(() => count.value * 2)
  function increment() {
    count.value++
  }
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
  return { count, doubleCount, increment, user }
})
