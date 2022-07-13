package com.rainbow.rainbowconsole

import com.rainbow.rainbowconsole.config.AppConfig
import com.rainbow.rainbowconsole.repository.MemberRepositoryImpl
import com.rainbow.rainbowconsole.repository.MemberRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleUnitTest {
    private val memberRepository : MemberRepository = MemberRepositoryImpl(AppConfig.firestore)
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun checkFirestore(){
        val name = "조진수"

        CoroutineScope(Dispatchers.IO).launch {
            val user = memberRepository.findByName(name)
            println(user.toString())
        }
    }


}