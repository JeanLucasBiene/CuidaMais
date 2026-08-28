package com.example.cuidamais.model

data class Medicamento(
    val id: Int,
    val nome: String,
    val dosagem: String,
    val horario: String,
    val tomado: Boolean = false
)