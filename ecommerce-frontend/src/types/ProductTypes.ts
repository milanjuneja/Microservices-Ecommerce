import { Seller } from "./SellerTypes"

export interface Product {
  id?:number,
  title:string,
  description:string,
  mrpPrice:number,
  sellingPrice:number,
  discountPercentage:number,
  quantity:number,
  color:string,
  images: string[],
  numOfRatings:number,
  category:Category,
  seller: Seller,
  createdAt:Date,
  sizes:string[],
  reviews:Reviews
}

export interface Category{
  id?:number,
  name:string,
  categoryId:string,
  parentCategory:Category,
  level:number
}

interface Reviews{

}