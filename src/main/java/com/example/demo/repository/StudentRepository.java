package com.example.demo.repository;

import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> { //tipo della classe e della chiave in tabella

    @Transactional
    @Query(value = "SELECT * FROM Student s WHERE s.email = :email", nativeQuery = true)
    Optional<Student> findStudentByEmail(String email);


}
