import { IItem } from 'app/entities/item/item.model';

export interface IOrderItem {
  id?: number;
  quantity?: number;
  item?: IItem | null;
}

export class OrderItem implements IOrderItem {
  constructor(public id?: number, public quantity?: number, public item?: IItem | null) {}
}

export function getOrderItemIdentifier(orderItem: IOrderItem): number | undefined {
  return orderItem.id;
}
