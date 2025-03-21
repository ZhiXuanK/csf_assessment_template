package vttp.batch5.csf.assessment.server.controllers;

import java.io.StringReader;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.csf.assessment.server.services.RestaurantService;

@RestController
@RequestMapping("/api")
public class RestaurantController {

  @Autowired
  private RestaurantService restSvc;

  // TODO: Task 2.2
  // You may change the method's signature
  @GetMapping(path="/menu", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getMenus() {

    JsonArray menu = restSvc.getMenu();

    return ResponseEntity.ok(menu.toString());
  }

  // TODO: Task 4
  // Do not change the method's signature
  public ResponseEntity<String> postFoodOrder(@RequestBody String payload) {

    JsonReader reader = Json.createReader(new StringReader(payload));
    JsonObject order = reader.readObject();

    Boolean userValidation = restSvc.validateUser(order.getString("username"), order.getString("password"));

    //return error message if user not validated
    if (!userValidation){
      JsonObject resp = Json.createObjectBuilder()
        .add("message", "Invalid username and/or password")
        .build();

      return ResponseEntity.status(401).body(resp.toString());
    }

    //generate order id
    String order_id = UUID.randomUUID().toString().substring(0,8);

    

    return ResponseEntity.ok("{}");
  }
}
