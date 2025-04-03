package br.com.health.cloud.repository

import br.com.health.cloud.entities.Patient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PatientRepository : JpaRepository<Patient, Long> {

    @Query(value = "SELECT p FROM Patient p WHERE p.fullName = :fullName AND p.address.complement = :complement")
    fun checkPatientAlreadyExists(
        @Param("fullName") fullName: String? = null,
        @Param("complement") complement: String? = null
    ): Patient?
}
