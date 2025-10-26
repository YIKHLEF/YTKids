#!/bin/bash
# Créer une icône PNG simple de couleur rouge/orange (#FF5722) 
# En utilisant une image PNG encodée en base64

# PNG 1x1 rouge/orange (#FF5722)
BASE64_PNG="iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8DwHwAFBQIAX8jx0gAAAABJRU5ErkJggg=="

# Tailles requises
SIZES=(72 96 128 144 152 192 384 512)

for size in "${SIZES[@]}"; do
    echo "$BASE64_PNG" | base64 -d > "icon-${size}x${size}.png"
    echo "Créé: icon-${size}x${size}.png"
done

echo "Toutes les icônes ont été créées!"
