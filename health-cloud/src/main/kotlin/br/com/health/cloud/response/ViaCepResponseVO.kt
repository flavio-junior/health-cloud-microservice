package br.com.health.cloud.response

import br.com.health.cloud.utils.common.UF

data class ViaCepResponseVO(
    val cep: String,
    val logradouro: String,
    val complemento: String,
    val bairro: String,
    val localidade: String,
    val uf: UF,
    val unidade: String,
    val ibge: String,
    val gia: String
)
