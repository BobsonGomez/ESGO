package com.esgo.backend.repository;

import com.esgo.backend.model.Report;
import com.esgo.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByUserOrderByIdDesc(User user); // Get latest first
}