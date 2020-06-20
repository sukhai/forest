#!/usr/bin/env bash

ROOT=$(git rev-parse --show-toplevel)
PROJECT_VERSION_FILE="${ROOT}/gradle/release.properties"
CHANGELOG_DIR="${ROOT}/docs/src/orchid/resources/changelog"

function _get_latest_changelog_content() {
    local latest_changelog_version

    latest_changelog_version=$(find "$CHANGELOG_DIR" -type f -regex ".*\.md" \
        | sed 's/.*\/\(.*\)\.md/\1/' \
        | sort \
        | tail -n 1)

    cat "${CHANGELOG_DIR}/${latest_changelog_version}.md"
}

function _get_latest_changelog_version() {
    local latest_changelog_version

    latest_changelog_version=$(find "$CHANGELOG_DIR" -type f -regex ".*\.md" \
        | sed 's/.*\/\(.*\)\.md/\1/' \
        | sort \
        | tail -n 1)

    echo "$latest_changelog_version"
}

function _has_new_changelog() {
    local latest_changelog_version
    local project_version

    latest_changelog_version=$(_get_latest_changelog_version)
    project_version=$(cat "$PROJECT_VERSION_FILE" \
        | grep -e 'release.version' \
        | sed 's/.*\=\(.*\)$/\1/')

    if [[ "$latest_changelog_version" == "$project_version" ]]; then
        echo false
    else
        echo true
    fi
}

function _update_properties() {
    new_version="$1"
    echo "Updating to version $new_version"

    local release_file="$ROOT/gradle/release.properties"
    local output=""
    output=$(sed "s/release\.version=.*$/release\.version=$new_version/g" "$release_file")
    echo -n "$output" > "$release_file"
}

function main() {
    local get_changelog=false
    local update_project_version=false
    local new_project_version

    while [[ $# -gt 0 ]]; do
        case $1 in
            -c | --changelog)
                get_changelog=true
                shift # past argument
                ;;
            -v | --project-version)
                update_project_version=true
                new_project_version="$2"
                shift # past argument
                shift # past value
                ;;
            *)
                echo "Unknown parameter passed: $1"
                exit 1
                ;;
        esac
    done

    echo "new_project_version: $new_project_version"

    if [[ "$get_changelog" == "true" ]] && [[ "$update_project_version" == "true" ]]; then
        echo "Only one of the two options can be enabled at a time."
        exit 1
    fi

    if [[ "$get_changelog" == "true" ]]; then
        _get_latest_changelog_content
        exit 0
    fi

    if [[ "$update_project_version" == "true" ]]; then
        _update_properties "$new_project_version"
        exit 0
    fi

    if [[ "$(_has_new_changelog)" == "true" ]]; then
        _get_latest_changelog_version
    else
        echo "No new changelog"
        echo ""
    fi
}

main "$@"
