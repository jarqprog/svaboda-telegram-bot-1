package com.svaboda.bot.fileresources

import com.svaboda.bot.commands.CommandTestUtils.cyrillicCommand
import com.svaboda.bot.domain.ResourcesProperties

object FileResourcesUtils {

    private const val TEXTS_PATH = "texts/"
    const val TEXTS_FILE_EXTENSION = ".txt"
    const val MAX_RESOURCE_SIZE = 3_000
    const val MIN_RESOURCE_SIZE = 500
    const val TOPICS_ENRICHMENT_LINE = "back to topics -> /topics"
    const val GO_TO_ARTICLE_LINE = "go to article ->"
    const val HEADER = "______________________________________________________"
    private const val CYRILLIC_CONTENT = "source=https://ru.wikipedia.org/wiki/%D0%9C%D0%B8%D1%86%D0%BA%D0%B5%D0%B2%D0%B8%D1%87,_%D0%90%D0%B4%D0%B0%D0%BC деятель польского национального движения, член общества филоматов. Оказал большое влияние на становление польской и белорусской литературы в XIX века[10]. В Польше считается одним из трёх величайших польских поэтов эпохи романтизма (наряду с Юлиушем Словацким и Зигмунтом Красинским). Некоторыми из белорусских литераторов Мицкевич выделяется как один из родоначальников новой белорусской литературы[11] и белорусским польскоговорящим поэтом[12]."
    private const val TOPICS_CONTENT = """
______________________________________________________

test test
test testtest test
test testtest testtest test

test testtest testtest testtest test ///
test testtest test
...

"""

    fun resourceProperties() = ResourcesProperties(
            TEXTS_PATH, TEXTS_FILE_EXTENSION, MAX_RESOURCE_SIZE,
            TOPICS_ENRICHMENT_LINE, GO_TO_ARTICLE_LINE, HEADER
    )

    fun topicsContent() = TOPICS_CONTENT
    fun cyrillicContent() = CYRILLIC_CONTENT
    fun cyrillicContentWithLinkAndTopics() = "\n$HEADER\n\n$CYRILLIC_CONTENT\n\n$GO_TO_ARTICLE_LINE ${
        cyrillicCommand()
                .externalLink()
                .get()
    }\n\n$TOPICS_ENRICHMENT_LINE\n\n"
}