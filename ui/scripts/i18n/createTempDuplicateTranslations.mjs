import fs from 'fs';
import path from 'path';

const appDirectory = process.cwd();

const translationPath = path.resolve(appDirectory, 'src/i18n/');
const tempPath = path.resolve(translationPath, 'temp');

if (!fs.existsSync(tempPath)) {
    fs.mkdirSync(tempPath);
}

// Creating duplicate of the main translations file
fs.copyFileSync(path.resolve(translationPath, 'translations.en.json'), path.resolve(translationPath, 'temp/temp-translations.en.json'));
