package com.example.demo.service;

import com.example.demo.entity.Course;
import com.example.demo.entity.Instructor;
import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TakeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.in;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock //invece che usare @Autowired nei test si usa @Mockito
    private InstructorRepository instructorRepositoryUnderTest;

    @Mock
    private CourseRepository courseRepositoryUnderTest;

    private AutoCloseable autoCloseable;

    private CourseService underTest;

    @BeforeEach
        //prima di ogni test
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this); // iniziliazza tutti mock
        underTest = new CourseService(courseRepositoryUnderTest, instructorRepositoryUnderTest);
    }

    @AfterEach
        //dopo i test
    void tearDown() throws Exception {
        autoCloseable.close(); //questo chiude le risorse dopo i test
        instructorRepositoryUnderTest.deleteAll();
        courseRepositoryUnderTest.deleteAll();
    }

    //----- INIZIO TEST -----

    /**
     * Test metodo getCourses
     * permette di ottenere la lista dei corsi
     */
    @Test
    void shouldIGetTheCoureList() {
        //given
        Instructor instructorTest = new Instructor(
                "Sergej ",
                "Lavrov ",
                "Foreign Policy");

        Course courseTest = new Course(
                "Geography",
                "Basic knowledge about geography",
                instructorTest);

        instructorRepositoryUnderTest.save(instructorTest);
        courseRepositoryUnderTest.save(courseTest);
        //When
        underTest.getCourses();

        //Then
        verify(courseRepositoryUnderTest).findAll();
    }

    /**
     * Test metodo addNewCourse
     * permette di aggiungere un nuovo corso
     */
    @Test
    void shouldIAddANewCourse() {
        //given
        Instructor instructorTest = new Instructor(
                "Sergej ",
                "Lavrov ",
                "Foreign Policy");

        Course courseTest = new Course(
                "Geography",
                "Basic knowledge about geography",
                instructorTest);

        //When
        given(courseRepositoryUnderTest.findById(courseTest.getCourseId()))
                .willReturn(Optional.empty());
        underTest.addNewCourse(courseTest);

        //Then
        verify(courseRepositoryUnderTest).save(courseTest);
    }

    /**
     * Test metodo addNewCourse
     * caso in cui l'id sia già inserito
     */
    @Test
    void shouldNotIAddANewCourseBeacuseCourseIdIsInvalid() {
        //given
        Instructor instructorTest = new Instructor(
                "Sergej ",
                "Lavrov ",
                "Foreign Policy");

        Course courseTest = new Course(
                "Geography",
                "Basic knowledge about geography",
                instructorTest);

        //When
        given(courseRepositoryUnderTest.findById(courseTest.getCourseId()))
                .willReturn(Optional.of(courseTest));

        //Then
        assertThatThrownBy( () -> underTest.addNewCourse(courseTest))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Course with id "+courseTest.getCourseId()+"already exist");

        verify(courseRepositoryUnderTest,never()).save(any());
    }

    /**
     * Test metodo addNewCourse
     * restituisce la descrizione del corso
     */
    @Test
    void ShouldIGetADescription() {
        //given
        Instructor instructorTest = new Instructor(
                "Sergej ",
                "Lavrov ",
                "Foreign Policy");

        Course courseTest = new Course(
                "Geography",
                "Basic knowledge about geography",
                instructorTest);

        instructorRepositoryUnderTest.save(instructorTest);
        courseRepositoryUnderTest.save(courseTest);

        //When
        given(courseRepositoryUnderTest.findById(courseTest.getCourseId()))
                .willReturn(Optional.of(courseTest));

        String descriptionObtained = underTest.getDescription(courseTest.getCourseId());

        //Then
        verify(courseRepositoryUnderTest).getDescriptionById(courseTest.getCourseId());
    }

    /**
     * Test metodo addNewCourse
     * caso in cui l'id non esiste
     */
    @Test
    void shouldNotIGetTheDescriptionCourseBeacuseIdDoesNotExist() {
        //given
        Instructor instructorTest = new Instructor(
                "Sergej ",
                "Lavrov ",
                "Foreign Policy");

        Course courseTest = new Course(
                "Geography",
                "Basic knowledge about geography",
                instructorTest);

        //When
        given(courseRepositoryUnderTest.findById(courseTest.getCourseId()))
                .willReturn(Optional.empty());

        //Then
        assertThatThrownBy( () -> underTest.getDescription(courseTest.getCourseId()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Course with id "+courseTest.getCourseId()+"does not exist");

        verify(courseRepositoryUnderTest,never()).getDescriptionById(courseTest.getCourseId());
    }

    /**
     * Test metodo getInstructorCourse
     * restituisce l'istruttore che tiene il corso
     */
    @Test
    void getInstructorOfTheCoursePassedById() {
        //given
        Instructor instructorTest = new Instructor(
                "Sergej ",
                "Lavrov ",
                "Foreign Policy");

        Course courseTest = new Course(
                "Geography",
                "Basic knowledge about geography",
                instructorTest);

        instructorRepositoryUnderTest.save(instructorTest);
        courseRepositoryUnderTest.save(courseTest);

        //When
        Long courseId = any(long.class);
        given(courseRepositoryUnderTest.findById(courseId))
                .willReturn(Optional.of(courseTest));

        underTest.getInstructorCourse(courseId);

        //Then
        verify(courseRepositoryUnderTest).getInstructorId(courseId);
        verify(instructorRepositoryUnderTest).getNameAndSurnameInstructor(courseId);

    }

    /**
     * Test metodo getInstructorCourse
     * errore durante la restituzione del istruttore
     */
    @Test
    void getingErrorFromTakenInstructorOfTheCoursePassedByIdWithIdNotValid() {
        //given
        Instructor instructorTest = new Instructor(
                "Sergej ",
                "Lavrov ",
                "Foreign Policy");

        Course courseTest = new Course(
                "Geography",
                "Basic knowledge about geography",
                instructorTest);

        instructorRepositoryUnderTest.save(instructorTest);
        courseRepositoryUnderTest.save(courseTest);

        //When
        Long courseId = any(long.class);
        given(courseRepositoryUnderTest.findById(courseId))
                .willReturn(Optional.empty());

        //Then
        assertThatThrownBy( () -> underTest.getInstructorCourse(courseId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Course with id "+courseId+" does not exist");
    }

    /**
     * Test metodo getNameCourse
     * Restituisce il nome del corso
     */
    @Test
    void ShouldGetNameCourse() {
        //given
        Instructor instructorTest = new Instructor(
                "Sergej ",
                "Lavrov ",
                "Foreign Policy");

        Course courseTest = new Course(
                "Geography",
                "Basic knowledge about geography",
                instructorTest);

        instructorRepositoryUnderTest.save(instructorTest);
        courseRepositoryUnderTest.save(courseTest);

        //When
        Long courseId = any(long.class);
        given(courseRepositoryUnderTest.findById(courseId))
                .willReturn(Optional.of(courseTest));

        underTest.getNameCourse(courseId);

        //Then
        verify(courseRepositoryUnderTest).getName(courseId);
    }

    /**
     * Test metodo getNameCourse
     * Non restituisce il nome del corso perchè inciampa nell'eccezione
     */
    @Test
    void ShouldNotGetNameCourse() {
        //given
        Instructor instructorTest = new Instructor(
                "Sergej ",
                "Lavrov ",
                "Foreign Policy");

        Course courseTest = new Course(
                "Geography",
                "Basic knowledge about geography",
                instructorTest);

        instructorRepositoryUnderTest.save(instructorTest);
        courseRepositoryUnderTest.save(courseTest);

        //When
        Long courseId = any(long.class);
        given(courseRepositoryUnderTest.findById(courseId))
                .willReturn(Optional.empty());

        //Then
        assertThatThrownBy( () -> underTest.getNameCourse(courseId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Course with id "+courseId+"does not exist");
    }

    /**
     * getNumberOfCoursesTaughtByAnInstructort
     * restituisce il numero di corsi tenuti da un insegnante
     */
    @Test
    void itShouldTakeThenumberOfCoursesTaughtByAnInstructor(){
        //given
        Instructor instructorTest = new Instructor(
                "Sergej ",
                "Lavrov ",
                "Foreign Policy");

        //when
        Long instructorId = any(long.class);
        given(instructorRepositoryUnderTest.findById(instructorId))
                .willReturn(Optional.of(instructorTest));

        underTest.getNumberOfCoursesTaughtByAnInstructor(instructorId);

        //Then
        verify(courseRepositoryUnderTest).getNumberOfCoursesTaughtByAnInstructor(any());

    }

    /**
     * getNumberOfCoursesTaughtByAnInstructort
     * controlliamo l'errore, l'id dellistruttore passato non è validop
     */
    @Test
    void itShouldNotTakeThenumberOfCoursesTaughtByAnInstructorBecauseTheInstructorIdIsNotValid(){
        //given
        Instructor instructorTest = new Instructor(
                "Sergej ",
                "Lavrov ",
                "Foreign Policy");

        //when
        Long instructorId = any(long.class);
        given(instructorRepositoryUnderTest.findById(instructorId))
                .willReturn(Optional.empty());

        //Then
        assertThatThrownBy( () -> underTest.getNumberOfCoursesTaughtByAnInstructor(instructorId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("The instructor with"+ instructorId +" does not exist");

        verify(courseRepositoryUnderTest, never()).getNumberOfCoursesTaughtByAnInstructor(instructorId);
    }
}