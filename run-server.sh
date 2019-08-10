#!/usr/bin/env bash

# Region - Constants
BASE_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

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
# EndRegion - Constants

# Region - Methods
log_d() {
    printf "${CONFIG_COLOR_CYAN}[FileAndroid][Server]${CONFIG_COLOR_RESET} $1\n"
}
log_jump() {
    printf "\n"
}
log_delimiter() {
    printf "${CONFIG_COLOR_RED}~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~${CONFIG_COLOR_RESET}\n"
}
# EndRegion - Methods

log_jump
log_jump

log_delimiter
log_d "Server!!!"
log_delimiter

log_jump
log_jump

log_d "Script base directory: $BASE_DIR"
log_jump

pushd "$BASE_DIR"

    log_jump

    log_d "Remove file $BASE_DIR/build/file-server.jar"
    rm -f "./build/file-server.jar"
    log_jump

    bash ./gradlew server:fatJar

    if [ -d "$BASE_DIR/build/static" ]; then
        log_d "Pull portfolio GitHub project"
        pushd "$BASE_DIR/build/static"
            git pull
        popd
    else
        log_d "Clone portfolio GitHub project"
        mkdir -p "$BASE_DIR/build"
        pushd "$BASE_DIR/build"
            git clone https://github.com/Mercandj/mercandj.github.io.git
            mv mercandj.github.io static
        popd
    fi

    pushd "$BASE_DIR/build/static"

        if [ -d "$BASE_DIR/build/static/timothe" ]; then
            log_d "Pull Timothe portfolio GitHub project"
            pushd "$BASE_DIR/build/static/timothe"
                git pull
            popd
        else
            log_d "Clone Timothe portfolio GitHub project"
            git clone https://github.com/Mercandj/timothe.git
        fi

        if [ -d "$BASE_DIR/build/static/1418" ]; then
            log_d "Pull 1418 GitHub project"
            pushd "$BASE_DIR/build/static/1418"
                git pull
            popd
        else
            log_d "Clone 1418 GitHub project"
            git clone https://github.com/Mercandb/1418.git
        fi

    popd

    java -jar ./build/file-server.jar

popd

