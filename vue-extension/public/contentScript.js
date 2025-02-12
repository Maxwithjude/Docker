// <1> 별담 사이트에서 postMessage로 전달된 로그인 정보 수신
window.addEventListener("message", (event) => {
  if (event.origin !== window.location.origin || event.source !== window)
    return;

  // 로그인 이벤트 수신
  if (event.data && event.data.type === "LOGIN") {
    chrome.runtime.sendMessage({
      action: "login",
      loginData: event.data.data,
    });
  }

  // 로그아웃 이벤트 수신
  if (event.data && event.data.type === "LOGOUT") {
    chrome.runtime.sendMessage({
      action: "logout",
      loginData: null,
    });
  }
});

// <2> 웹페이지의 DOM에 직접 접근하여 읽기 시간 계산(WPM)
function calculateReadingTime() {
  // 1. 본문 찾기
  const content = document.querySelector('article, main') || document.body;
  const text = content.textContent.trim();
  
  // 2. 문자 분석
  const stats = {
    koreanChars: (text.match(/[\uAC00-\uD7AF]/g) || []).length,
    englishWords: (text.match(/[a-zA-Z]+/g) || []).length,
    numbers: (text.match(/\d+/g) || []).length,
    specialChars: (text.match(/[!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]+/g) || []).length
  };
  
  // 3. 분당 읽기 시간 계산
  const readingSpeed = {
    korean: 500,    
    english: 200,   
    number: 100,    
    special: 1000
  };
  
  const readingTime = 
    (stats.koreanChars / readingSpeed.korean) +
    (stats.englishWords / readingSpeed.english) +
    (stats.numbers / readingSpeed.number) +
    (stats.specialChars / readingSpeed.special);
    
  const totalTime = Math.max(1, Math.ceil(readingTime));
  
  return {
    totalTime,
    stats
  };
}

// <3> background.js로 페이지 정보 전송
const readingTimeInfo = calculateReadingTime();
chrome.runtime.sendMessage({
  action: "GET_PAGE_INFO",
  url: window.location.href,
  readingTime: readingTimeInfo.totalTime,
  stats: readingTimeInfo.stats
});