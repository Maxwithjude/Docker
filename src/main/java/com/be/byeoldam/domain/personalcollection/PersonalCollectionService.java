package com.be.byeoldam.domain.personalcollection;

import com.be.byeoldam.domain.personalcollection.model.PersonalCollection;
import com.be.byeoldam.domain.personalcollection.repository.PersonalCollectionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class PersonalCollectionService {
    private PersonalCollectionRepository personalCollectionRepository;

    @Transactional
    public void save(PersonalCollection personalCollection) {

    }
}
