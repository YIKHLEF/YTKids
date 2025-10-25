// Gestion de l'API YouTube Data v3 pour YouTube Kids PWA

class YouTubeAPI {
    constructor() {
        // IMPORTANT: Remplacer par votre clé API YouTube
        this.API_KEY = 'YOUR_YOUTUBE_API_KEY_HERE';
        this.BASE_URL = 'https://www.googleapis.com/youtube/v3';
    }

    // Configurer la clé API
    setApiKey(apiKey) {
        this.API_KEY = apiKey;
    }

    // Extraire l'ID de vidéo depuis une URL YouTube
    extractVideoId(url) {
        const patterns = [
            /(?:youtube\.com\/watch\?v=|youtu\.be\/)([a-zA-Z0-9_-]{11})/,
            /youtube\.com\/embed\/([a-zA-Z0-9_-]{11})/,
            /youtube\.com\/v\/([a-zA-Z0-9_-]{11})/
        ];

        for (const pattern of patterns) {
            const match = url.match(pattern);
            if (match) return match[1];
        }
        return null;
    }

    // Extraire l'ID de playlist depuis une URL YouTube
    extractPlaylistId(url) {
        const pattern = /[?&]list=([a-zA-Z0-9_-]+)/;
        const match = url.match(pattern);
        return match ? match[1] : null;
    }

    // Vérifier si c'est une URL de playlist
    isPlaylistUrl(url) {
        return url.includes('list=');
    }

    // Récupérer les détails d'une vidéo
    async getVideoDetails(videoId) {
        try {
            const url = `${this.BASE_URL}/videos?part=snippet,contentDetails&id=${videoId}&key=${this.API_KEY}`;
            const response = await fetch(url);

            if (!response.ok) {
                throw new Error(`Erreur API: ${response.status}`);
            }

            const data = await response.json();

            if (!data.items || data.items.length === 0) {
                throw new Error('Vidéo introuvable');
            }

            const item = data.items[0];
            const snippet = item.snippet;
            const contentDetails = item.contentDetails;

            // Obtenir la meilleure qualité de vignette
            const thumbnails = snippet.thumbnails;
            const thumbnailUrl = thumbnails.maxres?.url ||
                                thumbnails.standard?.url ||
                                thumbnails.high?.url ||
                                thumbnails.medium?.url ||
                                thumbnails.default?.url;

            return {
                videoId: videoId,
                title: snippet.title,
                thumbnailUrl: thumbnailUrl,
                duration: this.parseDuration(contentDetails.duration),
                sourceType: 'single'
            };
        } catch (error) {
            console.error('Erreur lors de la récupération de la vidéo:', error);
            throw error;
        }
    }

    // Récupérer les vidéos d'une playlist
    async getPlaylistVideos(playlistId) {
        try {
            const videos = [];
            let nextPageToken = null;

            do {
                const url = `${this.BASE_URL}/playlistItems?part=snippet,contentDetails&playlistId=${playlistId}&maxResults=50${nextPageToken ? '&pageToken=' + nextPageToken : ''}&key=${this.API_KEY}`;
                const response = await fetch(url);

                if (!response.ok) {
                    throw new Error(`Erreur API: ${response.status}`);
                }

                const data = await response.json();

                if (!data.items || data.items.length === 0) {
                    break;
                }

                // Récupérer les détails de chaque vidéo
                for (const item of data.items) {
                    const videoId = item.contentDetails.videoId;
                    try {
                        const videoDetails = await this.getVideoDetails(videoId);
                        videoDetails.sourceType = 'playlist';
                        videoDetails.playlistId = playlistId;
                        videos.push(videoDetails);
                    } catch (error) {
                        console.warn(`Vidéo ${videoId} ignorée:`, error);
                    }
                }

                nextPageToken = data.nextPageToken;
            } while (nextPageToken);

            return videos;
        } catch (error) {
            console.error('Erreur lors de la récupération de la playlist:', error);
            throw error;
        }
    }

    // Parser la durée ISO 8601 (PT4M13S) en secondes
    parseDuration(duration) {
        const match = duration.match(/PT(?:(\d+)H)?(?:(\d+)M)?(?:(\d+)S)?/);
        if (!match) return 0;

        const hours = parseInt(match[1] || 0);
        const minutes = parseInt(match[2] || 0);
        const seconds = parseInt(match[3] || 0);

        return hours * 3600 + minutes * 60 + seconds;
    }

    // Formater la durée en texte (4:13)
    formatDuration(seconds) {
        const hours = Math.floor(seconds / 3600);
        const minutes = Math.floor((seconds % 3600) / 60);
        const secs = seconds % 60;

        if (hours > 0) {
            return `${hours}:${minutes.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
        }
        return `${minutes}:${secs.toString().padStart(2, '0')}`;
    }
}

// Instance globale
const youtubeAPI = new YouTubeAPI();
