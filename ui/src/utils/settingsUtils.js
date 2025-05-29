import {findByKey} from "./listUtils";

export function getImagePathFromSettings(settings, type) {
    const setting = findByKey(settings, "name", type);

    return setting != null ? setting.val : setting;
}