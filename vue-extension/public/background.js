// <+> 컨텍스트 메뉴 - 우클릭시 나타나는 '별담에 저장' 메뉴 생성
chrome.runtime.onInstalled.addListener(() => {
  chrome.contextMenus.create({
    id: "saveToByeoldam",
    title: "별담에 저장",
    contexts: ["page", "selection", "link", "image"],
  });
});

// ===============================================================================================
// [로그인 상태 관리 및 응답 처리]
// ===============================================================================================

let cachedLoginData = null;

chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  if (message.action === "login") {
    cachedLoginData = message.loginData;
  }

  if (message.action === "logout") {
    cachedLoginData = null;
    chrome.storage.local.remove(["userId", "access_token"]);
  }

  // popupOpened 처리 후 로그인 정보 저장
  if (message.action === "popupOpened") {
    if (cachedLoginData) {
      saveLoginData(cachedLoginData);
      sendResponse({ status: "success" }); // 응답을 보내 popupOpened 처리가 완료되었음을 알림
    } else {
      // else >> *********** test용 **************
      const testLoginData = {
        userId: "tester@naver.com",
        access_token: "test_access_token_value",
      };
      saveLoginData(testLoginData);
      sendResponse({ status: "success" });
    }
  }
});

// 로그인 정보를 Extension local Storage에 저장하는 함수
function saveLoginData(userLoginInfo) {
  if (!userLoginInfo) {
    console.warn("userLoginInfo가 null이어서 저장을 건너뜁니다.");
    return;
  }

  chrome.storage.local.get(["userId"], (result) => {
    if (result.userId !== userLoginInfo.userId) {
      chrome.storage.local.set({
        userId: userLoginInfo.userId,
        access_token: userLoginInfo.access_token,
      });
    }
  });
}

// ===============================================================================================
// [URL 정보 수신 및 응답 처리]
// ===============================================================================================

// 페이지 정보를 저장할 변수
let currentUrl = null;
let readingTimeInfo = {
  readingTime: null,
  stats: null
};

// <1> contentScript.js로부터 페이지 정보 수신
chrome.runtime.onMessage.addListener((message, sender) => {
  if (message.action === "GET_PAGE_INFO") {
    currentUrl = message.url;
    readingTimeInfo.readingTime = message.readingTime;
    readingTimeInfo.stats = message.stats;
    console.log("저장된 통계:", readingTimeInfo.stats);
  }
});

// <2> StorageView로 페이지 정보 응답
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  if (message.action === "getPageInfo") {
    sendResponse({ url: currentUrl, readingTime: readingTimeInfo.readingTime });
  }
  return true;
});


