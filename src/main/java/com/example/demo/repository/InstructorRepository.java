package com.example.demo.repository;

import com.example.demo.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {

    @Transactional
    @Query(value = "SELECT i.name FROM instructor i WHERE i.instructor_id  = :instructorId", nativeQuery = true)
    public String getNameInstructor(Long instructorId);

    @Transactional
    @Query(value = "SELECT i.surname FROM instructor i WHERE i.instructor_id  = :instructorId", nativeQuery = true)
    public String getSurnameInstructor(Long instructorId);

    @Transactional
    @Query(value = "SELECT i.name, i.surname FROM instructor i WHERE i.instructor_id  = :instructorId", nativeQuery = true)
    public String getNameAndSurnameInstructor(Long instructorId);

}
