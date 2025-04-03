package br.com.health.cloud.repository

import br.com.health.cloud.entities.Patient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PatientRepository : JpaRepository<Patient, Long>
