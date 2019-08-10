package com.mercandalli.sdk.feature_aes

import org.junit.Assert
import org.junit.Test

class AesBase64ManagerImplTest {

    @Test
    fun generateKeyAndIv() {
        // Given
        val aesBase64Manager = AesModule().createAesBase64Manager()

        // When
        val key = aesBase64Manager.generateKey(AesKeySize.KEY_256)
        val iv = aesBase64Manager.generateInitializationVector(AesInitializationVectorSize.IV_12)

        // Then
        println(key)
        println(iv)
    }

    @Test
    fun keyIsLong() {
        // Given
        val aesBase64Manager = AesModule().createAesBase64Manager()

        // When
        val key = aesBase64Manager.generateKey(AesKeySize.KEY_256)

        // Then
        Assert.assertEquals(
            "key: $key | Key size: ${key.length}",
            44,
            key.length
        )
    }

    @Test
    fun initializationVectorIsLong() {
        // Given
        val aesBase64Manager = AesModule().createAesBase64Manager()

        // When
        val initializationVector = aesBase64Manager.generateInitializationVector(
            AesInitializationVectorSize.IV_12
        )

        // Then
        Assert.assertEquals(
            "initializationVector: $initializationVector | " +
                "Key size: ${initializationVector.length}",
            16,
            initializationVector.length
        )
    }

    @Test
    fun commute() {
        // Given
        val aesBase64Manager = AesModule().createAesBase64Manager()

        val cryptEntries = getCryptEntries()
        for (cryptEntry in cryptEntries) {
            val aesCrypter = aesBase64Manager.getAesCrypter(
                AesOpMode.CRYPT,
                AesMode.GCM,
                AesPadding.NO,
                cryptEntry.key,
                cryptEntry.initialisation
            )
            val unclear = aesCrypter.crypt(cryptEntry.messageClear)
            Assert.assertEquals(
                "Unclear does not match for $cryptEntry", cryptEntry.unclear, unclear
            )
            val aesDecrypter = aesBase64Manager.getAesCrypter(
                AesOpMode.DECRYPT,
                AesMode.GCM,
                AesPadding.NO,
                cryptEntry.key,
                cryptEntry.initialisation
            )
            val decrypt = aesDecrypter.decrypt(unclear)
            Assert.assertEquals(
                "Clear does not match for $cryptEntry", cryptEntry.messageClear, decrypt
            )
        }
    }

    private fun getCryptEntries(): Set<CryptGcmNoPaddingEntry> {
        return setOf(
            CryptGcmNoPaddingEntry(
                "Lorem Ipsum is simply dummy text of the printing and typesetting " +
                    "industry. Lorem Ipsum has been the industry's standard dummy text ever " +
                    "since the 1500s, when an unknown printer took a galley of type and " +
                    "scrambled it to make a type specimen book. It has survived not only five " +
                    "centuries, but also the leap into electronic typesetting, remaining " +
                    "essentially unchanged. It was popularised in the 1960s with the release " +
                    "of Letraset sheets containing Lorem Ipsum passages, and more recently " +
                    "with desktop publishing software like Aldus PageMaker including versions " +
                    "of Lorem Ipsum.",
                "RchOvbt0RHxaM08Kmt/PjIbq7q4hPIP/i/PkmgqNzBc=",
                "NaRbeWN25ZmayTYx",
                "nFJIDiBhnHnH5mChhKK5AvcOSeBVvozxx6IoAz2c/CjCYFe1MUEFeyUz/GRzgtRd6lgr4" +
                    "IyQ0nSa06xO6UbZamwevoP2umDIrkqwlCyUWgREGHYWOQHPHpdaHJeHvJaocT+aSDJkqVASrc" +
                    "va6InALKC0KaJBf1bWFioNoCVAMEFsaciErNPInRxTfqAzpl/2g2C+GNzfOpocqIpKBWxYK2r" +
                    "zqs7oePvVbjC0PpmcK2rIz8YdVD+9u/whY9awOWXHwNqX/ps/K20G/P1q2bq8PjY2IKORrSza" +
                    "U5gxrghrGUSdkKx/zFtKGrfO1F373I/9+m8EFa75h6kAu8hKCZ1Qz6RsfDiKrlS+mRS49SeeDX" +
                    "1co+4XUI70NWn/getP/beVdSTf2de57PL2eTJgE6IO7PDQxgm03a6JsHoGNJiM/0cE59TDssRV" +
                    "CN4kACHVG1YL4yqxBHjra3jB2lQiZzr/k9zyUgKGBhNZIJHew2dANbKqyteEHjWd4Hz1Ta0fSI" +
                    "L1Mgk7MA01/P9mYhC05D6YSTgZux5jP3MJ3cr9CiHi6aPkA/V7SMOk3rsypX8xrP4rKOBhVcwQ" +
                    "4mfRhY8bOwcG//rjaxeQWvVZYZPLVKzLp/sCRTQrVVkvxRPqj/9mlQ48jimZb1iQ5/lGRzCJuTS" +
                    "kzDTBZ1LuuwJj7UQU7AMyIr8Po3P6fWg1Nm7ufpWZZLqr0QD4TzGfVQfHump7tbVYuZwJ7peSmI" +
                    "D5LIkJ81u1QM2kc3aQx1QwquAct/jO+ENgOpWfyyybp5mCRQ5zXzJuWOAvFPjPdMKzoEulB0BB" +
                    "ipTiAdmh9KGPT0lDpgMSCHeJIaX6kQXFEeECO0/V8rKBQ9SezjNY8Swpownmr0GavBfuVRwz+n" +
                    "Hn7kmnaHaC2HhTMsTmACu18BKS6w61mvAXWzcGWZD3jiIi2YGkAa7l9tNBNu+076ixbnzj+Hu0" +
                    "jWmFT2XWsLTw4GSjTQptqsapGIDaG+r2El/RbjAzkWl/xS2FTaOC+sijx9QHFz0TZMriu4zQ8H" +
                    "xcj9dHLRjVnNUs0Q=="
            ),

            CryptGcmNoPaddingEntry(
                "火多非時課洲之長不這術題送友如需樹廣細陽時約，她是懷護是她包重課里快要我！" +
                    "記靈這的，場亮情高的專他企輕制……看說意什不隊因東黑臺有。理特直法斷全營不定？言強不統。" +
                    "草軍機小帶山少，長英續高北中把；有活作十程，市食候教企理還的賽服不直心。認媽營車們成" +
                    "活了：動息是重馬來發是公石而服歌紀內，立已業河！",
                "RchOvbt0RHxaM08Kmt/PjIbq7q4hPIP/i/PkmgqNzBc=",
                "NaRbeWN25ZmayTYx",
                "/SE2BU9X/3+i5BC+1YXkJqAddf4D/rP8kak9ekKB+SqVew/OTlAUTXYw6lcn0cp3uEYAz" +
                    "vCBu0HO5fg9vkuVRRAokcOZm1eS/iWhj3n8YQ0VL3cgVk/eK8IAN+vR7pPkIRzHWVNM9Rp+s" +
                    "cmOjqHrIPGCTvAVYWnqaTQigklcdV8+d8Wl0PaPmnBQSZhigXyR/mSKPYmnAatIn+prUFhKF" +
                    "gWOjf+9F+n/PxahOsiTGHOZzd8iKxaGkqt5XP/ME3Thr8Wj0eAnE1hTyfNbiNG9YGAwIabHq" +
                    "y2GBMA8l1wTDEDht71WmEAKa9ja9HSpwcfArkwzGtLO0blUnsV0Wod3j8t1R0LbwlWR5Wa9x" +
                    "0jpIjIwpdFPB6vxEAbYsb8b/LGdJFeC2ICiw86ZS0Zpbd0lv6Xc+i/ijJXW3AQDDvfsnFhpv" +
                    "q725fRTJbswJAiBLX0XnwC8ZS7nWDqQ1UUxGx+Jt4+GaTnSMTxfX5/xyzYaEZjFmfSoTySyw" +
                    "SjucqNmZLHXYWYTNWES/c8xaQGzsDX/TW8H3zQ1Slg1iJPWMV3H1pOINfQtHvfe8e8Vv0Jgq" +
                    "OseR41sfp4B+1KG3Z0tRAMDqq3EVCnHe/BiDt/8CPqawtNUHyUYOkUm+EPYvPcxzCU14TLZS" +
                    "jee9d46bTXoxQ6Tk2L0VGK7vzkzulonw1cFK6VgkFj8KVgSSDnISoHhfOfeqX3YEGDdUEGTo" +
                    "H844ccmpMkt+6n9v7/NQ6t5yDTrUcHLcnOHqTsHi7FpgM6a3XB1a6yF2EDb1ZbRNTFBoppRW" +
                    "zRPHYiVNE7fxWoOcQ=="
            )
        )
    }

    data class CryptGcmNoPaddingEntry(
        val messageClear: String,
        val key: String,
        val initialisation: String,
        val unclear: String
    )
}
