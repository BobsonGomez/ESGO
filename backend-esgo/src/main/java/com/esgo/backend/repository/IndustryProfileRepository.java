package com.esgo.backend.repository;

import com.esgo.backend.model.IndustryProfile;
import com.esgo.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface IndustryProfileRepository extends JpaRepository<IndustryProfile, Long> {
    Optional<IndustryProfile> findByUser(User user);
}