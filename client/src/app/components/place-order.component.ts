import { Component, inject, OnDestroy, OnInit } from '@angular/core';
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
    const totalPrice = (price*quantity)
    //this.cartPrice = this.cartPrice + totalPrice
    return totalPrice
  }

  countIndivItem(item:MenuItem):number{
    return this.cart.filter(i => i === item).length
  }

  processForm(){
    const order = {
      username: this.userForm.value.username,
      password: this.userForm.value.password,
      items: this.cartItems.map(item => ({
        id: item.id,
        price: item.price,
        quantity: this.cart.filter(i => i === item).length
      }))
    }
    this.restSvc.postOrder(order).subscribe({
      next: (data) => {
        console.log(data)
        this.restSvc.orderDetails = data
        this.router.navigate(['/confirm'])
      },
      error: (err) => alert(err.error.message)
    })
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
