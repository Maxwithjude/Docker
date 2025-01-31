// 1. 알림배지

// 2. 컨텍스트 메뉴 생성 및 관리
// - 우클릭시 나타나는 '별담에 저장' 메뉴 생성
chrome.runtime.onInstalled.addListener(() => {
  chrome.contextMenus.create({
    id: "saveToStardam",
    title: "별담에 저장",
    contexts: ["page", "selection", "link"], // 페이지, 텍스트 선택, 링크 우클릭시 표시
  });
});

// - 메뉴 클릭 이벤트 처리

// 3. 탭 정보 관리
// - 현재 활성화된 탭의 URL, 제목 정보 가져오기
// - 탭 변경 이벤트 감지

// 4. API 통신 처리(백엔드 서버와 통신)

// 5. 메시지 리스너(extension창과 background script 사이의 통신)

// 6. 스토리지 관리
// - 사용자 설정 저장/로드
// - 캐시 데이터 관리

// 7. 에러 핸들링
