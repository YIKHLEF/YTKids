# 🚀 Guide de Démarrage Rapide - YouTube Kids

## Installation en 5 Minutes

### ✅ Ce dont vous avez besoin

1. **Android Studio** installé
   - Si ce n'est pas le cas : https://developer.android.com/studio

2. **Une clé API YouTube** (gratuite)
   - Aller sur : https://console.cloud.google.com/
   - Créer un projet → Activer "YouTube Data API v3" → Créer une clé API

---

## 📋 Étapes Rapides

### 1️⃣ Cloner/Télécharger le Projet

```bash
git clone <url-du-repo>
cd YTKids
```

Ou téléchargez et décompressez le ZIP.

### 2️⃣ Configurer la Clé API

Ouvrez le fichier `local.properties` et ajoutez :

```properties
YOUTUBE_API_KEY=VotreCléAPIIci
```

💡 **Remplacez** `VotreCléAPIIci` par votre vraie clé API YouTube.

### 3️⃣ Compiler l'APK

**Avec Android Studio** :
1. Ouvrir le projet dans Android Studio
2. Attendre la synchronisation Gradle
3. Cliquer sur **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
4. Récupérer l'APK dans : `app/build/outputs/apk/debug/app-debug.apk`

**En ligne de commande** :

**Linux/Mac** :
```bash
chmod +x build.sh
./build.sh
```

**Windows** :
```cmd
build.bat
```

### 4️⃣ Installer l'APK

**Option A - USB** :
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

**Option B - Transfert Manuel** :
1. Copier l'APK sur votre téléphone/tablette
2. Ouvrir le fichier APK
3. Autoriser l'installation

---

## 🎯 Premier Test

1. **Lancer l'app** sur votre appareil
2. **Créer un PIN** : `1234` (par exemple)
3. L'interface admin s'ouvre automatiquement
4. **Ajouter une vidéo de test** :
   - Cliquer sur le bouton **+**
   - Coller : `https://www.youtube.com/watch?v=dQw4w9WgXcQ`
   - Cliquer sur **Ajouter**
5. **Revenir** à l'écran principal (flèche ←)
6. **Taper** sur la vidéo pour la lire

---

## 🆘 Problèmes ?

| Problème | Solution |
|----------|----------|
| "SDK not found" | Définir `sdk.dir` dans `local.properties` |
| "API key error" | Vérifier `YOUTUBE_API_KEY` dans `local.properties` |
| Compilation lente | Première fois normal (télécharge les dépendances) |
| APK ne s'installe pas | Activer "Sources inconnues" dans les paramètres Android |

📖 **Guide complet** : Voir `BUILD_INSTRUCTIONS.md`

---

## 📞 Support

- README technique : `README.md`
- Guide utilisateur : `GUIDE_UTILISATEUR.md`
- Instructions de build : `BUILD_INSTRUCTIONS.md`

---

**C'est tout ! Profitez de YouTube Kids ! 🎉**
