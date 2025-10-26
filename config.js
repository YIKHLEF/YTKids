// Configuration centralisée pour YouTube Kids PWA
// Modifier ce fichier pour personnaliser l'application

const CONFIG = {
    // Clé API YouTube (REQUIS)
    // Obtenir une clé sur: https://console.cloud.google.com/
    YOUTUBE_API_KEY: 'YOUR_YOUTUBE_API_KEY_HERE',

    // Durée de l'appui long pour accéder à l'admin (en millisecondes)
    ADMIN_LONG_PRESS_DURATION: 3000, // 3 secondes

    // Nombre maximum de tentatives PIN avant blocage
    MAX_PIN_ATTEMPTS: 3,

    // Durée du blocage après tentatives échouées (en millisecondes)
    PIN_LOCKOUT_DURATION: 30000, // 30 secondes

    // Nom de l'application
    APP_NAME: 'YouTube Kids',

    // Couleurs de l'application (overrides CSS)
    COLORS: {
        primary: '#FF5722',
        accent: '#4CAF50',
        adminPrimary: '#1976D2'
    },

    // Options du lecteur YouTube
    PLAYER_OPTIONS: {
        autoplay: true,
        controls: true,
        rel: 0, // Ne pas afficher les vidéos similaires
        modestbranding: 1,
        fs: 1 // Bouton plein écran
    },

    // Messages personnalisables
    MESSAGES: {
        emptyState: 'Aucune vidéo disponible.\nDemandez à un adulte d\'en ajouter!',
        videoAdded: 'Vidéo ajoutée avec succès !',
        playlistAdded: 'vidéos ajoutées avec succès !',
        deleteConfirm: 'Supprimer cette vidéo ?',
        invalidUrl: 'Lien YouTube invalide',
        noInternet: 'Pas de connexion Internet'
    }
};

// Appliquer la clé API si elle est configurée
if (CONFIG.YOUTUBE_API_KEY && CONFIG.YOUTUBE_API_KEY !== 'YOUR_YOUTUBE_API_KEY_HERE') {
    if (typeof youtubeAPI !== 'undefined') {
        youtubeAPI.setApiKey(CONFIG.YOUTUBE_API_KEY);
    }
}
