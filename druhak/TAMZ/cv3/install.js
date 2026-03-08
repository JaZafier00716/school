if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('worker.js')
        .then(function(reg) {
            console.log('Service Worker registered:', reg.scope);
        })
        .catch(error => {
            console.error('Service Worker registration failure:', error);
        });
}
