// TODO: update this for better handling
import {getImagePathFromSettings} from "./settingsUtils";

export const getImageUrlFromPlex = (settings, apiKey) => {
    if (apiKey.name === "PLEX") {
        const imageUrl = getImagePathFromSettings(settings, "thumb");
        if (imageUrl != null) {
            return apiKey.url + imageUrl + '?X-Plex-Token=' + apiKey.key;
        } else {
            return null;
        }
    } else {
        return null;
    }
}