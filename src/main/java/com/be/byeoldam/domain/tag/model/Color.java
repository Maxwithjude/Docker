package com.be.byeoldam.domain.tag.model;

public enum Color {
    PASTEL_PINK("#FFB6C1"), // 파스텔 핑크
    PASTEL_BLUE("#AEC6FF"), // 파스텔 블루
    PASTEL_YELLOW("#FFFFCC"), // 파스텔 옐로우
    PASTEL_GREEN("#B2FFB6"), // 파스텔 그린
    PASTEL_ORANGE("#FFCC99"), // 파스텔 오렌지
    PASTEL_LAVENDER("#E6E6FF"); // 파스텔 라벤더

    private final String hex; // 색상의 HEX 코드 저장을 위한 필드

    // 생성자
    Color(String hex) {
        this.hex = hex; // 생성자에서 HEX 값 초기화
    }

    // HEX 값을 반환하는 메소드
    public String getHex() {
        return hex;
    }

    // enum 항목을 문자열로 출력할 때 HEX 값을 반환하도록 오버라이드
    @Override
    public String toString() {
        return hex;
    }
}
