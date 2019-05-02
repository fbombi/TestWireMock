import com.github.tomakehurst.wiremock.WireMockServer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import java.io.FileNotFoundException;
import java.io.FileReader;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static net.serenitybdd.rest.SerenityRest.rest;

public class Testclass {

//https://www.petrikainulainen.net/wiremock-tutorial/
    //https://www.baeldung.com/introduction-to-wiremock
    //https://www.baeldung.com/wiremock-scenarios
    
    private WireMockServer wireMockServer;

    @BeforeMethod
    void configureSystemUnderTest() {
        this.wireMockServer = new WireMockServer(options().
                dynamicPort());
        this.wireMockServer.start();
        configureFor(this.wireMockServer.port());
    }
    private static final String filePath = "JSON/testjson.json";

    String createJson() {
        JsonParser parser = new JsonParser();
        FileReader fr = null;
        try {
            System.out.println(System.getProperty("user.dir")+"\\JSON\\testjson.json");
            fr = new FileReader(System.getProperty("user.dir")+"\\JSON\\testjson.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JsonElement datos = parser.parse(fr);
        return datos.toString();
    }


    @AfterMethod
    void stopServer() {
        this.wireMockServer.stop();
    }

    @Test
    void shouldIgnoreRequestMethod() {
        createJson();
//        givenThat(get(urlEqualTo("/api/message?id=1")).willReturn(aResponse().
//                withBodyFile(System.getProperty("user.dir")+"JSON/testjson.json")));
//          givenThat(get(urlEqualTo("/api/message?id=1")).willReturn(aResponse()
//                .withStatus(200).withBody("Hola Fabio"))
//        );


        String serverUrl = buildApiMethodUrl(1L);
        Response response = rest().given().contentType(ContentType.ANY).when().get(serverUrl);
        System.out.println(response.body().asString());
    }

    private String buildApiMethodUrl(Long messageId) {
        return String.format("http://localhost:%d/api/message?id=%d",
                this.wireMockServer.port(),
                messageId
        );
    }
}
