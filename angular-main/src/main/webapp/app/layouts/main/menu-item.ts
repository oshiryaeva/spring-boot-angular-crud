import { NbMenuItem } from '@nebular/theme';

export const USER_MENU: NbMenuItem[] = [
  {
    title: 'Dashboard',
    icon: 'home-outline',
    link: '/',
  },
  {
    title: 'Application',
    icon: 'copy-outline',
    expanded: false,
    children: [
      {
        title: 'Artists',
        icon: 'people-outline',
        link: 'artist',
      },
      {
        title: 'Customers',
        icon: 'people-outline',
        link: 'customer',
      },
      {
        title: 'Publishers',
        icon: 'people-outline',
        link: 'publisher',
      },
      {
        title: 'News Items',
        icon: 'people-outline',
        link: 'news-item',
      },
      {
        title: 'Orders',
        icon: 'people-outline',
        link: 'order',
      },
      {
        title: 'Items',
        icon: 'people-outline',
        link: 'item',
      },
      {
        title: 'Images',
        icon: 'people-outline',
        link: 'image',
      }
    ],
  }
];

