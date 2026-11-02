package com.example.cuidamais.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

// tela de cadastro de um novo medicamento
// os valores e callbacks vem de fora (state hoisting), aqui e so o layout
@Composable
fun FormularioMedicamento(
    nome: String,
    onNomeChange: (String) -> Unit,
    dosagem: String,
    onDosagemChange: (String) -> Unit,
    horario: String,
    onHorarioChange: (String) -> Unit,
    onAdicionarClick: () -> Unit
) {
    // so mostra erro se ja digitou algo e o horario nao existe (ex: 25:00)
    val horarioComErro = horario.isNotEmpty() && !horarioValido(horario)


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "Novo Medicamento",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = onNomeChange,
            label = { Text("Nome do medicamento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // so aceita numero, o "mg" aparece sozinho ao lado
        OutlinedTextField(
            value = dosagem,
            onValueChange = { novo ->
                onDosagemChange(novo.filter { it.isDigit() }.take(5))
            },
            label = { Text("Dosagem (ex: 500)") },
            suffix = { Text("mg") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // so aceita numero, os dois pontos entram sozinhos (ex: 12 vira 12:00, 1230 vira 12:30)
        OutlinedTextField(
            value = horario,
            onValueChange = { novo -> onHorarioChange(limparHorario(novo)) },
            label = { Text("Horário (ex: 1230)") },
            visualTransformation = HorarioTransformation(),
            isError = horarioComErro,
            supportingText = {
                if (horarioComErro) {
                    Text("Horário inválido")
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAdicionarClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar")
        }
    }
}


// ---------- funcoes do horario ----------
// o estado guarda so os numeros digitados (ex: "1230"), os dois pontos so aparecem na tela


// deixa so numeros e limita o tamanho
// se o primeiro numero for maior que 2 a hora tem so 1 digito (ex: 830 = 08:30), entao cabe 3 numeros
fun limparHorario(texto: String): String {
    val digitos = texto.filter { it.isDigit() }.take(4)

    return if (digitos.isNotEmpty() && digitos[0] > '2') digitos.take(3) else digitos
}


// transforma os numeros no horario final, ex: "12" vira "12:00", "830" vira "08:30"
fun formatarHorario(digitos: String): String {
    if (digitos.isEmpty()) return ""

    val tamanhoHora = if (digitos[0] > '2') 1 else 2
    val hora = digitos.take(tamanhoHora).padStart(2, '0')
    val minutos = digitos.drop(tamanhoHora).padEnd(2, '0')

    return "$hora:$minutos"
}


// confere se o horario existe (hora ate 23 e minutos ate 59)
fun horarioValido(digitos: String): Boolean {
    if (digitos.isEmpty()) return false

    val formatado = formatarHorario(digitos)
    val hora = formatado.substring(0, 2).toIntOrNull() ?: return false
    val minutos = formatado.substring(3, 5).toIntOrNull() ?: return false

    return hora in 0..23 && minutos in 0..59
}


// mostra os numeros digitados ja no formato de horario, o cursor fica sempre no final
class HorarioTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formatado = formatarHorario(text.text)

        val mapeamento = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = formatado.length
            override fun transformedToOriginal(offset: Int): Int = text.length
        }

        return TransformedText(AnnotatedString(formatado), mapeamento)
    }
}