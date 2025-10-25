// Gestion de la base de données IndexedDB pour YouTube Kids PWA

class Database {
    constructor() {
        this.dbName = 'YouTubeKidsDB';
        this.dbVersion = 1;
        this.db = null;
    }

    // Initialiser la base de données
    async init() {
        return new Promise((resolve, reject) => {
            const request = indexedDB.open(this.dbName, this.dbVersion);

            request.onerror = () => reject(request.error);
            request.onsuccess = () => {
                this.db = request.result;
                resolve(this.db);
            };

            request.onupgradeneeded = (event) => {
                const db = event.target.result;

                // Store pour les vidéos
                if (!db.objectStoreNames.contains('videos')) {
                    const videoStore = db.createObjectStore('videos', {
                        keyPath: 'id',
                        autoIncrement: true
                    });
                    videoStore.createIndex('videoId', 'videoId', { unique: true });
                    videoStore.createIndex('position', 'position', { unique: false });
                }

                // Store pour les paramètres
                if (!db.objectStoreNames.contains('settings')) {
                    db.createObjectStore('settings', { keyPath: 'key' });
                }
            };
        });
    }

    // ========== VIDÉOS ==========

    // Obtenir toutes les vidéos (triées par position)
    async getAllVideos() {
        const transaction = this.db.transaction(['videos'], 'readonly');
        const store = transaction.objectStore('videos');
        const index = store.index('position');

        return new Promise((resolve, reject) => {
            const request = index.getAll();
            request.onsuccess = () => resolve(request.result);
            request.onerror = () => reject(request.error);
        });
    }

    // Obtenir une vidéo par ID
    async getVideo(id) {
        const transaction = this.db.transaction(['videos'], 'readonly');
        const store = transaction.objectStore('videos');

        return new Promise((resolve, reject) => {
            const request = store.get(id);
            request.onsuccess = () => resolve(request.result);
            request.onerror = () => reject(request.error);
        });
    }

    // Obtenir une vidéo par videoId YouTube
    async getVideoByYouTubeId(videoId) {
        const transaction = this.db.transaction(['videos'], 'readonly');
        const store = transaction.objectStore('videos');
        const index = store.index('videoId');

        return new Promise((resolve, reject) => {
            const request = index.get(videoId);
            request.onsuccess = () => resolve(request.result);
            request.onerror = () => reject(request.error);
        });
    }

    // Ajouter une vidéo
    async addVideo(video) {
        // Vérifier si la vidéo existe déjà
        const existing = await this.getVideoByYouTubeId(video.videoId);
        if (existing) {
            throw new Error('Cette vidéo est déjà ajoutée');
        }

        // Obtenir la prochaine position
        const videos = await this.getAllVideos();
        video.position = videos.length;
        video.dateAdded = Date.now();

        const transaction = this.db.transaction(['videos'], 'readwrite');
        const store = transaction.objectStore('videos');

        return new Promise((resolve, reject) => {
            const request = store.add(video);
            request.onsuccess = () => resolve(request.result);
            request.onerror = () => reject(request.error);
        });
    }

    // Ajouter plusieurs vidéos
    async addVideos(videos) {
        const currentVideos = await this.getAllVideos();
        let position = currentVideos.length;
        const timestamp = Date.now();

        const transaction = this.db.transaction(['videos'], 'readwrite');
        const store = transaction.objectStore('videos');

        return new Promise((resolve, reject) => {
            const addedIds = [];
            let completed = 0;

            videos.forEach(video => {
                video.position = position++;
                video.dateAdded = timestamp;

                const request = store.add(video);
                request.onsuccess = () => {
                    addedIds.push(request.result);
                    completed++;
                    if (completed === videos.length) {
                        resolve(addedIds);
                    }
                };
                request.onerror = () => {
                    completed++;
                    if (completed === videos.length) {
                        resolve(addedIds);
                    }
                };
            });
        });
    }

    // Supprimer une vidéo
    async deleteVideo(id) {
        const transaction = this.db.transaction(['videos'], 'readwrite');
        const store = transaction.objectStore('videos');

        return new Promise((resolve, reject) => {
            const request = store.delete(id);
            request.onsuccess = () => resolve();
            request.onerror = () => reject(request.error);
        });
    }

    // Supprimer toutes les vidéos
    async deleteAllVideos() {
        const transaction = this.db.transaction(['videos'], 'readwrite');
        const store = transaction.objectStore('videos');

        return new Promise((resolve, reject) => {
            const request = store.clear();
            request.onsuccess = () => resolve();
            request.onerror = () => reject(request.error);
        });
    }

    // ========== PARAMÈTRES ==========

    // Obtenir un paramètre
    async getSetting(key) {
        const transaction = this.db.transaction(['settings'], 'readonly');
        const store = transaction.objectStore('settings');

        return new Promise((resolve, reject) => {
            const request = store.get(key);
            request.onsuccess = () => resolve(request.result?.value);
            request.onerror = () => reject(request.error);
        });
    }

    // Enregistrer un paramètre
    async setSetting(key, value) {
        const transaction = this.db.transaction(['settings'], 'readwrite');
        const store = transaction.objectStore('settings');

        return new Promise((resolve, reject) => {
            const request = store.put({ key, value });
            request.onsuccess = () => resolve();
            request.onerror = () => reject(request.error);
        });
    }

    // Supprimer un paramètre
    async deleteSetting(key) {
        const transaction = this.db.transaction(['settings'], 'readwrite');
        const store = transaction.objectStore('settings');

        return new Promise((resolve, reject) => {
            const request = store.delete(key);
            request.onsuccess = () => resolve();
            request.onerror = () => reject(request.error);
        });
    }

    // Supprimer tous les paramètres
    async deleteAllSettings() {
        const transaction = this.db.transaction(['settings'], 'readwrite');
        const store = transaction.objectStore('settings');

        return new Promise((resolve, reject) => {
            const request = store.clear();
            request.onsuccess = () => resolve();
            request.onerror = () => reject(request.error);
        });
    }
}

// Instance globale
const db = new Database();
