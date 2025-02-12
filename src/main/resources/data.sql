-- 유저 더미 데이터
INSERT INTO user (email, password, nickname, role, profile_url, is_verified, is_active, provider, alert_day)
VALUES
    ('user1@example.com', 'password1', 'User1', 'ROLE_USER', 'https://example.com/default-profile.png', true, 'ACTIVE', 'LOCAL', 7),
    ('user2@example.com', 'password2', 'User2', 'ROLE_USER', 'https://example.com/default-profile.png', true, 'ACTIVE', 'LOCAL', 3),
    ('user3@example.com', 'password3', 'User3', 'ROLE_USER', 'https://example.com/default-profile.png', true, 'ACTIVE', 'LOCAL', 5);

-- 개인 컬렉션 더미 데이터
INSERT INTO personal_collection (name, user_id, created_at, updated_at)
VALUES
    ('개인 컬렉션 1', 1, NOW(), NOW()),
    ( '개인 컬렉션 2', 2, NOW(), NOW());

-- 공유 컬렉션 더미 데이터
INSERT INTO shared_collection (name, created_at, updated_at)
VALUES
    ('공유 컬렉션 A', NOW(), NOW()),
    ('공유 컬렉션 B', NOW(), NOW());

-- 공유 컬렉션 유저 관계 (OWNER & MEMBER)
INSERT INTO shared_user (user_id, shared_collection_id, role, created_at, updated_at)
VALUES
    (1, 1, 'OWNER', NOW(), NOW()),
    (2, 1, 'MEMBER', NOW(), NOW()),
    (3, 2, 'OWNER', NOW(), NOW());

-- 북마크 URL 더미 데이터
INSERT INTO bookmark_url (url, reference_count, reading_time)
VALUES
    ('https://github.com/eenzzi', 2, 300),
    ('https://www.yna.co.kr/view/PYH20250210099600057?section=photo/index&site=visual_photo_list', 1, 450);

-- 북마크 더미 데이터 (개인 및 공유 컬렉션)
INSERT INTO bookmarks (personal_collection_id, shared_collection_id, url_id, user_id, created_at, updated_at, is_read, priority)
VALUES
    (1, NULL, 1, 1, NOW(), NOW(), false, true),
    (2, NULL, 2, 2, NOW(), NOW(), true, false),
    (NULL, 1, 1, 2, NOW(), NOW(), false, true);

-- 태그 더미 데이터
INSERT INTO tag (name, reference_count, color, bolder_color)
VALUES
    ('IT', 2, 'PASTEL_BLUE', 'PASTEL_BLUE'),
    ('개발', 1, 'PASTEL_GREEN', 'PASTEL_GREEN'),
    ( '디자인', 1, 'PASTEL_PINK', 'PASTEL_PINK');

-- 북마크-태그 관계
INSERT INTO bookmark_url_tag (bookmark_url_id, tag_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 3);

-- 유저 관심 태그
INSERT INTO user_tag (user_id, tag_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3);

-- RSS 더미 데이터
INSERT INTO rss ( name, rss_url, created_at, updated_at)
VALUES
    ('SBS 뉴스', 'https://news.sbs.co.kr/news/sitemapRSS.do', NOW(), NOW()),
    ( 'shinny96님의 블로그', 'https://rss.blog.naver.com/shinny96.xml', NOW(), NOW());

-- 유저-RSS 구독 관계
INSERT INTO user_rss (user_id, rss_id, latest_title, previous_title, is_read)
VALUES
    ( 1, 1, 'Latest SBS 뉴스', 'Previous SBS 뉴스', 0),
    ( 2, 2, 'Latest shinny96님의 블로그', 'Previous shinny96님의 블로그', 1);

-- 알림 더미 데이터
INSERT INTO notification (user_id, bookmark_id, shared_collection_id, type, message, nickname, created_at, updated_at)
VALUES
    (1, 1, NULL, 'BOOKMARK', '7일 동안 읽지 않은 북마크가 있습니다.', null, NOW(), NOW()),
    (2, NULL, 1, 'INVITE', '공유 컬렉션 A에 초대되었습니다.', NOW(), 'User1', NOW());

-- 메모 더미 데이터
INSERT INTO memo (user_id, bookmark_id, content, created_at, updated_at)
VALUES
    (1, 1, '이 글 정말 유용하네요!', NOW(), NOW()),
    (2, 2, '나중에 다시 읽어봐야지', NOW(), NOW());