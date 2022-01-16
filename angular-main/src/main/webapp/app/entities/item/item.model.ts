import { IArtist } from 'app/entities/artist/artist.model';
import { IPublisher } from 'app/entities/publisher/publisher.model';
import { IImage } from 'app/entities/image/image.model';
import { Medium } from 'app/entities/enumerations/medium.model';

export interface IItem {
  id?: number;
  title?: string;
  description?: string | null;
  price?: number | null;
  medium?: Medium | null;
  artist?: IArtist | null;
  publisher?: IPublisher | null;
  image?: IImage | null;
}

export class Item implements IItem {
  constructor(
    public id?: number,
    public title?: string,
    public description?: string | null,
    public price?: number | null,
    public medium?: Medium | null,
    public artist?: IArtist | null,
    public publisher?: IPublisher | null,
    public image?: IImage | null
  ) {}
}

export function getItemIdentifier(item: IItem): number | undefined {
  return item.id;
}
