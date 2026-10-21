#!/bin/bash
# Fetch the latest Kotlin grammar from the kotlin-spec develop branch, copy it into this project and build.
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
SPEC_DIR="$ROOT/spec-tools/kotlin-spec"
TARGET="$ROOT/src/main/antlr/io/jumpco/open/kfsm/viz/grammar"

mkdir -p "$ROOT/spec-tools"
if [ -d "$SPEC_DIR/.git" ]; then
  git -C "$SPEC_DIR" fetch origin develop
  git -C "$SPEC_DIR" checkout develop
  git -C "$SPEC_DIR" pull --ff-only origin develop
else
  git clone --branch develop https://github.com/Kotlin/kotlin-spec.git "$SPEC_DIR"
fi

mkdir -p "$TARGET"
cp -v "$SPEC_DIR"/grammar/src/main/antlr/* "$TARGET"/
git -C "$SPEC_DIR" rev-parse HEAD > "$TARGET/KOTLIN_SPEC_COMMIT"

cd "$ROOT"
chmod a+x ./gradlew
./gradlew --no-daemon clean build
