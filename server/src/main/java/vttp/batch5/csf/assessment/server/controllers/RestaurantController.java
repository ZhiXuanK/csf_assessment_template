package vttp.batch5.csf.assessment.server.controllers;

import java.io.StringReader;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.csf.assessment.server.models.PaymentReceipt;
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
  @PostMapping(path="/food_order", consumes=MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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

    Double totalPrice = (double) 0;
    JsonArray items = order.getJsonArray("items");
    for (int i=0; i<items.size(); i++){
      JsonObject obj = items.getJsonObject(i);
      totalPrice = totalPrice + (obj.getJsonNumber("price").doubleValue() * obj.getInt("quantity"));
    }

    PaymentReceipt paymentReceipt = null;
    try {
      paymentReceipt = restSvc.makePayment(order_id, order, totalPrice);

      //if payment is successful, add to database
      restSvc.saveDetails(order, paymentReceipt, totalPrice);

    } catch (Exception e) {
      JsonObject err = Json.createObjectBuilder()
        .add("message", e.getMessage())
        .build();

      return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(err.toString());
    }

    JsonObject resp = Json.createObjectBuilder()
      .add("orderId", order_id)
      .add("paymentId", paymentReceipt.getPayment_id())
      .add("total", totalPrice)
      .add("timestamp", paymentReceipt.getTimestamp())
      .build();

    return ResponseEntity.ok(resp.toString());
  }
}
