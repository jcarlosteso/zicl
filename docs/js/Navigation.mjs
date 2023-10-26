export const Root = '/zicl';

export const Url = (path) => `${Root}${path}`;

const Navigation = [
  { url: Url('/index.html'), label: 'Introduction', matcher: /^(\/)(?:zicl(?:\/)?)?(?:index(?:\.html?)?)?$/ },
  { url: Url('/overview/before_we_start.html'), label: 'Before we start', matcher: /overview\/before_we_start/ },
  { url: Url('/overview/structure.html'), label: 'Zork code structure', matcher: /overview\/structure/ },
  { url: Url('/overview/project.html'), label: 'Project organization', matcher: /overview\/project/ },
  { url: Url('/engine/intro.html'), label: 'The game engine', matcher: /engine\/intro/ },
  { url: Url('/engine/state.html'), label: 'Game state', matcher: /engine\/state/ }
]

export default Navigation;