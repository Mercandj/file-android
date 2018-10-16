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
    printf "${CONFIG_COLOR_CYAN}[Gradle][CI]${CONFIG_COLOR_RESET}$1\n"
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
log_d "CI script!!!"
log_line

log_jump
log_jump


BASEDIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"
log_d "Script base directory: $BASEDIR"
log_jump

pushd "$BASEDIR/../../"

    pwd
    log_d "[app] clean"
    ./gradlew app:clean
    log_d "[app] assembleDebug"
    ./gradlew app:assembleDebug
    log_d "[app] check"
    ./gradlew app:check
    log_d "[app] ktlint"
    ./gradlew ktlint
    log_d "[server] fatJar"
    ./gradlew server:fatJar
    log_d "[server] check"
    ./gradlew server:check

popd