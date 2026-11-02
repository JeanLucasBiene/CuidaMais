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
fun TelaAgenda(
    // a lista agora vem da TelaPrincipal, senao ela some quando troca de aba
    medicamentos: List<Medicamento>,
    onMedicamentosChange: (List<Medicamento>) -> Unit
) {
    // estado dos campos do formulario, fica aqui em cima (state hoisting)
    var nome by remember { mutableStateOf("") }
    var dosagem by remember { mutableStateOf("") }
    var horario by remember { mutableStateOf("") }

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
                    // so adiciona se tiver nome, dosagem maior que zero e um horario que existe
                    if (nome.isNotBlank() && (dosagem.toIntOrNull() ?: 0) > 0 && horarioValido(horario)) {
                        // id novo = maior id que ja existe + 1
                        val novoId = (medicamentos.maxOfOrNull { it.id } ?: 0) + 1

                        onMedicamentosChange(
                            medicamentos + Medicamento(
                                id = novoId,
                                nome = nome,
                                // junta o mg e os dois pontos aqui, no formulario so tem os numeros
                                dosagem = "$dosagem mg",
                                horario = formatarHorario(horario)
                            )
                        )

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

            if (medicamentos.isNotEmpty()) {
                Text(
                    text = "Marque a caixinha do medicamento pra ver a opção de excluir",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

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
                // key = id faz o card lembrar de qual medicamento ele é, mesmo depois de excluir um
                items(medicamentos, key = { it.id }) { medicamento ->
                    ItemMedicamento(
                        medicamento = medicamento,
                        onExcluir = { id ->
                            // cria uma lista nova sem o medicamento excluido
                            onMedicamentosChange(medicamentos.filter { it.id != id })
                        }
                    )
                }
            }
        }
    }
}