package com.be.byeoldam.domain.memo;

import com.be.byeoldam.domain.bookmark.model.Bookmark;
import com.be.byeoldam.domain.bookmark.repository.BookmarkRepository;
import com.be.byeoldam.domain.memo.dto.MemoRequest;
import com.be.byeoldam.domain.memo.dto.MemoResponse;
import com.be.byeoldam.domain.memo.model.Memo;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import com.be.byeoldam.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemoServiceTest {

    @InjectMocks
    private MemoService memoService;

    @Mock
    private MemoRepository memoRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookmarkRepository bookmarkRepository;

    private User user;
    private Bookmark bookmark;
    private Memo memo;
    private MemoRequest request;

    @BeforeEach
    void setUp() {
        user = User.builder().nickname("testUser").build();
        ReflectionTestUtils.setField(user, "id", 1L);

        bookmark = Bookmark.builder()
                .user(user)
                .build();
        ReflectionTestUtils.setField(bookmark, "id", 1L);

        memo = Memo.builder()
                .content("테스트 메모")
                .user(user)
                .bookmark(bookmark)
                .build();
        ReflectionTestUtils.setField(memo, "id", 1L);

        request = new MemoRequest("수정된 메모 내용");
    }

    @Test
    @DisplayName("메모 생성 테스트 (성공)")
    void createMemo_Success() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(bookmarkRepository.findById(1L)).thenReturn(Optional.of(bookmark));
        when(memoRepository.save(any(Memo.class))).thenReturn(memo);

        // when
        MemoResponse response = memoService.createMemo(1L, 1L, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isEqualTo("수정된 메모 내용");
        verify(memoRepository, times(1)).save(any(Memo.class));
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 메모 생성 시 예외 발생")
    void createMemo_UserNotFound_ThrowsException() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memoService.createMemo(1L, 1L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("메모 수정 테스트 (성공)")
    void updateMemo_Success() {
        // given
        when(memoRepository.findById(1L)).thenReturn(Optional.of(memo));

        // when
        MemoResponse response = memoService.updateMemo(1L, 1L, 1L, request);

        // then
        assertThat(response.getContent()).isEqualTo(request.getContent());
    }

    @Test
    @DisplayName("본인이 작성하지 않은 메모 수정 시 예외 발생")
    void updateMemo_Unauthorized_ThrowsException() {
        // given
        User anotherUser = User.builder().nickname("anotherUser").build();
        ReflectionTestUtils.setField(anotherUser, "id", 2L);

        Memo otherUserMemo = Memo.builder()
                .content("다른 사용자의 메모")
                .user(anotherUser)
                .bookmark(bookmark)
                .build();

        when(memoRepository.findById(1L)).thenReturn(Optional.of(otherUserMemo));

        // when & then
        assertThatThrownBy(() -> memoService.updateMemo(1L, 1L, 1L, request))
                .isInstanceOf(CustomException.class)
                .hasMessage("해당 메모에 대한 권한이 없습니다.");
    }

    @Test
    @DisplayName("메모 삭제 테스트 (성공)")
    void deleteMemo_Success() {
        // given
        when(memoRepository.findById(1L)).thenReturn(Optional.of(memo));

        // when
        memoService.deleteMemo(1L, 1L, 1L);

        // then
        verify(memoRepository, times(1)).delete(memo);
    }

    @Test
    @DisplayName("본인이 작성하지 않은 메모 삭제 시 예외 발생")
    void deleteMemo_Unauthorized_ThrowsException() {
        // given
        User anotherUser = User.builder().nickname("anotherUser").build();
        ReflectionTestUtils.setField(anotherUser, "id", 2L);

        Memo otherUserMemo = Memo.builder()
                .content("다른 사용자의 메모")
                .user(anotherUser)
                .bookmark(bookmark)
                .build();

        when(memoRepository.findById(1L)).thenReturn(Optional.of(otherUserMemo));

        // when & then
        assertThatThrownBy(() -> memoService.deleteMemo(1L, 1L, 1L))
                .isInstanceOf(CustomException.class)
                .hasMessage("해당 메모에 대한 권한이 없습니다.");
    }

    @Test
    @DisplayName("특정 북마크에 속한 메모 목록 조회 테스트 (성공)")
    void getMemoList_Success() {
        // given
        when(bookmarkRepository.existsById(1L)).thenReturn(true);
        when(memoRepository.findByBookmarkId(1L)).thenReturn(List.of(memo));

        // when
        List<MemoResponse> responseList = memoService.getMemoList(1L);

        // then
        assertThat(responseList).hasSize(1);
        assertThat(responseList.get(0).getContent()).isEqualTo("테스트 메모");
    }

    @Test
    @DisplayName("존재하지 않는 북마크의 메모 조회 시 예외 발생")
    void getMemoList_BookmarkNotFound_ThrowsException() {
        // given
        when(bookmarkRepository.existsById(1L)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> memoService.getMemoList(1L))
                .isInstanceOf(CustomException.class)
                .hasMessage("북마크를 찾을 수 없습니다.");
    }
}
