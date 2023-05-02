package org.acme.web;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.acme.utils.TestContainerResource;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import org.acme.domain.enums.CartStatus;
import static javax.ws.rs.core.Response.Status.INTERNAL_SERVER_ERROR;
import static org.hamcrest.Matchers.containsString;
import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import static org.assertj.core.api.Assertions.assertThat;
import java.sql.SQLException;
import java.util.Map;

@QuarkusTest
@QuarkusTestResource(TestContainerResource.class)
public class CartResourceTest {
    private static final String INSERT_WRONG_CART_IN_DB = "insert into carts values (999, current_timestamp, current_timestamp,'NEW', 3)";
    private static final String DELETE_WRONG_CART_IN_DB = "delete from carts where id = 999";
    @Inject
    DataSource datasource;

    @Test
    void testGetActiveCartForCustomerWhenThereAreTwoCartsInDB() {
        executeSql(INSERT_WRONG_CART_IN_DB);
        get("/carts/customer/3").then()
                .statusCode(INTERNAL_SERVER_ERROR.getStatusCode())
                .body(containsString(INTERNAL_SERVER_ERROR.getReasonPhrase()))
                .body(containsString("Many active carts detected !!!"));
        executeSql(DELETE_WRONG_CART_IN_DB);
    }
    private void executeSql(String query) {
        try (var connection = datasource.getConnection()) {
            var statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (SQLException e) {
            throw new IllegalStateException("Error has occurred while trying to execute SQL Query: " + e.getMessage());
        }
    }
    @Test
    void testFailCreateCartWhileHavingAlreadyActiveCart() {
        var requestParams = Map.of("firstName", "Saul", "lastName", "Berenson",
                "email", "call.saul@mail.com");var newCustomerId = given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(requestParams)
                .post("/customers").then()
                .statusCode(OK.getStatusCode())
                .extract()
                .jsonPath()
                .getLong("id");
        var newCartId = post("/carts/customer/" + newCustomerId).then()
                .statusCode(OK.getStatusCode())
                .extract()
                .jsonPath()
                .getLong("id");
        post("/carts/customer/" + newCustomerId).then()
                .statusCode(INTERNAL_SERVER_ERROR.getStatusCode())
                .body(containsString(INTERNAL_SERVER_ERROR.getReasonPhrase()))
                .body(containsString("There is already an active cart"));
        assertThat(newCartId).isNotNull();
        delete("/carts/" + newCartId).then()
                .statusCode(NO_CONTENT.getStatusCode());
        delete("/customers/" + newCustomerId).then()
                .statusCode(NO_CONTENT.getStatusCode());
    }

    @Test
    void testFindAll() {
        get("/carts").then()
                .statusCode(UNAUTHORIZED.getStatusCode());
    }

    @Test
    void testFindAllActiveCarts() {
        get("/carts/active").then()
                .statusCode(UNAUTHORIZED.getStatusCode());
    }

    @Test
    void testGetActiveCartForCustomer() {
        get("/carts/customer/3").then()
                .statusCode(UNAUTHORIZED.getStatusCode());
    }

    @Test
    void testFindById() {
        get("/carts/3").then()
                .statusCode(UNAUTHORIZED.getStatusCode());

        get("/carts/100").then()
                .statusCode(UNAUTHORIZED.getStatusCode());
    }

    @Test
    void testCreateCart() {
        var requestParams = Map.of("firstName", "Saul", "lastName", "Berenson",
                "email", "call.saul@mail.com");
        var newCustomerId = given()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .body(requestParams).post("/customers").then()
                .statusCode(OK.getStatusCode())
                .extract().jsonPath().getInt("id");
        var response = post("/carts/customer/" + newCustomerId).then()
                .statusCode(OK.getStatusCode())
                .extract().jsonPath()
                .getMap("$");
        assertThat(response.get("id")).isNotNull();
        assertThat(response).containsEntry("status", CartStatus.NEW.name());
        delete("/carts/" + response.get("id")).then()
                .statusCode(NO_CONTENT.getStatusCode());
        delete("/customers/" + newCustomerId).then()
                .statusCode(NO_CONTENT.getStatusCode());
    }

    @Test
    void testDelete() {
        get("/carts/active").then()
                .statusCode(UNAUTHORIZED.getStatusCode());

        delete("/carts/1").then()
                .statusCode(UNAUTHORIZED.getStatusCode());

        get("/carts/1").then()
                .statusCode(UNAUTHORIZED.getStatusCode());
    }
}
