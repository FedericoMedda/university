package com.example.demo.repository;

import com.example.demo.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> { //tipo della classe e della chiave in tabella{

        @Transactional
        @Query(value = "SELECT c.title FROM courses c WHERE c.course_id = :course_id", nativeQuery = true)
        public String getNameCourseByCourseId(Long course_id);

        @Transactional
        @Query(value = "SELECT c.description FROM courses c WHERE c.course_id  = :courseId", nativeQuery = true)
        public String getDescriptionById(Long courseId);

        @Transactional
        @Query(value = "SELECT c.instructor_id FROM courses c WHERE c.course_id  = :courseId", nativeQuery = true)
        Long getInstructorId(Long courseId);

        @Transactional
        @Query(value = "SELECT c.title FROM courses c WHERE c.course_id  = :courseId", nativeQuery = true)
        String getName(Long courseId);

        @Transactional
        @Query(value = "SELECT COUNT(c.course_id) FROM courses c WHERE c.instructor_id = :instructorId", nativeQuery = true)
        int getNumberOfCoursesTaughtByAnInstructor(Long instructorId);

}