package com.tag.shared.dependency_injection

import com.tag.domain.repository.TagRepository
import com.tag.domain.service.TagService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TagDomainModule {

    @Provides
    fun provideTagService (tagRepository: TagRepository): TagService {
        return TagService(tagRepository)
    }


}
