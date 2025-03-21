package vttp.batch5.csf.assessment.server.services;

import java.io.StringReader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.csf.assessment.server.models.PaymentReceipt;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

  @Autowired
  private RestaurantRepository restRepo;

  @Autowired
  private OrdersRepository ordersRepo;

  public final String payment_gateway = "https://payment-service-production-a75a.up.railway.app/api/payment";

  // TODO: Task 2.2
  // You may change the method's signature
  public JsonArray getMenu() {

    List<Document> results = ordersRepo.getMenu();

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

    Boolean dbPassword = restRepo.checkPassword(username, password);

    return dbPassword;
  }

  public PaymentReceipt makePayment(String order_id, JsonObject order, Double totalPrice) throws Exception{

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
    PaymentReceipt paymentReceipt = null;
    try {
        ResponseEntity<String> resp = template.exchange(req, String.class);
        String payload = resp.getBody();
        JsonReader r = Json.createReader(new StringReader(payload));
        JsonObject obj = r.readObject();
        paymentReceipt = new PaymentReceipt(
          obj.getString("payment_id"),
          obj.getString("order_id"),
          obj.getJsonNumber("timestamp").longValue()
        );
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e.getMessage());
    }
    return paymentReceipt;
  }

  @Transactional
  public void saveDetails(JsonObject order, PaymentReceipt receipt, Double totalPrice) throws Exception{
    try {
      restRepo.insertDetails(order, receipt, totalPrice);

      JsonArray itemsArray = order.getJsonArray("items");
      List<Document> items = new LinkedList<>();
      for (int i =0; i<itemsArray.size(); i++){
        JsonObject obj = itemsArray.getJsonObject(i);
        Document doc = new Document()
          .append("id", obj.getString("id"))
          .append("price", obj.getJsonNumber("price").doubleValue())
          .append("quantity", obj.getInt("quantity"));
        items.add(doc);
      }

      Document toInsert = new Document()
        .append("_id", receipt.getOrder_id())
        .append("order_id", receipt.getOrder_id())
        .append("username", order.getString("username"))
        .append("total", totalPrice)
        .append("timestamp", new Date(receipt.getTimestamp()))
        .append("items", items);
  
      ordersRepo.insertOrder(toInsert);
    } catch (Exception e) {
      e.printStackTrace();
      throw new Exception(e.getMessage());
    }


  }

}
