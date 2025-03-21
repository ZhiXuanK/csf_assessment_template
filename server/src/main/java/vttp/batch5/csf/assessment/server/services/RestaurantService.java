package vttp.batch5.csf.assessment.server.services;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp.batch5.csf.assessment.server.models.PaymentReceipt;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

  @Autowired
  private RestaurantRepository restRepo;

  @Autowired
  private OrdersRepository ordersRepo;

  public final String payment_gateway = "https://payment-service-production-a75a.up.railway.app";

  // TODO: Task 2.2
  // You may change the method's signature
  public JsonArray getMenu() {

    List<Document> results = ordersRepo.getMenu();

    System.out.println(results.toString());

    JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

    for (Document d:results){

      JsonObject obj = Json.createObjectBuilder()
        .add("id", d.getString("id"))
        .add("name", d.getString("name"))
        .add("description", d.getString("description"))
        .add("price", d.getDouble("price"))
        .build();
      
      arrBuilder.add(obj);
    }

    JsonArray array = arrBuilder.build();

    return array;

  }
  
  // TODO: Task 4
  public Boolean validateUser(String username, String password){

    String dbPassword = restRepo.getPassword(username);

    if (dbPassword.equals(password)){
      return true;
    } else {
      return false;
    }
  }

  public PaymentReceipt makePayment(String order_id, JsonObject order){

    Double totalPrice = order.getJsonNumber("price").doubleValue() * order.getInt("quantity");

    JsonObject body = Json.createObjectBuilder()
      .add("order_id", order_id)
      .add("payer", order.getString("username"))
      .add("payee", "Kang Zhi Xuan")
      .add("payment", totalPrice)
      .build();

    RequestEntity<String> req = RequestEntity
      .post(payment_gateway)
      .header("Accept", "application/json")
      .header("Content-Type", "application/json")
      .header("X-Authenticate", order.getString("username"))
      .body(body.toString(), String.class);

      RestTemplate template = new RestTemplate();
//stop here
      try {
         ResponseEntity<String> resp = template.exchange(req, String.class);
         String payload = resp.getBody();
         System.out.printf(">> %s\n", payload);
      } catch (Exception ex) {
         ex.printStackTrace();
      }
   }
  }

}
