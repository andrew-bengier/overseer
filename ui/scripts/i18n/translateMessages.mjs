import fs from 'fs';
import path from 'path';
import util from 'util';
import {exec as execCallback} from 'child_process';
const exec = util.promisify(execCallback);

const mode = process.argv[2] && process.argv[2] !== 'false' ? process.argv[2].trim() : 'default';

const appDirectory = process.cwd();

const translationsPath = path.resolve(appDirectory, 'src/i18n/');
const baseTranslationFilename = 'translations.en.json';
const translationFileName = 'translations';

function printMap(map) {
    for (const key in map) {
        if (Object.prototype.hasOwnProperty.call(map, key)) {
            const value = map[key];
            // Here you can process the key-value pairs as needed
            console.log(`Key: ${key}, Value: ${value}`);
        }
    }
}

function getLanguageFromFilename(filename) {
    const match = filename.match(/translations\.(\w+)\.json/);
    if (match) {
        return match[1];
    }
    return null;
}

function writeToFile(filePath, filename, data) {
    fs.writeFile(filePath, data, (err) => {
        if (err) {
            console.error('Error writing file:', err);
            return;
        }
        // [TEST]
        // console.log('Messages updated successfully in', filename);
    });
}

function jsonToMap(jsonData) {
    const map = new Map();
    for (const key in jsonData) {
        if (Object.prototype.hasOwnProperty.call(jsonData, key)) {
            const value = jsonData[key];
            // console.log(`Key: ${key}`);
            // console.log(`Value: ${value}`);
            map.set(key, value);
        }
    }

    return map;
}

function getMissingKeys(baseMessages, translationMessages) {
    const missingKeys = [];
    for (const key of baseMessages.keys()) {
        if (!translationMessages.has(key) || translationMessages.get(key) === "") {
            // [TEST]
            console.log(`Missing key: ${key}`);
            missingKeys.push(key);
        }
    }
    return missingKeys;
}

function getBaseMessages() {
    try {
        const data = fs.readFileSync(path.join(translationsPath, baseTranslationFilename), 'utf8');
        return jsonToMap(JSON.parse(data));
    } catch (parseError) {
        console.error(`Error parsing JSON from ${baseTranslationFilename}:`, parseError);
    }
}

function getMessagesToTranslate(filePath, baseMessages) {
    // From NON base language files
    const data = fs.readFileSync(filePath, 'utf8');
    let translationMessages = jsonToMap(JSON.parse(data));

    if (mode === 'default') {
        const missingStrings = getMissingKeys(baseMessages, translationMessages);
        for (const key of missingStrings) {
            translationMessages.set(key, "");
        }
    } else {
        // Full mode - replaces all keys
        translationMessages = new Map();
        for (const [key, value] of baseMessages) {
            translationMessages.set(key, "");
        }
    }

    return translationMessages;
}

async function translateMessage(message, language) {
    // TODO: this is a functional POC - it should be replaced with a proper translation service
    const url = `https://translate.googleapis.com/translate_a/single?client=gtx&sl=en&tl=${language}&dt=t&q=${message}`;
    const browser = 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36';

    try {
        const { stdout, stderr } = await exec(`curl -sA "${browser}" "${url}"`);
        if (stderr) {
            console.error(`Error: ${stderr}`);
            return "";
        }

        // console.log(`Output: ${JSON.parse(stdout)}`);
        const translation = JSON.parse(stdout)[0][0][0];

        // console.log(`Translation for "${message}" in ${language}: ${translation}`);
        return translation;
    } catch (error) {
        console.error(`Execution failed: ${error.message}`);
        return "";
    }
}

async function processMessages(baseMessages, translationMessages, language) {
    // [TEST]
    // console.log(`Total Messages: ${translationMessages.size}`);
    let missingCount = 0;

    for (const [key, value] of translationMessages) {
        if (value === "") {                        
            missingCount++;
            const baseMessage = baseMessages.get(key).replace(/ /g, '+');
            // [TEST]
            //console.log(`Translating message: ${baseMessage} to ${language}`);
    
            const translation = await translateMessage(baseMessage, language);
            translationMessages.set(key, translation);
            // [TEST]
            // console.log(`Translated message: ${translation}`);
        }
    }

    // [TEST]
    // console.log(`Updated Messages: ${missingCount}`);

    return translationMessages;
}

let baseMessages = getBaseMessages();

fs.readdir(translationsPath, (err, files) => {
    if (err) {
        console.error('Error reading translations directory:', err);
        return;
    }

    files.forEach(file => {
        if (file !== baseTranslationFilename && file.startsWith(translationFileName) && file.endsWith('.json')) {
            const language = getLanguageFromFilename(file);
            console.log(`Processing file: ${file}`);
            const filePath = path.join(translationsPath, file);
            
            let translationMessages = getMessagesToTranslate(filePath, baseMessages);
 
            processMessages(baseMessages, translationMessages, language).then((response) => {
                // [TEST]
                // console.log(`Writing updated messages to ${filePath}`);
                // for (const [key, value] of response) {
                //     console.log(`Key: ${key}, Value: ${value}`);
                // }
                writeToFile(filePath, file, JSON.stringify(Object.fromEntries(translationMessages), null, 2));
            });
        }
    });
});

