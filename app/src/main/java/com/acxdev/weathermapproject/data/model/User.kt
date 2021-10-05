package com.acxdev.weathermapproject.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.acxdev.weathermapproject.util.NotEmptyString
import io.konad.*
import io.konad.applicative.builders.on
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotBlank
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate

@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var username: String,
    var email: String,
    var name: String,
    var pin: String
) {
    companion object {
//        fun of(
//            username: String,
//            email: String,
//            name: String,
//            pin: String
//        ): Result<User> =
//            ::User.curry()
//                .on(NotEmptyString.of(username))
//                .on(NotEmptyString.of(email))
//                .on(NotEmptyString.of(name))
//                .on(NotEmptyString.of(pin))
//                .result
    }
}