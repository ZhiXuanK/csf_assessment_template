package vttp.batch5.csf.assessment.server.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.stereotype.Repository;


@Repository
public class OrdersRepository {

  @Autowired
  private MongoTemplate mongoTemplate;

  // TODO: Task 2.2
  // You may change the method's signature
  // Write the native MongoDB query in the comment below
  //
/*    db.menus.aggregate([
        {
          $project: {
              id: "$_id",
              name: 1,
              description: 1,
              price: 1
          }   
        },
        {
            $sort: {name: 1}
        }
    ]);
*/
  //
  public List<Document> getMenu() {

    ProjectionOperation projectMenu = Aggregation.project("name", "description", "price").and("_id").as("id");

    SortOperation sortByName = Aggregation.sort(Sort.by(Direction.ASC, "name"));

    Aggregation pipeline = Aggregation.newAggregation(projectMenu, sortByName);

    AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, "menus", Document.class);

    return results.getMappedResults();

  }

  // TODO: Task 4
  // Write the native MongoDB query for your access methods in the comment below
  // db.orders.insert({
  //     _id: "abcd1234",
  //     order_id: "abcd1234",
  //     payment_id: "xyz789",
  //     username: "fred",
  //     total: 23.10,
  //     timestamp: new Date(03/03/03),
  //     items: [
  //         {id: "xxx", price: 7.70, quantity: 2},
  //         {id: "yyy", price: 8.80, quantity: 3}
  //     ]
  // });
  //  Native MongoDB query here
  public void insertOrder(Document document){
    mongoTemplate.insert(document, "orders");
  }

}
