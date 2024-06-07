package com.example.studentdirectoryfb;

public class Doctor {
    String name, degree, specialization;

    public Doctor() {
    }

    public Doctor(String name, String degree, String specialization) {
        this.name = name;
        this.degree = degree;
        this.specialization = specialization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
