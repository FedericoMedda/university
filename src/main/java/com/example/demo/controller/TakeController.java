package com.example.demo.controller;

import com.example.demo.service.TakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/take") //path in cui trovare le API
public class TakeController {

    private final TakeService takeService; //riferimento per il livello service

    @Autowired
    public TakeController(TakeService takeService) { //costruttore riferimento livello service
        this.takeService = takeService;
    }


    //Numero di Corsi seguiti da uno studente dato il suo id
    @GetMapping(path = "/getNumberOfCourseFallowedByAStudent/{studentId}")
    public ResponseEntity<?> getNumberOfCourseFallowedByAStudent(@PathVariable("studentId") Long studentId) {
        try {
            return new ResponseEntity<Integer>(
                    this.takeService.getNumberOfCourseFallowedByAStudent(studentId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //Numero di studenti che seguono un corso, dato il suo id
    @GetMapping(path = "/getNumberOfStudentsThatFollowACourseByCourseId/{courseId}")
    public ResponseEntity<?> getNumberOfStudentsThatFollowACourseByCourseId(@PathVariable("courseId") Long courseId) {
        try {
            return new ResponseEntity<Integer>(
                    this.takeService.getNumberOfStudentsThatFollowACourseByCourseId(courseId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
