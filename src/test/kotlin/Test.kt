import kotlin.test.Test
import io.restassured.RestAssured
import io.restassured.RestAssured.given

internal class Test {
    @Test
    fun successSendMessage() {
        RestAssured.baseURI = "https://api.telegram.org/bot6005881032:AAGdw14Aa7VwFUjBIjcQtW50G-09C3jKaOY";
        given()
            .param("text", "connection_TEST")
            .param("chat_id", "753442299")
            .`when`()
            .get("/sendMessage")
            .then()
            .statusCode(200)
    }

    @Test
    fun unSuccessSendMessage() {
        RestAssured.baseURI = "https://api.telegram.org/bot6005881032:AAGdw14Aa7VwFUjBIjcQtW50G-09C3jKaOY";
        given()
            .param("text", "unSuccess_TEST")
            .param("chat_id", "753442299")
            .param("parse_mode", "Markdown")
            .`when`()
            .get("/sendMessage")
            .then()
            .statusCode(400)
    }

    @Test
    fun getMeSuccess() {
        RestAssured.baseURI = "https://api.telegram.org/bot6005881032:AAGdw14Aa7VwFUjBIjcQtW50G-09C3jKaOY";
        given()
            .param("chat_id", "753442299")
            .`when`()
            .get("/getMe")
            .then()
            .statusCode(200)
    }

    @Test
    fun photoSuccess() {
        RestAssured.baseURI = "https://api.telegram.org/bot6005881032:AAGdw14Aa7VwFUjBIjcQtW50G-09C3jKaOY";
        given()
            .param("chat_id", "753442299")
            .param("photo", "https://img.delo-vcusa.ru/2019/11/Spagetti-Putaneska.jpg")
            .`when`()
            .get("/sendPhoto")
            .then()
            .statusCode(200)
    }

    @Test
    fun forwardSuccess() {
        RestAssured.baseURI = "https://api.telegram.org/bot6005881032:AAGdw14Aa7VwFUjBIjcQtW50G-09C3jKaOY";
        given()
            .param("chat_id", "753442299")
            .param("from_chat_id", "1053983931")
            .param("message_id", "1588")
            .`when`()
            .get("/forwardMessage")
            .then()
            .statusCode(200)
    }

    @Test
    fun copySuccess() {
        RestAssured.baseURI = "https://api.telegram.org/bot6005881032:AAGdw14Aa7VwFUjBIjcQtW50G-09C3jKaOY";
        given()
            .param("chat_id", "753442299")
            .param("from_chat_id", "1053983931")
            .param("message_id", "1588")
            .`when`()
            .get("/copyMessage")
            .then()
            .statusCode(200)
    }
}