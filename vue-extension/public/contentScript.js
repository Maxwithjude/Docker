// 웹페이지의 DOM 및 JavaScript 환경에 접근(CORS 제한)
// 웹페이지에서 발생하는 이벤트를 직접 수신하고 처리

// 1. 로그인 성공 시 정보 받기
window.addEventListener('message', (event) => {
  // 출처 확인 (보안 강화)
  if (event.origin !== window.location.origin) return;

  if (event.data && event.data.type === 'FROM_WEBSITE') {
    const loginData = event.data.data;
    console.log("웹사이트에서 받은 로그인 정보: ", loginData);

    chrome.runtime.sendMessage({
      action: 'setLoginInfo',
      data: loginData,
    });
  }
});

// 2. 웹페이지 URL 가져오기
const currentUrl = window.location.href;
chrome.runtime.sendMessage({
  action: 'getUrl',
  url: currentUrl,
});
