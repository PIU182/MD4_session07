package com.example.patientservice.service;
import com.example.patientservice.dto.PatientRequestDTO;
import com.example.patientservice.dto.PatientResponseDTO;
import java.util.List;
public interface PatientService {
    PatientResponseDTO create(PatientRequestDTO request);
    PatientResponseDTO getById(Long id);
    List<PatientResponseDTO> getAll();
}
