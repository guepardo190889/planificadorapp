package com.example.planificadorapp.tipos

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateTypeAdapter :  TypeAdapter<LocalDate>() {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun write(out: JsonWriter, value: LocalDate?) {
        out.value(value?.format(formatter))
    }

    override fun read(input: JsonReader): LocalDate? {
        return LocalDate.parse(input.nextString(), formatter)
    }
}