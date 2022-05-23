package com.example.demo.repository;

import com.example.demo.entity.Course;
import com.example.demo.entity.Instructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Andiamo a testare tutti i metodi da noi creati in CourseRepository
 */

@DataJpaTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository underTest;

    @Autowired
    private InstructorRepository underTest2;

    @AfterEach //Dopo ogni test viene eliminato tutto, per avere un ambiente pulito
    void tearDown(){
        underTest.deleteAll();
        underTest2.deleteAll();
    }

    /**
     Test del metodo getNameCourseByCourseId all'interno di CourseRepository
     public String getNameCourseByCourseId(Long course_id);
    */
    @Test
    void itShouldCheckIfGetCourseNameByCourseId() {
        //given
        Instructor instructorTest = new Instructor(
                "Sergej ",
                "Lavrov ",
                "Foreign Policy"
        );

        Course courseTest = new Course(
                "Geography",
                "Basic knowledge about geography",
                instructorTest
        );

        underTest2.save(instructorTest);
        underTest.save(courseTest);

        //when
        Long courseId = courseTest.getCourseId();
        String courseTitleCaptured = underTest.getNameCourseByCourseId(courseId);

        //then
        String courseTitleExpected = courseTest.getTitle();
        assertThat(courseTitleCaptured).isEqualTo(courseTitleExpected);
    }

    /**
     *  Test del metodo getDescriptionById all'interno di CourseRepository
     * public String getDescriptionById(Long courseId);
     */
    @Test
    void itShouldReturnTheDescriptionOfTheCourseByCourseId() {
        //given
        Instructor instructorTest = new Instructor(
                "Sergej ",
                "Lavrov ",
                "Foreign Policy"
        );

        Course courseTest = new Course(
                "Geography",
                "Basic knowledge about geography",
                instructorTest
        );

        underTest2.save(instructorTest);
        underTest.save(courseTest);

        //when
        Long courseId = courseTest.getCourseId();
        String capturedCourseDescription = underTest.getDescriptionById(courseId);

        //then
        String expectedCourseDescription = courseTest.getDescription();
        assertThat(capturedCourseDescription).isEqualTo(expectedCourseDescription);
    }

    /**
     *  Test del metodo getInstructorId all'interno di CourseRepository
     * String getInstructorId(Long courseId);
     */
    @Test
    void itShouldReturnTheInstructorIdOfTheCourseByCourseId() {
        //given
        Instructor instructorTest = new Instructor(
                "Sergej ",
                "Lavrov ",
                "Foreign Policy"
        );

        Course courseTest = new Course(
                "Geography",
                "Basic knowledge about geography",
                instructorTest
        );

        underTest2.save(instructorTest);
        underTest.save(courseTest);

        //when
        Long courseId = courseTest.getCourseId();
        Long capturedInstructorId = underTest.getInstructorId(courseId);

        //then
        Long expectedInstructorId = instructorTest.getInstructorId();
        assertThat(expectedInstructorId).isEqualTo(capturedInstructorId);
    }

    /**
     * getNumberOfCoursesTaughtByAnInstructor
     * deve restituire il numero di corsi tenuti da un professore, dato il suo id
     * int getNumberOfCoursesTaughtByAnInstructor(Long instructorId);
     */
    @Test
    void itShouldReturnTheNumberOfCoursesTaughtByAInstructorGivenHisId(){
        //given
        Instructor instructorTest = new Instructor(
                "Sergej ",
                "Lavrov ",
                "Foreign Policy"
        );

        Course courseTest1 = new Course(
                "Geography 1",
                "Basic knowledge about geography",
                instructorTest
        );

        Course courseTest2 = new Course(
                "Geography 2",
                "Advanced knowledge about geography",
                instructorTest
        );
        underTest2.save(instructorTest);
        underTest.save(courseTest1);
        underTest.save(courseTest2);

        //when
        Long instructorId = instructorTest.getInstructorId();
        int courseTaughtByInstructorExtracted = underTest.getNumberOfCoursesTaughtByAnInstructor(instructorId);

        //then
        int courseTaughtByInstructorExpected = 2;
        assertThat(courseTaughtByInstructorExpected).isEqualTo(courseTaughtByInstructorExtracted);
    }


}