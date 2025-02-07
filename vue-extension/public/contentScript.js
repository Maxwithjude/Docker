// 웹페이지의 DOM 및 JavaScript 환경에 접근
// 웹페이지에서 발생하는 이벤트를 직접 수신하고 처리

// <1> 웹페이지에서 postMessage로 전달된 로그인 정보 수신
window.addEventListener('message', (event) => {

  if (event.origin !== window.location.origin || event.source !== window) return;

  // 로그인 이벤트 수신
  if (event.data && event.data.type === 'LOGIN') {
    chrome.runtime.sendMessage({
      action: 'login',
      loginData: event.data.data
    });
  }

  // 로그아웃 이벤트 수신
  if (event.data && event.data.type === 'LOGOUT') {
    chrome.runtime.sendMessage({
      action: 'logout',
      loginData: null
    });
  }
});


// <2> 현재 웹페이지의 URL을 가져와 background.js로 전달
const currentUrl = window.location.href;
chrome.runtime.sendMessage({
  action: 'setUrl',
  url: currentUrl,
});
