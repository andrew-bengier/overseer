import fs from 'fs';
import path from 'path';

const appDirectory = process.cwd();

const translationPath = path.resolve(appDirectory, 'src/i18n/');

const tempMessages = fs.readFileSync(path.resolve(translationPath, 'temp/temp-messages.json'));
const tempTranslations = fs.readFileSync(path.resolve(translationPath, 'temp/temp-translations.en.json'));

const extractedMessages = JSON.parse(tempMessages);
const extractedTempTranslations = JSON.parse(tempTranslations);
const translations = {};

Object.keys(extractedMessages).forEach(key => {
  const message = extractedMessages[key].defaultMessage;
  const description = extractedTempTranslations[key + '__comment'] ?? extractedMessages[key].description;

  translations[key] = message;
  translations[key + '__comment'] = description ?? '';
});

fs.writeFileSync(path.resolve(translationPath, 'translations.en.json'), JSON.stringify(translations, null, 2))
