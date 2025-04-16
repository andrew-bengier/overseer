#!/bin/bash
set -e

function cleanupTempFiles () {
  rm -rf './src/i18n/temp'
}

trap 'echo "*** Something went wrong"; cleanupTempFiles' ERR

echo "*** Starting extracting messages..."

# Create previous main file with translations
node --experimental-modules scripts/i18n/createTempDuplicateTranslations.mjs

# Generate main file with translations
npx formatjs extract 'src/**/*.js' --out-file 'src/i18n/temp/temp-messages.json' --id-interpolation-pattern '[sha512:contenthash:hex:24]'

# Compile messages
node --experimental-modules scripts/i18n/compileMessages.mjs

# Update translation files based on the main file
node --experimental-modules scripts/i18n/clearUpdatedMessages.mjs

# Remove temp files
cleanupTempFiles

echo "*** Extracting messages successfully"
