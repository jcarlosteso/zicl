export const Root = '/zicl';

export const Url = (path) => `${Root}${path}`;

const Navigation = [
  { url: Url('/index.html'), label: 'Introduction' },
  { url: Url('/overview/before_we_start.html'), label: 'Before we start' },
  { url: Url('/overview/structure.html'), label: 'Zork code structure' },
  { url: Url('/overview/project.html'), label: 'Project organization' },
  { url: Url('/engine/intro.html'), label: 'The game engine' }
]

export default Navigation;