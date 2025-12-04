package com.mbrown.myapplication.characterDetail

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mbrown.myapplication.composables.ThemedPreview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    characterName: String,
    locationId: Long,
    modifier: Modifier = Modifier,
    viewModel: CharacterDetailViewModel = viewModel(
        factory = CharacterDetailViewModel.factory(
            locationId = locationId
        )
    )
) {
    val location by viewModel.location.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Rick and Morty")
                }
            )
        }
    ) {
        location?.let { location ->
            CharacterDetailScreenContent(
                name = characterName,
                planet = location.name,
                dimension = location.dimension,
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Composable
private fun CharacterDetailScreenContent(
    name: String,
    planet: String,
    dimension: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp), verticalArrangement = spacedBy(8.dp)) {
        Text(text = name)
        Text(text = planet)
        Text(text = dimension)
    }
}

@Composable
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
private fun CharacterDetailScreenContentPreview() {
    ThemedPreview {
        CharacterDetailScreenContent(
            name = "Rick Sanchez",
            planet = "Earth",
            dimension = "Dimension C-137"
        )
    }
}