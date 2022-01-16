export interface ICustomer {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string | null;
}

export class Customer implements ICustomer {
  constructor(public id?: number, public firstName?: string, public lastName?: string, public email?: string | null) {}
}

export function getCustomerIdentifier(customer: ICustomer): number | undefined {
  return customer.id;
}
