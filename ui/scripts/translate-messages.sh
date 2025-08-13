#!/bin/bash

# Check files for supported languages
supported_languages=(
    "en" 
    # "es" 
    # "fr" "de" "it" "uk" "hi" "bn" "ur"
)

appDirectory=$(PWD)
translationsDiretory="$appDirectory/src/i18n/"

fullMode=false

if [[ "$@" =~ "--full" ]]; then
  echo "Full mode enabled."
  fullMode=true
fi

# [TEST]
# echo "App Directory: $appDirectory"
# echo "Translation Directory: $translationsDiretory"
# echo "Supported Languages: ${supported_languages[*]}"

for language in "${supported_languages[@]}"; do
    translationFile="$translationsDiretory/translations.$language.json"
    
    if [[ ! -f "$translationFile" ]]; then
        echo "{}" > "$translationFile"
    fi
done

if [[ ${#supported_languages[@]} -gt 1 ]]; then
    node "$appDirectory/scripts/i18n/translateMessages.mjs" "$fullMode"
fi

echo "Translations updated successfully."
