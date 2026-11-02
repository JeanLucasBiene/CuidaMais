package com.example.cuidamais.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.example.cuidamais.model.ItemChecklist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaChecklistAmbiente(
    itens: List<ItemChecklist>,
    onItensChange: (List<ItemChecklist>) -> Unit
) {
    val total = itens.size
    val feitos = itens.count { it.ok }

    // agrupa por comodo pra mostrar um titulo antes de cada bloco
    val porComodo = itens.groupBy { it.comodo }


    // quando o modo edicao ta ligado aparece o botao de excluir em cada item
    var modoEdicao by remember { mutableStateOf(false) }

    // item que a pessoa clicou pra excluir, fica guardado ate ela confirmar ou cancelar
    var itemParaExcluir by remember { mutableStateOf<ItemChecklist?>(null) }


    // controle da janelinha de novo item, esses campos so servem enquanto ela ta aberta
    var mostrarDialogo by remember { mutableStateOf(false) }
    var novoComodo by remember { mutableStateOf("") }
    var novoTexto by remember { mutableStateOf("") }
    var novaDica by remember { mutableStateOf("") }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checklist do ambiente") },
                actions = {
                    TextButton(onClick = { modoEdicao = !modoEdicao }) {
                        Text(if (modoEdicao) "Concluir" else "Editar")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(onClick = { mostrarDialogo = true }) {
                Text("+ Novo item")
            }
        }
    ) { paddingInterno ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno),
            // espaco extra embaixo pro botao nao cobrir o ultimo item
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 88.dp)
        ) {
            // topo com o progresso geral
            item {
                Text(
                    text = "$feitos de $total itens seguros",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { if (total == 0) 0f else feitos / total.toFloat() },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Marque o que já está seguro na casa. O que ficar sem marcar mostra uma dica de como resolver. Use \"+ Novo item\" pra incluir outros e \"Editar\" pra excluir.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (itens.isEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text = "Nenhum item no checklist ainda")
                }
            }


            porComodo.forEach { (comodo, lista) ->
                item(key = comodo) {
                    Text(
                        text = comodo,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 20.dp, bottom = 4.dp)
                    )
                }

                items(lista, key = { it.id }) { elemento ->
                    ItemChecklistAmbiente(
                        item = elemento,
                        modoEdicao = modoEdicao,
                        onToggle = { id ->
                            // cria uma lista nova trocando so o item clicado
                            onItensChange(
                                itens.map {
                                    if (it.id == id) it.copy(ok = !it.ok) else it
                                }
                            )
                        },
                        onExcluirClick = { itemParaExcluir = it }
                    )
                }
            }
        }
    }


    // confirmacao pra ninguem apagar sem querer
    itemParaExcluir?.let { alvo ->
        AlertDialog(
            onDismissRequest = { itemParaExcluir = null },
            title = { Text("Excluir item") },
            text = { Text("Quer mesmo excluir \"${alvo.texto}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // cria uma lista nova sem o item excluido
                        onItensChange(itens.filter { it.id != alvo.id })
                        itemParaExcluir = null
                    }
                ) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                TextButton(onClick = { itemParaExcluir = null }) {
                    Text("Cancelar")
                }
            }
        )
    }


    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Novo item") },
            text = {
                Column {
                    OutlinedTextField(
                        value = novoComodo,
                        onValueChange = { novoComodo = it },
                        label = { Text("Cômodo (ex: Garagem)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = novoTexto,
                        onValueChange = { novoTexto = it },
                        label = { Text("Item (ex: Piso sem buracos)") },
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = novaDica,
                        onValueChange = { novaDica = it },
                        label = { Text("Dica (opcional)") },
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val comodoLimpo = novoComodo.trim()

                        // se ja existe um comodo com esse nome (sem ligar pra maiuscula), usa o mesmo
                        val comodoFinal = itens
                            .firstOrNull { it.comodo.equals(comodoLimpo, ignoreCase = true) }
                            ?.comodo ?: comodoLimpo

                        // id novo = maior id que ja existe + 1
                        val novoId = (itens.maxOfOrNull { it.id } ?: 0) + 1

                        onItensChange(
                            itens + ItemChecklist(
                                id = novoId,
                                comodo = comodoFinal,
                                texto = novoTexto.trim(),
                                dica = novaDica.trim()
                            )
                        )

                        // limpa os campos e fecha a janelinha
                        novoComodo = ""
                        novoTexto = ""
                        novaDica = ""
                        mostrarDialogo = false
                    },
                    // so deixa adicionar se tiver comodo e item, a dica pode ficar vazia
                    enabled = novoComodo.isNotBlank() && novoTexto.isNotBlank()
                ) {
                    Text("Adicionar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}


// card de cada item do checklist, so mostra e avisa o pai quando clicam
@Composable
fun ItemChecklistAmbiente(
    item: ItemChecklist,
    modoEdicao: Boolean,
    onToggle: (Int) -> Unit,
    onExcluirClick: (ItemChecklist) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle(item.id) }
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // o clique ja é tratado pela linha toda, entao o checkbox fica so de enfeite
            Checkbox(
                checked = item.ok,
                onCheckedChange = null
            )

            Spacer(modifier = Modifier.width(12.dp))

            // weight empurra o botao de excluir pro canto direito
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.texto,
                    style = MaterialTheme.typography.bodyLarge
                )

                // itens criados pelo usuario podem ficar sem dica, ai nao mostra nada
                if (!item.ok && item.dica.isNotBlank()) {
                    Text(
                        text = "Dica: ${item.dica}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (modoEdicao) {
                TextButton(
                    onClick = { onExcluirClick(item) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Excluir")
                }
            }
        }
    }
}