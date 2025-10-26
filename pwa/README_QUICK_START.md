# 🚀 Démarrage Rapide - YouTube Kids PWA

Bienvenue ! Voici tout ce dont vous avez besoin pour tester l'application.

---

## 📋 Étape par Étape (3 minutes)

### 1️⃣ Lancer le serveur

```bash
cd pwa
python -m http.server 8000
```

Ou si vous êtes déjà dans le bon dossier :
```bash
python3 -m http.server 8000
```

Le serveur affichera :
```
Serving HTTP on :: port 8000 (http://[::]:8000/) ...
```

### 2️⃣ Configurer le PIN

Ouvrez dans votre navigateur :
```
http://localhost:8000/init-pin.html
```

Cliquez sur **"Configurer PIN à 2021"** → ✅ Done!

### 3️⃣ Ajouter des vidéos de démo

Ouvrez :
```
http://localhost:8000/test-features.html
```

Dans la section "Test 3: Gestion des Vidéos", cliquez sur **"Ajouter vidéos de démo"**

### 4️⃣ Voir l'application

Ouvrez :
```
http://localhost:8000/
```

Vous devriez voir 3 vidéos colorées ! 🎉

---

## 🔗 Liens Rapides

| Page | URL | Description |
|------|-----|-------------|
| **Interface Enfant** | http://localhost:8000/ | Page principale avec les vidéos |
| **Interface Admin** | http://localhost:8000/admin.html | Gestion des vidéos (PIN: 2021) |
| **Configuration PIN** | http://localhost:8000/init-pin.html | Configurer le PIN à 2021 |
| **Tests Automatiques** | http://localhost:8000/test-features.html | Suite de tests complète |
| **Lecteur Vidéo** | http://localhost:8000/player.html | Lecteur (automatique) |

---

## 🧪 Tester Toutes les Fonctionnalités

### Option A : Tests Automatiques (1 clic)

1. Ouvrez : http://localhost:8000/test-features.html
2. Cliquez sur **"▶️ Lancer tous les tests"**
3. Attendez 5 secondes
4. Tous les tests passent ✅

### Option B : Tests Manuels

Suivez le guide complet : [GUIDE_TEST.md](GUIDE_TEST.md)

---

## 🎮 Comment Utiliser l'Application

### Mode Enfant (Interface Principale)

1. Ouvrez http://localhost:8000/
2. Les vidéos s'affichent en grille colorée
3. Cliquez sur une vidéo pour la lire
4. Bouton ← Retour pour revenir

### Mode Admin (Interface d'Administration)

**Méthode 1 - Appui Long :**
1. Sur l'interface enfant (http://localhost:8000/)
2. Appuyez **3 secondes** dans le coin supérieur droit
3. Entrez le PIN : **2021**

**Méthode 2 - Accès Direct :**
1. Ouvrez http://localhost:8000/admin.html
2. Entrez le PIN : **2021**

**Dans l'admin, vous pouvez :**
- ➕ Ajouter des vidéos YouTube (bouton + en bas à droite)
- 🗑️ Supprimer des vidéos (icône poubelle)
- ← Retour à l'interface enfant

---

## 🔑 Configuration de la Clé API YouTube (Optionnel)

Pour ajouter de **vraies** vidéos YouTube :

### 1. Obtenir une clé API (Gratuit)

1. Allez sur https://console.cloud.google.com/
2. Créez un nouveau projet
3. Activez **"YouTube Data API v3"**
4. Créez des identifiants → **Clé API**
5. Copiez la clé

### 2. Configurer la clé

Ouvrez le fichier :
```
pwa/js/youtube-api.js
```

Ligne 6, remplacez :
```javascript
this.API_KEY = 'YOUR_YOUTUBE_API_KEY_HERE';
```

Par :
```javascript
this.API_KEY = 'VOTRE_VRAIE_CLE_API';
```

### 3. Tester

1. Allez sur http://localhost:8000/admin.html
2. Entrez le PIN : 2021
3. Cliquez sur le bouton **+**
4. Collez une URL YouTube :
   ```
   https://www.youtube.com/watch?v=dQw4w9WgXcQ
   ```
5. Cliquez sur **Ajouter**
6. La vidéo s'ajoute automatiquement ! 🎉

---

## ❓ FAQ - Questions Fréquentes

### Q: Le serveur ne démarre pas ?

**R:** Vérifiez que vous êtes dans le bon dossier :
```bash
cd /chemin/vers/YTKids/pwa
python -m http.server 8000
```

### Q: J'ai une erreur 404 ?

**R:** Utilisez les URLs sans le préfixe `/pwa/` :
- ✅ Bon : `http://localhost:8000/admin.html`
- ❌ Mauvais : `http://localhost:8000/pwa/admin.html`

### Q: Comment accéder à l'admin ?

**R:** Deux méthodes :
1. Appui long 3 secondes en haut à droite (interface enfant)
2. URL directe : http://localhost:8000/admin.html

### Q: Je ne peux pas ajouter de vidéos YouTube ?

**R:** Sans clé API, vous ne pouvez pas ajouter de vraies vidéos YouTube.

**Solutions :**
1. Utilisez les vidéos de démo : http://localhost:8000/test-features.html → "Ajouter vidéos de démo"
2. Configurez une clé API YouTube (voir section ci-dessus)

### Q: Le PIN ne fonctionne pas ?

**R:** Réinitialisez le PIN :
1. Ouvrez http://localhost:8000/test-features.html
2. Dans "Test 2", cliquez sur **"Réinitialiser PIN"**
3. Puis cliquez sur **"Créer PIN '2021'"**

### Q: Comment vider toutes les données ?

**R:**
1. Ouvrez http://localhost:8000/test-features.html
2. Dans "Test 1", cliquez sur **"Vider la BDD"**
3. Confirmez

### Q: L'appli fonctionne hors ligne ?

**R:** Oui ! Grâce au Service Worker :
1. Chargez l'application une fois en ligne
2. Fermez la connexion internet
3. L'interface se charge depuis le cache
4. Les vidéos déjà ajoutées restent visibles

### Q: Comment installer l'application sur mobile ?

**R:**
1. Ouvrez http://localhost:8000/ sur votre téléphone
2. Le navigateur proposera "Ajouter à l'écran d'accueil"
3. Acceptez
4. L'icône apparaît comme une vraie application

**Note :** Nécessite HTTPS en production (localhost fonctionne en développement)

---

## 🐛 Dépannage

### Erreur : "Cannot read property 'result' of undefined"

**Cause :** IndexedDB n'est pas initialisée

**Solution :**
```javascript
await db.init();
```

### Erreur : "Service Worker registration failed"

**Cause :** Le Service Worker nécessite HTTPS ou localhost

**Solution :** Utilisez `http://localhost:8000` (pas `http://127.0.0.1:8000`)

### Les vidéos ne se chargent pas

**Cause :** Pas de vidéos dans la base

**Solution :**
1. http://localhost:8000/test-features.html
2. Ajouter vidéos de démo

### Le PIN est refusé

**Cause :** PIN non configuré ou corrompu

**Solution :**
1. http://localhost:8000/init-pin.html
2. Configurer PIN à 2021

---

## 📱 Test sur Mobile / Tablette

### Méthode 1 : Réseau Local

1. Vérifiez l'IP de votre ordinateur :
   ```bash
   # Windows
   ipconfig

   # Mac/Linux
   ifconfig
   ```

2. Trouvez votre IP locale (ex: 192.168.1.100)

3. Sur votre mobile, ouvrez :
   ```
   http://192.168.1.100:8000/
   ```

### Méthode 2 : Tunnel (ngrok)

```bash
ngrok http 8000
```

Utilisez l'URL fournie (ex: https://abc123.ngrok.io)

---

## 📊 Checklist de Vérification

Avant de dire "ça marche" :

- [x] Le serveur démarre sur le port 8000
- [x] http://localhost:8000/ se charge sans erreur
- [x] Le PIN est configuré (2021)
- [x] Des vidéos de démo sont ajoutées
- [x] Les vidéos s'affichent dans l'interface enfant
- [x] L'appui long 3s ouvre l'admin
- [x] Le PIN est accepté dans l'admin
- [x] Les vidéos peuvent être supprimées
- [x] La navigation fonctionne (← Retour)
- [x] Les tests automatiques passent (http://localhost:8000/test-features.html)

---

## 🎯 Commandes Utiles

### Lancer le serveur
```bash
python -m http.server 8000
```

### Voir les logs en temps réel
```bash
# Le serveur affiche déjà les logs dans le terminal
```

### Arrêter le serveur
```
Ctrl + C
```

### Ouvrir DevTools (Debug)
```
F12 (navigateur)
```

### Vider le cache du navigateur
```
Ctrl + Shift + Delete (Chrome/Firefox)
```

---

## 📚 Documentation Complète

- **Guide de test détaillé** : [GUIDE_TEST.md](GUIDE_TEST.md)
- **Documentation technique** : [README.md](README.md)
- **Documentation PWA** : [PWA_vs_ANDROID.md](../PWA_vs_ANDROID.md)

---

## 🎉 C'est Tout !

Votre application YouTube Kids PWA est prête à être testée !

**Prochaines étapes :**

1. ✅ Testez avec les vidéos de démo
2. 🔑 (Optionnel) Configurez une clé API YouTube
3. 📱 Testez sur mobile
4. 🚀 Déployez en production (avec HTTPS)

---

**Besoin d'aide ?**

- 📖 Consultez le [GUIDE_TEST.md](GUIDE_TEST.md) pour les tests détaillés
- 🧪 Utilisez http://localhost:8000/test-features.html pour le débogage
- 🔍 Ouvrez la console (F12) pour voir les erreurs JavaScript

**Bon test ! 🎬**
