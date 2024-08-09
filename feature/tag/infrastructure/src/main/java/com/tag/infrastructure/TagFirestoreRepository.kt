package com.tag.infrastructure

import com.google.firebase.firestore.FirebaseFirestore
import com.auth.domain.model.User
import com.pokemon.domain.model.Pokemon
import com.tag.domain.model.Tag
import com.tag.domain.repository.TagRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class TagFirestoreRepository(private val firebase: FirebaseFirestore) : TagRepository {

    override suspend fun createTag(tag: Tag, user: User?) {
        val pokemonsData = tag.pokemons.map { pokemon ->
            hashMapOf(
                "name" to pokemon.name,
                "url" to pokemon.image
            )
        }

        val data = hashMapOf(
            "tag_name" to tag.name,
            "pokemons" to pokemonsData,
        )

        firebase.collection("tags/${user?.userId}")
            .add(data)
            .await()
    }

    override suspend fun deleteTag(tagName: String, user: User?) {
        val collection = firebase.collection("tags/${user?.userId}")

        val querySnapshot = collection.whereEqualTo("tag_name", tagName).get().await()

        for (document in querySnapshot.documents) {
            document.reference.delete().await()
        }
    }

    override suspend fun updateTag(tag: Tag, user: User?) {
        TODO("Not yet implemented")
    }

    override fun getTagByName(name: String, user: User?): Flow<Tag?> = callbackFlow {
        try {
            // Obtén la referencia de la colección del usuario
            val collection = firebase.collection("tags/${user?.userId}")

            // Realiza la consulta para encontrar el documento con el nombre del tag
            val query = collection.whereEqualTo("tag_name", name)

            // Escucha los cambios en la consulta
            val listenerRegistration = query.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    close(exception) // Cierra el flujo con un error si ocurre
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    // Mapear los resultados a objetos Tag
                    val tags = snapshot.documents.mapNotNull { document ->
                        val tagName = document.getString("tag_name")
                        val pokemonsData =
                            document.get("pokemons") as? List<Map<String, Any>> ?: emptyList()
                        val pokemons = pokemonsData.map { pokemonData ->
                            Pokemon(
                                name = pokemonData["name"] as? String ?: "",
                                image = pokemonData["url"] as? String ?: ""
                            )
                        }
                        tagName?.let { Tag(it, pokemons) }
                    }
                    // Emitir el primer tag encontrado o null si no hay resultados
                    trySend(tags.firstOrNull())
                } else {
                    trySend(null) // Emitir null si no se encontraron resultados
                }
            }

            awaitClose { listenerRegistration.remove() } // Limpiar el listener cuando el flujo se cierra

        } catch (e: Exception) {
            close(e) // Cierra el flujo con un error en caso de excepción
        }
    }


}