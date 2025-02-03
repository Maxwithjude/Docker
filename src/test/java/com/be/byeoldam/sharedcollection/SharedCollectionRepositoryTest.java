package com.be.byeoldam.sharedcollection;

import com.be.byeoldam.config.AuditingConfig;
import com.be.byeoldam.domain.sharedcollection.repository.SharedCollectionRepository;
import com.be.byeoldam.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(AuditingConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SharedCollectionRepositoryTest {

    @Autowired
    private SharedCollectionRepository sharedCollectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("공유컬렉션 생성")
    void createSharedCollection() {

    }
}
