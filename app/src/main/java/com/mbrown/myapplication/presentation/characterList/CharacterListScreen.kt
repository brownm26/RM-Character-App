package com.mbrown.myapplication.presentation.characterList

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.mbrown.myapplication.api.models.Character
import com.mbrown.myapplication.api.models.Location
import com.mbrown.myapplication.composables.DebugPlaceholder
import com.mbrown.myapplication.composables.ThemedPreview
import com.mbrown.myapplication.composables.debugPlaceholder
import java.net.URI

@Composable
fun CharacterListScreen(
    modifier: Modifier = Modifier,
    viewModel: CharacterListViewModel = viewModel(),
    onCharacterClicked: (Character) -> Unit
) {
    val characters by viewModel.characters.collectAsStateWithLifecycle()
    CharacterListScreenContent(
        characters = characters,
        modifier = modifier,
        isLoading = viewModel.isLoading,
        onScrolledToBottom = viewModel::onScrolledToBottom,
        onCharacterClicked = onCharacterClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterListScreenContent(
    characters: List<Character>,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onScrolledToBottom: () -> Unit,
    onCharacterClicked: (Character) -> Unit
) {
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
        LazyColumn(
            modifier = Modifier.padding(it)
        ) {
            items(characters) { character ->
                Column {
                    CharacterListItem(
                        imageUri = character.image,
                        name = character.name,
                        description = character.species,
                        onClick = { onCharacterClicked(character) }
                    )
                }
            }

            item {
                LaunchedEffect(Unit) {
                    onScrolledToBottom()
                }
                if(isLoading) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterListItem(
    imageUri: String,
    name: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    description: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(
                enabled = enabled,
                onClick = onClick
            ),
        horizontalArrangement = spacedBy(8.dp)
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = null,
            placeholder = debugPlaceholder(DebugPlaceholder.CHARACTER),
            modifier = Modifier.size(64.dp)
        )
        Column(
            verticalArrangement = spacedBy(4.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge
            )
            description?.let {
                Text(
                    text = description,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CharacterListItemPreview() {
    ThemedPreview {
        CharacterListItem(
            imageUri = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            name = "Rick Sanchez",
            description = "Alive",
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CharacterListScreenPreview() {
    val characters = listOf(
        Character(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            origin = Location(
                name = "Earth (C-137)",
                url = URI.create("https://rickandmortyapi.com/api/location/1")
            ),
            location = Location(
                name = "Earth (Replacement Dimension)",
                url = URI.create("https://rickandmortyapi.com/api/location/1")
            ),
            image = "",
            episode = emptyList(),
            url = "",
            created = ""
        )
    )

    ThemedPreview {
        CharacterListScreenContent(
            characters = characters,
            onScrolledToBottom = {},
            onCharacterClicked = {}
        )
    }
}
