package com.example.planificadorapp.tipos

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeTypeAdapter : TypeAdapter<LocalDateTime>() {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: LocalDateTime?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.format(formatter))
        }
    }

    @Throws(IOException::class)
    override fun read(`in`: JsonReader): LocalDateTime {
        val value = `in`.nextString()
        return LocalDateTime.parse(value, formatter)
    }
}