// Interface d'administration

let isPinVerified = false;
let pinMode = 'verify'; // 'verify' ou 'create'

// Initialisation
async function init() {
    try {
        await db.init();

        // Vérifier si c'est le premier lancement
        const isFirst = await security.isFirstLaunch();
        const isPinConfigured = await security.isPinConfigured();

        if (isFirst || !isPinConfigured) {
            // Premier lancement - créer un PIN
            pinMode = 'create';
            showPinModal();
        } else {
            // Vérifier le PIN
            pinMode = 'verify';
            showPinModal();
        }

        // Configurer les événements
        setupEventListeners();

    } catch (error) {
        console.error('Erreur d\'initialisation admin:', error);
        alert('Erreur lors du chargement de l\'administration');
    }
}

// Configurer les écouteurs d'événements
function setupEventListeners() {
    // Bouton retour
    document.getElementById('back-button').addEventListener('click', () => {
        window.location.href = 'index.html';
    });

    // FAB Ajouter
    document.getElementById('fab-add').addEventListener('click', () => {
        if (isPinVerified) {
            showAddVideoModal();
        }
    });

    // Modal PIN
    const pinConfirmBtn = document.getElementById('pin-confirm');
    const pinInput = document.getElementById('pin-input');

    pinInput.addEventListener('keypress', (e) => {
        if (e.key === 'Enter') {
            pinConfirmBtn.click();
        }
    });

    pinConfirmBtn.addEventListener('click', handlePinConfirm);

    // Modal Ajout vidéo
    document.querySelector('.modal-close').addEventListener('click', hideAddVideoModal);
    document.getElementById('cancel-add').addEventListener('click', hideAddVideoModal);
    document.getElementById('add-video-btn').addEventListener('click', handleAddVideo);

    // Input URL
    const urlInput = document.getElementById('video-url');
    urlInput.addEventListener('input', () => {
        document.getElementById('error-message').style.display = 'none';
    });
}

// Afficher la modal PIN
function showPinModal() {
    const modal = document.getElementById('pin-modal');
    const title = document.getElementById('pin-title');

    if (pinMode === 'create') {
        title.textContent = 'Créez un code PIN (4 chiffres)';
    } else {
        title.textContent = 'Entrez le code PIN';
    }

    modal.style.display = 'flex';
    document.getElementById('pin-input').focus();
}

// Gérer la confirmation du PIN
async function handlePinConfirm() {
    const pinInput = document.getElementById('pin-input');
    const pin = pinInput.value;
    const errorDiv = document.getElementById('pin-error');

    if (pin.length !== 4 || !/^\d{4}$/.test(pin)) {
        errorDiv.textContent = 'Le PIN doit contenir exactement 4 chiffres';
        errorDiv.style.display = 'block';
        return;
    }

    try {
        if (pinMode === 'create') {
            // Demander confirmation
            if (!pinInput.dataset.confirmed) {
                pinInput.dataset.firstPin = pin;
                pinInput.dataset.confirmed = 'true';
                pinInput.value = '';
                document.getElementById('pin-title').textContent = 'Confirmez le code PIN';
                return;
            }

            // Vérifier la confirmation
            const firstPin = pinInput.dataset.firstPin;
            if (pin !== firstPin) {
                errorDiv.textContent = 'Les codes PIN ne correspondent pas';
                errorDiv.style.display = 'block';
                delete pinInput.dataset.firstPin;
                delete pinInput.dataset.confirmed;
                pinInput.value = '';
                document.getElementById('pin-title').textContent = 'Créez un code PIN (4 chiffres)';
                return;
            }

            // Créer le PIN
            await security.createPin(pin);
            isPinVerified = true;
            document.getElementById('pin-modal').style.display = 'none';
            await loadVideos();

        } else {
            // Vérifier le PIN
            const isValid = await security.verifyPin(pin);

            if (isValid) {
                isPinVerified = true;
                document.getElementById('pin-modal').style.display = 'none';
                await loadVideos();
            } else {
                errorDiv.textContent = 'Code PIN incorrect';
                errorDiv.style.display = 'block';
                pinInput.value = '';
            }
        }
    } catch (error) {
        console.error('Erreur PIN:', error);
        errorDiv.textContent = 'Une erreur s\'est produite';
        errorDiv.style.display = 'block';
    }
}

// Charger les vidéos
async function loadVideos() {
    try {
        const videos = await db.getAllVideos();
        displayVideos(videos);
    } catch (error) {
        console.error('Erreur chargement vidéos:', error);
    }
}

// Afficher les vidéos
function displayVideos(videos) {
    const list = document.getElementById('admin-videos-list');
    const emptyState = document.getElementById('admin-empty-state');

    if (videos.length === 0) {
        list.style.display = 'none';
        emptyState.style.display = 'block';
        return;
    }

    emptyState.style.display = 'none';
    list.style.display = 'flex';
    list.innerHTML = '';

    videos.forEach(video => {
        const item = createVideoItem(video);
        list.appendChild(item);
    });
}

// Créer un élément vidéo admin
function createVideoItem(video) {
    const div = document.createElement('div');
    div.className = 'admin-video-item';

    const formattedDuration = youtubeAPI.formatDuration(video.duration);

    div.innerHTML = `
        <img src="${video.thumbnailUrl}" alt="${video.title}" class="admin-video-thumbnail">
        <div class="admin-video-info">
            <div class="admin-video-title">${escapeHtml(video.title)}</div>
            <div class="admin-video-duration">${formattedDuration}</div>
        </div>
        <button class="delete-button" data-id="${video.id}">🗑️</button>
    `;

    // Événement suppression
    const deleteBtn = div.querySelector('.delete-button');
    deleteBtn.addEventListener('click', () => handleDeleteVideo(video));

    return div;
}

// Supprimer une vidéo
async function handleDeleteVideo(video) {
    if (!confirm(`Supprimer "${video.title}" ?`)) {
        return;
    }

    try {
        await db.deleteVideo(video.id);
        await loadVideos();
    } catch (error) {
        console.error('Erreur suppression:', error);
        alert('Erreur lors de la suppression');
    }
}

// Afficher la modal d'ajout
function showAddVideoModal() {
    document.getElementById('add-video-modal').style.display = 'flex';
    document.getElementById('video-url').value = '';
    document.getElementById('preview-section').style.display = 'none';
    document.getElementById('error-message').style.display = 'none';
    document.getElementById('video-url').focus();
}

// Cacher la modal d'ajout
function hideAddVideoModal() {
    document.getElementById('add-video-modal').style.display = 'none';
}

// Ajouter une vidéo
async function handleAddVideo() {
    const urlInput = document.getElementById('video-url');
    const url = urlInput.value.trim();
    const errorDiv = document.getElementById('error-message');
    const loadingDiv = document.getElementById('modal-loading');
    const previewSection = document.getElementById('preview-section');

    if (!url) {
        errorDiv.textContent = 'Veuillez coller un lien YouTube';
        errorDiv.style.display = 'block';
        return;
    }

    try {
        // Afficher le chargement
        loadingDiv.style.display = 'block';
        errorDiv.style.display = 'none';
        previewSection.style.display = 'none';

        if (youtubeAPI.isPlaylistUrl(url)) {
            // C'est une playlist
            const playlistId = youtubeAPI.extractPlaylistId(url);
            if (!playlistId) {
                throw new Error('Lien de playlist invalide');
            }

            const videos = await youtubeAPI.getPlaylistVideos(playlistId);

            // Afficher aperçu
            showPlaylistPreview(videos);
            loadingDiv.style.display = 'none';

            // Demander confirmation
            if (confirm(`Cette playlist contient ${videos.length} vidéos. Tout ajouter ?`)) {
                loadingDiv.style.display = 'block';
                await db.addVideos(videos);
                loadingDiv.style.display = 'none';
                alert(`${videos.length} vidéos ajoutées avec succès !`);
                hideAddVideoModal();
                await loadVideos();
            } else {
                loadingDiv.style.display = 'none';
            }

        } else {
            // C'est une vidéo unique
            const videoId = youtubeAPI.extractVideoId(url);
            if (!videoId) {
                throw new Error('Lien YouTube invalide');
            }

            const video = await youtubeAPI.getVideoDetails(videoId);

            // Afficher aperçu
            showVideoPreview(video);
            loadingDiv.style.display = 'none';

            // Ajouter automatiquement
            setTimeout(async () => {
                loadingDiv.style.display = 'block';
                await db.addVideo(video);
                loadingDiv.style.display = 'none';
                alert('Vidéo ajoutée avec succès !');
                hideAddVideoModal();
                await loadVideos();
            }, 1000);
        }

    } catch (error) {
        console.error('Erreur ajout:', error);
        loadingDiv.style.display = 'none';
        errorDiv.textContent = error.message || 'Erreur lors de l\'ajout';
        errorDiv.style.display = 'block';
    }
}

// Afficher l'aperçu d'une vidéo
function showVideoPreview(video) {
    const previewSection = document.getElementById('preview-section');
    const previewContent = document.getElementById('preview-content');

    const formattedDuration = youtubeAPI.formatDuration(video.duration);

    previewContent.innerHTML = `
        <img src="${video.thumbnailUrl}" alt="${video.title}" class="preview-thumbnail">
        <div class="preview-info">
            <div class="preview-title">${escapeHtml(video.title)}</div>
            <div class="preview-duration">${formattedDuration}</div>
        </div>
    `;

    previewSection.style.display = 'block';
}

// Afficher l'aperçu d'une playlist
function showPlaylistPreview(videos) {
    if (videos.length === 0) return;

    const previewSection = document.getElementById('preview-section');
    const previewContent = document.getElementById('preview-content');
    const firstVideo = videos[0];

    previewContent.innerHTML = `
        <img src="${firstVideo.thumbnailUrl}" alt="Playlist" class="preview-thumbnail">
        <div class="preview-info">
            <div class="preview-title">Playlist: ${videos.length} vidéos</div>
            <div class="preview-duration">Première: ${escapeHtml(firstVideo.title)}</div>
        </div>
    `;

    previewSection.style.display = 'block';
}

// Échapper HTML
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Initialiser au chargement
document.addEventListener('DOMContentLoaded', init);
