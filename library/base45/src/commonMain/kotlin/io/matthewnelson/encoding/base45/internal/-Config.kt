/*
 * Copyright (c) 2025 Matthew Nelson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/
@file:Suppress("NOTHING_TO_INLINE")

package io.matthewnelson.encoding.base45.internal

import io.matthewnelson.encoding.base45.Base45

internal inline fun ((Boolean) -> Base45.Config).build(
    b: Base45.Builder,
    noinline base45: (Base45.Config, Any?) -> Base45,
): Base45 {
    if (b._backFillBuffers == Base45.Default.DELEGATE.config.backFillBuffers) {
        return Base45.Default.DELEGATE
    }
    val config = this(
        b._backFillBuffers,
    )
    return base45(config, null)
}
