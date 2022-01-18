import { IItem } from 'app/entities/item/item.model';
import { IOrder } from 'app/entities/order/order.model';

export interface IOrderItem {
  id?: number;
  quantity?: number;
  item?: IItem | null;
  order?: IOrder | null;
}

export class OrderItem implements IOrderItem {
  constructor(public id?: number, public quantity?: number, public item?: IItem | null, public order?: IOrder | null) {}
}

export function getOrderItemIdentifier(orderItem: IOrderItem): number | undefined {
  return orderItem.id;
}
