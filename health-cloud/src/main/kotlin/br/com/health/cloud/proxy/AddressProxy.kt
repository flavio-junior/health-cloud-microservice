package br.com.health.cloud.proxy

import br.com.health.cloud.response.ViaCepResponseVO
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "viacep", url = "https://viacep.com.br/ws")
interface AddressProxy {

    @GetMapping(value = ["/{cep}/json"])
    fun getAddress(
        @PathVariable("cep") cep: Long
    ): ViaCepResponseVO?
}
