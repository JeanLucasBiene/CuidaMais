package com.example.cuidamais.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(text = "Novo Medicamento", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = nome,
            onValueChange = onNomeChange,
            label = { Text("Nome do medicamento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = dosagem,
            onValueChange = onDosagemChange,
            label = { Text("Dosagem (ex: 500mg)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = horario,
            onValueChange = onHorarioChange,
            label = { Text("Horário (ex: 08:00)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onAdicionarClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar")
        }
    }
}