#!/bin/sh

MVN_VERSION=$(mvn -q \
    -Dexec.executable=echo \
    -Dexec.args='${project.version}' \
    --non-recursive \
    exec:exec)

echo Updating build app version: $MVN_VERSION

script_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"

template_file="$script_dir/templates/build_app.xml"
build_file="$script_dir/build_app.xml"
version='value="version"'
updateVersion='value="'$MVN_VERSION'"'

runtime='dir="runtime"'
updatedRuntime=$1

#echo Temp: $template_file
#echo Build: $build_file

rm $build_file &> /dev/null
cp $template_file $build_file
sed -i 's/'$version'/'$updateVersion'/g' $template_file
sed -i 's/'$runtime'/'$updatedRuntime'/g' $template_file

ant -buildfile $build_file

if [ $? != 0 ]
then
echo "Ant failed, exiting"
  exit 2
fi

sleep 1

APP_NAME=$(mvn -q \
   -Dexec.executable=echo \
   -Dexec.args='${project.name}' \
   --non-recursive \
   exec:exec)

sed -i '' '/\/dict/i \
<key>NSHighResolutionCapable</key> \
<true/>
' release/${APP_NAME}.app/Contents/Info.plist
