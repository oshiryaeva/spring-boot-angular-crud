import * as dayjs from 'dayjs';
import { IArtist } from 'app/entities/artist/artist.model';

export interface INewsItem {
  id?: number;
  date?: dayjs.Dayjs | null;
  title?: string | null;
  description?: string | null;
  artist?: IArtist | null;
}

export class NewsItem implements INewsItem {
  constructor(
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public title?: string | null,
    public description?: string | null,
    public artist?: IArtist | null
  ) {}
}

export function getNewsItemIdentifier(newsItem: INewsItem): number | undefined {
  return newsItem.id;
}
