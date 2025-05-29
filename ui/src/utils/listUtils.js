export function findByKey(arr, property, value) {
    for (let i = 0; i < arr.length; i++) {
        if (arr[i][property.toLowerCase()].toLowerCase() === value.toLowerCase()) {
            return arr[i];
        }
    }

    return null;
}