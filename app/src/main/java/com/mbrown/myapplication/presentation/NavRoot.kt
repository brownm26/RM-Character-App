package com.mbrown.myapplication.presentation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.mbrown.myapplication.presentation.characterDetail.CharacterDetailScreen
import com.mbrown.myapplication.presentation.characterList.CharacterListScreen
import kotlinx.serialization.Serializable

@Serializable
data object CharacterList : NavKey

@Serializable
data class CharacterDetail(val name: String, val locationId: Long) : NavKey


@Composable
fun RMAppNavRoot() {
    val backStack = rememberNavBackStack(CharacterList)

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<CharacterList> {
                CharacterListScreen(
                    onCharacterClicked = { character ->
                        backStack.add(
                            CharacterDetail(
                                name = character.name,
                                locationId = character.location.id
                            )
                        )
                    }
                )
            }

            entry<CharacterDetail> { key ->
                CharacterDetailScreen(
                    characterName = key.name,
                    locationId = key.locationId
                )
            }
        }
    )
}