package PlivoApiAutomation;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;

import java.io.File;
import java.util.*;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

public class StepDefinitions {
    String temp;
    String valueOfRandomString;
    private Properties properties;
    private String authToken,respValue;
    Utils util = new Utils();

    HashMap<String, Object> map = new HashMap<>();
    private RequestSpecification request = RestAssured.with();
    private Response response;

    @Before(order = 1)
    public void setProperties() {

        properties = util.fetchFromPropertiesFile();
    }

    @Given("^Request headers$")
    public void request_headers_are(DataTable headerParams) {
        try {
            map = new HashMap<>();
            List<Map<String, String>> data = headerParams.asMaps(String.class, String.class);
            for (int i = 0; i < data.size(); i++) {
                String keyMain = data.get(i).get("Key");
                String keyValue = data.get(i).get("Value");

                if(keyMain.equalsIgnoreCase("Authorization")){
                    if(keyValue.equalsIgnoreCase("false")){
                        keyValue = "abc";
                        authToken = "abc";
                        map.put("Authorization", authToken);
                    }
                    else {
                        authToken=  "Bearer " + properties.getProperty("authToken");
                        keyValue = authToken;
                    }
                }
                map.put(keyMain, keyValue);
            }

            request.given()
                    .headers(map).log().all();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @And("^Request params$")
    public void request_params_are(DataTable queryParams) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, String>> data = queryParams.asMaps(String.class, String.class);
        for (int i = 0; i < data.size(); i++) {
            String keyMain = data.get(i).get("Key");
            String keyValue = data.get(i).get("Value");

            if(keyValue.equals("response")){
                keyValue = respValue;
            }
            map.put(keyMain, keyValue);
        }
        request.given()
                .queryParams(map);
    }


    @And("^Request Body Form Data$")
    public void request_formParams_are(DataTable formParams) {
        try {
            map = new HashMap<>();
            List<Map<String, String>> data = formParams.asMaps(String.class, String.class);
            for (int i = 0; i < data.size(); i++) {
                String keyMain = data.get(i).get("Key");
                String keyValue = data.get(i).get("Value");
                if(keyValue.equalsIgnoreCase("randomString")){
                    temp = util.randomAlphaNumeric(10).toLowerCase();
                    keyValue = "testautomation" + temp ;
                    valueOfRandomString = keyValue;

                }
                if(keyValue.equalsIgnoreCase("response")){
                    keyValue = respValue;
                }

                map.put(keyMain, keyValue);
            }
//            request.given().contentType(ContentType.URLENC.withCharset("UTF-8")).formParams(map).log().all();
            request.given().urlEncodingEnabled(true).formParams(map).log().all();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @When("^user hits \"(.*?)\" (.*?) api$")
    public void api_request_is_made(String uri, String method) {
        try {
            response = apiOperation(method,properties.getProperty("baseURI")+uri);
            request.log().all();
            System.out.println("The Response Body is: ");
            response.prettyPrint();
        } catch (Exception e) {
            e.printStackTrace();
            fail("unable to hit the request");
        } finally {
            request = RestAssured.with();
            System.out.println("Base URL is:" + properties.getProperty("baseURI"));
            request.given().baseUri(properties.getProperty("baseURI"));
        }
    }
    public Response apiOperation(String method, String uri){
        if(method.equalsIgnoreCase("get")){
            response = request.when().get(uri);
        }
        else if(method.equalsIgnoreCase("post")){
            response = request.when().post(uri);
        }
        else if(method.equalsIgnoreCase("delete")){
            response = request.when().post(uri);
        }
        else if(method.equalsIgnoreCase("put")){
            response = request.when().post(uri);
        }
        return response;
    }

    @Then("the status code is (\\d+)$")
 public void verify_status_code(int statusCode){
        response.then().statusCode(statusCode);
    }
    @And("^Returned json schema is \"(.*?)\"$")
    public void schema_is(String fileName) {
        String basePath = properties.getProperty("schema.path");
        File file = new File(basePath + fileName);
        response.then()
                .body(matchesJsonSchema(file));
    }
    @And("^get the (.*?) from the response$")
    public String get_the_value_from_response(String key) {

        if (key.equals("response")) {
            respValue = response.body().prettyPrint();

        } else {
            Object valueFromResp = response.then().extract().body().jsonPath().get(key);

            if (valueFromResp instanceof ArrayList) {
                ArrayList arrayId = ((ArrayList) valueFromResp);
                respValue = arrayId.get(0).toString();
            }
            else if (valueFromResp instanceof Integer)
                respValue = String.valueOf(valueFromResp);

            else {
                respValue = valueFromResp.toString();
            }
        }

        return respValue;
    }

    @And("^Response should contain$")
    public void response_should_contain_data(DataTable responseParams) {
        List<Map<String, String>> data = responseParams.asMaps(String.class, String.class);
        for (int j = 0; j < data.size(); j++) {
            String keyMain = data.get(j).get("Key");
            String keyValue = data.get(j).get("Value");
            if (keyValue.equalsIgnoreCase("response")) {
                keyValue = respValue;
            }
            if(keyValue.equalsIgnoreCase("valueOfRandomString")){
                keyValue = valueOfRandomString;
            }
            if (!keyValue.equals("null")) {
                assertThat(response.path(keyMain).toString(), containsString(keyValue));
            } else {
                assertNull(response.path(keyMain));
            }
        }
    }
    @And("^Verify if (.*?) present in response body$")
    public void verify_if_value_presentInResponseBody(String value) {
        if(value.equalsIgnoreCase("valueOfRandomString")){
            value = valueOfRandomString;
        }

        ResponseBody body = response.getBody();
        String bodyString = body.asString();

            Assert.assertEquals(bodyString.contains(value), true);
    }

}
