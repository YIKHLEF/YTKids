# 🚀 Déploiement sur Vercel - Guide Complet

Ce guide vous montre comment déployer YouTube Kids PWA sur Vercel en protégeant votre clé API YouTube.

---

## 🔒 Sécurité de la Clé API

### ✅ Solution Implémentée

Votre application utilise maintenant **2 modes** :

| Mode | Environnement | Clé API | Sécurité |
|------|--------------|---------|----------|
| **Développement** | localhost | Fichier JS (claire) | ⚠️ Locale uniquement |
| **Production** | Vercel | Variable d'environnement | ✅ Protégée |

**En production**, la clé API est :
- ✅ Stockée dans les variables d'environnement Vercel (jamais dans Git)
- ✅ Utilisée uniquement côté serveur (Serverless Functions)
- ✅ Jamais exposée au client
- ✅ Protégée par HTTPS

---

## 📋 Prérequis

### 1. Compte Vercel

Créez un compte gratuit sur [vercel.com](https://vercel.com)

### 2. Clé API YouTube

1. Allez sur [Google Cloud Console](https://console.cloud.google.com/)
2. Créez un nouveau projet
3. Activez **"YouTube Data API v3"**
4. Créez des identifiants → **Clé API**
5. **IMPORTANT** : Restrictions de la clé API

#### Configuration de Sécurité de la Clé API

Dans Google Cloud Console :

**Option 1 : Restriction par HTTP Referer (Sites Web)**
```
Restrictions d'API → Clés HTTP Referer
Ajouter :
- https://votre-app.vercel.app/*
- https://*.vercel.app/* (pour les previews)
```

**Option 2 : Restrictions d'API**
```
Restrictions d'API → Restriction clés API
Sélectionner uniquement :
- YouTube Data API v3
```

**Option 3 : Quotas**
```
Quotas → YouTube Data API v3
- 10,000 requêtes/jour (gratuit)
- Configurez des alertes si proche de la limite
```

---

## 🚀 Déploiement Étape par Étape

### Étape 1 : Préparer le Repository Git

```bash
# Assurez-vous que tout est committé
git status

# Si des changements non commités
git add .
git commit -m "Préparation déploiement Vercel"
git push origin claude/youtube-kids-app-011CUUWuqasJWvdg3RPM2r4g
```

### Étape 2 : Connecter Vercel à Git

1. Allez sur [vercel.com/new](https://vercel.com/new)
2. Sélectionnez **Import Git Repository**
3. Connectez votre compte GitHub/GitLab/Bitbucket
4. Sélectionnez le repository **YTKids**

### Étape 3 : Configuration du Projet

**Framework Preset** : Other (ou None)

**Root Directory** : `./` (racine)

**Build Command** : (laisser vide)

**Output Directory** : `./` (racine)

### Étape 4 : Configurer les Variables d'Environnement

🔒 **CRITIQUE : C'est ici que vous protégez votre clé API !**

Dans Vercel Dashboard :

1. Cliquez sur **Environment Variables**
2. Ajoutez une nouvelle variable :

```
Name:  YOUTUBE_API_KEY
Value: [VOTRE_CLÉ_API_ICI]
```

**Environnements** : Cochez les 3 :
- ✅ Production
- ✅ Preview
- ✅ Development

3. Cliquez sur **Add**

### Étape 5 : Déployer

1. Cliquez sur **Deploy**
2. Attendez 1-2 minutes
3. Vercel vous donne une URL : `https://votre-app.vercel.app`

---

## ✅ Vérification du Déploiement

### Test 1 : Vérifier que la Clé API est Protégée

1. Ouvrez DevTools (F12) → Console
2. Allez sur votre app Vercel
3. Vous devriez voir :
   ```
   🔒 YouTube API: Mode sécurisé (proxy Vercel)
   ```

### Test 2 : Tester l'Ajout de Vidéo

1. Allez sur `https://votre-app.vercel.app/admin.html`
2. Entrez le PIN (2021)
3. Cliquez sur **+**
4. Collez une URL YouTube
5. La vidéo devrait s'ajouter ✅

### Test 3 : Vérifier les Logs

Dans Vercel Dashboard :

1. Allez dans **Deployments** → Votre déploiement
2. Cliquez sur **Functions**
3. Vérifiez les logs de `/api/youtube`
4. Vous devriez voir les appels API

### Test 4 : Vérifier que la Clé n'est PAS Exposée

1. DevTools → Network
2. Ajoutez une vidéo
3. Vérifiez les requêtes `/api/youtube`
4. La clé API ne doit PAS apparaître dans l'URL ✅

---

## 🔧 Configuration Avancée

### Domaine Personnalisé

1. Dans Vercel Dashboard → **Settings** → **Domains**
2. Ajoutez votre domaine personnalisé
3. Suivez les instructions DNS

**N'oubliez pas** de mettre à jour les restrictions de votre clé API YouTube !

### HTTPS et Sécurité

Vercel fournit automatiquement :
- ✅ HTTPS (SSL/TLS)
- ✅ HTTP/2
- ✅ Certificats auto-renouvelés
- ✅ DDoS protection

### Limites Vercel (Plan Gratuit)

| Resource | Limite |
|----------|--------|
| Bandwidth | 100 GB/mois |
| Serverless Functions | 100 GB-Hrs |
| Deployments | Illimité |
| Custom Domains | 50 |

Pour YouTube Kids, c'est **largement suffisant** !

---

## 📊 Monitoring

### Surveiller l'Utilisation de la Clé API

1. [Google Cloud Console](https://console.cloud.google.com/)
2. APIs & Services → Dashboard
3. YouTube Data API v3 → Quotas
4. Surveillez : **Queries per day**

### Alertes

Configurez une alerte si vous approchez 8,000 requêtes/jour :

1. Cloud Console → Monitoring
2. Alerting → Create Policy
3. Metric : YouTube Data API → Quota usage
4. Threshold : 8000

---

## 🐛 Dépannage

### Erreur : "YouTube API key not configured"

**Cause** : Variable d'environnement manquante

**Solution** :
1. Vercel Dashboard → Settings → Environment Variables
2. Vérifiez que `YOUTUBE_API_KEY` existe
3. Si manquante, ajoutez-la
4. Redéployez (Deployments → ... → Redeploy)

### Erreur : "API quota exceeded"

**Cause** : Vous avez dépassé 10,000 requêtes/jour

**Solutions** :
- Attendez minuit (heure de Los Angeles)
- Activez la facturation dans Google Cloud (coût très faible)
- Optimisez : mettez en cache les résultats

### Erreur : "CORS blocked"

**Cause** : Configuration CORS manquante

**Solution** : Vérifiez que `api/youtube.js` contient :
```javascript
res.setHeader('Access-Control-Allow-Origin', '*');
```

### Vidéos ne se chargent pas en production

**Vérification** :

1. DevTools → Console : Erreurs ?
2. DevTools → Network → `/api/youtube` : Status 200 ?
3. Vercel → Functions → Logs : Erreurs ?

**Solution commune** :
```bash
# Redéployer avec les variables d'environnement
vercel --prod
```

---

## 🔄 Mises à Jour

### Déployer de Nouvelles Modifications

```bash
# Local
git add .
git commit -m "Nouvelle fonctionnalité"
git push

# Vercel déploie automatiquement !
```

### Rollback (Revenir en Arrière)

1. Vercel Dashboard → Deployments
2. Trouvez un déploiement précédent
3. Cliquez sur **...** → **Promote to Production**

---

## 💰 Coûts

### Plan Gratuit Vercel

- ✅ **0€/mois**
- ✅ 100 GB bandwidth
- ✅ Domaine .vercel.app inclus
- ✅ HTTPS automatique
- ✅ Parfait pour YouTube Kids

### YouTube Data API

- ✅ **Gratuit** jusqu'à 10,000 requêtes/jour
- ⚠️ Au-delà : ~0.002€ par requête

**Estimation YouTube Kids** :
- 50 utilisateurs/jour
- 20 vidéos ajoutées/jour
- ~400 requêtes/jour
- → **100% gratuit** ✅

---

## 📚 Ressources

- [Documentation Vercel](https://vercel.com/docs)
- [Vercel Serverless Functions](https://vercel.com/docs/concepts/functions/serverless-functions)
- [YouTube Data API](https://developers.google.com/youtube/v3)
- [Vercel Environment Variables](https://vercel.com/docs/concepts/projects/environment-variables)

---

## ✅ Checklist de Déploiement

Avant de déployer :

- [ ] Clé API YouTube obtenue
- [ ] Restrictions API configurées (domaine Vercel)
- [ ] Repository Git à jour
- [ ] Compte Vercel créé
- [ ] Variable `YOUTUBE_API_KEY` configurée dans Vercel
- [ ] Test en local réussi
- [ ] `.env` ajouté au `.gitignore`

Après déploiement :

- [ ] URL Vercel fonctionnelle
- [ ] Console affiche "Mode sécurisé (proxy Vercel)"
- [ ] Ajout de vidéo fonctionne
- [ ] Clé API non visible dans Network
- [ ] PIN fonctionne
- [ ] Service Worker enregistré

---

## 🎉 Félicitations !

Votre YouTube Kids PWA est maintenant :

✅ Déployé sur Vercel
✅ Accessible mondialement
✅ Clé API sécurisée
✅ HTTPS automatique
✅ Déploiement continu (Git push → déploiement)

**URL** : `https://votre-app.vercel.app`

---

## 🔒 Sécurité - Résumé

| Élément | Sécurité | Comment |
|---------|----------|---------|
| **Clé API** | ✅ Protégée | Variables d'environnement Vercel |
| **HTTPS** | ✅ Activé | Automatique Vercel |
| **CORS** | ✅ Configuré | Serverless function |
| **Git** | ✅ Propre | .env dans .gitignore |
| **Domaine** | ✅ Restreint | Google Cloud API restrictions |

**Votre clé API ne sera JAMAIS exposée au client !** 🔒

---

**Besoin d'aide ?** Consultez les logs Vercel ou la documentation YouTube API.
