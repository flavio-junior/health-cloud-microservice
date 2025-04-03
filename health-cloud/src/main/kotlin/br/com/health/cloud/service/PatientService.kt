package br.com.health.cloud.service

import br.com.health.cloud.entities.Address
import br.com.health.cloud.entities.Patient
import br.com.health.cloud.exceptions.ObjectDuplicateException
import br.com.health.cloud.utils.common.UF
import br.com.health.cloud.repository.PatientRepository
import br.com.health.cloud.utils.common.Country
import br.com.health.cloud.utils.others.ConverterUtils.parseListObjects
import br.com.health.cloud.utils.others.ConverterUtils.parseObject
import br.com.health.cloud.vo.PatientRequestVO
import br.com.health.cloud.vo.PatientResponseVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class PatientService {

    @Autowired
    private lateinit var patientRepository: PatientRepository

    fun findAllPatients(): List<PatientResponseVO> {
        return parseListObjects(origin = patientRepository.findAll(), destination = PatientResponseVO::class.java)
    }

    fun savePatient(
        patient: PatientRequestVO,
        zipCode: Int? = null,
        city: String? = null,
        uf: UF? = null
    ): PatientResponseVO {
        val checkPatient = patientRepository.checkPatientAlreadyExists(
            fullName = patient.fullName,
            complement = patient.address?.complement
        )
        if (checkPatient != null) {
            throw ObjectDuplicateException(message = PATIENT_ALREADY_EXISTS)
        } else {
            val patientToSaved = parseObject(patient, Patient::class.java)
            patientToSaved.createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)
            patientToSaved.address = Address(
                street = patient.address?.street,
                number = patient.address?.number,
                district = patient.address?.district,
                complement = patient.address?.complement,
                code = zipCode,
                city = city,
                uf = uf,
                country = Country.BR
            )
            val patientSaved = patientRepository.save(patientToSaved)
            return parseObject(patientSaved, PatientResponseVO::class.java)
        }
    }

    companion object {
        const val PATIENT_ALREADY_EXISTS = "Patient Already Exists!"
    }
}
