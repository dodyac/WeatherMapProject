package com.acxdev.weathermapproject.util

import io.konad.*
import io.konad.applicative.builders.on
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotBlank
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate

@JvmInline
value class NotEmptyString
@Deprecated(level = DeprecationLevel.ERROR, message = "use companion method 'of'")
constructor(val value: String) {
    companion object {
        @Suppress("DEPRECATION_ERROR")
        fun of(value: String): Result<NotEmptyString> = valikate {
            validate(NotEmptyString(value)) {
                validate(NotEmptyString::value).isNotBlank()
            }
        }
    }
}

internal inline fun <reified T> valikate(valikateFn: () -> T): Result<T> = try {
    valikateFn().ok()
} catch (ex: ConstraintViolationException) {
    ex.constraintViolations
        .mapToMessage()
        .joinToString("\n") { "\t\"${it.value}\" of ${T::class.simpleName}.${it.property}: ${it.message}" }
        .error()
}