package br.com.health.cloud.controller

import br.com.health.cloud.proxy.AddressProxy
import br.com.health.cloud.service.PatientService
import br.com.health.cloud.vo.PatientRequestVO
import br.com.health.cloud.vo.PatientResponseVO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping(value = ["/api/health/cloud/patient/v1"])
class PatientController {

    @Autowired
    private lateinit var proxy: AddressProxy

    @Autowired
    private lateinit var patientService: PatientService

    @GetMapping
    fun findAllPatients(): ResponseEntity<List<PatientResponseVO>> {
        return ResponseEntity.ok(patientService.findAllPatients())
    }

    @PostMapping(value = ["/{cep}"])
    fun savePatient(
        @PathVariable("cep") cep: Long,
        @RequestBody patient: PatientRequestVO
    ): ResponseEntity<PatientResponseVO>? {
        val addressSaved = proxy.getAddress(cep)
        val patientSaved = patientService.savePatient(
            patient = patient,
            zipCode = addressSaved?.cep?.replace(oldValue = "-", newValue = "")?.toInt(),
            city = addressSaved?.localidade,
            uf = addressSaved?.uf
        )
        val uri: URI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(patientSaved.id).toUri()
        return ResponseEntity.created(uri).body(patientSaved)
    }
}
