#!/bin/bash

# Script de compilation pour YouTube Kids Android App
# Auteur: Claude Code
# Date: 2025-10-25

set -e  # Arrêter en cas d'erreur

echo "=========================================="
echo "  YouTube Kids - Script de Compilation"
echo "=========================================="
echo ""

# Couleurs pour le terminal
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Fonction d'affichage
print_step() {
    echo -e "${BLUE}[ÉTAPE]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[OK]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERREUR]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[ATTENTION]${NC} $1"
}

# Vérification des prérequis
print_step "Vérification des prérequis..."

# Vérifier Java
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | cut -d'.' -f1)
    print_success "Java trouvé (version $JAVA_VERSION)"
else
    print_error "Java n'est pas installé. Veuillez installer JDK 17 ou supérieur."
    exit 1
fi

# Vérifier ANDROID_HOME
if [ -z "$ANDROID_HOME" ]; then
    print_warning "ANDROID_HOME n'est pas défini."
    print_warning "Tentative de détection automatique du SDK Android..."

    # Chemins courants du SDK Android
    POSSIBLE_SDK_PATHS=(
        "$HOME/Android/Sdk"
        "$HOME/Library/Android/sdk"
        "/usr/local/android-sdk"
        "/opt/android-sdk"
    )

    for path in "${POSSIBLE_SDK_PATHS[@]}"; do
        if [ -d "$path" ]; then
            export ANDROID_HOME="$path"
            print_success "SDK Android trouvé : $ANDROID_HOME"
            break
        fi
    done

    if [ -z "$ANDROID_HOME" ]; then
        print_error "SDK Android introuvable. Veuillez installer Android Studio."
        print_error "Ou définir la variable ANDROID_HOME manuellement."
        exit 1
    fi
else
    print_success "SDK Android : $ANDROID_HOME"
fi

# Vérifier local.properties
print_step "Vérification de la configuration..."

if [ ! -f "local.properties" ]; then
    print_error "Fichier local.properties manquant !"
    exit 1
fi

if ! grep -q "YOUTUBE_API_KEY=" local.properties || grep -q "YOUTUBE_API_KEY=$" local.properties; then
    print_warning "Clé API YouTube non configurée dans local.properties"
    print_warning "La compilation continuera, mais l'app ne pourra pas ajouter de vidéos."
    echo ""
    echo "Pour ajouter votre clé API, éditez local.properties et ajoutez :"
    echo "YOUTUBE_API_KEY=VotreCléAPIIci"
    echo ""
    read -p "Continuer quand même ? (o/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Oo]$ ]]; then
        exit 1
    fi
else
    print_success "Clé API YouTube configurée"
fi

# Nettoyer les builds précédents
print_step "Nettoyage des builds précédents..."
rm -rf app/build
rm -rf build
print_success "Nettoyage effectué"

# Rendre gradlew exécutable
chmod +x gradlew

# Télécharger les dépendances
print_step "Téléchargement des dépendances Gradle..."
./gradlew --refresh-dependencies

# Compilation
print_step "Compilation de l'APK Debug..."
./gradlew assembleDebug

# Vérifier si l'APK a été généré
APK_PATH="app/build/outputs/apk/debug/app-debug.apk"

if [ -f "$APK_PATH" ]; then
    print_success "APK généré avec succès !"
    echo ""
    echo "=========================================="
    echo -e "${GREEN}✓ COMPILATION RÉUSSIE${NC}"
    echo "=========================================="
    echo ""
    echo "📦 APK généré : $APK_PATH"

    # Afficher la taille de l'APK
    APK_SIZE=$(du -h "$APK_PATH" | cut -f1)
    echo "📊 Taille de l'APK : $APK_SIZE"

    echo ""
    echo "🚀 Pour installer l'APK sur votre appareil :"
    echo ""
    echo "  Option 1 - Via USB (Débogage USB activé) :"
    echo "    adb install -r $APK_PATH"
    echo ""
    echo "  Option 2 - Transfert manuel :"
    echo "    1. Copier le fichier $APK_PATH sur votre appareil"
    echo "    2. Ouvrir le fichier APK sur l'appareil"
    echo "    3. Autoriser l'installation depuis sources inconnues"
    echo ""

    # Proposer l'installation automatique si ADB est disponible
    if command -v adb &> /dev/null; then
        # Vérifier si un appareil est connecté
        DEVICE_COUNT=$(adb devices | grep -v "List" | grep "device" | wc -l)

        if [ "$DEVICE_COUNT" -gt 0 ]; then
            echo -e "${YELLOW}Un appareil Android a été détecté.${NC}"
            read -p "Voulez-vous installer l'APK maintenant ? (o/n) " -n 1 -r
            echo
            if [[ $REPLY =~ ^[Oo]$ ]]; then
                print_step "Installation de l'APK..."
                adb install -r "$APK_PATH"
                if [ $? -eq 0 ]; then
                    print_success "Application installée avec succès !"
                else
                    print_error "Erreur lors de l'installation."
                fi
            fi
        fi
    fi
else
    print_error "L'APK n'a pas été généré. Vérifiez les erreurs ci-dessus."
    exit 1
fi

echo ""
echo "=========================================="
echo "  Merci d'utiliser YouTube Kids App!"
echo "=========================================="
