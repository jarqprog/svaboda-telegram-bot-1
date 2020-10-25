package com.svaboda.utils

import com.svaboda.utils.ArgsValidation.notEmpty
import com.svaboda.utils.ArgsValidation.notNull
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ArgValidationTest {

    @Test
    fun `should throw exception when empty param provided`() {
        // given
        val invalidParams = listOf(null, "", " ")

        //then
        invalidParams.forEach {
            assertThrows<IllegalArgumentException> { notEmpty(it) }
        }
    }

    @Test
    fun `should return validated value`() {
        // given
        val validParams = listOf("valid", "another", "again")

        //then
        validParams.forEach {
            assertThat(notEmpty(it) == it)
        }
    }

    @Test
    fun `should throw exception when null param provided`() {
        //then
        assertThrows<IllegalArgumentException> { notNull(null) }
    }

    @Test
    fun `should return validated Long value`() {
        // given
        val validParam = 125L

        //then
        assertThat(notNull(validParam) == validParam)
    }
}