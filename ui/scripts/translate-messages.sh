#!/bin/bash

# Check files for default supported languages
supported_languages=(
    "en" 
    # "es" 
    # "fr" "de" "it" "uk" "hi" "bn" "ur"
)

appDirectory=$(PWD)
translationsDiretory="$appDirectory/src/i18n/"

fullMode=false

while test $# -gt 0; do
  case "$1" in
    -h|--help)
      echo "$package - attempt to capture frames"
      echo " "
      echo "$package [options] application [arguments]"
      echo " "
      echo "options:"
      echo "-h, --help                show brief help"
      echo "-a, --action=ACTION       specify an action to use"
      echo "-o, --output-dir=DIR      specify a directory to store output in"
      exit 0
      ;;
    -f|--full)
      shift
      echo "Full mode enabled."
      fullMode=true
      shift
      ;;
    -l|--lang)
      shift
      if test $# -gt 0; then
        language=$1
        if [[ ! "$language" = "${supported_languages[*]}" ]]; then
          echo "'${language}' added"
          supported_languages+=(${language})
        else
          echo Language ${language} already supported
        fi
      else
        echo "no language specified"
        exit 1
      fi
      shift
      ;;
    *)
      break
      ;;
  esac
done

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
