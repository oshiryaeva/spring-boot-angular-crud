export interface IPublisher {
  id?: number;
  name?: string | null;
}

export class Publisher implements IPublisher {
  constructor(public id?: number, public name?: string | null) {}
}

export function getPublisherIdentifier(publisher: IPublisher): number | undefined {
  return publisher.id;
}
