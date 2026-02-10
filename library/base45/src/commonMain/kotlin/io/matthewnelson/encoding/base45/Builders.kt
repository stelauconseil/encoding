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
@file:Suppress("DEPRECATION")

package io.matthewnelson.encoding.base45

import kotlin.jvm.JvmField
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmSynthetic

/**
 * DEPRECATED since `2.6.0`
 * @suppress
 * @see [Base45.Builder]
 * @see [Base45.Companion.Builder]
 * */
@Deprecated(
    message = "Use Base45.Builder or Base45.Companion.Builder",
    level = DeprecationLevel.WARNING,
)
public fun Base45(
    config: Base45.Config?,
    block: Base45ConfigBuilder.() -> Unit,
): Base45 = Base45ConfigBuilder(config).apply(block).buildCompat()

/**
 * DEPRECATED since `2.6.0`
 * @suppress
 * @see [Base45.Builder]
 * @see [Base45.Companion.Builder]
 * */
@Deprecated(
    message = "Use Base45.Builder or Base45.Companion.Builder",
    level = DeprecationLevel.WARNING,
)
public fun Base45(
    block: Base45ConfigBuilder.() -> Unit,
): Base45 = Base45(null, block)

/**
 * DEPRECATED since `2.6.0`
 * @suppress
 * @see [Base45.Builder]
 * @see [Base45.Companion.Builder]
 * */
@Deprecated(
    message = "Use Base45.Builder or Base45.Companion.Builder",
    level = DeprecationLevel.WARNING,
)
public class Base45ConfigBuilder {

    private val compat: Base45.Builder

    public constructor(): this(config = null)
    public constructor(config: Base45.Config?) {
        compat = Base45.Builder(other = config)
    }


    /**
     * Refer to [Base45.Builder.build] documentation.
     * */
    public fun build(): Base45.Config = buildCompat().config

    @JvmSynthetic
    internal fun buildCompat(): Base45 = compat
        .build()

    /**
     * DEPRECATED since `2.3.1`
     * @suppress
     * */
    @JvmField
    @Deprecated(
        message = "Implementation is always constant time. Performance impact is negligible.",
        level = DeprecationLevel.ERROR,
    )
    public var isConstantTime: Boolean = true
}
