import fs from 'fs';
import path from 'path';

const appDirectory = process.cwd();
const translationsPath = path.resolve(appDirectory, 'src/i18n/');

const filesWithTranslations = [];
const keysOfUpdatedMessages = [];

const newMessages = JSON.parse(fs.readFileSync(path.resolve(translationsPath, 'translations.en.json')));
const previousMessages = JSON.parse(fs.readFileSync(path.resolve(translationsPath, 'temp/temp-translations.en.json')));
const newMessagesKeys = Object.keys(newMessages);

// get all files with translations
fs.readdirSync(translationsPath).forEach((fileName) => {
    const filePath = path.join(translationsPath, fileName);

    if ((path.extname(fileName) === '.json') && fileName !== 'translations.en.json') {
        const messages = JSON.parse(fs.readFileSync(path.resolve(appDirectory, filePath)));
        filesWithTranslations.push({fileName, messages});
    }
});

// find updated messages with existing keys and add them into keysOfUpdatedMessages
Object.keys(newMessages).forEach((key) => {
    if (newMessages[key] !== previousMessages[key]) {
        keysOfUpdatedMessages.push(key)
    }
});

// rewrite existing messages with
filesWithTranslations.forEach(({fileName, messages}) => {
    const newIntlTranslations = {};
    newMessagesKeys.forEach(newTranslationsKey => {
        // do not add existing message if message in main translation file was updated
        if (keysOfUpdatedMessages.includes(newTranslationsKey)) {
            return;
        }
        // add existing intl message
        if (Object.prototype.hasOwnProperty.call(messages, newTranslationsKey)) {
            newIntlTranslations[newTranslationsKey] = messages[newTranslationsKey];
        }
    });

    fs.writeFileSync(path.resolve(translationsPath, `${fileName}`), JSON.stringify(newIntlTranslations, null, 2));
})
