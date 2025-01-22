package com.be.byeoldam.exception;

import com.be.byeoldam.common.ResponseTemplate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;


    @Test
    @DisplayName("CustomException 예외 처리 테스트")
    void handleCustomException() {

        // given
        CustomException customException = new CustomException("CustomException");

        // when
        ResponseTemplate<String> responseTemplate = globalExceptionHandler.handleCustomException(customException);

        // then
        assertThat(customException).isInstanceOf(CustomException.class);
        assertThat(responseTemplate.getMessage()).isEqualTo("CustomException");
        assertThat(responseTemplate.isStatus()).isFalse();
    }

    @Test
    @DisplayName("Exception 예외 처리 테스트")
    void handleException() {

        // given
        Exception exception = new Exception("Exception");

        // when
        ResponseTemplate<String> responseTemplate = globalExceptionHandler.handleException(exception);

        // then
        assertThat(exception).isInstanceOf(Exception.class);
        assertThat(responseTemplate.getMessage()).isEqualTo("Exception");
        assertThat(responseTemplate.isStatus()).isFalse();
    }
}