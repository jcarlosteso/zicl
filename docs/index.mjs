import Navigation, { Url } from "./js/Navigation.mjs";

document.addEventListener('DOMContentLoaded', () => {
  console.log('Starting...');
  const root = document.querySelector('body');

  const contents = Array.from(root.children).filter(item => item.localName !== 'aside');
  const tldr  = root.querySelector('aside');

  root.appendChild(Header('The Adven<em>j</em>urer reloaded'));
  root.appendChild(Section(contents, tldr));
})

const Header = (html) => {
  const header = document.createElement('header');
  const h1 = header.appendChild(document.createElement('h1'));
  h1.innerHTML = html;
  
  return header;
}

const currentPage = () => (location.pathname === Url("/")
                            ? Url('/index.html')
                            : location.pathname)

const Index = (occult) => {
  const aside = document.createElement('aside');

  const img = document.createElement('img');
  img.setAttribute('src', `/zicl/img/${occult ? 'occult' : 'cropped-advenjurer-1'}.png`)
  aside.appendChild(img);

  const heading = aside.appendChild(document.createElement('h3'));
  heading.textContent = 'Contents';
  
  const page = currentPage()

  Navigation.forEach(({url, label, matcher}) => {
    const current = page.match(matcher);

    const child = document.createElement(
      current ? 'p' : 'a'
    )

    aside.appendChild(child);
    child.textContent = label;

    if (!current) child.href = url;
  });

  return aside;
}

const PageLink = ({url, label}) => {
  const small = document.createElement('small');
  const anchor = small.appendChild(document.createElement('a'));

  anchor.setAttribute('href', url);
  anchor.textContent = label;

  return small;
}

const Article = (contents) => {
  const article = document.createElement('article');
  const page = currentPage();
  const pageIndex = Navigation.findIndex(({ matcher }) => {
    const matches = page.match(matcher)
    console.log(page, matcher, matches)
    return matches
  })
  
  if (pageIndex === 0) {
    article.id = 'intro';
  } else {
    article.appendChild(PageLink(Navigation[pageIndex - 1]));
    if (pageIndex < Navigation.length - 1) {
      article.appendChild(PageLink(Navigation[pageIndex + 1]))
    }
  }
    
  
  contents.forEach(node => article.appendChild(node));

  if (pageIndex === 0) {
    article.appendChild(PageLink(Navigation[1]));
  } else {
    article.appendChild(PageLink(Navigation[pageIndex - 1]));
    if (pageIndex < Navigation.length - 1) {
      article.appendChild(PageLink(Navigation[pageIndex + 1]));
    }
  }

  return article;
}

const Section = (contents, tldr) => {
  const section = document.createElement('section');
  const index = Index();


  section.appendChild(index);
  section.appendChild(Article(contents));
  section.appendChild(tldr);

  return section;
}