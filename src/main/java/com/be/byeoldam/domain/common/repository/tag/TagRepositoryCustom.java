package com.be.byeoldam.domain.common.repository.tag;

public interface TagRepositoryCustom {
    void decrementReferenceCountByName(String name);  // name을 기준으로 referenceCount 감소
}
