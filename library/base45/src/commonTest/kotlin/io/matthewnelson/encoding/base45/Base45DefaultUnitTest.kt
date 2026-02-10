/*
 * Copyright (c) 2023 Matthew Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
@file:Suppress("SpellCheckingInspection")

package io.matthewnelson.encoding.base45

import io.matthewnelson.encoding.core.Decoder.Companion.decodeToByteArray
import io.matthewnelson.encoding.core.Decoder.Companion.decodeToByteArrayOrNull
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToCharArray
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import io.matthewnelson.encoding.test.BaseNEncodingTest
import kotlin.test.Test
import kotlin.test.assertEquals

class Base45DefaultUnitTest: BaseNEncodingTest() {

    private fun base45(): Base45 = Base45.Builder {}

    override val decodeFailureDataSet: Set<Data<String, Any?>> = setOf(
        Data("%69 VD92EX^", expected = null, message = "Character '^' should return null"),
        Data("%69 VD92\nEX0", expected = null, message = "Whitespace characters should be rejected per RFC 9285"),
    )

    override val decodeSuccessHelloWorld: Data<String, ByteArray> =
        Data(raw = "%69 VD92EX0", expected = "Hello!!".encodeToByteArray())

    override val decodeSuccessDataSet: Set<Data<String, ByteArray>> = setOf(
        decodeSuccessHelloWorld,
        // RFC 9285 Appendix A test vectors
        Data(raw = "BB8", expected = "AB".encodeToByteArray()),
        Data(raw = "UJCLQE7W581", expected = "base-45".encodeToByteArray()),
    )

    override val encodeSuccessDataSet: Set<Data<String, String>> = setOf(
        Data(raw = "Hello!!", expected = "%69 VD92EX0"),
        Data(raw = "AB", expected = "BB8"),
        Data(raw = "base-45", expected = "UJCLQE7W581"),
    )

    override fun decode(data: String): ByteArray? {
        return data.decodeToByteArrayOrNull(base45())
    }

    override fun encode(data: ByteArray): String {
        return data.encodeToCharArray(base45()).joinToString("")
    }

    @Test
    fun givenString_whenEncoded_MatchesRfc9285Spec() {
        checkEncodeSuccessForDataSet(encodeSuccessDataSet)
    }

    @Test
    fun givenBadEncoding_whenDecoded_ReturnsNull() {
        checkDecodeFailureForDataSet(decodeFailureDataSet)
    }

    @Test
    fun givenEncodedData_whenDecoded_MatchesRfc9285Spec() {
        checkDecodeSuccessForDataSet(decodeSuccessDataSet)
    }

    @Test
    fun givenUniversalEncoderParameters_whenChecked_areSuccessful() {
        checkUniversalEncoderParameters()
    }

    @Test
    fun givenBase45_whenDecodeEncode_thenReturnsSameValue() {
        val expected = "UJCLQE7W581"
        val encoder = base45()
        val decoded = expected.decodeToByteArray(encoder)
        val actual = decoded.encodeToString(encoder)
        assertEquals(expected, actual)
    }

    @Test
    fun givenBase45_whenEncodeDecodeRandomData_thenBytesMatch() {
        checkRandomData()
    }

    @Test
    fun givenBase45_whenDecodeOutMaxSize_thenReturnsExpected() {
        for (s in 0..100) {
            val groups = (s + 2L) / 3L
            val expected = groups * 2L
            val actual = Base45.Default.config.decodeOutMaxSize(s.toLong())
            assertEquals(expected, actual, "Failed for input size $s")
        }

        val maxGroups = (Int.MAX_VALUE + 2L) / 3L
        val expectedMax = maxGroups * 2L
        assertEquals(expectedMax, Base45.Default.config.decodeOutMaxSize(Int.MAX_VALUE.toLong()))
    }

    @Test
    fun givenMixedLengths_whenEncodeDecode_thenRoundTripSuccessfully() {
        val encoder = base45()
        for (size in 1..20) {
            val data = ByteArray(size) { ((it * 31) + size).toByte() }
            val encoded = data.encodeToString(encoder)
            val decoded = encoded.decodeToByteArray(encoder)
            assertEquals(data.toList(), decoded.toList(), "size=$size")
        }
    }

    @Test
    fun givenLengths_whenEncodedSizeMatchesFormula() {
        val encoder = base45()
        for (size in 0..16) {
            val data = ByteArray(size) { it.toByte() }
            val encoded = data.encodeToString(encoder)
            val expectedLength = (size / 2) * 3 + (if (size % 2 == 0) 0 else 2)
            assertEquals(expectedLength, encoded.length, "size=$size")
        }
    }
}
