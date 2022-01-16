export interface IImage {
  id?: number;
  name?: string | null;
  contentContentType?: string;
  content?: string;
}

export class Image implements IImage {
  constructor(public id?: number, public name?: string | null, public contentContentType?: string, public content?: string) {}
}

export function getImageIdentifier(image: IImage): number | undefined {
  return image.id;
}
