#!/usr/bin/env bash

# Region - Color
CONFIG_COLOR_BOLD=$(tput bold)
CONFIG_COLOR_RED=`tput setaf 1`
CONFIG_COLOR_GREEN=`tput setaf 2`
CONFIG_COLOR_GREEN_LIGHT="$CONFIG_COLOR_BOLD$CONFIG_COLOR_GREEN"
CONFIG_COLOR_CYAN=`tput setaf 6`
CONFIG_COLOR_GRAY=`tput setaf 7`
CONFIG_COLOR_FAWN=$(tput setaf 3); CONFIG_COLOR_BEIGE="$CONFIG_COLOR_FAWN"
CONFIG_COLOR_YELLOW="$CONFIG_COLOR_BOLD$CONFIG_COLOR_FAWN"
CONFIG_COLOR_PURPLE=$(tput setaf 5);
CONFIG_COLOR_PINK="$CONFIG_COLOR_BOLD$CONFIG_COLOR_PURPLE"
CONFIG_COLOR_DARKCYAN=$(tput setaf 6)
CONFIG_COLOR_CYAN="$CONFIG_COLOR_BOLD$CONFIG_COLOR_DARKCYAN"
CONFIG_COLOR_RESET=`tput sgr0`
# EndRegion - Color


log_d() {
    printf "${CONFIG_COLOR_CYAN}[FileAndroid][Ktor]${CONFIG_COLOR_RESET} $1\n"
}
log_jump() {
    printf "\n"
}
log_line() {
    printf "${CONFIG_COLOR_RED}-------------------------------------------------------------------------------------------${CONFIG_COLOR_RESET}\n"
}


log_jump
log_jump

log_line
log_d "Server ktor!!!"
log_line

log_jump
log_jump

BASEDIR=$(dirname "$0")
log_d "Script base directory: $BASEDIR"
log_jump

pushd "$BASEDIR"

    log_jump

    log_d "Remove folder $BASEDIR/build"
    rm -r "./build"
    log_jump

    log_d "Remove folder $BASEDIR/app/build"
    rm -r "./app/build"
    log_jump

    log_d "Remove folder $BASEDIR/app/out"
    rm -r "./app/out"
    log_jump

    bash ./gradlew app:fatJar

    pushd build
        git clone https://github.com/Mercandj/mercandj.github.io.git
        mv mercandj.github.io static
    popd

    java -jar ./build/file-ktor.jar

popd

