# YouTube Kids - Application Android Personnalisée

Une application Android native permettant aux parents de créer un environnement YouTube sécurisé pour leurs enfants, en affichant uniquement des vidéos présélectionnées.

## 📱 Caractéristiques Principales

- **Interface Enfant Colorée et Intuitive** : Grandes vignettes avec images de prévisualisation
- **Lecture Sécurisée** : Uniquement les vidéos présélectionnées par les parents
- **Gestion Locale** : Aucun compte utilisateur requis, stockage local
- **Protection par Code PIN** : Interface d'administration protégée
- **Support des Playlists** : Ajout de vidéos individuelles ou de playlists complètes

## 🛠️ Technologies Utilisées

- **Langage** : Kotlin
- **Architecture** : MVVM (Model-View-ViewModel)
- **Base de données** : Room (SQLite)
- **API** : YouTube Data API v3
- **Player** : Android YouTube Player
- **Chargement d'images** : Glide
- **Networking** : Retrofit + OkHttp
- **Asynchrone** : Kotlin Coroutines + Flow

## 📋 Prérequis

### Environnement de Développement

- Android Studio Giraffe (2022.3.1) ou plus récent
- JDK 17
- Android SDK
  - SDK minimum : API 24 (Android 7.0)
  - SDK cible : API 34 (Android 14)

### Clé API YouTube

1. Créer un projet sur [Google Cloud Console](https://console.cloud.google.com/)
2. Activer **YouTube Data API v3**
3. Créer des identifiants → Clé API
4. Copier la clé API

## 🚀 Installation et Configuration

### 1. Cloner le Projet

```bash
git clone <repository-url>
cd YTKids
```

### 2. Configurer la Clé API YouTube

Ouvrir le fichier `local.properties` à la racine du projet et ajouter :

```properties
YOUTUBE_API_KEY=VOTRE_CLE_API_ICI
```

**Important** : Ne jamais commiter ce fichier dans Git !

### 3. Synchroniser le Projet

- Ouvrir le projet dans Android Studio
- Laisser Gradle synchroniser automatiquement
- Attendre la fin du téléchargement des dépendances

### 4. Compiler et Exécuter

**Via Android Studio** :
- Connecter un appareil Android ou lancer un émulateur
- Cliquer sur Run (▶️)

**Via Ligne de Commande** :
```bash
./gradlew assembleDebug
./gradlew installDebug
```

## 📖 Guide d'Utilisation

### Premier Lancement

1. **Création du Code PIN**
   - Au premier lancement, l'app demandera de créer un code PIN à 4 chiffres
   - Entrer le code deux fois pour confirmation
   - **Important** : Mémoriser ce code, il protège l'accès admin

2. **Ajouter des Vidéos**
   - L'interface admin s'ouvre automatiquement
   - Cliquer sur le bouton **+** (en bas à droite)
   - Coller un lien YouTube (vidéo ou playlist)
   - La vidéo sera automatiquement ajoutée

### Interface Enfant

- **Affichage** : Grille de vidéos avec grandes vignettes colorées
- **Lire une Vidéo** : Taper sur une vignette
- **Navigation** : Bouton "Retour" pour revenir à la grille
- **Accès Admin** : Appui long (3 secondes) sur le coin supérieur droit

### Interface Admin (Protégée par PIN)

#### Accéder à l'Administration

1. Depuis l'écran principal, appuyer longtemps (3 secondes) sur le coin supérieur droit
2. Entrer le code PIN à 4 chiffres
3. L'interface admin s'ouvre

#### Ajouter une Vidéo

1. Cliquer sur le bouton **+** (FAB en bas à droite)
2. Coller le lien YouTube dans le champ :
   - **Vidéo unique** : `https://www.youtube.com/watch?v=VIDEO_ID`
   - **Vidéo courte** : `https://youtu.be/VIDEO_ID`
   - **Playlist** : `https://www.youtube.com/playlist?list=PLAYLIST_ID`
3. Cliquer sur **Ajouter**
4. La vidéo (ou toutes les vidéos de la playlist) sera ajoutée

#### Supprimer une Vidéo

1. Dans la liste admin, cliquer sur l'icône **Poubelle** (🗑️)
2. Confirmer la suppression

## 🎨 Structure du Projet

```
app/
├── src/main/
│   ├── java/com/youtubekids/app/
│   │   ├── data/
│   │   │   ├── local/              # Base de données Room
│   │   │   │   ├── dao/            # Data Access Objects
│   │   │   │   ├── entities/       # Entités de la BD
│   │   │   │   └── AppDatabase.kt
│   │   │   ├── remote/             # API YouTube
│   │   │   │   ├── models/         # Modèles de réponse API
│   │   │   │   └── YouTubeApiService.kt
│   │   │   └── repository/         # Repository pattern
│   │   ├── ui/
│   │   │   ├── main/               # Écran principal (enfant)
│   │   │   ├── player/             # Lecteur vidéo
│   │   │   └── admin/              # Interface admin
│   │   └── utils/                  # Utilitaires
│   └── res/
│       ├── layout/                 # Fichiers XML des layouts
│       ├── values/                 # Strings, colors, themes
│       └── xml/                    # Configurations
└── build.gradle.kts
```

## 🔒 Sécurité et Confidentialité

### Protection des Données

- **Stockage Local** : Toutes les données sont stockées localement sur l'appareil
- **Pas de Compte** : Aucune connexion à un compte externe
- **Code PIN Sécurisé** : Hashage SHA-256 avec salt aléatoire
- **Backup Exclu** : Les données sensibles ne sont pas sauvegardées dans le cloud

### Protection des Enfants

- **Pas de Navigation Externe** : Impossible d'accéder à YouTube.com
- **Pas de Suggestions** : Désactivation des suggestions de vidéos YouTube
- **Pas de Commentaires** : Les commentaires ne sont pas visibles
- **Pas de Partage** : Fonction de partage désactivée

## ⚙️ Configuration Avancée

### Modes d'Affichage

L'application supporte différents modes d'affichage (modifiable dans le code) :
- **Grille 2x2** : 2 colonnes (par défaut sur smartphone)
- **Grille 3x3** : 3 colonnes (recommandé pour tablettes)
- **Liste** : Affichage en liste verticale

Pour changer le mode, modifier dans `MainActivity.kt` :
```kotlin
GridLayoutManager(this@MainActivity, 2) // Changer le 2
```

### Personnalisation des Couleurs

Modifier les couleurs dans `res/values/colors.xml` :
```xml
<color name="primary">#FF5722</color>      <!-- Orange vif -->
<color name="accent">#4CAF50</color>        <!-- Vert -->
```

## 🐛 Résolution de Problèmes

### Erreur : "API Key not found"

**Solution** : Vérifier que `YOUTUBE_API_KEY` est bien définie dans `local.properties`

### Erreur : "Quota exceeded"

**Cause** : Limite de 10,000 requêtes/jour atteinte

**Solution** : Attendre le lendemain ou créer un nouveau projet Google Cloud

### Vidéos ne se chargent pas

**Solutions** :
1. Vérifier la connexion Internet
2. Vérifier que la vidéo n'est pas privée/supprimée
3. Vérifier la clé API YouTube

### Code PIN oublié

**Solution** :
1. Aller dans les paramètres Android → Applications
2. Trouver "YouTube Kids"
3. Cliquer sur "Stockage" → "Effacer les données"
4. **Attention** : Cela supprimera toutes les vidéos ajoutées !

## 📦 Génération de l'APK

### APK Debug (pour tests)

```bash
./gradlew assembleDebug
```

L'APK sera généré dans : `app/build/outputs/apk/debug/app-debug.apk`

### APK Release (pour distribution)

1. Créer un keystore :
```bash
keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-alias
```

2. Configurer dans `app/build.gradle.kts` :
```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("path/to/my-release-key.jks")
            storePassword = "password"
            keyAlias = "my-alias"
            keyPassword = "password"
        }
    }
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

3. Générer l'APK :
```bash
./gradlew assembleRelease
```

## 🔄 Mises à Jour Futures

### Fonctionnalités Prévues

- [ ] Support multilingue (FR/EN)
- [ ] Catégories de vidéos
- [ ] Vidéos favorites
- [ ] Statistiques de visionnage
- [ ] Import/Export de liste de vidéos
- [ ] Mode sombre
- [ ] Support des formats verticaux (Shorts)

## 📄 Licence

Ce projet est développé pour un usage personnel et éducatif.

**Important** :
- Respecter les [Conditions d'Utilisation de YouTube](https://www.youtube.com/t/terms)
- Ne pas télécharger les vidéos (violation des CGU)
- Usage personnel uniquement

## 👨‍💻 Support et Contact

Pour toute question ou problème :
- Ouvrir une issue sur GitHub
- Consulter la documentation YouTube Data API : https://developers.google.com/youtube/v3

## 🙏 Remerciements

- **YouTube Data API v3** : Récupération des métadonnées
- **PierfrancescoSoffritti/android-youtube-player** : Lecteur YouTube
- **Bumptech/Glide** : Chargement d'images
- **Square/Retrofit** : Client HTTP

---

**Développé avec ❤️ pour créer un environnement YouTube sûr pour les enfants**
