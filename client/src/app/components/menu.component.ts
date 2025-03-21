import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant.service';
import { MenuItem } from '../models';
import { Observable } from 'rxjs';
import { CartStore } from '../cart.store';
import { Router } from '@angular/router';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit{
  // TODO: Task 2

  private restSvc = inject(RestaurantService)
  private cartStore = inject(CartStore)
  private router = inject(Router)
  
  menuItems$ !: Observable<MenuItem[]>
  totalPrice$ !: Observable<number>
  cartCount$ !: Observable<number>

  cartItem !: MenuItem[]
  disabled !: boolean

  ngOnInit():void{
    this.menuItems$ = this.restSvc.getMenuItems()
    this.totalPrice$ = this.cartStore.getTotalPrice
    this.cartCount$ = this.cartStore.countItemsInCart
    this.cartStore.getItems.subscribe(
      res => {
        this.cartItem = res
        if (res .length> 0){
          this.disabled = false
        } else {
          this.disabled = true
        }
      } 
    ) 
  }

  deleteOneItem(item:MenuItem){
    this.cartStore.deleteItem(item)
  }

  addOneItem(item:MenuItem){
    this.cartStore.addToCart(item)
  }

  countIndivItem(item:MenuItem):number{
    return this.cartItem.filter(i => i === item).length
  }

  placeOrder(){
    this.router.navigate(['/order'])
  }

}
