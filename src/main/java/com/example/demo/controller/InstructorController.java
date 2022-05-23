package com.example.demo.controller;

import com.example.demo.entity.Instructor;
import com.example.demo.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/instructor") //path in cui trovare le API
public class InstructorController {

    private final InstructorService instructorService; //riferimento per il livello service

    @Autowired
    public InstructorController(InstructorService instructorService) { //costruttore riferimento livello service
        this.instructorService = instructorService;
    }


    //ottieni la lista degli insegnanti
    @GetMapping(path="/getInstructors")
    public ResponseEntity<?> getInstructors(){
        try {
            return new ResponseEntity<List<Instructor>>(
                    instructorService.getInstructors(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    //ottieni informazioni su insegnante, dato il suo id
    @GetMapping(path = "getInstructor/{instructorId}")
    public ResponseEntity<?> getInstructor(@PathVariable("instructorId")Long instructorId){
        try {
            return new ResponseEntity<Instructor>(
                    instructorService.getInstructor(instructorId), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    //cancella un insegnante dato il suo id
    @DeleteMapping(path = "deleteInstructor/{instructorId}")
    public ResponseEntity<?> deleteInstructor(@PathVariable("instructorId")Long instructorId){
        try {
            instructorService.deleteInstructor(instructorId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
