package com.example.demo.service;

import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TakeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TakeService {

    @Autowired
    private final StudentRepository studentRepository;

    @Autowired
    private final CourseRepository courseRepository;

    @Autowired
    private final TakeRepository takeRepository;

    @Autowired
    public TakeService(StudentRepository studentRepository, CourseRepository courseRepository, TakeRepository takeRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.takeRepository = takeRepository;
    }


    public int getNumberOfCourseFallowedByAStudent(Long studentId) {
        //controllo se l'id passato esiste
        if( !studentRepository.findById(studentId).isPresent() ){
            throw new IllegalStateException("The student with id "+studentId+ "doesn't exist");
        }
        return takeRepository.getNumberOfCoursesFollowedByAStudentByStudentId(studentId);
    }

    public int getNumberOfStudentsThatFollowACourseByCourseId(Long courseId) {
        courseRepository.findById(courseId)
                .orElseThrow( ()-> new IllegalStateException("Course with id "+courseId+" doesn't exist") );
        return takeRepository.getNumberOfStudentsThatFollowACourseByCourseId(courseId);
    }
}
