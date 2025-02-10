import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  }
});

// 요청 인터셉터
api.interceptors.request.use(
  (config) => {
    
    const accessToken =  chrome.storage.local.get(["access_token"]);
    if (accessToken) {
      config.headers['accessToken'] = accessToken;
      console.log('요청 URL:', config.baseURL + config.url);
      console.log('요청 헤더:', config.headers);
    } else {
      console.log('토큰이 없습니다!');
    }
    return config;
  },
  (error) => {
    console.error('요청 인터셉터 에러:', error);
    return Promise.reject(error);
  }
);

export default api;