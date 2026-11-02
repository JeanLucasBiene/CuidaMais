package com.example.cuidamais

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.cuidamais.ui.TelaLogin
import com.example.cuidamais.ui.TelaPrincipal
import com.example.cuidamais.ui.theme.CuidaMaisTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CuidaMaisTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // controla qual tela aparece, depois a gente troca por navegacao de verdade
                    var logado by remember { mutableStateOf(false) }

                    var email by remember { mutableStateOf("") }
                    var senha by remember { mutableStateOf("") }
                    var erro by remember { mutableStateOf<String?>(null) }


                    if (logado) {
                        TelaPrincipal()
                    } else {
                        TelaLogin(
                            email = email,
                            onEmailChange = { email = it },
                            senha = senha,
                            onSenhaChange = { senha = it },
                            erro = erro,
                            onEntrarClick = {
                                // validacao provisoria, so confere se preencheu os dois campos
                                // depois vai checar no banco
                                if (email.isNotBlank() && senha.isNotBlank()) {
                                    erro = null
                                    logado = true
                                } else {
                                    erro = "Preencha e-mail e senha"
                                }
                            },
                            onCriarContaClick = {
                                // ainda nao tem tela de cadastro
                                erro = "Cadastro ainda em desenvolvimento"
                            }
                        )
                    }
                }
            }
        }
    }
}