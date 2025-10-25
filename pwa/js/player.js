// Lecteur vidéo YouTube

let player;
let currentVideo;

// Charger l'API YouTube IFrame
function onYouTubeIframeAPIReady() {
    initPlayer();
}

// Initialiser le lecteur
function initPlayer() {
    // Récupérer la vidéo depuis sessionStorage
    const videoData = sessionStorage.getItem('currentVideo');

    if (!videoData) {
        showError();
        return;
    }

    try {
        currentVideo = JSON.parse(videoData);

        // Afficher le titre
        document.getElementById('player-title').textContent = currentVideo.title;

        // Créer le lecteur YouTube
        player = new YT.Player('youtube-player', {
            height: '100%',
            width: '100%',
            videoId: currentVideo.videoId,
            playerVars: {
                'autoplay': 1,
                'controls': 1,
                'rel': 0,  // Ne pas afficher les vidéos similaires
                'modestbranding': 1,  // Logo YouTube minimal
                'playsinline': 1,
                'fs': 1,  // Bouton plein écran
                'iv_load_policy': 3  // Désactiver les annotations
            },
            events: {
                'onReady': onPlayerReady,
                'onError': onPlayerError,
                'onStateChange': onPlayerStateChange
            }
        });

    } catch (error) {
        console.error('Erreur initialisation lecteur:', error);
        showError();
    }
}

// Quand le lecteur est prêt
function onPlayerReady(event) {
    // Lancer la lecture automatiquement
    event.target.playVideo();

    // Forcer le mode paysage sur mobile
    if (screen.orientation && screen.orientation.lock) {
        try {
            screen.orientation.lock('landscape').catch(() => {
                console.log('Impossible de verrouiller l\'orientation');
            });
        } catch (e) {
            console.log('Lock orientation non supporté');
        }
    }
}

// En cas d'erreur du lecteur
function onPlayerError(event) {
    console.error('Erreur lecteur YouTube:', event.data);
    showError();
}

// Changement d'état du lecteur
function onPlayerStateChange(event) {
    // Optionnel: actions basées sur l'état (lecture, pause, fin)
}

// Afficher une erreur
function showError() {
    document.getElementById('youtube-player').style.display = 'none';
    document.getElementById('player-error').style.display = 'block';
}

// Bouton retour
document.getElementById('player-back').addEventListener('click', () => {
    // Arrêter la vidéo
    if (player && player.stopVideo) {
        player.stopVideo();
    }

    // Déverrouiller l'orientation
    if (screen.orientation && screen.orientation.unlock) {
        try {
            screen.orientation.unlock();
        } catch (e) {
            console.log('Unlock orientation non supporté');
        }
    }

    // Retour à l'accueil
    window.location.href = 'index.html';
});

// Charger l'API YouTube si elle n'est pas déjà chargée
if (!window.YT) {
    const tag = document.createElement('script');
    tag.src = 'https://www.youtube.com/iframe_api';
    const firstScriptTag = document.getElementsByTagName('script')[0];
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
}

// Éviter le défilement de la page
document.body.style.overflow = 'hidden';

// Mettre l'écran en mode plein écran automatiquement (si supporté)
document.addEventListener('DOMContentLoaded', () => {
    const elem = document.documentElement;

    if (elem.requestFullscreen) {
        elem.requestFullscreen().catch(() => {
            console.log('Plein écran refusé');
        });
    } else if (elem.webkitRequestFullscreen) {
        elem.webkitRequestFullscreen().catch(() => {
            console.log('Plein écran refusé');
        });
    } else if (elem.mozRequestFullScreen) {
        elem.mozRequestFullScreen().catch(() => {
            console.log('Plein écran refusé');
        });
    }
});
