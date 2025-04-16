import {viewableFileExtensions} from "./formatUtils";

export function scrubRoutePath(routePath) {
    if (routePath) {
        return routePath.replace(/\s+/g, '');
    } else {
        return '';
    }
}

export function shortenSha(sha) {
    return sha.substring(0, 7);
}

export function formatStringToMarkdown(string) {
    return string.replaceAll("\r\n", "  \r\n");
}

export function isFileViewable(fileName) {
    return viewableFileExtensions.includes(fileName.slice((fileName.lastIndexOf(".") - 1 >>> 0) + 2));
}