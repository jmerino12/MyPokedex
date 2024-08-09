package com.example.mypokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.auth.ui.AuthUiState
import com.auth.ui.AuthViewModel
import com.auth.ui.login.LoginScreen
import com.auth.ui.login.LoginViewModel
import com.auth.ui.register.RegisterScreen
import com.auth.ui.register.RegisterViewModel
import com.example.mypokedex.ui.theme.MyPokedexTheme
import com.pokemon.ui.screen.PokemonScreen
import com.pokemon.ui.screen.PokemonViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyPokedexTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel = hiltViewModel<AuthViewModel>()
                    val state by viewModel.uiState.collectAsState()
                    PokedexNavGraph(
                        modifier = Modifier.padding(innerPadding),
                        state = state
                    )
                }
            }
        }
    }
}


@Composable
fun PokedexNavGraph(
    modifier: Modifier,
    state: AuthUiState,
    navHostController: NavHostController = rememberNavController(),
) {
    LaunchedEffect(state) {
        when (state) {

            AuthUiState.AUTHENTICATED -> navHostController.navigate("home") {

            }

            AuthUiState.UNAUTHENTICATED -> navHostController.navigate("login") {

            }

            else -> Unit
        }
    }


    NavHost(navController = navHostController, startDestination = "login") {
        composable("login") {
            val viewModel = hiltViewModel<LoginViewModel>()
            val state by viewModel.uiState.collectAsState()
            LoginScreen(
                loginUiState = state,
                goToRegisterScreen = { navHostController.navigate("register") },
                onLogin = { email, pass -> viewModel.login(email = email, password = pass) })
        }

        composable("register") {
            val viewModel = hiltViewModel<RegisterViewModel>()
            val state by viewModel.uiState.collectAsState()
            RegisterScreen(
                registerUiState = state,
                goBackScreen = { navHostController.navigateUp() },
                onRegisterUser = { email, password, name ->
                    viewModel.register(
                        email,
                        password,
                        name
                    )
                }
            )
        }

        composable("home") {
            val viewModel = hiltViewModel<PokemonViewModel>()
            val state by viewModel.uiState.collectAsState()
            PokemonScreen(pokemonUiState = state, getPokemons = { viewModel.getPokemons() })
        }

    }
}