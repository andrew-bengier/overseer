const axios = require('axios');

export async function getApiInfo() {
  return await axios.get('/api/info');
}
