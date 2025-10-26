/**
 * Vercel Serverless Function - YouTube API Proxy
 *
 * Cette fonction proxy les appels à l'API YouTube pour protéger la clé API.
 * La clé API est stockée dans les variables d'environnement Vercel.
 */

const YOUTUBE_API_BASE = 'https://www.googleapis.com/youtube/v3';

/**
 * Handler principal
 */
module.exports = async (req, res) => {
  // Configuration CORS
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type');

  // Gérer les requêtes OPTIONS (preflight)
  if (req.method === 'OPTIONS') {
    return res.status(200).end();
  }

  // Vérifier que la clé API est configurée
  const API_KEY = process.env.YOUTUBE_API_KEY;
  if (!API_KEY) {
    return res.status(500).json({
      error: 'YouTube API key not configured',
      message: 'YOUTUBE_API_KEY environment variable is missing'
    });
  }

  // Extraire les paramètres de la requête
  const { endpoint, ...params } = req.query;

  if (!endpoint) {
    return res.status(400).json({
      error: 'Missing endpoint parameter',
      message: 'Please provide an endpoint (videos, playlistItems, etc.)'
    });
  }

  try {
    // Construire l'URL de l'API YouTube
    const queryParams = new URLSearchParams({
      ...params,
      key: API_KEY
    });

    const url = `${YOUTUBE_API_BASE}/${endpoint}?${queryParams.toString()}`;

    console.log(`[YouTube API] Calling: ${endpoint}`);

    // Faire l'appel à l'API YouTube
    const response = await fetch(url);
    const data = await response.json();

    // Vérifier les erreurs de l'API
    if (!response.ok) {
      console.error(`[YouTube API] Error:`, data);
      return res.status(response.status).json({
        error: 'YouTube API error',
        details: data
      });
    }

    // Retourner les données
    return res.status(200).json(data);

  } catch (error) {
    console.error(`[YouTube API] Exception:`, error);
    return res.status(500).json({
      error: 'Internal server error',
      message: error.message
    });
  }
};
