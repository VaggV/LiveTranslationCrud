package com.vaggelis.livetranslationcrud.repository;

import com.vaggelis.livetranslationcrud.model.Translation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TranslationRepository extends JpaRepository<Translation, Long> {
    List<Translation> findByUserid(String userid);
}
