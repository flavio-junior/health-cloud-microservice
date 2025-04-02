package br.com.auth.utils

object CoreUtils {
    const val EMPTY_FIELDS = "Alert! Fields are empty! Please check and complete!"
    const val EXPIRED_CODE = "Expired Code!"
    const val FROM: Long = 1000
    const val UNTIL: Long = 10000
    const val SUBJECT = "Confirmação do endereço de email"
    const val SUBJECT_RECOVER_PASSWORD = "Recuperação de senha"
    const val THE_EMAIL_ALREADY_EXISTS = "The Email Already Exists!"
    const val THE_USER_ALREADY_EXISTS = "The User Already Exists!"
    const val CODE_NOT_FOUND = "Code Not Found!"
    const val EMAIL_NOT_FOUND = "Email Not Found!"
    const val OPERATION_UNAUTHORIZED = "Alert! Operation Unauthorized!"
    const val APPLICATION_JSON = "application/json"
}

fun sendRecoverPassword(
    code: Long,
    minutes: Long
): String {
    return "Utilize o código abaixo para redefinir sua senha:\n Código: $code\n " +
            "O código gerado tem validade de $minutes minutos."
}