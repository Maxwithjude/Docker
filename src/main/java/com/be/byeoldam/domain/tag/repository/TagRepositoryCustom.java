package com.be.byeoldam.domain.tag.repository;

public interface TagRepositoryCustom {
    void decrementReferenceCountByName(String name);  // name을 기준으로 referenceCount 감소
}
