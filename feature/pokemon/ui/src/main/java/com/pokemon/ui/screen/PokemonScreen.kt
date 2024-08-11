package com.pokemon.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.pokemon.domain.model.Pokemon
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PokemonScreen(
    modifier: Modifier = Modifier,
    pokemonUiState: PokemonUiState,
    messageError: Int?,
    successCreateTag: Int?,
    getPokemons: () -> Unit,
    onBackButton: () -> Unit,
    saveTag: (String, List<Pokemon>) -> Unit,
    resetSuccessCreateTag: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var isDialogVisible by remember { mutableStateOf(false) }
    var dialogText by remember { mutableStateOf("") }

    var isInSelectionMode by remember {
        mutableStateOf(true)
    }
    val selectedItems = remember {
        mutableStateListOf<Pokemon>()
    }

    val resetSelectionMode = {
        isInSelectionMode = false
        selectedItems.clear()
        resetSuccessCreateTag()
    }
    BackHandler(
        enabled = isInSelectionMode,
    ) {
        resetSelectionMode()
    }
    LaunchedEffect(
        key1 = isInSelectionMode,
        key2 = selectedItems.size,
    ) {
        if (isInSelectionMode && selectedItems.isEmpty()) {
            isInSelectionMode = false
        }
    }
    LaunchedEffect(Unit) {
        getPokemons()
    }
    Scaffold(
        topBar = {
            if (isInSelectionMode) {
                SelectionModeTopAppBar(
                    selectedItems = selectedItems,
                    resetSelectionMode = resetSelectionMode,
                    onDialogVisibilityChange = { isDialogVisible = it }
                )
            } else {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "Pokedex",
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackButton) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        when {
            pokemonUiState is PokemonUiState.LOADING -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }

            }

            pokemonUiState is PokemonUiState.SUCCESS -> {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    items(pokemonUiState.pokemons) { pokemon ->
                        val isSelected = selectedItems.contains(pokemon)
                        ListItem(
                            modifier = Modifier.combinedClickable(
                                onClick = {
                                    if (isInSelectionMode) {
                                        if (isSelected) {
                                            selectedItems.remove(pokemon)
                                        } else {
                                            selectedItems.add(pokemon)
                                        }
                                    }
                                },
                                onLongClick = {
                                    if (isInSelectionMode) {
                                        if (isSelected) {
                                            selectedItems.remove(pokemon)
                                        } else {
                                            selectedItems.add(pokemon)
                                        }
                                    } else {
                                        isInSelectionMode = true
                                        selectedItems.add(pokemon)
                                    }
                                },
                            ),
                            headlineContent = {
                                Text(
                                    text = pokemon.name.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.getDefault()
                                        ) else it.toString()
                                    },
                                    style = MaterialTheme.typography.titleLarge
                                )
                            }, leadingContent = {
                                if (isInSelectionMode) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Rounded.CheckCircle,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Rounded.Close,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.outline,
                                        )
                                    }
                                } else {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(pokemon.image)
                                            .decoderFactory(SvgDecoder.Factory())
                                            .build(),
                                        contentDescription = pokemon.name,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            })
                    }
                }

            }

            pokemonUiState is PokemonUiState.ERROR -> {

            }
        }
        if (isDialogVisible) {
            MinimalDialog(
                onDismissRequest = { isDialogVisible = false },
                text = dialogText,
                onTextChange = { dialogText = it },
                selectedItems = selectedItems,
                saveTag = saveTag,
                resetSelectionMode = resetSelectionMode
            )
        }

        if (messageError != null) {
            val message = stringResource(id = messageError)
            LaunchedEffect(messageError) {
                snackbarHostState.showSnackbar(
                    message = message,
                )
            }
        }
        if (successCreateTag != null) {
            val message = stringResource(id = successCreateTag)
            LaunchedEffect(messageError) {
                val snackBarResult = snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = "Ententido"
                )
                if (snackBarResult == SnackbarResult.ActionPerformed) {
                    resetSelectionMode()
                }

            }
        }


    }

}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SelectionModeTopAppBar(
    selectedItems: SnapshotStateList<Pokemon>,
    resetSelectionMode: () -> Unit,
    onDialogVisibilityChange: (Boolean) -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "${selectedItems.size}",
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.onBackground,
                ),
            )
        },
        navigationIcon = {
            IconButton(
                onClick = resetSelectionMode,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        },
        actions = {
            var isDropDownVisible by remember {
                mutableStateOf(false)
            }
            Box(
                modifier = Modifier,
            ) {
                IconButton(
                    onClick = {
                        isDropDownVisible = true
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
                DropdownMenu(
                    expanded = isDropDownVisible,
                    onDismissRequest = {
                        isDropDownVisible = false
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "Crear Tag",
                            )
                        },
                        onClick = {
                            isDropDownVisible = false
                            onDialogVisibilityChange(true)
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Edit,
                                contentDescription = null
                            )
                        },
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
    )
}

@Composable
fun MinimalDialog(
    text: String,
    onTextChange: (String) -> Unit,
    onDismissRequest: () -> Unit,
    selectedItems: SnapshotStateList<Pokemon>,
    saveTag: (String, List<Pokemon>) -> Unit,
    resetSelectionMode: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Creación de tag",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                    modifier = Modifier
                        .wrapContentSize(Alignment.Center),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = text,
                    maxLines = 1,
                    onValueChange = { onTextChange(it) },
                    label = { Text("Ingrese nombre") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onDismissRequest() }
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { onDismissRequest() }
                    ) {
                        Text("Cerrar")
                    }
                    Button(
                        onClick = {
                            saveTag(text, selectedItems)
                            onDismissRequest()
                        }
                    ) {
                        Text("Aceptar")
                    }
                }

            }


        }
    }
}

@Preview
@Composable
private fun PokemonScreenPreview() {
    PokemonScreen(
        pokemonUiState = PokemonUiState.SUCCESS(listOf()),
        getPokemons = {},
        onBackButton = {},
        saveTag = { _, _ -> },
        messageError = null,
        successCreateTag = null,
        resetSuccessCreateTag = {}
    )
}

@Preview
@Composable
private fun DialogPreview() {
    MinimalDialog(
        onDismissRequest = {},
        onTextChange = { _ -> },
        text = "dada",
        saveTag = { _, _ -> },
        selectedItems = SnapshotStateList(),
        resetSelectionMode = {}
    )
}
