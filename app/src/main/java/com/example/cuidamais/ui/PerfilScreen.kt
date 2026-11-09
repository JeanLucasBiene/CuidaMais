package com.example.cuidamais.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun PerfilScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("perfil", Context.MODE_PRIVATE) }

    var nome by rememberSaveable { mutableStateOf(prefs.getString("nome", "") ?: "") }
    var idade by rememberSaveable { mutableStateOf(prefs.getString("idade", "") ?: "") }
    var condicoes by rememberSaveable { mutableStateOf(prefs.getString("condicoes", "") ?: "") }
    var alergias by rememberSaveable { mutableStateOf(prefs.getString("alergias", "") ?: "") }
    var contatoNome by rememberSaveable { mutableStateOf(prefs.getString("contatoNome", "") ?: "") }
    var contatoTelefone by rememberSaveable { mutableStateOf(prefs.getString("contatoTelefone", "") ?: "") }

    // abre o discador com o numero preenchido (nao precisa de permissao)
    fun ligar(numero: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$numero"))
        context.startActivity(intent)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Perfil do idoso", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = nome, onValueChange = { nome = it },
            label = { Text("Nome") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = idade, onValueChange = { idade = it },
            label = { Text("Idade") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = condicoes, onValueChange = { condicoes = it },
            label = { Text("Condições de saúde") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = alergias, onValueChange = { alergias = it },
            label = { Text("Alergias") }, modifier = Modifier.fillMaxWidth()
        )

        Text("Contato de emergência", style = MaterialTheme.typography.titleMedium)

        OutlinedTextField(
            value = contatoNome, onValueChange = { contatoNome = it },
            label = { Text("Nome do contato") }, modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = contatoTelefone, onValueChange = { contatoTelefone = it },
            label = { Text("Telefone") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                prefs.edit()
                    .putString("nome", nome)
                    .putString("idade", idade)
                    .putString("condicoes", condicoes)
                    .putString("alergias", alergias)
                    .putString("contatoNome", contatoNome)
                    .putString("contatoTelefone", contatoTelefone)
                    .apply()
                Toast.makeText(context, "Perfil salvo!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Salvar perfil") }

        OutlinedButton(
            onClick = { ligar(contatoTelefone) },
            enabled = contatoTelefone.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Ligar para o contato") }

        OutlinedButton(
            onClick = { ligar("192") },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Ligar para o SAMU (192)") }
    }
}