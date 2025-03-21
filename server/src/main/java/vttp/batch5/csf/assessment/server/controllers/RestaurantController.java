package vttp.batch5.csf.assessment.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.JsonArray;
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
    return ResponseEntity.ok("{}");
  }
}
