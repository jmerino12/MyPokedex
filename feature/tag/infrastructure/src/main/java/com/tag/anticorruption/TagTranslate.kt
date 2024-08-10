package com.tag.anticorruption

import com.core.database.tag.entity.TagEntity
import com.core.database.tag.entity.TagsWithPokemons
import com.pokemon.infrastructure.anticorruption.PokemonTranslate
import com.tag.domain.model.Tag

class TagTranslate {
    companion object {
        fun fromDomainToEntityTag(tag: Tag): TagEntity {
            return TagEntity(
                0L,
                tag.name,
            )
        }

        fun fromEntityTagToDomain(tag: TagsWithPokemons): Tag {
            return Tag(
                tag.tag.id,
                tag.tag.name,
                tag.pokemons.map { PokemonTranslate.fromEntityToDomain(it) }
            )
        }
    }
}