export async function checkAppRequirements() {
    // Check base application requirements:
    // - apikey -> tmdb
    // - at least one media server -> plex
    // -
    // return await getApiKeys({"name": "TMDB"}).then(response => {
    //     if (response.data?.length === 0) {
    //         return "TMDB API key not found";
    //     }
    // });

    return "TMDB API key not found";
}
