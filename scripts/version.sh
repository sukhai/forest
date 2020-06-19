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

function update_properties() {
    local latest_changelog_version
    latest_changelog_version=$(_get_latest_changelog_version)
    echo "Updating to version $latest_changelog_version"

    local release_file="$ROOT/gradle/release.properties"
    local output=""
    output=$(sed "s/release\.version=.*$/release\.version=$latest_changelog_version/g" "$release_file")
    echo -n "$output" > "$release_file"
}

if [[ "$1" == 'get_latest_changelog_content' ]]; then
    _get_latest_changelog_content
    exit 0
fi

if [[ "$(_has_new_changelog)" == 'true' ]]; then
    _get_latest_changelog_version
else
    echo "No new changelog"
    echo ""
fi
