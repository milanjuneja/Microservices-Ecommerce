import { Product } from "./ProductTypes";
import { Address, User } from "./userTypes";

export interface OrderState {
  orders: Order[], 
  orderItem: OrderItem | null,
  currentOrder: Order | null,
  paymentOrder: any | null,
  loading: boolean,
  error: string | null,
  orderCancelled: boolean
}

export interface Order {
  id: number;
  orderId: string;
  user: User;
  sellerId: number;
  orderItems: OrderItem[];
  orderDate: string; 
  shipmentAddress: Address;
  paymentDetails: any;
  totalMrpPrice: number;
  totalSellingPrice: number;
  discount?: number;
  orderStatus: OrderStatus;
  totalItem: number;
  deliverDate: string; // ISO string format
}

export enum OrderStatus {
  PENDING = "PENDING",
  CONFIRMED = "CONFIRMED",
  SHIPPED = "SHIPPED",
  DELIVERED = "DELIVERED",
  CANCELLED = "CANCELLED"
}
export interface OrderItem {
  id: number;
  order: Order;
  product: Product;
  size: string;
  quantity: number;
  mrpPrice: number;
  sellingPrice: number;
  userId: number;
}