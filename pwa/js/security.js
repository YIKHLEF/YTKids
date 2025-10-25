// Gestion de la sécurité (PIN) pour YouTube Kids PWA

class Security {
    constructor() {
        this.SETTING_PIN_HASH = 'pin_hash';
        this.SETTING_PIN_SALT = 'pin_salt';
        this.SETTING_FIRST_LAUNCH = 'first_launch';
    }

    // Générer un salt aléatoire
    generateSalt() {
        const array = new Uint8Array(16);
        crypto.getRandomValues(array);
        return Array.from(array, byte => byte.toString(16).padStart(2, '0')).join('');
    }

    // Hasher un PIN avec SHA-256
    async hashPin(pin, salt) {
        const encoder = new TextEncoder();
        const data = encoder.encode(pin + salt);
        const hashBuffer = await crypto.subtle.digest('SHA-256', data);
        const hashArray = Array.from(new Uint8Array(hashBuffer));
        return hashArray.map(byte => byte.toString(16).padStart(2, '0')).join('');
    }

    // Créer un PIN
    async createPin(pin) {
        if (pin.length !== 4 || !/^\d{4}$/.test(pin)) {
            throw new Error('Le PIN doit contenir exactement 4 chiffres');
        }

        const salt = this.generateSalt();
        const hash = await this.hashPin(pin, salt);

        await db.setSetting(this.SETTING_PIN_SALT, salt);
        await db.setSetting(this.SETTING_PIN_HASH, hash);
        await db.setSetting(this.SETTING_FIRST_LAUNCH, 'false');
    }

    // Vérifier un PIN
    async verifyPin(pin) {
        const storedHash = await db.getSetting(this.SETTING_PIN_HASH);
        const salt = await db.getSetting(this.SETTING_PIN_SALT);

        if (!storedHash || !salt) {
            return false;
        }

        const hash = await this.hashPin(pin, salt);
        return hash === storedHash;
    }

    // Vérifier si c'est le premier lancement
    async isFirstLaunch() {
        const firstLaunch = await db.getSetting(this.SETTING_FIRST_LAUNCH);
        return firstLaunch === null || firstLaunch === 'true';
    }

    // Vérifier si le PIN est configuré
    async isPinConfigured() {
        const hash = await db.getSetting(this.SETTING_PIN_HASH);
        return hash !== null && hash !== undefined;
    }
}

// Instance globale
const security = new Security();
