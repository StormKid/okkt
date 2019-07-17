package com.stormkid.okhttpdemo

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val string = "<span class=\"emoji emoji1f450\"></span"
        val pattern = Regex("1f\\d{1,3}")
        val result = pattern.find(string)
       print( result?.value)
    }
}
