package com.example.cuidamais.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cuidamais.model.Medicamento

// card que mostra um medicamento ja cadastrado na lista/agenda
// marcando a caixinha aparece o botao de excluir
@Composable
fun ItemMedicamento(
    medicamento: Medicamento,
    onExcluir: (Int) -> Unit
) {
    // esses dois so controlam o que aparece no card, por isso ficam aqui mesmo
    var selecionado by remember { mutableStateOf(false) }
    var mostrarConfirmacao by remember { mutableStateOf(false) }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                // tocar no card faz o mesmo que tocar na caixinha
                .clickable { selecionado = !selecionado }
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = medicamento.nome,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(text = "Dosagem: ${medicamento.dosagem}")
                    Text(text = "Horário: ${medicamento.horario}")
                }

                Checkbox(
                    checked = selecionado,
                    onCheckedChange = { selecionado = it }
                )
            }

            if (selecionado) {
                TextButton(
                    onClick = { mostrarConfirmacao = true },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Excluir")
                }
            }
        }
    }


    // confirmacao pra ninguem apagar sem querer
    if (mostrarConfirmacao) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacao = false },
            title = { Text("Excluir medicamento") },
            text = { Text("Quer mesmo excluir ${medicamento.nome}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacao = false
                        onExcluir(medicamento.id)
                    }
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacao = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}