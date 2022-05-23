package com.example.demo.repository;

import com.example.demo.entity.Course;
import com.example.demo.entity.Instructor;
import com.example.demo.entity.Student;
import com.example.demo.entity.Take;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TakeRepositoryTest {

    @Autowired
    private TakeRepository underTest;

    @Autowired
    private StudentRepository underTest2;

    @Autowired
    private CourseRepository underTest3;

    @Autowired
    private InstructorRepository underTest4;


    @AfterEach
        //Dopo ogni test viene eliminato tutto, per avere un ambiente pulito
    void tearDown(){
        underTest.deleteAll();
        underTest2.deleteAll();
        underTest3.deleteAll();
        underTest4.deleteAll();
    }


    /**
         Test del metodo getNumberOfCourseFallowedByAStudent all'interno di TakeRepository
         public int getNumberOfCourseFallowedByAStudent(Long studentId);
     */
    @Test
    void itShouldReturnTheNumberOfCourseFallowedByAStudent() {
        //given
        Student studentTest = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        Instructor instructorTest = new Instructor(
                "Walter",
                "White",
                "Chemistry");

        Course courseTest = new Course(
                "Physics",
                "Basic knowledge about physics",
                instructorTest);

        Take takeTest = new Take(studentTest, courseTest);

        underTest2.save(studentTest);
        underTest4.save(instructorTest);
        underTest3.save(courseTest);
        underTest.save(takeTest);

        //when
        String email = "federicomedda93@gmail.com";
        int numberOfCourseFallowed = underTest.getNumberOfCoursesFollowedByAStudentByStudentId(studentTest.getId());

        //then
        assertThat(numberOfCourseFallowed).isEqualTo(1);
    }

    /** Deve restituire il numero dei corsi seguiti da uno studente, passato l'id dello studente
     *  public int getNumberOfCoursesFallowedByAStudent(Long studentId);
     */
    @Test
    void itShouldTakeTheNumberOfCourseFollowedByAStudentByStudentId(){
        //given
        Student studentTest = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        Instructor instructorTest = new Instructor(
                "Walter",
                "White",
                "Chemistry");

        Course courseTest1 = new Course(
                "Physics",
                "Basic knowledge about physics",
                instructorTest);

        Course courseTest2 = new Course(
                "Computer Science",
                "Basic knowledge about computer science",
                instructorTest);

        Take takeTest1 = new Take(studentTest, courseTest1);
        Take takeTest2 = new Take(studentTest, courseTest2);

        underTest2.save(studentTest);
        underTest4.save(instructorTest);
        underTest3.save(courseTest1);
        underTest.save(takeTest1);
        underTest3.save(courseTest2);
        underTest.save(takeTest2);

        //when
        int numberOfCoursesExpected = 2;
        int numberOfCourseExtracted = underTest.getNumberOfCoursesFollowedByAStudentByStudentId(studentTest.getId());

        //then
        assertThat(numberOfCoursesExpected).isEqualTo(numberOfCourseExtracted);
    }

    /** Deve restituire il numero degli studenti che seguono un corso, passato l'id del corso
     *   public int getNumberOfStudentsThatFollowACourseByCourseId(Long studentId);
     */
    @Test
    void itShouldTakeTheNumberOfStudentsThatFollowACourseByCourseId(){
        //given
        Student studentTest1 = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        Student studentTest2 = new Student(
                "Giovanna",
                "D'Arco",
                "giovannadarco@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        Instructor instructorTest = new Instructor(
                "Walter",
                "White",
                "Chemistry");

        Course courseTest = new Course(
                "Physics",
                "Basic knowledge about physics",
                instructorTest);


        Take takeTest1 = new Take(studentTest1, courseTest);
        Take takeTest2 = new Take(studentTest2, courseTest);

        underTest2.save(studentTest1);
        underTest2.save(studentTest2);
        underTest4.save(instructorTest);
        underTest3.save(courseTest);
        underTest.save(takeTest1);
        underTest.save(takeTest2);

        //when
        int numberOfStudentsExpected = 2;
        Long courseId = courseTest.getCourseId();
        int numberOfStudentsExtracted = underTest.getNumberOfStudentsThatFollowACourseByCourseId(courseId);

        //then
        assertThat(numberOfStudentsExpected).isEqualTo(numberOfStudentsExtracted);
    }


}