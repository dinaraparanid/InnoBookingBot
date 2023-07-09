import com.paranid5.innobookingbot.data.firebase.configureFirebase
import com.paranid5.innobookingbot.data.firebase.isUserSignedIn
import com.paranid5.innobookingbot.data.firebase.sendLoginEmail
import com.paranid5.innobookingbot.data.firebase.userData
import kotlin.test.Test
import kotlin.test.assertEquals

class MockUpTest {
    private val userEmail = "n.petukhov@innopolis.university"

    @Test
    fun test1() {
        configureFirebase()
        val id = 800791305L
        //assertEquals(id.isUserSignedIn, false);
    }

    @Test
    fun test2() {
        val id = 753442299L
        //assertEquals(id.isUserSignedIn, true);
    }

    @Test
    fun test3() {
        val id = 753442299L
        //assertEquals(id.userData!!.get("id"), userEmail)
    }

    @Test
    fun test4() {
        val userID = 753442299;
        val id = 753442299L;
        //assertEquals(id.userData!!.get("tg_id"), userID.toString());
    }

    @Test
    fun verifyEmailSent() {
        //val num = sendLoginEmail(userEmail);
        //assertEquals(num, num);
    }
}