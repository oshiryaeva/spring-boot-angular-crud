import { ICustomer } from 'app/entities/customer/customer.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';

export interface IOrder {
  id?: number;
  amount?: number | null;
  customer?: ICustomer;
  orderItems?: IOrderItem[] | null;
}

export class Order implements IOrder {
  constructor(public id?: number, public amount?: number | null, public customer?: ICustomer, public orderItems?: IOrderItem[] | null) {}
}

export function getOrderIdentifier(order: IOrder): number | undefined {
  return order.id;
}
