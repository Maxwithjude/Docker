package com.be.byeoldam.common.resolver;

import com.be.byeoldam.common.annotation.UserId;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class LoginUserResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // @UserId가 붙어 있고, 파라미터 타입이 long이면 처리
        return parameter.hasParameterAnnotation(UserId.class) &&
                (parameter.getParameterType().equals(Long.class) || parameter.getParameterType().equals(long.class));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Object userId = request.getAttribute("userId");  // 필터에서 저장한 값 가져옴

        if (userId == null) {
            throw new IllegalArgumentException("userId를 찾을 수 없습니다.");
        }

        return Long.parseLong(userId.toString());  // long 타입으로 변환해서 반환
    }
}
