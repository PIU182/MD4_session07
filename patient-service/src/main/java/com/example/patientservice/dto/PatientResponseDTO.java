package com.example.patientservice.dto;

import java.time.LocalDate;

public class PatientResponseDTO {
    private Long id;
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String address;
    private String medicalHistory;

    public PatientResponseDTO() {}
    public PatientResponseDTO(Long id, String fullName, LocalDate dateOfBirth, String gender,
                              String phoneNumber, String address, String medicalHistory) {
        this.id=id; this.fullName=fullName; this.dateOfBirth=dateOfBirth; this.gender=gender;
        this.phoneNumber=phoneNumber; this.address=address; this.medicalHistory=medicalHistory;
    }
    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public String getGender() { return gender; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }
    public String getMedicalHistory() { return medicalHistory; }
}
