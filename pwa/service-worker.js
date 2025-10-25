const CACHE_NAME = 'youtube-kids-v1';
const urlsToCache = [
  '/pwa/',
  '/pwa/index.html',
  '/pwa/admin.html',
  '/pwa/player.html',
  '/pwa/css/style.css',
  '/pwa/js/app.js',
  '/pwa/js/db.js',
  '/pwa/js/security.js',
  '/pwa/js/youtube-api.js'
];

// Installation du Service Worker
self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(cache => {
        console.log('Cache ouvert');
        return cache.addAll(urlsToCache);
      })
  );
  self.skipWaiting();
});

// Activation du Service Worker
self.addEventListener('activate', event => {
  event.waitUntil(
    caches.keys().then(cacheNames => {
      return Promise.all(
        cacheNames.map(cacheName => {
          if (cacheName !== CACHE_NAME) {
            console.log('Suppression ancien cache:', cacheName);
            return caches.delete(cacheName);
          }
        })
      );
    })
  );
  self.clients.claim();
});

// Interception des requêtes
self.addEventListener('fetch', event => {
  // Ne pas cacher les requêtes vers YouTube API
  if (event.request.url.includes('googleapis.com') ||
      event.request.url.includes('youtube.com')) {
    return fetch(event.request);
  }

  event.respondWith(
    caches.match(event.request)
      .then(response => {
        // Retourner la ressource en cache si disponible
        if (response) {
          return response;
        }

        // Sinon, faire la requête réseau
        return fetch(event.request).then(response => {
          // Vérifier si la réponse est valide
          if (!response || response.status !== 200 || response.type !== 'basic') {
            return response;
          }

          // Cloner la réponse
          const responseToCache = response.clone();

          caches.open(CACHE_NAME)
            .then(cache => {
              cache.put(event.request, responseToCache);
            });

          return response;
        });
      })
      .catch(() => {
        // Page offline de fallback
        return caches.match('/pwa/index.html');
      })
  );
});
