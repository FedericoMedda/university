package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private final CourseRepository courseRepository;

    @Autowired
    private final InstructorRepository instructorRepository;

    @Autowired
    public CourseService(CourseRepository courseRepository, InstructorRepository instructorRepository) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
    }


    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    public void addNewCourse(Course course) {
        if( courseRepository.findById(course.getCourseId()).isPresent() ){
            throw new IllegalStateException("Course with id "+course.getCourseId()+"already exist");
        }
        courseRepository.save(course);
    }

    public String getDescription(Long courseId) {
        if( !courseRepository.findById(courseId).isPresent() ){
            throw new IllegalStateException("Course with id "+courseId+"does not exist");
        }
        return courseRepository.getDescriptionById(courseId);
    }

    public String getInstructorCourse(Long courseId) {

        if( !courseRepository.findById(courseId).isPresent() )
        {
            throw new IllegalStateException("Course with id "+courseId+" does not exist");
        }
        long instructorId = courseRepository.getInstructorId(courseId);
        return instructorRepository.getNameAndSurnameInstructor(instructorId);
    }

    public String getNameCourse(Long courseId) {

        if( !courseRepository.findById(courseId).isPresent() ){
            throw new IllegalStateException("Course with id "+courseId+"does not exist");
        }
        return courseRepository.getName(courseId);

    }

    public int getNumberOfCoursesTaughtByAnInstructor(Long instructorId) {
        instructorRepository.findById(instructorId)
                .orElseThrow( ()-> new IllegalStateException("The instructor with"+ instructorId +" does not exist"));
        return courseRepository.getNumberOfCoursesTaughtByAnInstructor(instructorId);
    }
}
