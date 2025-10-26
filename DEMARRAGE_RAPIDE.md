# 🚀 Démarrage Rapide - YouTube Kids PWA

## En 5 Minutes Chrono ! ⏱️

### Étape 1 : Obtenir une Clé API YouTube (2 minutes)

1. Aller sur : https://console.cloud.google.com/
2. Créer un projet
3. Activer **"YouTube Data API v3"**
4. Créer une **Clé API**
5. **Copier la clé**

### Étape 2 : Configurer la Clé (30 secondes)

Ouvrir le fichier `js/youtube-api.js` et remplacer :

```javascript
this.API_KEY = 'YOUR_YOUTUBE_API_KEY_HERE';
```

Par votre clé :

```javascript
this.API_KEY = 'AIzaSyAbc123...'; // Votre clé ici
```

### Étape 3 : Lancer l'Application (30 secondes)

**Option A - Python (le plus simple) :**
```bash
cd pwa
python -m http.server 8000
```

**Option B - Node.js :**
```bash
npm install -g http-server
cd pwa
http-server -p 8000
```

**Option C - PHP :**
```bash
cd pwa
php -S localhost:8000
```

### Étape 4 : Ouvrir dans le Navigateur

```
http://localhost:8000
```

### Étape 5 : Installer sur Mobile (Optionnel)

1. Ouvrir l'URL sur votre smartphone
2. Menu → **"Ajouter à l'écran d'accueil"**
3. ✅ L'app est installée !

---

## 🎉 C'est Tout !

Votre application YouTube Kids fonctionne maintenant !

### Premier Lancement

1. **Créer un PIN** (4 chiffres)
2. **Ajouter des vidéos** avec le bouton +
3. **Profiter** de l'expérience sécurisée

---

## 🌐 Déployer en Ligne (Bonus)

### GitHub Pages (Gratuit)

1. Créer un repo sur GitHub
2. Pousser le dossier `pwa`
3. Settings → Pages → Activer
4. **C'est en ligne !**

### Netlify (Le Plus Simple)

1. Aller sur : https://netlify.com
2. **Glisser-déposer** le dossier `pwa`
3. **C'est en ligne en 30 secondes !**

URL automatique fournie : `https://votre-site.netlify.app`

---

## ❓ Problèmes ?

### La clé API ne fonctionne pas

✅ Vérifier que l'API YouTube Data v3 est **activée**
✅ Vérifier que la clé est **copiée correctement**
✅ Attendre 1-2 minutes (propagation de la clé)

### Le serveur ne démarre pas

✅ Vérifier que Python/Node/PHP est installé
✅ Essayer un autre port : `python -m http.server 8080`
✅ Vérifier que le port n'est pas déjà utilisé

### L'app ne s'installe pas

✅ Utiliser **Chrome** ou **Edge** (meilleur support PWA)
✅ Vérifier que vous êtes en **HTTPS** ou **localhost**

---

## 📱 Tester sur Mobile

1. **Trouver votre IP locale** :
   - Windows : `ipconfig`
   - Mac/Linux : `ifconfig` ou `ip a`

2. **Ouvrir sur mobile** :
   ```
   http://VOTRE_IP:8000
   ```
   Exemple : `http://192.168.1.10:8000`

3. **Installer l'app** sur mobile

---

## 🎨 Personnaliser

### Changer les couleurs

Modifier `css/style.css` :

```css
:root {
    --color-primary: #FF5722;  /* Votre couleur */
}
```

### Changer le nom

Modifier `manifest.json` :

```json
{
  "name": "Mon YouTube Kids",
  "short_name": "My YTK"
}
```

---

## 📚 Documentation Complète

Pour plus de détails, voir : **`README.md`**

---

**Bonne utilisation ! 🎉**
