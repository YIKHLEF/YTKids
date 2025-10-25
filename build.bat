@echo off
REM Script de compilation pour YouTube Kids Android App (Windows)
REM Auteur: Claude Code
REM Date: 2025-10-25

setlocal enabledelayedexpansion

echo ==========================================
echo   YouTube Kids - Script de Compilation
echo ==========================================
echo.

REM Vérifier Java
java -version >nul 2>&1
if errorlevel 1 (
    echo [ERREUR] Java n'est pas installé ou n'est pas dans le PATH
    echo Veuillez installer JDK 17 ou supérieur
    exit /b 1
) else (
    echo [OK] Java trouvé
)

REM Vérifier ANDROID_HOME
if "%ANDROID_HOME%"=="" (
    echo [ATTENTION] ANDROID_HOME n'est pas défini
    echo Tentative de détection automatique...

    REM Chemins courants du SDK Android sur Windows
    set SDK_PATH=%LOCALAPPDATA%\Android\Sdk
    if exist "!SDK_PATH!" (
        set ANDROID_HOME=!SDK_PATH!
        echo [OK] SDK Android trouvé : !ANDROID_HOME!
    ) else (
        echo [ERREUR] SDK Android introuvable
        echo Veuillez installer Android Studio
        exit /b 1
    )
) else (
    echo [OK] SDK Android : %ANDROID_HOME%
)

REM Vérifier local.properties
if not exist "local.properties" (
    echo [ERREUR] Fichier local.properties manquant !
    exit /b 1
)

findstr /C:"YOUTUBE_API_KEY=" local.properties >nul
if errorlevel 1 (
    echo [ATTENTION] Clé API YouTube non configurée
    echo Pour ajouter votre clé API, éditez local.properties et ajoutez :
    echo YOUTUBE_API_KEY=VotreCléAPIIci
    echo.
    set /p CONTINUE="Continuer quand même ? (o/n): "
    if /i not "!CONTINUE!"=="o" exit /b 1
) else (
    echo [OK] Clé API YouTube configurée
)

REM Nettoyer les builds précédents
echo.
echo [ÉTAPE] Nettoyage des builds précédents...
if exist "app\build" rd /s /q "app\build"
if exist "build" rd /s /q "build"
echo [OK] Nettoyage effectué

REM Compilation
echo.
echo [ÉTAPE] Compilation de l'APK Debug...
call gradlew.bat assembleDebug

if errorlevel 1 (
    echo.
    echo [ERREUR] La compilation a échoué
    exit /b 1
)

REM Vérifier si l'APK a été généré
set APK_PATH=app\build\outputs\apk\debug\app-debug.apk

if exist "%APK_PATH%" (
    echo.
    echo ==========================================
    echo   COMPILATION RÉUSSIE
    echo ==========================================
    echo.
    echo APK généré : %APK_PATH%

    REM Afficher la taille
    for %%A in ("%APK_PATH%") do set APK_SIZE=%%~zA
    set /a APK_SIZE_MB=!APK_SIZE! / 1048576
    echo Taille de l'APK : !APK_SIZE_MB! MB

    echo.
    echo Pour installer l'APK sur votre appareil :
    echo.
    echo   Option 1 - Via USB (Débogage USB activé) :
    echo     adb install -r %APK_PATH%
    echo.
    echo   Option 2 - Transfert manuel :
    echo     1. Copier le fichier %APK_PATH% sur votre appareil
    echo     2. Ouvrir le fichier APK sur l'appareil
    echo     3. Autoriser l'installation depuis sources inconnues
    echo.

    REM Proposer l'installation si ADB est disponible
    adb version >nul 2>&1
    if not errorlevel 1 (
        adb devices | find "device" >nul
        if not errorlevel 1 (
            echo Un appareil Android a été détecté.
            set /p INSTALL="Voulez-vous installer l'APK maintenant ? (o/n): "
            if /i "!INSTALL!"=="o" (
                echo.
                echo [ÉTAPE] Installation de l'APK...
                adb install -r "%APK_PATH%"
                if not errorlevel 1 (
                    echo [OK] Application installée avec succès !
                ) else (
                    echo [ERREUR] Erreur lors de l'installation
                )
            )
        )
    )
) else (
    echo.
    echo [ERREUR] L'APK n'a pas été généré
    exit /b 1
)

echo.
echo ==========================================
echo   Merci d'utiliser YouTube Kids App!
echo ==========================================

endlocal
