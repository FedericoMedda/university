package com.example.demo.repository;

import com.example.demo.entity.Take;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TakeRepository extends JpaRepository<Take, Long> {

    @Transactional
    @Query(value = "SELECT COUNT(t.course_id) FROM take t WHERE t.student_id = :studentId", nativeQuery = true)
    public int getNumberOfCoursesFollowedByAStudentByStudentId(Long studentId);

    @Transactional
    @Query(value = "SELECT COUNT(t.student_id) FROM take t WHERE t.course_id = :course_id", nativeQuery = true)
    public int getNumberOfStudentsThatFollowACourseByCourseId(Long course_id);

}
