package br.com.auth.utils

import br.com.auth.exceptions.ForbiddenActionRequestException
import br.com.auth.utils.CoreUtils.EXPIRED_CODE
import br.com.auth.utils.CoreUtils.FROM
import br.com.auth.utils.CoreUtils.UNTIL
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.random.Random

fun LocalDateTime.toDate(): Date {
    return Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
}

fun generateCode(): Long = Random.nextLong(from = FROM, until = UNTIL)

fun getLocalDateTime(): LocalDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)

fun getLocalDateTimeAndPlusMinutes(
    minutes: Long = 5
): LocalDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusMinutes(minutes)

fun verifyLocalDateTimeIsValid(
    dateTime: LocalDateTime? = null
) {
    if (dateTime?.isBefore(getLocalDateTime()) == true) {
        throw ForbiddenActionRequestException(EXPIRED_CODE)
    }
}
