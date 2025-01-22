package com.be.byeoldam.user;


import com.be.byeoldam.config.AuditingConfig;
import com.be.byeoldam.domain.user.dto.UserRegisterDto;
import com.be.byeoldam.domain.user.model.User;
import com.be.byeoldam.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@DataJpaTest
@Import(AuditingConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @Rollback(false)
    void testSaveAndFindUser() {
        UserRegisterDto dto = new UserRegisterDto("test@example.com", "1234", "testNickname");

        User user = User.fromDto(dto)
                .provider(User.Provider.KAKAO)
                .profileUrl("www.naver.com")
                .build();
        userRepository.save(user);

        User retrievedUser = userRepository.findById(user.getId()).orElse(null);
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(retrievedUser.getNickname()).isEqualTo("testNickname");
        assertThat(retrievedUser.getIsActive()).isEqualTo(User.AccountStatus.ACTIVE);
        assertThat(retrievedUser.getProvider()).isEqualTo(User.Provider.KAKAO);
        assertThat(retrievedUser.getProfileUrl()).isEqualTo("www.naver.com");
    }
}
