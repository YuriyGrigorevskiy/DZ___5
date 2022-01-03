package org.example.api.pet;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.example.model.Pet;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class StoreApiTest {
    @BeforeClass
    public void prepareTest() throws IOException {

        System.getProperties().load(ClassLoader.getSystemResourceAsStream("my.properties"));

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/")
                .addHeader("api_key", System.getProperty("api.key"))

                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        RestAssured.filters(new ResponseLoggingFilter());
    }

    @Test
    public void addPet(){
        Pet kotik = new Pet();
        int id = new Random().nextInt(500000);
        String name = "Pet_" + UUID.randomUUID().toString();
        kotik.setId(id);
        kotik.setName(name);

        Pet pet = given()
                .body(kotik)
                .when().post("/pet")
                .then().statusCode(200)
                .extract().as(Pet.class);
        Assert.assertNotNull(pet.getId());
    }

    @Test
    public void tetDelete() {
        given()
                .pathParam("petId", 84662)
                .when()
                .delete("/pet/{petId}")
                .then()
                .statusCode(200);
        given()
                .pathParam("petId", 84662)
                .when()
                .get("/pet/{petId}")
                .then()
                .statusCode(404);
}}
