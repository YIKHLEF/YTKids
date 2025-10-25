// Application principale - Interface Enfant

let deferredPrompt;
let longPressTimer;
let isLongPress = false;

// Initialisation de l'application
async function init() {
    try {
        // Initialiser la base de données
        await db.init();

        // Charger et afficher les vidéos
        await loadVideos();

        // Configurer l'accès admin
        setupAdminAccess();

        // Configurer le bouton d'installation PWA
        setupInstallButton();

    } catch (error) {
        console.error('Erreur d\'initialisation:', error);
        showError('Erreur lors du chargement de l\'application');
    }
}

// Charger les vidéos depuis la base de données
async function loadVideos() {
    try {
        const videos = await db.getAllVideos();
        displayVideos(videos);
    } catch (error) {
        console.error('Erreur lors du chargement des vidéos:', error);
        showError('Impossible de charger les vidéos');
    }
}

// Afficher les vidéos
function displayVideos(videos) {
    const videosGrid = document.getElementById('videos-grid');
    const emptyState = document.getElementById('empty-state');

    if (videos.length === 0) {
        videosGrid.style.display = 'none';
        emptyState.style.display = 'block';
        return;
    }

    emptyState.style.display = 'none';
    videosGrid.style.display = 'grid';
    videosGrid.innerHTML = '';

    videos.forEach(video => {
        const card = createVideoCard(video);
        videosGrid.appendChild(card);
    });
}

// Créer une carte de vidéo
function createVideoCard(video) {
    const card = document.createElement('div');
    card.className = 'video-card';
    card.onclick = () => playVideo(video);

    const formattedDuration = youtubeAPI.formatDuration(video.duration);

    card.innerHTML = `
        <img src="${video.thumbnailUrl}" alt="${video.title}" class="video-thumbnail">
        <div class="video-info">
            <div class="video-title">${escapeHtml(video.title)}</div>
            <div class="video-duration">${formattedDuration}</div>
        </div>
    `;

    return card;
}

// Lire une vidéo
function playVideo(video) {
    // Stocker la vidéo dans sessionStorage pour la page du lecteur
    sessionStorage.setItem('currentVideo', JSON.stringify(video));
    // Rediriger vers la page du lecteur
    window.location.href = 'player.html';
}

// Configurer l'accès admin (appui long 3 secondes)
function setupAdminAccess() {
    const adminArea = document.getElementById('admin-access-area');

    adminArea.addEventListener('touchstart', handleTouchStart);
    adminArea.addEventListener('touchend', handleTouchEnd);
    adminArea.addEventListener('touchcancel', handleTouchEnd);

    // Support souris pour desktop
    adminArea.addEventListener('mousedown', handleTouchStart);
    adminArea.addEventListener('mouseup', handleTouchEnd);
    adminArea.addEventListener('mouseleave', handleTouchEnd);
}

function handleTouchStart(e) {
    isLongPress = false;
    longPressTimer = setTimeout(() => {
        isLongPress = true;
        openAdminPanel();
    }, 3000); // 3 secondes
}

function handleTouchEnd(e) {
    clearTimeout(longPressTimer);
}

// Ouvrir le panneau d'administration
function openAdminPanel() {
    if (isLongPress) {
        window.location.href = 'admin.html';
    }
}

// Configurer le bouton d'installation PWA
function setupInstallButton() {
    const installButton = document.getElementById('install-button');

    // Écouter l'événement beforeinstallprompt
    window.addEventListener('beforeinstallprompt', (e) => {
        // Empêcher le mini-infobar de Chrome
        e.preventDefault();
        // Stocker l'événement
        deferredPrompt = e;
        // Afficher le bouton
        installButton.style.display = 'block';
    });

    // Gérer le clic sur le bouton
    installButton.addEventListener('click', async () => {
        if (!deferredPrompt) {
            return;
        }

        // Afficher le prompt d'installation
        deferredPrompt.prompt();

        // Attendre la réponse de l'utilisateur
        const { outcome } = await deferredPrompt.userChoice;

        if (outcome === 'accepted') {
            console.log('PWA installée');
        } else {
            console.log('Installation refusée');
        }

        // Réinitialiser
        deferredPrompt = null;
        installButton.style.display = 'none';
    });

    // Cacher le bouton si déjà installé
    window.addEventListener('appinstalled', () => {
        installButton.style.display = 'none';
        deferredPrompt = null;
    });
}

// Échapper le HTML pour éviter XSS
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Afficher un message d'erreur
function showError(message) {
    alert(message);
}

// Lancer l'application au chargement de la page
document.addEventListener('DOMContentLoaded', init);

// Recharger les vidéos quand on revient sur la page
window.addEventListener('focus', () => {
    loadVideos();
});
