package com.tag.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun TagScreen(
    modifier: Modifier = Modifier,
    tagUiState: TagUiState,
    getTags: () -> Unit,
    goToPokemonScreen: () -> Unit
) {
    val composition by
    rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.empty_state))
    LaunchedEffect(Unit) {
        getTags()
    }

    Scaffold(topBar = {
        if (tagUiState is TagUiState.EMPTY) {
            TopAppBar(
                title = { Text("Pokedex") },
                actions = {
                    IconButton(onClick =  goToPokemonScreen ) {
                        Icon(Icons.Default.AddCircle, contentDescription = "Add")
                    }
                }
            )
        } else {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Pokedex",
                    )
                },
            )
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                tagUiState is TagUiState.LOADING -> {
                    CircularProgressIndicator()
                }

                tagUiState is TagUiState.SUCCESS -> {
                    LazyColumn {
                        items(tagUiState.tags) {

                        }
                    }
                }

                tagUiState is TagUiState.EMPTY -> {
                    LottieAnimation(
                        iterations = LottieConstants.IterateForever,
                        composition = composition,
                        modifier = Modifier.size(200.dp)
                    )
                    Text(
                        text = "No hay tags te invitamos a crear.",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }


    }

}


@Preview
@Composable
private fun TagScreenPreview() {
    TagScreen(tagUiState = TagUiState.EMPTY, getTags = {}, goToPokemonScreen = {})
}