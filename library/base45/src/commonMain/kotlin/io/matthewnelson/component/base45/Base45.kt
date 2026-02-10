/*
*  Copyright 2013 Square, Inc.
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*
*  This is a derivative work from Okio's Base45 implementation which can
*  be found here:
*
*      https://github.com/square/okio/blob/master/okio/src/commonMain/kotlin/okio/-Base45.kt
*
*  Original Author:
*
*      Alexander Y. Kleymenov
* */
@file:Suppress("DEPRECATION_ERROR", "NOTHING_TO_INLINE", "UNUSED_PARAMETER")

package io.matthewnelson.component.base45

import io.matthewnelson.encoding.core.Decoder.Companion.decodeToByteArrayOrNull
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToByteArray
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToCharArray
import io.matthewnelson.encoding.core.Encoder.Companion.encodeToString
import kotlin.jvm.JvmOverloads

/**
 * DEPRECATED since `1.2.0`
 * @suppress
 * */
@Deprecated(
    message = "Replaced by EncoderDecoder. Will be removed in future versions.",
    replaceWith = ReplaceWith(
        expression = "Base45",
        imports = [
            "io.matthewnelson.encoding.base45.Base45",
        ]
    ),
    level = DeprecationLevel.ERROR,
)
public sealed class Base45 {

    public object Default: Base45() {
        public const val CHARS: String = io.matthewnelson.encoding.base45.Base45.Default.CHARS
    }
}

/**
 * DEPRECATED since `1.2.0`
 * @suppress
 * */
@Deprecated(
    message = "Replaced by EncoderDecoders. Will be removed in future versions.",
    replaceWith = ReplaceWith(
        expression = "this.decodeToByteArrayOrNull(Base45.Builder {})",
        imports = [
            "io.matthewnelson.encoding.base45.Base45",
            "io.matthewnelson.encoding.core.Decoder.Companion.decodeToByteArrayOrNull",
        ],
    ),
    level = DeprecationLevel.HIDDEN,
)
public inline fun String.decodeBase45ToArray(): ByteArray? {
    return decodeToByteArrayOrNull(io.matthewnelson.encoding.base45.Base45.Builder {})
}

/**
 * DEPRECATED since `1.2.0`
 * @suppress
 * */
@Deprecated(
    message = "Replaced by EncoderDecoders. Will be removed in future versions.",
    replaceWith = ReplaceWith(
        expression = "this.decodeToByteArrayOrNull(Base45.Builder {})",
        imports = [
            "io.matthewnelson.encoding.base45.Base45",
            "io.matthewnelson.encoding.core.Decoder.Companion.decodeToByteArrayOrNull",
        ],
    ),
    level = DeprecationLevel.HIDDEN,
)
public fun CharArray.decodeBase45ToArray(): ByteArray? {
    return decodeToByteArrayOrNull(io.matthewnelson.encoding.base45.Base45.Builder {})
}

/**
 * DEPRECATED since `1.2.0`
 * @suppress
 * */
@Deprecated(
    message = "Replaced by EncoderDecoders. Will be removed in future versions.",
    replaceWith = ReplaceWith(
        expression = "this.encodeToString(Base45.Builder {})",
        imports = [
            "io.matthewnelson.encoding.base45.Base45",
            "io.matthewnelson.encoding.core.Encoder.Companion.encodeToString",
        ],
    ),
    level = DeprecationLevel.HIDDEN,
)
@JvmOverloads
public inline fun ByteArray.encodeBase45(base45: Base45.Default = Base45.Default): String {
    return encodeToString(io.matthewnelson.encoding.base45.Base45.Builder {})
}

/**
 * DEPRECATED since `1.2.0`
 * @suppress
 * */
@Deprecated(
    message = "Replaced by EncoderDecoders. Will be removed in future versions.",
    replaceWith = ReplaceWith(
        expression = "this.encodeToCharArray(Base45.Builder {})",
        imports = [
            "io.matthewnelson.encoding.base45.Base45",
            "io.matthewnelson.encoding.core.Encoder.Companion.encodeToCharArray",
        ],
    ),
    level = DeprecationLevel.HIDDEN,
)
@JvmOverloads
public inline fun ByteArray.encodeBase45ToCharArray(base45: Base45.Default = Base45.Default): CharArray {
    return encodeToCharArray(io.matthewnelson.encoding.base45.Base45.Builder {})
}

/**
 * DEPRECATED since `1.2.0`
 * @suppress
 * */
@Deprecated(
    message = "Replaced by EncoderDecoders. Will be removed in future versions.",
    replaceWith = ReplaceWith(
        expression = "this.encodeToByteArray(Base45.Builder {})",
        imports = [
            "io.matthewnelson.encoding.base45.Base45",
            "io.matthewnelson.encoding.core.Encoder.Companion.encodeToByteArray",
        ],
    ),
    level = DeprecationLevel.HIDDEN,
)
@JvmOverloads
public inline fun ByteArray.encodeBase45ToByteArray(base45: Base45.Default = Base45.Default): ByteArray {
    return encodeToByteArray(io.matthewnelson.encoding.base45.Base45.Builder {})
}

