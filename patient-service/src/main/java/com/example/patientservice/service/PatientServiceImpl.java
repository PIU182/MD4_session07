package com.example.patientservice.service;

import com.example.patientservice.dto.PatientRequestDTO;
import com.example.patientservice.dto.PatientResponseDTO;
import com.example.patientservice.entity.Patient;
import com.example.patientservice.exception.ResourceNotFoundException;
import com.example.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    public PatientServiceImpl(PatientRepository patientRepository) { this.patientRepository = patientRepository; }

    @Override
    public PatientResponseDTO create(PatientRequestDTO r) {
        Patient p = new Patient();
        p.setFullName(r.getFullName()); p.setDateOfBirth(r.getDateOfBirth()); p.setGender(r.getGender());
        p.setPhoneNumber(r.getPhoneNumber()); p.setAddress(r.getAddress()); p.setMedicalHistory(r.getMedicalHistory());
        return toResponse(patientRepository.save(p));
    }

    @Override
    public PatientResponseDTO getById(Long id) {
        return toResponse(patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id = " + id)));
    }

    @Override
    public List<PatientResponseDTO> getAll() {
        return patientRepository.findAll().stream().map(this::toResponse).toList();
    }

    private PatientResponseDTO toResponse(Patient p) {
        return new PatientResponseDTO(p.getId(), p.getFullName(), p.getDateOfBirth(), p.getGender(),
                p.getPhoneNumber(), p.getAddress(), p.getMedicalHistory());
    }
}
