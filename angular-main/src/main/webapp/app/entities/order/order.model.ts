export interface IOrder {
  id?: number;
}

export class Order implements IOrder {
  constructor(public id?: number) {}
}

export function getOrderIdentifier(order: IOrder): number | undefined {
  return order.id;
}
