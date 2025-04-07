export function scrubRoutePath(routePath) {
    if (routePath) {
        return routePath.replace(/\s+/g, '');
    } else {
        return '';
    }
}