# 📱 Comment Générer l'APK de YouTube Kids

## ⚠️ Important

L'environnement actuel (serveur) ne dispose pas du **SDK Android**, qui est nécessaire pour compiler l'application. Vous devez donc générer l'APK sur votre propre machine.

---

## 🎯 Solution : 3 Options Faciles

### Option 1 : Avec Android Studio (⭐ RECOMMANDÉ)

C'est la méthode la plus simple et la plus fiable.

#### Étapes :

1. **Télécharger Android Studio**
   - Site officiel : https://developer.android.com/studio
   - Version gratuite, compatible Windows/Mac/Linux
   - Installation : ~10-15 minutes

2. **Cloner/Télécharger le Projet**
   ```bash
   git clone <url-de-votre-repo>
   ```
   Ou téléchargez le ZIP depuis GitHub

3. **Ouvrir le Projet**
   - Lancer Android Studio
   - File → Open
   - Sélectionner le dossier `YTKids`
   - Attendre la synchronisation Gradle (2-5 minutes la première fois)

4. **Configurer la Clé API YouTube**
   - Ouvrir `local.properties`
   - Ajouter la ligne :
     ```
     YOUTUBE_API_KEY=VotreCléAPIYouTube
     ```
   - [Comment obtenir une clé API](https://console.cloud.google.com/)

5. **Générer l'APK**
   - Menu : **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
   - Attendre 1-3 minutes
   - Notification : "APK(s) generated successfully"
   - Cliquer sur **locate** pour voir le fichier

6. **Récupérer l'APK**
   - Emplacement : `YTKids/app/build/outputs/apk/debug/app-debug.apk`
   - Copier ce fichier sur votre téléphone/tablette

✅ **Avantages** :
- Interface graphique simple
- Gestion automatique des dépendances
- Détection d'erreurs en temps réel
- Idéal pour les débutants

---

### Option 2 : En Ligne de Commande (Pour Utilisateurs Avancés)

Si vous préférez le terminal et que vous avez déjà le SDK Android installé.

#### Linux/Mac :

```bash
# 1. Aller dans le dossier du projet
cd YTKids

# 2. Configurer la clé API dans local.properties
echo "YOUTUBE_API_KEY=VotreCléAPI" >> local.properties

# 3. Rendre le script exécutable (première fois seulement)
chmod +x build.sh

# 4. Lancer la compilation
./build.sh
```

#### Windows :

```cmd
REM 1. Aller dans le dossier du projet
cd YTKids

REM 2. Éditer local.properties et ajouter :
REM YOUTUBE_API_KEY=VotreCléAPI

REM 3. Lancer la compilation
build.bat
```

Le script va :
- ✅ Vérifier les prérequis (Java, SDK Android)
- ✅ Nettoyer les builds précédents
- ✅ Télécharger les dépendances
- ✅ Compiler l'APK
- ✅ Proposer l'installation automatique (si appareil connecté)

✅ **Avantages** :
- Rapide pour les développeurs
- Automatisation complète
- Installation directe sur appareil (optionnel)

---

### Option 3 : Gradle Direct

Pour les experts qui veulent un contrôle total.

```bash
# Linux/Mac
./gradlew assembleDebug

# Windows
gradlew.bat assembleDebug
```

L'APK sera généré dans : `app/build/outputs/apk/debug/app-debug.apk`

---

## 🔑 Obtenir une Clé API YouTube (Gratuit)

### Étapes Rapides :

1. Aller sur [Google Cloud Console](https://console.cloud.google.com/)
2. Créer un nouveau projet (ou en sélectionner un existant)
3. Activer l'API :
   - Menu → **API et services** → **Bibliothèque**
   - Rechercher : "YouTube Data API v3"
   - Cliquer sur **Activer**
4. Créer les identifiants :
   - Menu → **API et services** → **Identifiants**
   - Cliquer sur **+ Créer des identifiants**
   - Sélectionner **Clé API**
   - Copier la clé générée
5. *(Optionnel mais recommandé)* Restreindre la clé :
   - Cliquer sur la clé créée
   - Restrictions de l'API → Sélectionner "YouTube Data API v3"
   - Sauvegarder

💰 **Quota gratuit** : 10,000 requêtes/jour (largement suffisant pour usage personnel)

---

## 📲 Installer l'APK sur votre Appareil

### Méthode 1 : Via USB (Débogage USB)

1. **Activer le Mode Développeur** sur votre appareil Android :
   - Paramètres → À propos du téléphone
   - Taper 7 fois sur "Numéro de build"

2. **Activer le Débogage USB** :
   - Paramètres → Options pour les développeurs → Débogage USB (activer)

3. **Connecter l'appareil** à votre PC via USB

4. **Installer via ADB** :
   ```bash
   adb install -r app/build/outputs/apk/debug/app-debug.apk
   ```
   Ou utiliser le script `build.sh` / `build.bat` qui le fera automatiquement

### Méthode 2 : Transfert Manuel

1. **Copier l'APK** sur votre téléphone/tablette
   - Via USB
   - Via email
   - Via Google Drive/Dropbox/etc.

2. **Ouvrir le fichier APK** sur l'appareil

3. **Autoriser l'installation** depuis sources inconnues
   - Android demandera l'autorisation
   - Activer temporairement pour cette installation

4. **Installer** l'application

---

## ⏱️ Temps de Compilation Estimé

| Étape | Première fois | Fois suivantes |
|-------|---------------|----------------|
| Installation Android Studio | 15 minutes | - |
| Synchronisation Gradle | 5 minutes | 30 secondes |
| Compilation APK | 3 minutes | 1 minute |
| **TOTAL** | **~25 minutes** | **~2 minutes** |

---

## 🐛 Problèmes Courants

### "SDK location not found"

**Cause** : Android Studio pas installé ou SDK non configuré

**Solution** :
1. Installer Android Studio
2. Ou ajouter dans `local.properties` :
   ```
   sdk.dir=/chemin/vers/Android/Sdk
   ```
   - Windows : `C:\\Users\\VotreNom\\AppData\\Local\\Android\\Sdk`
   - Mac : `/Users/VotreNom/Library/Android/sdk`
   - Linux : `/home/VotreNom/Android/Sdk`

### "YOUTUBE_API_KEY not found"

**Cause** : Clé API non configurée

**Solution** :
- Éditer `local.properties` et ajouter :
  ```
  YOUTUBE_API_KEY=VotreCléAPIIci
  ```

### "Gradle sync failed"

**Solution** :
1. File → Invalidate Caches → Invalidate and Restart
2. Redémarrer Android Studio
3. Supprimer les dossiers `.gradle` et `build` puis re-synchroniser

### L'APK ne s'installe pas

**Solutions** :
- Vérifier la version Android : minimum 7.0 requis
- Activer "Sources inconnues" dans Sécurité
- Désinstaller l'ancienne version si présente

---

## 📚 Documentation Complète

Pour plus de détails, consultez :

- **Guide rapide** : `QUICK_START.md`
- **Instructions complètes** : `BUILD_INSTRUCTIONS.md`
- **Documentation technique** : `README.md`
- **Guide utilisateur** : `GUIDE_UTILISATEUR.md`

---

## ✅ Récapitulatif

1. ✅ Installer Android Studio
2. ✅ Cloner le projet
3. ✅ Ouvrir dans Android Studio
4. ✅ Ajouter la clé API YouTube dans `local.properties`
5. ✅ Build → Build APK(s)
6. ✅ Récupérer l'APK dans `app/build/outputs/apk/debug/`
7. ✅ Installer sur votre appareil

---

## 🎉 Félicitations !

Une fois l'APK généré et installé, vous pourrez :
- Créer un code PIN sécurisé
- Ajouter des vidéos YouTube présélectionnées
- Offrir un environnement YouTube sûr à vos enfants

**Bon visionnage ! 📺**
