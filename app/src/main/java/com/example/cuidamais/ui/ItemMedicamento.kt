package com.example.cuidamais.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cuidamais.model.Medicamento

@Composable
fun ItemMedicamento(
    medicamento: Medicamento,
    onToggleTomado: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = medicamento.nome, style = MaterialTheme.typography.titleMedium)
                Text(text = "Dosagem: ${medicamento.dosagem}")
                Text(text = "Horário: ${medicamento.horario}")
            }
            Checkbox(
                checked = medicamento.tomado,
                onCheckedChange = { onToggleTomado(medicamento.id) }
            )
        }
    }
}