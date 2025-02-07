// <+> 컨텍스트 메뉴 생성 및 관리
// - 우클릭시 나타나는 '별담에 저장' 메뉴 생성
chrome.runtime.onInstalled.addListener(() => {
  chrome.contextMenus.create({
    id: "saveToByeoldam",
    title: "별담에 저장",
    contexts: ["page", "selection", "link", "image"],
  });
});

// ===============================================================================================
// [로그인 상태 관리 및 저장 처리]
// ===============================================================================================

let cachedLoginData = "tester@naver.com"; // 최신 로그인 정보 저장 => null로 바꿔주기!******

// <1> 로그인 및 로그아웃 이벤트 수신 및 처리
chrome.runtime.onMessage.addListener((message, sender) => {
  if (message.action === 'login') {
    cachedLoginData = message.loginData; 
  }

  if (message.action === 'logout') {
    cachedLoginData = null;
    chrome.storage.local.remove(['userId', 'access_token']);
  }
});

// <2> 사용자가 확장 아이콘을 클릭하면, Extension Storage에 로그인 정보가 없을 경우 저장
chrome.action.onClicked.addListener(() => {
  if (cachedLoginData) {
    saveLoginData(cachedLoginData);
  }
});

// <2*> Extension Storage에 로그인 정보 저장하는 함수
function saveLoginData(loginData) {

  chrome.storage.local.get(['userId'], (result) => { 
    // ***테스트용***
    // if (result.userId !== cachedLoginData.userId) {
    if (result.userId !== "tester@naver.com") {  
      chrome.storage.local.set({
        // ***테스트용***
        userId: "tester@naver.com",
        access_token: "test_access_token_value" 
        // userId: cachedLoginData.userId,
        // access_token: cachedLoginData.access_token
      });
    }
  });
}

// <3> App.vue에서 userId 요청에 대한 응답
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  if (message.action === 'getUserId') {
    chrome.storage.local.get(['userId'], (result) => {
      console.log('App.vue에서 userId 요청에 대한 응답', currentUrl);
      sendResponse({ userId: result.userId })
    });
    return true;
  }
})


// ===============================================================================================
// [URL 정보 수신 및 응답 처리]
// ===============================================================================================

let currentUrl = null;

// <1> contentScript에서 URL 정보 수신
chrome.runtime.onMessage.addListener((message, sender) => {
  if (message.action === 'setUrl') {
    currentUrl = message.url;
    console.log('URL 저장됨:', currentUrl);
  }
});

// <2> StorageView에서 URL 요청에 대한 응답
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  if (message.action === 'getCurrentUrl') {
    sendResponse({ url: currentUrl });
  }
  return true;
});


