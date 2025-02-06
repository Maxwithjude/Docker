// 웹페이지의 DOM 및 JavaScript 환경에 접근(CORS 제한)
// 웹페이지에서 발생하는 이벤트를 직접 수신하고 처리

let loginData = null;

// <웹페이지에서 postMessage로 전달된 로그인 정보 수신
window.addEventListener('message', (event) => {
  // 보안 강화: 출처와 이벤트 소스 확인(iframe, 팝업, 크로스 사이트에서 온 메시지를 차단)
  if (event.origin !== window.location.origin || event.source !== window) return;

  if (event.data && event.data.type === 'FROM_WEBSITE') {
    loginData = event.data.data;
    // console.log("웹사이트에서 받은 로그인 정보: ", loginData);
  }

  // 로그아웃 이벤트 수신
  if (event.data && event.data.type === 'LOGOUT') {
    loginData = null; // 로그인 정보 초기화
  }
});

// <2> background.js에서 요청 시 로그인 정보를 응답
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  if (message.action === 'getLoginInfo') {
    sendResponse({ loginData: loginData }); 
  }
});


// <3> 웹페이지 URL 가져오기
const currentUrl = window.location.href;
chrome.runtime.sendMessage({
  action: 'getUrl',
  url: currentUrl,
});
