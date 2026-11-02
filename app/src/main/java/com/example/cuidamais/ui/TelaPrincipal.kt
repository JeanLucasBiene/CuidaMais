package com.example.cuidamais.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.cuidamais.model.Medicamento
import com.example.cuidamais.model.RegistroSaude
import com.example.cuidamais.model.itensAmbientePadrao

// tela que aparece depois do login, com a barra de baixo pra trocar entre as 3 telas
@Composable
fun TelaPrincipal() {
    // aba selecionada: 0 = medicamentos, 1 = saude, 2 = ambiente
    var abaAtual by remember { mutableIntStateOf(0) }


    // o estado de cada tela fica aqui em cima, senao ele some quando troca de aba
    var medicamentos by remember { mutableStateOf(listOf<Medicamento>()) }
    var registroSaude by remember { mutableStateOf(RegistroSaude()) }
    var itensAmbiente by remember { mutableStateOf(itensAmbientePadrao()) }


    Scaffold(
        // as telas de dentro ja cuidam da barra de status, aqui so entra a barra de baixo
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar {
                // por enquanto os icones sao emojis, assim nao precisa de dependencia extra
                NavigationBarItem(
                    selected = abaAtual == 0,
                    onClick = { abaAtual = 0 },
                    icon = { Text("💊") },
                    label = { Text("Medicamentos") }
                )

                NavigationBarItem(
                    selected = abaAtual == 1,
                    onClick = { abaAtual = 1 },
                    icon = { Text("❤️") },
                    label = { Text("Saúde") }
                )

                NavigationBarItem(
                    selected = abaAtual == 2,
                    onClick = { abaAtual = 2 },
                    icon = { Text("🏠") },
                    label = { Text("Ambiente") }
                )
            }
        }
    ) { paddingInterno ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
                .consumeWindowInsets(paddingInterno)
        ) {
            when (abaAtual) {
                0 -> TelaAgenda(
                    medicamentos = medicamentos,
                    onMedicamentosChange = { medicamentos = it }
                )

                1 -> TelaChecklistSaude(
                    registro = registroSaude,
                    onRegistroChange = { registroSaude = it }
                )

                else -> TelaChecklistAmbiente(
                    itens = itensAmbiente,
                    onItensChange = { itensAmbiente = it }
                )
            }
        }
    }
}