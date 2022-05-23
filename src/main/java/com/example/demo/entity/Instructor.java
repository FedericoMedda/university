package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name="instructor")
public class Instructor {
    @Id
    @SequenceGenerator(
            name = "instructor_sequence",
            sequenceName = "instructor_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "instructor_sequence"
    )
    @Column(name = "instructor_id")
    private Long instructorId;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "subject_teach")
    private String subjectTeach;

    @OneToMany(mappedBy="instructor")
    private Set<Course> courses;

    public Instructor() {
    }

    public Instructor(String name, String surname, String subjectTeach) {
        this.name = name;
        this.surname = surname;
        this.subjectTeach = subjectTeach;
    }

    public Instructor(Long instructorId, String name, String surname, String subjectTeach) {
        this.instructorId = instructorId;
        this.name = name;
        this.surname = surname;
        this.subjectTeach = subjectTeach;
    }

    public Instructor(Long instructorId, String name, String surname, String subjectTeach, Set<Course> courses) {
        this.instructorId = instructorId;
        this.name = name;
        this.surname = surname;
        this.subjectTeach = subjectTeach;
        this.courses = courses;
    }


    public Long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSubjectTeach() {
        return subjectTeach;
    }

    public void setSubjectTeach(String subjectTeach) {
        this.subjectTeach = subjectTeach;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }
}


