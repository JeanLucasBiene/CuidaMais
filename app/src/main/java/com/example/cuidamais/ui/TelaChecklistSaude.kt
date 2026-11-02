package com.example.cuidamais.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.cuidamais.model.RegistroSaude

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaChecklistSaude(
    registro: RegistroSaude,
    onRegistroChange: (RegistroSaude) -> Unit
) {
    // limite de agua por dia em ml, ninguem toma mais que isso
    val limiteAguaMl = 6000


    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Checklist diário de saúde") })
        }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {

            // ---------- pressao ----------
            Text(
                text = "Pressão arterial",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = registro.pressaoMaxima,
                    onValueChange = { novo ->
                        // so aceita numero, ate 3 digitos
                        val limpo = novo.filter { it.isDigit() }.take(3)
                        onRegistroChange(registro.copy(pressaoMaxima = limpo, salvo = false))
                    },
                    label = { Text("Máxima (ex: 120)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )

                OutlinedTextField(
                    value = registro.pressaoMinima,
                    onValueChange = { novo ->
                        val limpo = novo.filter { it.isDigit() }.take(3)
                        onRegistroChange(registro.copy(pressaoMinima = limpo, salvo = false))
                    },
                    label = { Text("Mínima (ex: 80)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }


            // ---------- hidratacao ----------
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Hidratação",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // mostra em litros, ex: 1,5 L
            val litros = "%.1f".format(registro.aguaMl / 1000f)

            Text(text = "$litros L de ${limiteAguaMl / 1000} L")

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { registro.aguaMl / limiteAguaMl.toFloat() },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = {
                        // minOf garante que nunca passa do limite
                        val novo = minOf(registro.aguaMl + 500, limiteAguaMl)
                        onRegistroChange(registro.copy(aguaMl = novo, salvo = false))
                    },
                    enabled = registro.aguaMl < limiteAguaMl
                ) {
                    Text("+ 500 ml")
                }

                Button(
                    onClick = {
                        val novo = minOf(registro.aguaMl + 1000, limiteAguaMl)
                        onRegistroChange(registro.copy(aguaMl = novo, salvo = false))
                    },
                    enabled = registro.aguaMl < limiteAguaMl
                ) {
                    Text("+ 1 L")
                }
            }

            if (registro.aguaMl >= limiteAguaMl) {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Limite de 6 litros por dia atingido",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


            // ---------- sono ----------
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Sono da noite passada",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = registro.horasSono,
                onValueChange = { novo ->
                    val limpo = novo.filter { it.isDigit() }.take(2)

                    // ninguem dorme mais que 24h num dia
                    if ((limpo.toIntOrNull() ?: 0) <= 24) {
                        onRegistroChange(registro.copy(horasSono = limpo, salvo = false))
                    }
                },
                label = { Text("Horas dormidas (ex: 7)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            // a qualidade do sono sai sozinha das horas dormidas
            val horasDormidas = registro.horasSono.toIntOrNull()

            if (horasDormidas != null) {
                val qualidade = when {
                    horasDormidas >= 8 -> "Boa"
                    horasDormidas >= 6 -> "Regular"
                    else -> "Ruim"
                }

                Spacer(modifier = Modifier.height(12.dp))

                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Qualidade do sono: $qualidade",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // ---------- salvar ----------
            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    // por enquanto so marca como salvo na memoria, com banco isso grava de verdade
                    onRegistroChange(registro.copy(salvo = true))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Salvar registro do dia", style = MaterialTheme.typography.titleMedium)
            }

            if (registro.salvo) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Registro do dia salvo!",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "O app só guarda os registros, ele não faz diagnóstico. Em caso de dúvida, procure um profissional de saúde.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}