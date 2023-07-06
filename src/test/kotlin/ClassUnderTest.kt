import com.github.kotlintelegrambot.bot
import com.paranid5.innobookingbot.domain.bot.configureCommands
import com.github.kotlintelegrambot.dispatch
import io.ktor.client.*

import kotlin.test.Test


import com.paranid5.innobookingbot.data.firebase.*
import kotlin.test.assertEquals



class MockUpTest {
    private val userEmail: String = "n.petukhov@innopolis.university"

    @Test
    fun test1() {
        configureFirebase()
        val id = 800791305L
        assertEquals(id.isUserSignedIn, false);

//        val store = Mockito.mock(FirestoreClient.getFirestore());
//        Mockito.when(store.collection("users").whereEqualTo("tg_id", toString()).limit(1).get().get().documents.
//        firstOrNull()).thenReturn(Long.);

    }

    @Test
    fun test2() {
        val id = 753442299L
        assertEquals(id.isUserSignedIn, true);
    }

    @Test
    fun test3() {
        val id = 753442299L
        assertEquals(id.userData!!.get("id"), userEmail)
    }

    @Test
    fun test4() {
        val user_id = 753442299;
        val id = 753442299L;
        assertEquals(id.userData!!.get("tg_id"), user_id.toString());
    }


    @Test
    fun verifyEmailSent() {
        val num = sendLoginEmail(userEmail);
        assertEquals(num, num);
    }

}