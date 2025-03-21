import { Injectable } from "@angular/core";
import { CartSlice, MenuItem } from "./models";
import { ComponentStore } from "@ngrx/component-store";


const INIT_STORE:CartSlice = {
    items: [],
    quantity: 0,
    totalPrice: 0
}

@Injectable()
export class CartStore extends ComponentStore<CartSlice>{

    constructor(){
        super(INIT_STORE)
    }

    //selectors
    readonly countItemsInCart = this.select<number>(
        (store:CartSlice) => store.quantity
    )

    readonly getTotalPrice = this.select<number>(
        (store: CartSlice) => store.totalPrice
    )

    readonly getItems = this.select<MenuItem[]>(
        (store:CartSlice) => store.items
    )

    //mutators
    readonly addToCart = this.updater<MenuItem>(
        (store:CartSlice, newItem:MenuItem) => {

            //for duplicates, just add
            return {
                items: [...store.items, newItem],
                quantity: store.quantity + 1,
                totalPrice: store.totalPrice + newItem.price
            } as CartSlice

        }
    )

    readonly deleteItem = this.updater<MenuItem>(
        (store:CartSlice, itemToDelete:MenuItem) => {
            const index = store.items.findIndex((i) => i === itemToDelete)
            if (index == -1){
                return {
                    items: [...store.items],
                    quantity: store.quantity,
                    totalPrice: store.totalPrice
                }
            }
            const newItems = store.items
            newItems.splice(index, 1)
            return {
                items: newItems,
                quantity: store.quantity - 1,
                totalPrice: store.totalPrice - itemToDelete.price
            }
        }
    )

}