package com.example.leadManagementSystem2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.leadManagementSystem2.Entity.Leads;

public interface LeadsRepository extends JpaRepository<Leads, Long> {

}
