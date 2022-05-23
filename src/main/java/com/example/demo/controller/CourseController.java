package com.example.demo.controller;

import com.example.demo.entity.Course;
import com.example.demo.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/course") //path in cui trovare le API
public class CourseController {

    //riferimento per il livello service
    private final CourseService courseService;

    //costruttore riferimento livello service
    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    //Lista dei corsi presenti
    @GetMapping(path = "/getCourses")
    public ResponseEntity<?> getCourses() {
        try {
            return new ResponseEntity<List<Course>>(
                    this.courseService.getCourses(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //POST usata quando si vogliono aggiungere risorse al nostro DB
    @PostMapping
    public ResponseEntity<?> registerNewCourse(@RequestBody Course course) { //RequestBody serve per prendere la richiesta nel body della chiamata
        try {
            courseService.addNewCourse(course);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //restituiamo la descrizione di un corso, dato il suo id
    @GetMapping(path = "getDescription/{courseId}")
    public ResponseEntity<?> getDescription(@PathVariable("courseId") Long courseId) {
        try {
            return new ResponseEntity<String>(
                    this.courseService.getDescription(courseId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //restituiamo il nome di un corso, dato il suo id
    @GetMapping(path = "getNameCourse/{courseId}")
    public ResponseEntity<?> getNameCourse(@PathVariable("courseId") Long courseId) {
        try {
            return new ResponseEntity<String>(
                    this.courseService.getNameCourse(courseId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //restituiamo l'insegnante del corso, dato il suo id
    @GetMapping(path = "getInstructorCourse/{courseId}")
    public ResponseEntity<?> getInstructorCourse(@PathVariable("courseId") Long courseId) {
        try {
            return new ResponseEntity<String>(
                    this.courseService.getInstructorCourse(courseId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //restituiamo il numero di corsi tenuti da un insegnante, dato il suo id
    @GetMapping(path = "getNumberOfCoursesTaughtByAnInstructor/{instructorId}")
    public ResponseEntity<?> getNumberOfCoursesTaughtByAnInstructor(@PathVariable("instructorId") Long instructorId) {
        try {
            return new ResponseEntity<Integer>(
                    this.courseService.getNumberOfCoursesTaughtByAnInstructor(instructorId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



}