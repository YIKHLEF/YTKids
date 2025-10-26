# 🚀 Déploiement Rapide sur Vercel

Guide ultra-rapide pour déployer YouTube Kids PWA sur Vercel en 5 minutes.

---

## ⚡ Déploiement Express (5 minutes)

### 1. Obtenez votre Clé API YouTube (2 min)

```
1. https://console.cloud.google.com/
2. Créer un projet
3. Activer "YouTube Data API v3"
4. Créer clé API
5. COPIER la clé ✅
```

### 2. Déployez sur Vercel (2 min)

Cliquez sur ce bouton :

[![Deploy with Vercel](https://vercel.com/button)](https://vercel.com/new/clone?repository-url=https://github.com/VOTRE-USERNAME/YTKids)

OU manuellement :

```bash
# 1. Installer Vercel CLI
npm i -g vercel

# 2. Se connecter
vercel login

# 3. Déployer
vercel

# 4. Ajouter la clé API (dans le terminal)
vercel env add YOUTUBE_API_KEY
→ Coller votre clé API
→ Choisir : Production + Preview + Development

# 5. Redéployer pour activer la clé
vercel --prod
```

### 3. Configurez la Sécurité (1 min)

Dans [Google Cloud Console](https://console.cloud.google.com/) :

```
1. APIs & Services → Credentials
2. Cliquez sur votre clé API
3. Application restrictions → HTTP referrers
4. Ajouter : https://votre-app.vercel.app/*
5. SAVE
```

---

## ✅ C'est Tout !

Votre app est en ligne : **https://votre-app.vercel.app**

---

## 🔒 Votre Clé API est Protégée

✅ **Stockée** : Variables d'environnement Vercel (pas dans Git)
✅ **Utilisée** : Côté serveur uniquement (Serverless Functions)
✅ **Jamais exposée** : Au client/navigateur
✅ **HTTPS** : Automatique

---

## 🧪 Tester

```bash
# Aller sur votre app
https://votre-app.vercel.app

# Ouvrir la console (F12)
# Vous devriez voir :
🔒 YouTube API: Mode sécurisé (proxy Vercel)

# Tester l'admin
1. Appui long 3s en haut à droite
2. PIN: 2021
3. Cliquer sur +
4. Ajouter une vidéo YouTube
5. ✅ Ça marche !
```

---

## 📚 Documentation Complète

Pour plus de détails, voir [DEPLOY_VERCEL.md](DEPLOY_VERCEL.md)

---

## 🆘 Problèmes ?

### Erreur : "YouTube API key not configured"

```bash
# Ajouter la clé API
vercel env add YOUTUBE_API_KEY

# Redéployer
vercel --prod
```

### Erreur : "API quota exceeded"

Attendez minuit (heure de Los Angeles) ou activez la facturation Google Cloud.

### Vidéos ne se chargent pas

```bash
# Vérifier les logs
vercel logs

# Vérifier les variables d'environnement
vercel env ls
```

---

## 💡 Astuce Pro

**Domaine personnalisé** :

```bash
# Dans Vercel Dashboard
Settings → Domains → Add Domain
→ votre-domaine.com

# Mettre à jour Google Cloud
Credentials → Votre clé API
→ Ajouter : https://votre-domaine.com/*
```

---

## 🎉 Résumé

| Étape | Temps | Statut |
|-------|-------|--------|
| Clé API YouTube | 2 min | ✅ |
| Déployer Vercel | 2 min | ✅ |
| Config sécurité | 1 min | ✅ |
| **TOTAL** | **5 min** | **🚀** |

**Votre YouTube Kids PWA est en ligne et sécurisée !** 🎉
