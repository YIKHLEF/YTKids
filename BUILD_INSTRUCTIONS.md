# Instructions de Compilation - YouTube Kids App

## ⚠️ Prérequis

Pour compiler cette application Android, vous avez besoin de :

1. **Android Studio** (Giraffe 2022.3.1 ou plus récent)
   - Télécharger : https://developer.android.com/studio

2. **JDK 17** (généralement inclus avec Android Studio)

3. **Clé API YouTube Data v3**
   - Créer un projet sur : https://console.cloud.google.com/
   - Activer "YouTube Data API v3"
   - Créer une clé API

---

## 🚀 Méthode 1 : Avec Android Studio (Recommandé)

### Étape 1 : Ouvrir le Projet

1. Lancer **Android Studio**
2. Cliquer sur **File** → **Open**
3. Sélectionner le dossier `YTKids`
4. Attendre la synchronisation Gradle (première fois peut prendre 5-10 minutes)

### Étape 2 : Configurer la Clé API

1. Ouvrir le fichier `local.properties` à la racine du projet
2. Ajouter la ligne suivante :
   ```
   YOUTUBE_API_KEY=VOTRE_CLE_API_ICI
   ```
3. Remplacer `VOTRE_CLE_API_ICI` par votre vraie clé API
4. Sauvegarder le fichier

### Étape 3 : Générer l'APK Debug

**Option A - Via le Menu** :
1. Cliquer sur **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
2. Attendre la fin de la compilation (1-5 minutes)
3. Une notification apparaîtra : "APK(s) generated successfully"
4. Cliquer sur **locate** pour ouvrir le dossier de l'APK

**Option B - Via la Barre d'Outils** :
1. Sélectionner **app** dans le menu déroulant de configuration
2. Cliquer sur l'icône **marteau** 🔨 (Build Project)
3. Aller dans **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**

**Emplacement de l'APK** :
```
YTKids/app/build/outputs/apk/debug/app-debug.apk
```

### Étape 4 : Installer l'APK sur Appareil

**Via USB** :
1. Activer le **Mode Développeur** sur votre appareil Android :
   - Paramètres → À propos du téléphone
   - Taper 7 fois sur "Numéro de build"
2. Activer le **Débogage USB** :
   - Paramètres → Options pour les développeurs → Débogage USB
3. Connecter l'appareil à votre PC via USB
4. Dans Android Studio, cliquer sur **Run** ▶️
5. Sélectionner votre appareil

**Via Fichier APK** :
1. Copier `app-debug.apk` sur votre appareil
2. Ouvrir le fichier APK sur l'appareil
3. Accepter l'installation depuis une source inconnue
4. L'application s'installe

---

## 🖥️ Méthode 2 : En Ligne de Commande (Avancé)

### Prérequis

- Android SDK installé
- Variable d'environnement `ANDROID_HOME` configurée
- JDK 17 dans le PATH

### Sous Windows

```cmd
# Ouvrir le terminal dans le dossier du projet
cd chemin\vers\YTKids

# Compiler le projet
gradlew.bat assembleDebug

# L'APK sera généré dans :
# app\build\outputs\apk\debug\app-debug.apk
```

### Sous Linux/Mac

```bash
# Ouvrir le terminal dans le dossier du projet
cd /chemin/vers/YTKids

# Rendre le script executable (première fois seulement)
chmod +x gradlew

# Compiler le projet
./gradlew assembleDebug

# L'APK sera généré dans :
# app/build/outputs/apk/debug/app-debug.apk
```

### Installer l'APK via ADB

```bash
# Vérifier que l'appareil est connecté
adb devices

# Installer l'APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Si déjà installé, utiliser -r pour réinstaller
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## 📦 Générer un APK Release (Pour Distribution)

### Étape 1 : Créer un Keystore

```bash
keytool -genkey -v -keystore youtube-kids-release.keystore \
  -alias youtubekids -keyalg RSA -keysize 2048 -validity 10000
```

**Informations à fournir** :
- Mot de passe du keystore (mémorisez-le !)
- Nom, organisation, etc.
- Mot de passe de la clé (peut être le même)

⚠️ **Important** : Conservez ce fichier `.keystore` en lieu sûr !

### Étape 2 : Configurer la Signature

1. Créer un fichier `keystore.properties` à la racine du projet :
```properties
storePassword=VOTRE_MOT_DE_PASSE_KEYSTORE
keyPassword=VOTRE_MOT_DE_PASSE_CLE
keyAlias=youtubekids
storeFile=youtube-kids-release.keystore
```

2. Modifier `app/build.gradle.kts` pour ajouter la configuration de signature (voir README.md)

### Étape 3 : Compiler le Release APK

**Android Studio** :
- Build → Generate Signed Bundle / APK
- Sélectionner APK
- Choisir le keystore
- Build Variant : release

**Ligne de commande** :
```bash
./gradlew assembleRelease
```

**APK généré dans** :
```
app/build/outputs/apk/release/app-release.apk
```

---

## 🔧 Résolution de Problèmes

### Erreur : "SDK location not found"

**Solution** :
1. Créer/Modifier `local.properties`
2. Ajouter :
   ```
   sdk.dir=/chemin/vers/Android/Sdk
   ```
   - Windows : `C\:\\Users\\VotreNom\\AppData\\Local\\Android\\Sdk`
   - Mac : `/Users/VotreNom/Library/Android/sdk`
   - Linux : `/home/VotreNom/Android/Sdk`

### Erreur : "Gradle sync failed"

**Solutions** :
1. **File** → **Invalidate Caches** → **Invalidate and Restart**
2. Supprimer les dossiers `.gradle` et `build`
3. Re-synchroniser : **File** → **Sync Project with Gradle Files**

### Erreur : "YOUTUBE_API_KEY not found"

**Solution** :
- Vérifier que `local.properties` contient bien :
  ```
  YOUTUBE_API_KEY=VotreCléAPIIci
  ```
- Pas de guillemets, pas d'espaces

### Compilation très lente

**Solutions** :
1. Fermer les autres applications
2. Augmenter la mémoire Gradle :
   - Modifier `gradle.properties` :
     ```
     org.gradle.jvmargs=-Xmx4096m
     ```
3. Activer le mode offline Gradle (si toutes les dépendances sont déjà téléchargées)

### L'APK ne s'installe pas sur l'appareil

**Solutions** :
1. **Vérifier les sources inconnues** :
   - Paramètres → Sécurité → Sources inconnues (activer)
2. **Désinstaller l'ancienne version** si présente
3. **Vérifier la version Android** : Minimum Android 7.0 (API 24)

---

## 📊 Informations de Build

| Item | Valeur |
|------|--------|
| **Min SDK** | API 24 (Android 7.0) |
| **Target SDK** | API 34 (Android 14) |
| **Build Tools** | 34.0.0 |
| **Gradle** | 8.2+ |
| **Kotlin** | 1.9.20 |
| **JDK** | 17 |

---

## ✅ Checklist Avant Compilation

- [ ] Android Studio installé et à jour
- [ ] Projet ouvert et synchronisé
- [ ] Clé API YouTube ajoutée dans `local.properties`
- [ ] SDK Android configuré
- [ ] Connexion Internet (pour télécharger les dépendances)

---

## 📱 Tester l'Application

Après installation, pour tester :

1. **Premier lancement** :
   - L'app demande de créer un PIN (ex: 1234)
   - Entrer deux fois le même code

2. **Ajouter une vidéo de test** :
   - Interface admin s'ouvre automatiquement
   - Cliquer sur le bouton **+**
   - Coller ce lien de test : `https://www.youtube.com/watch?v=dQw4w9WgXcQ`
   - Cliquer sur **Ajouter**

3. **Tester la lecture** :
   - Revenir à l'écran principal (flèche retour)
   - Taper sur la vidéo ajoutée
   - La vidéo doit se lire en plein écran

4. **Tester l'accès admin** :
   - Retourner à l'écran principal
   - Appuyer longtemps (3 secondes) sur le coin supérieur droit
   - Entrer le PIN
   - Interface admin s'ouvre

---

## 🎉 Félicitations !

Si vous avez réussi toutes ces étapes, vous avez maintenant :
- ✅ Un APK fonctionnel de YouTube Kids
- ✅ Une application testée et prête à l'emploi
- ✅ La possibilité de la distribuer à votre famille

**Profitez-en bien ! 🚀**
