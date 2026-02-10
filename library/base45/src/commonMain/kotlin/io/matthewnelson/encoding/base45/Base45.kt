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
@file:Suppress("FunctionName", "NOTHING_TO_INLINE", "PropertyName", "RedundantModalityModifier", "RedundantVisibilityModifier", "RemoveRedundantQualifierName")

package io.matthewnelson.encoding.base45

import io.matthewnelson.encoding.base45.internal.build
import io.matthewnelson.encoding.core.Decoder
import io.matthewnelson.encoding.core.Encoder
import io.matthewnelson.encoding.core.EncoderDecoder
import io.matthewnelson.encoding.core.MalformedEncodingException
import io.matthewnelson.encoding.core.util.DecoderInput
import io.matthewnelson.encoding.core.util.FeedBuffer
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.jvm.JvmField
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic
import kotlin.jvm.JvmSynthetic

/**
 * Base45 encoding/decoding in accordance with [RFC 9285](https://www.rfc-editor.org/rfc/rfc9285.html).
 *
 * Base45 uses a 45-character alphabet and encodes data in groups:
 * - 2 bytes → 3 characters
 * - 1 byte → 2 characters
 * - No padding is used
 *
 * e.g.
 *
 *     val base45 = Base45.Builder {
 *         backFillBuffers(enable = true)
 *     }
 *
 *     val text = "Hello World!"
 *     val bytes = text.encodeToByteArray()
 *     val encoded = bytes.encodeToString(base45)
 *     println(encoded) // %69 VD92EX0
 *
 *     // Alternatively, use the static implementation containing
 *     // pre-configured settings, instead of creating your own.
 *     val decoded = encoded.decodeToByteArray(Base45.Default).decodeToString()
 *     assertEquals(text, decoded)
 *
 * @see [Builder]
 * @see [Companion.Builder]
 * @see [Default]
 * @see [Encoder.Companion]
 * @see [Decoder.Companion]
 * */
public class Base45: EncoderDecoder<Base45.Config> {

    public companion object {

        /**
         * Syntactic sugar for Kotlin consumers that like lambdas.
         * */
        @JvmStatic
        @JvmName("-Builder")
        @OptIn(ExperimentalContracts::class)
        public inline fun Builder(block: Builder.() -> Unit): Base45 {
            contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
            return Builder(other = null, block)
        }

        /**
         * Syntactic sugar for Kotlin consumers that like lambdas.
         * */
        @JvmStatic
        @JvmName("-Builder")
        @OptIn(ExperimentalContracts::class)
        public inline fun Builder(other: Base45.Config?, block: Builder.() -> Unit): Base45 {
            contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
            return Builder(other).apply(block).build()
        }

        private const val NAME = "Base45"
    }

    /**
     * A Builder
     *
     * @see [Companion.Builder]
     * */
    public class Builder {

        public constructor(): this(other = null)
        public constructor(other: Config?) {
            if (other == null) return
            this._backFillBuffers = other.backFillBuffers
        }

        @get:JvmSynthetic
        internal var _backFillBuffers: Boolean = true
            private set

        /**
         * DEFAULT: `true`
         *
         * @see [EncoderDecoder.Config.backFillBuffers]
         * */
        public fun backFillBuffers(enable: Boolean): Builder = apply { _backFillBuffers = enable }


        /**
         * Commits configured options to [Config], creating the [Base45] instance.
         * */
        public fun build(): Base45 = Config.build(this)
    }

    /**
     * Holder of a configuration for the [Base45] encoder/decoder instance.
     *
     * @see [Builder]
     * @see [Companion.Builder]
     * */
    public class Config private constructor(
        backFillBuffers: Boolean,
    ): EncoderDecoder.Config(
        isLenient = null,
        lineBreakInterval = 0,
        lineBreakResetOnFlush = false,
        paddingChar = null,
        maxDecodeEmit = 2,
        maxEncodeEmit = 3,
        backFillBuffers,
    ) {

        protected override fun decodeOutMaxSizeProtected(encodedSize: Long): Long {
            val groups = (encodedSize + 2L) / 3L
            return groups * 2L
        }

        protected override fun decodeOutMaxSizeOrFailProtected(encodedSize: Int, input: DecoderInput): Int {
            return decodeOutMaxSizeProtected(encodedSize.toLong()).toInt()
        }

        protected override fun encodeOutSizeProtected(unEncodedSize: Long): Long {
            if (unEncodedSize > MAX_UNENCODED_SIZE) {
                throw outSizeExceedsMaxEncodingSizeException(unEncodedSize, Long.MAX_VALUE)
            }

            val pairs = unEncodedSize / 2L
            val remainder = unEncodedSize % 2L
            return (pairs * 3L) + (remainder * 2L)
        }

        protected override fun toStringAddSettings(): Set<Setting> = buildSet(capacity = 1) {
            add(Setting(name = "isConstantTime", value = isConstantTime))
        }

        internal companion object {

            private const val MAX_UNENCODED_SIZE: Long = Long.MAX_VALUE / 3L * 2L

            @JvmSynthetic
            internal fun build(b: Builder): Base45 = ::Config.build(b, ::Base45)

            @get:JvmSynthetic
            internal val DEFAULT: Config = Config(
                backFillBuffers = true,
            )
        }

        /** @suppress */
        @JvmField
        public val isConstantTime: Boolean = true
    }

    /**
     * A static instance of [EncoderDecoder] configured according to RFC 9285.
     * */
    public object Default: EncoderDecoder<Base45.Config>(config = Base45.Config.DEFAULT) {

        /**
         * Base45 encoding characters per RFC 9285.
         * */
        public const val CHARS: String = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ $%*+-./:"

        @get:JvmSynthetic
        internal val DELEGATE = Base45(config, unused = null)
        override fun name(): String = DELEGATE.name()
        override fun newDecoderFeedProtected(out: Decoder.OutFeed): Decoder<Base45.Config>.Feed {
            return DELEGATE.newDecoderFeedProtected(out)
        }
        override fun newEncoderFeedProtected(out: Encoder.OutFeed): Encoder<Base45.Config>.Feed {
            return DELEGATE.newEncoderFeedProtected(out)
        }
    }

    protected final override fun name(): String = NAME

    protected final override fun newDecoderFeedProtected(out: Decoder.OutFeed): Decoder<Base45.Config>.Feed {
        return DecoderFeed(out)
    }

    protected final override fun newEncoderFeedProtected(out: Encoder.OutFeed): Encoder<Base45.Config>.Feed {
        return EncoderFeed(out)
    }

    private inner class DecoderFeed(out: Decoder.OutFeed): Decoder<Config>.Feed(_out = out) {

        private var _count = 0
        private var _buf = IntArray(3)

        override fun consumeProtected(input: Char) {
            val value = input.charToValue()
            if (value == -1) {
                throw MalformedEncodingException("Char[$input] is not a valid $NAME character")
            }

            _buf[_count++] = value

            if (_count == 3) {
                val c = _buf[0]
                val d = _buf[1]
                val e = _buf[2]

                val value = c + d * 45 + e * 45 * 45

                if (value > 65535) {
                    throw MalformedEncodingException("Invalid Base45 triple encoding")
                }

                _out.output((value shr 8).toByte())
                _out.output((value and 0xFF).toByte())
                _count = 0
            }
        }

        override fun doFinalProtected() {
            if (_count == 0) return

            if (_count == 1) {
                throw FeedBuffer.truncatedInputEncodingException(1)
            }

            if (_count == 2) {
                val c = _buf[0]
                val d = _buf[1]

                val value = c + d * 45

                if (value > 255) {
                    throw MalformedEncodingException("Invalid Base45 pair encoding")
                }

                _out.output(value.toByte())
                _count = 0
                return
            }
        }

        private fun Char.charToValue(): Int {
            return when (this) {
                in '0'..'9' -> this.code - '0'.code
                in 'A'..'Z' -> this.code - 'A'.code + 10
                ' ' -> 36
                '$' -> 37
                '%' -> 38
                '*' -> 39
                '+' -> 40
                '-' -> 41
                '.' -> 42
                '/' -> 43
                ':' -> 44
                else -> -1
            }
        }
    }

    private inner class EncoderFeed(out: Encoder.OutFeed): Encoder<Config>.Feed(_out = out) {

        private var _buf: Byte? = null

        override fun consumeProtected(input: Byte) {
            val buffered = _buf
            if (buffered == null) {
                _buf = input
                return
            }

            _buf = null

            val b1 = buffered.toInt() and 0xFF
            val b2 = input.toInt() and 0xFF
            val value = (b1 shl 8) or b2

            val c = value % 45
            val d = (value / 45) % 45
            val e = (value / 45 / 45) % 45

            _out.output(Default.CHARS[c])
            _out.output(Default.CHARS[d])
            _out.output(Default.CHARS[e])
        }

        override fun doFinalProtected() {
            val buffered = _buf ?: return
            _buf = null

            val value = buffered.toInt() and 0xFF

            val c = value % 45
            val d = (value / 45) % 45

            _out.output(Default.CHARS[c])
            _out.output(Default.CHARS[d])
        }
    }

    /**
     * DEPRECATED since `2.6.0`
     * @suppress
     * */
    @Deprecated(
        message = "This constructor is scheduled for removal. Use Base45.Builder or Base45.Companion.Builder.",
        level = DeprecationLevel.WARNING,
    )
    public constructor(config: Config): this(config, unused = null)

    @Suppress("UNUSED_PARAMETER")
    private constructor(config: Config, unused: Any?): super(config)
}
