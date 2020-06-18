#!/usr/bin/env bash
set -e

ROOT=$(git rev-parse --show-toplevel)

cd "$ROOT"

temp_dir=$(mktemp -d 2>/dev/null || mktemp -d -t 'temp_dir')
trap "{ rm -rf ${temp_dir}; }" EXIT

# Generate all the docs
./gradlew :docs:orchidDeploy -PorchidEnvironment=prod

mv docs/build/docs/orchid/* "$temp_dir"

git stash
git fetch
git checkout gh-pages

mv $temp_dir/* .

git add .
git commit -m "Update documentation."
git push origin
