#!/usr/bin/env bash
set -e

ROOT=$(git rev-parse --show-toplevel)

cd "$ROOT"

TEMP_DIR=$(mktemp -d 2>/dev/null || mktemp -d -t 'TEMP_DIR')
trap "{ rm -rf ${TEMP_DIR}; }" EXIT

function _generate_docs() {
  ./gradlew install
  ./gradlew dokka

  # Generate all the docs
  ./gradlew :docs:orchidDeploy -PorchidEnvironment=prod

  mv docs/build/docs/orchid/* "$TEMP_DIR"
}

function _git_commit() {
  git stash
  git fetch
  git checkout gh-pages

  cp -r $TEMP_DIR/* .

  git add .
  git commit -m "Update documentation." || {
    # Nothing to commit
    exit 0
  }
  git push origin
}

_generate_docs
_git_commit
