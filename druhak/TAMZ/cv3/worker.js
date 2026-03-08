//v0.0.1a
const cache_N = "tamz-pwa-v1";
const cache_URLs = [
    "./", //Directory index 
    "index.html", // Add any other HTML files you have
    "install.js",
    "tamz1-256x256.png",
    "https://cdn.jsdelivr.net/npm/@ionic/core/dist/ionic/ionic.esm.js",
    "https://cdn.jsdelivr.net/npm/@ionic/core/dist/ionic/ionic.js",
    "https://cdn.jsdelivr.net/npm/@ionic/core/css/ionic.bundle.css"
    // Other assets and resources to cache are placed here
];

self.addEventListener("install", (e) => {
    e.waitUntil(
        caches.open(cache_N).then((cache) => {
            return cache.addAll(cache_URLs);
        })
    );
});

self.addEventListener("fetch", (e) => {
  e.respondWith(
    (async () => {
      const r = await caches.match(e.request);
      //console.log(`[Service Worker] Fetching resource: ${e.request.url}`);
      if (r) { return r; } // Cached entry returned first
      const resp = await fetch(e.request); // Fetch second
      const cache = await caches.open(cache_N);
      console.log(`Caching new resource in service worker: ${e.request.url}`);
      //For on-line data within e.g. RESTFul API, we should compare URLs 
      // and skip puting them in cache
      cache.put(e.request, resp.clone());
      return resp;
    })(),
  );
});

self.addEventListener("activate", (e) => {
    e.waitUntil(
        caches.keys().then((cacheNames) => {
            return Promise.all(
                cacheNames
                    .filter((name) => name !== cache_N)
                    .map((name) => caches.delete(name))
            );
        })
    );
});

/* 
//Potential solution for needed refresh:
self.addEventListener('visibilitychange', function() {
    if (document.visibilityState === 'visible') {
        console.log('Application resumed, reloading');
        window.location.reload();
    }
});
*/
