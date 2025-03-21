import { Component, inject, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant.service';
import { PaymentReceipt } from '../models';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-confirmation',
  standalone: false,
  templateUrl: './confirmation.component.html',
  styleUrl: './confirmation.component.css'
})
export class ConfirmationComponent implements OnInit{

  private restSvc = inject(RestaurantService)
  private datePipe = inject(DatePipe)
  private router = inject(Router)

  orderDetails !: PaymentReceipt
  date !: string | null

  // TODO: Task 5
  ngOnInit(): void {
    this.orderDetails = this.restSvc.orderDetails
    this.date = this.datePipe.transform(new Date(this.orderDetails.timestamp),'MMM dd, yyyy')
  }

  clear(){
    this.router.navigate(['/'])
  .then(() => {
    window.location.reload();
  });
  }
}
