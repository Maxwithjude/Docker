let loginData = null; 
let currentUrl = null;

// <0> 컨텍스트 메뉴 생성 및 관리
// - 우클릭시 나타나는 '별담에 저장' 메뉴 생성
chrome.runtime.onInstalled.addListener(() => {
  chrome.contextMenus.create({
    id: "saveToByeoldam",
    title: "별담에 저장",
    contexts: ["page", "selection", "link", "image"],
  });
});


// <1> 사용자가 확장 아이콘을 클릭할 때 로그인 정보를 Extension Storage에 저장
chrome.browserAction.onClicked.addListener((tab) => {
  // contentScript.js에 로그인 정보 요청
  chrome.tabs.sendMessage(tab.id, { action: 'getLoginInfo' }, (response) => {
    if (response && response.loginData) {
      // 현재 저장된 로그인 정보와 비교
      chrome.storage.local.get(['userId'], (result) => {
        const currentUserId = result.userId;
        
        // 새로 수신한 로그인 정보와 다른 경우에만 Extension Storage에 저장
        if (response.loginData.userId !== currentUserId) {
          chrome.storage.local.set({
            userId: response.loginData.userId,
            access_token: response.loginData.access_token
          });
        } 
      });
    }
  });
});

// <2> 로그아웃 시 Extension Storage 비우기
chrome.runtime.onMessage.addListener((message, sender) => {
  if (message.action === 'logout') {
    chrome.storage.local.remove(['userId', 'access_token']);
  }
});

// URL 정보 수신 및 저장
chrome.runtime.onMessage.addListener((message, sender) => {
  if (message.action === 'getUrl') {
    currentUrl = message.url;
  }
});

// URL 정보 요청에 대한 응답
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  if (message.action === 'getCurrentUrl') {
    sendResponse({ url: currentUrl });
  }
});

// userId 요청에 대한 응답
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  if (message.action === 'getUserId') {
    chrome.storage.local.get(['userId'], (result) => {
      sendResponse({ userId: result.userId })
    })
    return true // 비동기 응답을 위해 true 반환
  }
})
