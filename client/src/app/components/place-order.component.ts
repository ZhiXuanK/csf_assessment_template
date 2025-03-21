import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { CartStore } from '../cart.store';
import { LineItem, MenuItem } from '../models';
import { RestaurantService } from '../restaurant.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent implements OnInit{

  // TODO: Task 3

  private restSvc = inject(RestaurantService)
  private cartStore = inject(CartStore)
  private router = inject(Router)
  private fb = inject(FormBuilder)
    
  totalPrice$ !: Observable<number>
  cart !: MenuItem[] //all items in cart
  cartItems !: MenuItem[] //unique values

  userForm !: FormGroup

  //cartPrice = 0

  ngOnInit(): void {
    this.userForm = this.createUserForm()
    this.cartStore.getItems.subscribe(
      res => {
        console.log(res)
        this.cart = res
        this.cartItems = [...new Set(res)]
      }
    )
    this.totalPrice$ = this.cartStore.getTotalPrice
  }

  // formLineItem(item:MenuItem):LineItem{
  //   const qty = this.cart.filter(i => i === item).length
  //   return {
  //     id: item.id,
  //     name: item.name,
  //     price: item.price,
  //     quantity: qty,
  //     totalPrice: (item.price) * (qty)
  //   }
  // }

  getQuantity(item:MenuItem){
    return this.cart.filter(i => i === item).length
  }

  getTotalPrice(price:number, quantity:number){
    const totalPrice = price*quantity
    //this.cartPrice = this.cartPrice + totalPrice
    return totalPrice
  }

  countIndivItem(item:MenuItem):number{
    return this.cart.filter(i => i === item).length
  }

  processForm(){
    console.log(this.userForm.value)
    const order = {
      username: this.userForm.value.username,
      password: this.userForm.value.password,
      items: this.cartItems.map(item => ({
        id: item.id,
        price: item.price,
        quantity: this.cart.filter(i => i === item).length
      }))
    }
    console.log(order)
    this.restSvc.postOrder(order).subscribe()
  }


  createUserForm():FormGroup{
    return this.fb.group({
      username: this.fb.control<string>('', [Validators.required]),
      password: this.fb.control<string>('', Validators.required)
    })
  }

  clear(){
    this.router.navigate(['/'])
  .then(() => {
    window.location.reload();
  });
  }
}
