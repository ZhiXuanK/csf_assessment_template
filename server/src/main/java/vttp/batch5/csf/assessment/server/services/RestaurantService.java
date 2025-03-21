package vttp.batch5.csf.assessment.server.services;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;
import vttp.batch5.csf.assessment.server.repositories.RestaurantRepository;

@Service
public class RestaurantService {

  @Autowired
  private RestaurantRepository restRepo;

  @Autowired
  private OrdersRepository ordersRepo;

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


}
