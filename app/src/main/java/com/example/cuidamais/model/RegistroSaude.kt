package com.example.cuidamais.model

// registro diario de saude do idoso
// por enquanto fica tudo em memoria, depois com banco a gente grava de verdade
data class RegistroSaude(
    val pressaoMaxima: String = "",
    val pressaoMinima: String = "",
    // agua em ml (500 = meio litro, 1000 = 1 litro)
    val aguaMl: Int = 0,
    val horasSono: String = "",
    val salvo: Boolean = false
)