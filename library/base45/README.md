# Module base45

Base45 encoding/decoding in accordance with [RFC 9285][url-rfc]

```kotlin
val base45 = Base45.Builder {
    isLenient(enable = true)
    lineBreak(interval = 64)
    lineBreakReset(onFlush = true)
    encodeUrlSafe(enable = false)
    padEncoded(enable = true)
    backFillBuffers(enable = true)
}

val text = "Hello World!"
val bytes = text.encodeToByteArray()
val encoded = bytes.encodeToString(base64)
println(encoded) // SGVsbG8gV29ybGQh

// Alternatively, use the static implementation containing
// pre-configured settings, instead of creating your own.
var decoded = encoded.decodeToByteArray(Base64.Default).decodeToString()
assertEquals(text, decoded)
decoded = encoded.decodeToByteArray(Base64.UrlSafe).decodeToString()
assertEquals(text, decoded)
```

[url-rfc]: https://www.ietf.org/rfc/rfc9265.html
