package com.example.cuidamais.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cuidamais.model.Medicamento

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAgenda() {
    // lista de medicamentos cadastrados, por enquanto so em memoria (sem banco ainda)
    var medicamentos by remember { mutableStateOf(listOf<Medicamento>()) }

    // estado dos campos do formulario, fica aqui em cima (state hoisting)
    var nome by remember { mutableStateOf("") }
    var dosagem by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }

    // id vai incrementando a cada cadastro novo
    var proximoId by remember { mutableIntStateOf(1) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("CuidaMais - Agenda de Medicamentos") })
        }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
        ) {
            FormularioMedicamento(
                nome = nome,
                onNomeChange = { nome = it },
                dosagem = dosagem,
                onDosagemChange = { dosagem = it },
                horario = horario,
                onHorarioChange = { horario = it },
                onAdicionarClick = {
                    // validacao simples, so adiciona se os 3 campos estiverem preenchidos
                    if (nome.isNotBlank() && dosagem.isNotBlank() && horario.isNotBlank()) {
                        medicamentos = medicamentos + Medicamento(
                            id = proximoId,
                            nome = nome,
                            dosagem = dosagem,
                            horario = horario
                        )
                        proximoId += 1

                        // limpa os campos pra poder cadastrar o proximo
                        nome = ""
                        dosagem = ""
                        horario = ""
                    }
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Medicamentos cadastrados (${medicamentos.size})",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            if (medicamentos.isEmpty()) {
                Text(
                    text = "Nenhum medicamento cadastrado ainda",
                    modifier = Modifier.padding(16.dp)
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                items(medicamentos) { medicamento ->
                    ItemMedicamento(
                        medicamento = medicamento,
                        onToggleTomado = { id ->
                            medicamentos = medicamentos.map {
                                if (it.id == id) it.copy(tomado = !it.tomado) else it
                            }
                        }
                    )
                }
            }
        }
    }
}