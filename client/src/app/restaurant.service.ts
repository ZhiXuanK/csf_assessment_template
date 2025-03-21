import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { firstValueFrom, Observable } from "rxjs";
import { MenuItem } from "./models";


@Injectable()
export class RestaurantService {

  private http = inject(HttpClient)

  // TODO: Task 2.2
  // You change the method's signature but not the name
  getMenuItems() : Observable<MenuItem[]>{

    return this.http.get<MenuItem[]>('/api/menu')

  }

  // TODO: Task 3.2
}
