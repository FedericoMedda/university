package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.entity.Instructor;
import com.example.demo.entity.Student;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TakeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TakeServiceTest {
    @Mock //invece che usare @Autowired nei test si usa @Mockito
    private StudentRepository studentRepositoryUnderTest;

    @Mock
    private CourseRepository courseRepositoryUnderTest;

    @Mock
    private TakeRepository takeRepositoryUnderTest;

    private AutoCloseable autoCloseable;

    private TakeService underTest;

    @BeforeEach
        //prima di ogni test
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this); // iniziliazza tutti mock
        underTest = new TakeService(studentRepositoryUnderTest, courseRepositoryUnderTest, takeRepositoryUnderTest);
    }

    @AfterEach
        //dopo i test
    void tearDown() throws Exception {
        autoCloseable.close(); //questo chiude le risorse dopo i test
        studentRepositoryUnderTest.deleteAll();
        courseRepositoryUnderTest.deleteAll();
        takeRepositoryUnderTest.deleteAll();
    }

    //----- INIZIO TEST -----

    /**
     * Test metodo getNumberOfCourseFallowedByAStudent
     * Se tutto va a buon fine, restituisce il numero di corsi seguiti da uno studente
     */
    @Test
    void itShouldTakeTheNumberOfCourseFollowedByAStudent() {
        //given
        Student studentTest = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        //when
        long studentId = any(long.class);

        given(studentRepositoryUnderTest.findById(studentId))
                .willReturn(Optional.of(studentTest));

        underTest.getNumberOfCourseFallowedByAStudent(studentId);

        //then
        verify(takeRepositoryUnderTest).getNumberOfCoursesFollowedByAStudentByStudentId(studentId);

    }

    /**
     * Test metodo getNumberOfCourseFallowedByAStudent
     * Se tutto va a buon fine, restituisce il numero di corsi seguiti da uno studente
     */
    @Test
    void itShouldNotTakeTheNumberOfCourseFollowedByAStudent() {
        //given
        Student studentTest = new Student(
                "Federico",
                "Medda",
                "federicomedda93@gmail.com",
                LocalDate.of(2000, Month.JANUARY, 5)
        );

        //when
        long studentId = any(long.class);

        given(studentRepositoryUnderTest.findById(studentId))
                .willReturn(Optional.empty());

        //then
        assertThatThrownBy( () -> underTest.getNumberOfCourseFallowedByAStudent(studentId)) //assicuriamo che scatti il throw quando chiamiamo il metodo uploadStudent
                .isInstanceOf(IllegalStateException.class) //classe dell'errore che si deve ottenere
                .hasMessageContaining("The student with id "+studentId+ "doesn't exist"); //scattato l'errore dobbiamo avere questo messaggio, cioè quello programmato

        verify(takeRepositoryUnderTest, never()).getNumberOfCoursesFollowedByAStudentByStudentId(any());

    }

    /**
     * Test metodo getNumberOfStudentsThatFollowACourseByCourseId
     * Se tutto va a buon fine, restituisce il numero di studenti che seguono un determinato corso
     * public int getNumberOfStudentsThatFollowACourseByCourseId(Long courseId)
     */
    @Test
    void itShouldTakeTheNumberOfStudentsThatFollowACourseByCourseId() {
        //given
        Instructor instructorTest = new Instructor(
                "Sergej ",
                "Lavrov ",
                "Foreign Policy");

        Course courseTest = new Course(
                "Geography",
                "Basic knowledge about geography",
                instructorTest);

        //when
        long courseId = any(long.class);

        given(courseRepositoryUnderTest.findById(courseId))
                .willReturn(Optional.of(courseTest));

        underTest.getNumberOfStudentsThatFollowACourseByCourseId(courseId);

        //then
        verify(takeRepositoryUnderTest).getNumberOfStudentsThatFollowACourseByCourseId(courseId);

    }

    /**
     * Test metodo getNumberOfStudentsThatFollowACourseByCourseId
     * Se tutto va a buon fine, restituisce il numero di corsi seguiti da uno studente
     * public int getNumberOfStudentsThatFollowACourseByCourseId(Long courseId)
     */
    @Test
    void itShouldNotTakeTheNumberOfStudentsThatFollowACourseByCourseIdBecauseTheCourseIdIsNotValid() {
        //given

        //when
        long courseId = any(long.class);

        given(courseRepositoryUnderTest.findById(courseId))
                .willReturn(Optional.empty());

        //then
        assertThatThrownBy( () -> underTest.getNumberOfStudentsThatFollowACourseByCourseId(courseId)) //assicuriamo che scatti il throw quando chiamiamo il metodo uploadStudent
                .isInstanceOf(IllegalStateException.class) //classe dell'errore che si deve ottenere
                .hasMessageContaining("Course with id "+courseId+" doesn't exist"); //scattato l'errore dobbiamo avere questo messaggio, cioè quello programmato

        verify(takeRepositoryUnderTest, never()).getNumberOfStudentsThatFollowACourseByCourseId(any());

    }
}