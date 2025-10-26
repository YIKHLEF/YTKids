# 📱 YouTube Kids PWA - Progressive Web App

Une application web progressive (PWA) permettant aux parents de créer un environnement YouTube sécurisé pour leurs enfants.

## ✨ Caractéristiques

- ✅ **PWA Complète** : Installable sur tous les appareils (Android, iOS, Desktop)
- ✅ **Interface Enfant** : Colorée, intuitive et sécurisée
- ✅ **Protection PIN** : Code à 4 chiffres avec hashage SHA-256
- ✅ **Hors Ligne** : Fonctionne même sans connexion (après installation)
- ✅ **Ajout de Vidéos** : Vidéos individuelles et playlists YouTube
- ✅ **Stockage Local** : Toutes les données restent sur l'appareil (IndexedDB)
- ✅ **Pas d'APK** : Aucune compilation nécessaire, juste un serveur web !

## 🚀 Installation Rapide

### Option 1 : Serveur Web Local

**Avec Python (le plus simple) :**

```bash
# Python 3
cd pwa
python -m http.server 8000

# Puis ouvrir : http://localhost:8000
```

**Avec Node.js :**

```bash
# Installer http-server globalement
npm install -g http-server

# Lancer le serveur
cd pwa
http-server -p 8000

# Puis ouvrir : http://localhost:8000
```

**Avec PHP :**

```bash
cd pwa
php -S localhost:8000

# Puis ouvrir : http://localhost:8000
```

### Option 2 : Déploiement en Ligne

**GitHub Pages (Gratuit) :**

1. Créer un repo GitHub
2. Pousser le dossier `pwa` dans le repo
3. Activer GitHub Pages dans Settings → Pages
4. Votre app sera disponible sur : `https://username.github.io/repo-name/`

**Netlify (Gratuit) :**

1. Créer un compte sur [netlify.com](https://netlify.com)
2. Glisser-déposer le dossier `pwa`
3. Votre app est en ligne instantanément !

**Vercel (Gratuit) :**

1. Installer Vercel CLI : `npm i -g vercel`
2. Dans le dossier `pwa` : `vercel`
3. Suivre les instructions

## 🔑 Configuration de la Clé API YouTube

**IMPORTANT :** Vous devez configurer votre clé API YouTube avant d'utiliser l'application.

### Étape 1 : Obtenir une Clé API

1. Aller sur [Google Cloud Console](https://console.cloud.google.com/)
2. Créer un projet (ou sélectionner un existant)
3. Activer l'API : **YouTube Data API v3**
   - Menu → APIs & Services → Library
   - Rechercher "YouTube Data API v3"
   - Cliquer sur **Enable**
4. Créer une clé API :
   - Menu → APIs & Services → Credentials
   - **+ Create Credentials** → **API Key**
   - Copier la clé générée

### Étape 2 : Configurer la Clé

Ouvrir le fichier `js/youtube-api.js` et remplacer :

```javascript
this.API_KEY = 'YOUR_YOUTUBE_API_KEY_HERE';
```

Par :

```javascript
this.API_KEY = 'VotreCléAPIIci';
```

**Important :** Ne committez jamais votre clé API dans un repo public !

## 📱 Installer l'App sur Mobile

### Sur Android (Chrome)

1. Ouvrir l'app dans Chrome
2. Menu ⋮ → **Ajouter à l'écran d'accueil**
3. Ou cliquer sur le bouton "Installer l'application" qui apparaît

### Sur iOS (Safari)

1. Ouvrir l'app dans Safari
2. Bouton Partager 􀈂
3. **Sur l'écran d'accueil**
4. L'app s'ajoute comme une app native

### Sur Desktop (Chrome, Edge)

1. Ouvrir l'app dans le navigateur
2. Icône d'installation dans la barre d'adresse (ou bouton "Installer")
3. Cliquer sur **Installer**

## 🎯 Utilisation

### Premier Lancement

1. **Créer un PIN** : Code à 4 chiffres pour protéger l'accès admin
2. **Interface Admin** : S'ouvre automatiquement
3. **Ajouter des vidéos** : Cliquer sur le bouton +
4. **Coller un lien YouTube** : Vidéo ou playlist
5. **Confirmation** : La vidéo s'ajoute automatiquement

### Interface Enfant

- **Voir les vidéos** : Grille colorée avec vignettes
- **Lire une vidéo** : Taper sur une vignette
- **Retour** : Bouton ← en haut à gauche

### Accès Admin

- **Appui long (3 secondes)** sur le coin supérieur droit
- **Entrer le PIN**
- **Interface admin** s'ouvre

### Gestion des Vidéos

- **Ajouter** : Bouton + flottant en bas à droite
- **Supprimer** : Icône 🗑️ sur chaque vidéo
- **Retour** : Flèche ← en haut à gauche

## 🗂️ Structure du Projet

```
pwa/
├── index.html              # Interface enfant (page principale)
├── admin.html              # Interface d'administration
├── player.html             # Lecteur vidéo
├── manifest.json           # Configuration PWA
├── service-worker.js       # Service Worker (offline + cache)
├── css/
│   └── style.css          # Styles de l'application
├── js/
│   ├── app.js             # Logique interface enfant
│   ├── admin.js           # Logique interface admin
│   ├── player.js          # Logique lecteur vidéo
│   ├── db.js              # Gestion IndexedDB
│   ├── security.js        # Gestion PIN (SHA-256)
│   └── youtube-api.js     # API YouTube Data v3
├── icons/                 # Icônes de l'app (à créer)
└── README.md             # Ce fichier
```

## 🎨 Créer les Icônes

Les icônes sont référencées dans `manifest.json` mais doivent être créées.

**Tailles nécessaires :**
- 72x72
- 96x96
- 128x128
- 144x144
- 152x152
- 192x192
- 384x384
- 512x512

**Outils recommandés :**
- [Favicon Generator](https://realfavicongenerator.net/)
- [PWA Asset Generator](https://github.com/elegantapp/pwa-asset-generator)

Ou utiliser une image simple et la redimensionner en ligne :
- [ResizeImage.net](https://resizeimage.net/)

## 🔧 Développement

### Tester en Local

```bash
cd pwa
python -m http.server 8000
# Ouvrir http://localhost:8000
```

### Déboguer

- **Chrome DevTools** : F12 → Console
- **Application Tab** : Voir le Service Worker, IndexedDB, Manifest
- **Lighthouse** : Auditer la PWA

### Désinstaller le Service Worker

Si vous avez des problèmes de cache :

1. F12 → Application → Service Workers
2. **Unregister**
3. Rafraîchir la page (Ctrl+Shift+R)

## 📊 Avantages vs App Android Native

| Critère | PWA | App Android Native |
|---------|-----|-------------------|
| Installation | ✅ Instantanée (1 clic) | ❌ Téléchargement APK |
| Taille | ✅ ~500 KB | ❌ ~15-20 MB |
| Mises à jour | ✅ Automatiques | ❌ Manuelles |
| Compatibilité | ✅ Android + iOS + Desktop | ❌ Android uniquement |
| Compilation | ✅ Aucune | ❌ Android Studio requis |
| Distribution | ✅ Simple lien web | ❌ Fichier APK à transférer |
| Développement | ✅ HTML/CSS/JS simple | ❌ Kotlin/Java complexe |

## ⚠️ Limitations

- **Connexion Internet** requise pour :
  - Ajouter des vidéos (récupération métadonnées)
  - Lire les vidéos (streaming YouTube)
- **Quota API YouTube** : 10,000 requêtes/jour (gratuit)
- **Stockage** : Limité par IndexedDB du navigateur (généralement >50 MB)
- **Notifications** : Non implémentées (possible en ajout futur)

## 🛠️ Personnalisation

### Changer les Couleurs

Modifier `css/style.css` :

```css
:root {
    --color-primary: #FF5722;  /* Couleur principale */
    --color-accent: #4CAF50;   /* Couleur accent */
    /* ... */
}
```

### Modifier le Nom de l'App

Dans `manifest.json` :

```json
{
  "name": "Votre Nom d'App",
  "short_name": "App Name",
  ...
}
```

### Changer le Délai d'Accès Admin

Dans `js/app.js` :

```javascript
longPressTimer = setTimeout(() => {
    // ...
}, 3000); // 3 secondes → modifier ce nombre
```

## 📱 Compatibilité

| Navigateur | Support | Installation PWA |
|------------|---------|------------------|
| Chrome (Android) | ✅ Complet | ✅ |
| Safari (iOS) | ✅ Complet | ✅ |
| Edge | ✅ Complet | ✅ |
| Firefox | ⚠️ Partiel | ❌ |
| Samsung Internet | ✅ Complet | ✅ |

## 🐛 Résolution de Problèmes

### "Failed to fetch" lors de l'ajout de vidéos

**Cause** : Clé API YouTube non configurée ou invalide

**Solution** :
1. Vérifier `js/youtube-api.js`
2. Remplacer `YOUR_YOUTUBE_API_KEY_HERE` par votre vraie clé

### L'app ne s'installe pas

**Solution** :
1. Vérifier que vous êtes en HTTPS (ou localhost)
2. Vérifier que `manifest.json` est accessible
3. Vérifier que le Service Worker est enregistré (F12 → Application)

### Les vidéos ne se chargent pas

**Solution** :
1. Vérifier la connexion Internet
2. Ouvrir la console (F12) pour voir les erreurs
3. Vérifier que la clé API YouTube est valide

### Le PIN ne fonctionne pas

**Solution** :
1. Effacer les données du site :
   - F12 → Application → Storage → Clear site data
2. Recharger la page
3. Créer un nouveau PIN

## 🎉 Avantages de la PWA

✅ **Aucune compilation** nécessaire
✅ **Fonctionne sur TOUS les appareils**
✅ **Déploiement instantané**
✅ **Mises à jour automatiques**
✅ **Léger et rapide**
✅ **Pas de dépendance Android Studio**
✅ **Pas de problèmes de versions Gradle/Java**
✅ **Simple à développer et maintenir**

## 📞 Support

Pour toute question :
- Ouvrir la console du navigateur (F12)
- Vérifier les messages d'erreur
- Consulter la documentation YouTube API

---

**Développé avec ❤️ pour les enfants**

**Technologies utilisées :**
- HTML5
- CSS3
- JavaScript ES6+
- IndexedDB
- Service Workers
- YouTube IFrame API
- YouTube Data API v3
