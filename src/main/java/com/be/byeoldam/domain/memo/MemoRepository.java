package com.be.byeoldam.domain.memo;

import com.be.byeoldam.domain.memo.model.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Long> {
}
