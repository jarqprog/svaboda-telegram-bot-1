package com.svaboda.bot.fileresources

import com.svaboda.bot.commands.CommandTestUtils
import com.svaboda.bot.commands.CommandTestUtils.cyrillicCommand
import com.svaboda.bot.commands.CommandTestUtils.topicsCommand
import com.svaboda.bot.domain.ResourceProvider
import com.svaboda.bot.domain.TelegramResource
import com.svaboda.bot.fileresources.FileResourcesUtils.cyrillicContent
import com.svaboda.bot.fileresources.FileResourcesUtils.cyrillicContentWithLinkAndTopics
import com.svaboda.bot.fileresources.FileResourcesUtils.topicsContent
import io.vavr.control.Try
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import java.lang.RuntimeException

class CachedFileResourceProviderSpec {

    private val resourcesProperties = FileResourcesUtils.resourceProperties()
    private val commandsProperties = CommandTestUtils.commandsProperties()
    private lateinit var cachedFileResourceProviderIT: ResourceProvider<String>

    @Test
    fun `should return telegram resource with proper text content when default command provided`() {
        //given
        cachedFileResourceProviderIT = CachedFileResourceProvider(
                TextFileResourceReader(resourcesProperties), TextTransformer(resourcesProperties, commandsProperties)
        )

        val defaultCommand = topicsCommand()

        //when
        val result = cachedFileResourceProviderIT.provideBy(defaultCommand).get()

        //then
        assertThat(result).isEqualTo(TelegramResource(topicsContent()))
    }

    @Test
    fun `should cache result of file resource reader function call`() {
        //given
        val command = cyrillicCommand()
        val resourceProvider = Mockito.mock(TextFileResourceReader::class.java)
        val resourceTransformer = Mockito.mock(TextTransformer::class.java)
        Mockito.`when`(resourceProvider.readFrom(command.resourceId()))
                .thenReturn(Try.success(cyrillicContent()))
        Mockito.`when`(resourceTransformer.asContent(command, cyrillicContent()))
                .thenReturn(Try.success(cyrillicContentWithLinkAndTopics()))
        cachedFileResourceProviderIT = CachedFileResourceProvider(resourceProvider, resourceTransformer)
        val numberOfCalls = 10

        //when
        for (call in 1..numberOfCalls) {
            cachedFileResourceProviderIT.provideBy(command)
        }

        //then
        Mockito.verify(resourceProvider, Mockito.times(1)).readFrom(ArgumentMatchers.any())
    }

    @Test
    fun `should return the same result when calling multiple times with the same command`() {
        //given
        cachedFileResourceProviderIT = CachedFileResourceProvider(
                TextFileResourceReader(resourcesProperties), TextTransformer(resourcesProperties, commandsProperties)
        )

        val numberOfCalls = 10
        val command = cyrillicCommand()
        val expectedResult = TelegramResource(cyrillicContentWithLinkAndTopics())

        //when
        val result = cachedFileResourceProviderIT.provideBy(command).get()

        //then
        for (call in 1..numberOfCalls) {
            assertThat(result).isEqualTo(expectedResult)
        }
    }

    @Test
    fun `should return return failure when underlying resources provider failed`() {
        //given
        val command = cyrillicCommand()
        val resourceProvider = Mockito.mock(TextFileResourceReader::class.java)
        val resourceTransformer = Mockito.mock(TextTransformer::class.java)
        Mockito.`when`(resourceProvider.readFrom(command.resourceId()))
                .thenReturn(Try.failure(RuntimeException()))

        cachedFileResourceProviderIT = CachedFileResourceProvider(resourceProvider, resourceTransformer)

        //when
        val result = cachedFileResourceProviderIT.provideBy(command)

        //then
        assertThat(result.isFailure).isTrue
    }

}