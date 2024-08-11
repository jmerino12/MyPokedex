package com.example.mypokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import com.tag.ui.TagScreen
import com.tag.ui.TagViewModel
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
                        state = state,
                        logout = { viewModel.logout() },
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
    logout: () -> Unit,
    navHostController: NavHostController = rememberNavController(),
) {
    LaunchedEffect(state) {
        when (state) {
            AuthUiState.AUTHENTICATED -> navHostController.navigate("tags") {
                popUpTo("login") { inclusive = true }
            }

            AuthUiState.UNAUTHENTICATED -> navHostController.navigate("login") {
                popUpTo("tags") { inclusive = true }
            }

            else -> Unit
        }
    }


    NavHost(navController = navHostController, startDestination = "tags") {
        composable("login") {
            val viewModel = hiltViewModel<LoginViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            val messageError by viewModel.errorMessage.collectAsState()
            LoginScreen(
                loginUiState = uiState,
                goToRegisterScreen = { navHostController.navigate("register") },
                onLogin = { email, pass -> viewModel.login(email = email, password = pass) },
                messageError = messageError
            )
        }

        composable("register") {
            val viewModel = hiltViewModel<RegisterViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            RegisterScreen(
                registerUiState = uiState,
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

        composable("pokemon") {
            val viewModel = hiltViewModel<PokemonViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            val messageError by viewModel.errorMessage.collectAsState()
            val successCreateTag by viewModel.successCreateTag.collectAsState()
            PokemonScreen(
                pokemonUiState = uiState,
                getPokemons = { viewModel.getPokemons() },
                onBackButton = { navHostController.navigateUp() },
                saveTag = { tagName, pokemons ->
                    viewModel.createTagWithPokemons(
                        tagName,
                        pokemons
                    )
                },
                messageError = messageError,
                successCreateTag = successCreateTag,
                resetSuccessCreateTag = { viewModel.resetSuccessCreateTag() }
            )

        }

        composable("tags") {
            val viewModel = hiltViewModel<TagViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            TagScreen(
                tagUiState = uiState,
                getTags = { viewModel.getAllTags() },
                goToPokemonScreen = { navHostController.navigate("pokemon") },
                logout = logout,
                deleteTag = { tagName -> viewModel.deleteTag(tagName) })

        }

    }
}