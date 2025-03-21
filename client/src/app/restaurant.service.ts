import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { firstValueFrom, Observable } from "rxjs";
import { MenuItem, PaymentReceipt } from "./models";


@Injectable()
export class RestaurantService {

  private http = inject(HttpClient)

  orderDetails!:any

  // TODO: Task 2.2
  // You change the method's signature but not the name
  getMenuItems() : Observable<MenuItem[]>{

    return this.http.get<MenuItem[]>('/api/menu')

  }

  // TODO: Task 3.2
  postOrder(order:any): Observable<PaymentReceipt>{
    return this.http.post<PaymentReceipt>('/api/food_order', order)
  }
}
