// You may use this file to create any models

export interface MenuItem{
    id: string
    name: string
    description: string
    price: number
}

export interface CartSlice {
    items: MenuItem[]
    quantity: number
    totalPrice: number
}

export interface LineItem {
    id: string
    name: string
    price: number
    quantity: number
    totalPrice: number
}