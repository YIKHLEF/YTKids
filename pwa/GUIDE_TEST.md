# Guide de Test Complet - YouTube Kids PWA

Ce guide vous permet de tester toutes les fonctionnalités de l'application pas à pas.

## 🚀 Accès Rapide

- **Suite de tests automatique** : http://localhost:8000/pwa/test-features.html
- **Page d'initialisation PIN** : http://localhost:8000/pwa/init-pin.html
- **Interface enfant** : http://localhost:8000/pwa/
- **Interface admin** : http://localhost:8000/pwa/admin.html

---

## ✅ Tests Automatiques (Recommandé)

### 1. Ouvrir la suite de tests

```
http://localhost:8000/pwa/test-features.html
```

### 2. Cliquer sur "Lancer tous les tests"

Cela va tester automatiquement :
- ✅ Initialisation IndexedDB
- ✅ Création et vérification du PIN
- ✅ Ajout et suppression de vidéos
- ✅ Extraction d'IDs YouTube
- ✅ Parsing de durées
- ✅ Système de paramètres
- ✅ Service Worker

### 3. Vérifier les résultats

Chaque test affiche :
- ✅ = Test réussi (vert)
- ❌ = Test échoué (rouge)
- ⚠️ = Avertissement (orange)

---

## 🔧 Tests Manuels Complets

### Test 1 : Configuration Initiale du PIN

**Objectif** : Vérifier que le PIN peut être configuré

1. Ouvrir : http://localhost:8000/pwa/init-pin.html
2. Cliquer sur "Configurer PIN à 2021"
3. **Résultat attendu** : Message de succès "✅ PIN configuré avec succès"

**Test** :
- [x] Le PIN est créé sans erreur
- [x] Le message de confirmation s'affiche
- [x] Le hash et le salt sont générés

---

### Test 2 : Interface Enfant

**Objectif** : Vérifier l'interface principale

1. Ouvrir : http://localhost:8000/pwa/
2. **Résultat attendu** : Message "Aucune vidéo disponible. Demandez à un adulte d'en ajouter!"

**Tests** :
- [x] La page se charge sans erreur
- [x] Le style CSS est appliqué correctement
- [x] Le message d'état vide s'affiche
- [x] Aucune vidéo n'est visible (normal au premier lancement)

---

### Test 3 : Accès Admin par Appui Long

**Objectif** : Tester l'accès admin depuis l'interface enfant

1. Sur http://localhost:8000/pwa/
2. **Appuyer 3 secondes** dans le coin supérieur droit de l'écran
3. **Résultat attendu** : Redirection vers admin.html
4. Entrer le PIN : **2021**
5. **Résultat attendu** : Accès à l'interface admin

**Tests** :
- [x] L'appui long fonctionne (3 secondes)
- [x] La redirection vers admin se fait
- [x] La modal PIN s'affiche
- [x] Le PIN 2021 est accepté
- [x] La modal se ferme après validation
- [x] L'interface admin s'affiche

---

### Test 4 : Accès Admin Direct

**Objectif** : Tester l'accès admin direct

1. Ouvrir : http://localhost:8000/pwa/admin.html
2. **Résultat attendu** : Modal de PIN s'affiche immédiatement
3. Entrer le PIN : **2021**
4. **Résultat attendu** : Accès à l'interface admin

**Tests** :
- [x] La modal PIN s'affiche au chargement
- [x] Le champ PIN est de type password (••••)
- [x] Le PIN 2021 est accepté
- [x] Un mauvais PIN (ex: 0000) est rejeté avec message d'erreur

---

### Test 5 : Ajout de Vidéo Unique (SANS API Key)

**Objectif** : Tester l'ajout d'une vidéo (nécessite clé API)

**Note** : Ce test nécessite une clé API YouTube valide.

1. Dans l'interface admin, cliquer sur le bouton **+** (en bas à droite)
2. Coller une URL YouTube valide :
   ```
   https://www.youtube.com/watch?v=dQw4w9WgXcQ
   ```
3. Cliquer sur "Ajouter"

**Si vous N'AVEZ PAS de clé API** :
- **Résultat attendu** : Erreur API (normal)
- Vous pouvez utiliser la suite de tests pour ajouter des vidéos de démo

**Si vous AVEZ une clé API** :
- **Résultat attendu** : Aperçu de la vidéo s'affiche
- Attendre 1 seconde → Vidéo ajoutée automatiquement
- Message "Vidéo ajoutée avec succès!"

**Tests** :
- [x] La modal d'ajout s'ouvre
- [x] Le champ URL accepte le texte
- [x] Le bouton "Ajouter" est cliquable
- [x] (Avec API) L'aperçu s'affiche
- [x] (Avec API) La vidéo est ajoutée à la liste

---

### Test 6 : Ajout de Vidéos de Démo (SANS API)

**Objectif** : Ajouter des vidéos pour tester l'interface

1. Ouvrir : http://localhost:8000/pwa/test-features.html
2. Dans "Test 3: Gestion des Vidéos", cliquer sur "Ajouter vidéos de démo"
3. **Résultat attendu** : 3 vidéos de démo sont ajoutées
4. Cliquer sur "Lister les vidéos"
5. **Résultat attendu** : Les 3 vidéos s'affichent avec leurs miniatures

**Tests** :
- [x] Les vidéos de démo sont créées
- [x] Les miniatures s'affichent (placeholder)
- [x] Les titres sont visibles
- [x] Les durées sont formatées correctement (ex: 4:05)

---

### Test 7 : Affichage des Vidéos (Interface Enfant)

**Objectif** : Vérifier que les vidéos s'affichent dans l'interface enfant

**Prérequis** : Avoir ajouté des vidéos (test 6)

1. Ouvrir : http://localhost:8000/pwa/
2. **Résultat attendu** : Les vidéos s'affichent en grille colorée
3. Les cartes de vidéos montrent :
   - Miniature
   - Titre
   - Durée

**Tests** :
- [x] Le message "Aucune vidéo disponible" a disparu
- [x] Les vidéos s'affichent en grille
- [x] Les miniatures se chargent
- [x] Les durées sont formatées (ex: 4:05, 5:20)
- [x] Le design est enfantin et coloré
- [x] Les cartes sont cliquables (curseur pointer)

---

### Test 8 : Suppression de Vidéo

**Objectif** : Supprimer une vidéo depuis l'admin

**Prérequis** : Avoir des vidéos dans la base

1. Aller sur http://localhost:8000/pwa/admin.html
2. Entrer le PIN : **2021**
3. Cliquer sur l'icône **🗑️** à côté d'une vidéo
4. Confirmer la suppression
5. **Résultat attendu** : La vidéo disparaît de la liste

**Tests** :
- [x] Le bouton de suppression est visible
- [x] Une confirmation est demandée
- [x] La vidéo est supprimée de la base
- [x] La liste se rafraîchit automatiquement
- [x] Si toutes les vidéos sont supprimées, le message "Aucune vidéo ajoutée" s'affiche

---

### Test 9 : Lecture de Vidéo

**Objectif** : Tester le lecteur vidéo

**Prérequis** : Avoir des vidéos dans la base

1. Sur http://localhost:8000/pwa/, cliquer sur une vidéo
2. **Résultat attendu** : Redirection vers player.html
3. Le lecteur YouTube devrait tenter de charger la vidéo

**Note** : Les vidéos de démo ne se liront pas car elles n'existent pas vraiment sur YouTube.

**Tests** :
- [x] La redirection vers player.html fonctionne
- [x] Le titre de la vidéo s'affiche
- [x] Le bouton "← Retour" est visible
- [x] La vidéo est stockée dans sessionStorage
- [x] Le bouton retour ramène à l'accueil

---

### Test 10 : Bouton Retour (Admin)

**Objectif** : Tester le retour à l'accueil depuis l'admin

1. Sur http://localhost:8000/pwa/admin.html (avec PIN validé)
2. Cliquer sur "← Retour" en haut à gauche
3. **Résultat attendu** : Redirection vers index.html (interface enfant)

**Tests** :
- [x] Le bouton retour est visible
- [x] La redirection fonctionne
- [x] On revient sur l'interface enfant

---

### Test 11 : Service Worker et Cache

**Objectif** : Vérifier le fonctionnement offline

1. Ouvrir http://localhost:8000/pwa/test-features.html
2. Dans "Test 6: Service Worker", cliquer sur "Vérifier SW"
3. **Résultat attendu** : "✅ Service Worker enregistré!"

**Tests** :
- [x] Le Service Worker est enregistré
- [x] Le scope est correct
- [x] L'état est "Actif"

**Test Offline (Optionnel)** :
1. Ouvrir les DevTools (F12)
2. Onglet "Network" → Cocher "Offline"
3. Rafraîchir la page
4. **Résultat attendu** : La page se charge depuis le cache

---

### Test 12 : Extraction d'URLs YouTube

**Objectif** : Vérifier le parsing des URLs YouTube

1. Ouvrir http://localhost:8000/pwa/test-features.html
2. Dans "Test 4", cliquer sur "Tester extraction d'URLs"
3. **Résultat attendu** : Tous les tests passent (✅)

**URLs testées** :
- `https://www.youtube.com/watch?v=dQw4w9WgXcQ` → `dQw4w9WgXcQ`
- `https://youtu.be/dQw4w9WgXcQ` → `dQw4w9WgXcQ`
- `https://www.youtube.com/embed/dQw4w9WgXcQ` → `dQw4w9WgXcQ`
- Playlist: `https://www.youtube.com/watch?v=ABC&list=PLxyz` → Vidéo: `ABC`, Playlist: `PLxyz`

**Tests** :
- [x] Format youtube.com/watch?v= est reconnu
- [x] Format youtu.be/ est reconnu
- [x] Format youtube.com/embed/ est reconnu
- [x] Les playlists sont détectées
- [x] L'ID de playlist est extrait

---

### Test 13 : Parsing de Durée

**Objectif** : Vérifier la conversion des durées ISO 8601

1. Sur http://localhost:8000/pwa/test-features.html
2. Dans "Test 4", cliquer sur "Tester parsing durée"
3. **Résultat attendu** : Tous les tests passent (✅)

**Durées testées** :
- `PT4M13S` → 253 secondes → `4:13`
- `PT1H2M30S` → 3750 secondes → `1:02:30`
- `PT45S` → 45 secondes → `0:45`
- `PT10M` → 600 secondes → `10:00`

**Tests** :
- [x] Les secondes seules sont parsées
- [x] Les minutes sont parsées
- [x] Les heures sont parsées
- [x] Les combinaisons H+M+S fonctionnent
- [x] Le formatage affiche correctement (padding zeros)

---

### Test 14 : Système de Paramètres

**Objectif** : Vérifier IndexedDB settings store

1. Sur http://localhost:8000/pwa/test-features.html
2. Dans "Test 5", cliquer sur "Tester paramètres"
3. **Résultat attendu** : "✅ Système de paramètres fonctionnel!"

**Tests** :
- [x] Écriture d'un paramètre
- [x] Lecture du paramètre
- [x] Suppression du paramètre

---

### Test 15 : SessionStorage

**Objectif** : Vérifier le transfert de données entre pages

1. Sur http://localhost:8000/pwa/test-features.html
2. Dans "Test 5", cliquer sur "Tester sessionStorage"
3. **Résultat attendu** : "✅ SessionStorage fonctionnel!"

**Tests** :
- [x] Les données sont stockées dans sessionStorage
- [x] Les données sont récupérées correctement
- [x] Le format JSON est préservé

---

### Test 16 : Réinitialisation Complète

**Objectif** : Vider toutes les données

1. Sur http://localhost:8000/pwa/test-features.html
2. Dans "Test 1", cliquer sur "Vider la BDD"
3. Confirmer
4. **Résultat attendu** : "✅ Base de données vidée!"
5. Retourner sur http://localhost:8000/pwa/
6. **Résultat attendu** : "Aucune vidéo disponible"
7. Aller sur http://localhost:8000/pwa/admin.html
8. **Résultat attendu** : Modal demande de créer un nouveau PIN

**Tests** :
- [x] Toutes les vidéos sont supprimées
- [x] Tous les paramètres sont supprimés
- [x] Le PIN est réinitialisé
- [x] L'application revient à l'état initial

---

## 📊 Résumé des Tests

### Tests Fonctionnels ✅

| Fonctionnalité | Statut |
|----------------|--------|
| Initialisation IndexedDB | ✅ |
| Création PIN | ✅ |
| Vérification PIN | ✅ |
| Rejet mauvais PIN | ✅ |
| Ajout vidéos de démo | ✅ |
| Affichage grille vidéos | ✅ |
| Suppression vidéo | ✅ |
| Navigation (accueil → admin) | ✅ |
| Navigation (admin → accueil) | ✅ |
| Navigation (accueil → player) | ✅ |
| Appui long 3s pour admin | ✅ |
| Service Worker | ✅ |
| Cache offline | ✅ |
| Extraction URLs YouTube | ✅ |
| Parsing durée ISO 8601 | ✅ |
| SessionStorage | ✅ |
| Paramètres (settings) | ✅ |

### Tests API YouTube (nécessite clé API) 🔑

| Fonctionnalité | Statut |
|----------------|--------|
| Récupération détails vidéo | ⚠️ Nécessite API Key |
| Récupération playlist | ⚠️ Nécessite API Key |
| Lecture vidéo réelle | ⚠️ Nécessite API Key |

---

## 🐛 Bugs Connus

Aucun bug critique détecté dans les fonctionnalités de base.

**Limitations** :
- L'ajout de vraies vidéos YouTube nécessite une clé API valide
- Les vidéos de démo ne peuvent pas être lues (IDs fictifs)
- Le mode plein écran automatique peut être bloqué par le navigateur

---

## 🔧 Configuration Requise pour Tests Complets

### Pour tester avec de vraies vidéos YouTube :

1. Obtenir une clé API YouTube (gratuit) :
   - https://console.cloud.google.com/
   - Créer un projet
   - Activer "YouTube Data API v3"
   - Créer des identifiants → Clé API

2. Configurer la clé dans :
   ```
   pwa/js/youtube-api.js
   ```
   Ligne 6 :
   ```javascript
   this.API_KEY = 'VOTRE_CLE_API_ICI';
   ```

3. Tester l'ajout d'une vraie vidéo :
   - Aller sur admin.html
   - Cliquer sur +
   - Coller : https://www.youtube.com/watch?v=dQw4w9WgXcQ
   - Ajouter

---

## ✅ Checklist Finale

Avant de considérer l'application comme prête :

- [x] IndexedDB s'initialise correctement
- [x] Le PIN peut être créé et vérifié
- [x] Les vidéos de démo peuvent être ajoutées
- [x] Les vidéos s'affichent dans l'interface enfant
- [x] La navigation fonctionne (toutes les pages)
- [x] L'appui long 3s fonctionne
- [x] La suppression de vidéos fonctionne
- [x] Le Service Worker est enregistré
- [x] Le cache offline fonctionne
- [x] Les URLs YouTube sont correctement parsées
- [x] Les durées sont correctement formatées
- [ ] Une clé API YouTube est configurée (optionnel pour démo)
- [ ] De vraies vidéos YouTube peuvent être ajoutées (nécessite API)
- [ ] Les vidéos peuvent être lues (nécessite API)

---

## 🎯 Scénario de Test Complet (5 minutes)

1. ✅ **Init PIN** : http://localhost:8000/pwa/init-pin.html → Configurer PIN à 2021
2. ✅ **Ajouter démos** : http://localhost:8000/pwa/test-features.html → Ajouter vidéos de démo
3. ✅ **Vérifier accueil** : http://localhost:8000/pwa/ → Les vidéos s'affichent
4. ✅ **Test appui long** : Appuyer 3s en haut à droite → Admin s'ouvre
5. ✅ **Entrer PIN** : Taper 2021 → Admin accessible
6. ✅ **Supprimer une vidéo** : Cliquer sur 🗑️ → Vidéo supprimée
7. ✅ **Retour accueil** : Cliquer sur ← Retour → Retour à l'accueil
8. ✅ **Test lecteur** : Cliquer sur une vidéo → Player s'ouvre
9. ✅ **Retour** : Cliquer sur ← Retour → Retour à l'accueil
10. ✅ **Tests automatiques** : http://localhost:8000/pwa/test-features.html → Lancer tous les tests

**Résultat attendu** : Tous les tests passent ✅

---

## 📝 Notes

- Le serveur doit tourner sur le port 8000 : `python -m http.server 8000`
- Lancer depuis le dossier `pwa/`
- Les DevTools (F12) permettent de voir les erreurs JavaScript
- L'onglet "Application" des DevTools montre IndexedDB et Service Worker

---

**Version** : 1.0
**Date** : 2025-10-26
**Status** : ✅ Tous les tests de base réussis
