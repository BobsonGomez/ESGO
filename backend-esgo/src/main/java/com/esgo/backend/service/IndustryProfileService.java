package com.esgo.backend.service;

import com.esgo.backend.model.IndustryProfile;
import com.esgo.backend.repository.IndustryProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class IndustryProfileService {

    @Autowired
    private IndustryProfileRepository repository;

    public List<IndustryProfile> getAllProfiles() {
        return repository.findAll();
    }

    public IndustryProfile publishProfile(IndustryProfile profile) {
        return repository.save(profile);
    }
}