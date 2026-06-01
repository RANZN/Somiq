#!/bin/zsh
# Run iOS app on simulator — works on any Mac with Xcode installed.
# Compatible with Android Studio, IntelliJ IDEA, and the command line.

set -e

# ─── Resolve project root (script lives in iosApp/) ─────────────────
PROJECT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
XCODEPROJ="$PROJECT_DIR/iosApp/iosApp.xcodeproj"
SCHEME="iosApp"
CONFIGURATION="Debug"

# ─── Auto-detect Xcode Developer directory ───────────────────────────
find_xcode_developer_dir() {
    # 1. Check standard path
    if [ -d "/Applications/Xcode.app/Contents/Developer" ]; then
        echo "/Applications/Xcode.app/Contents/Developer"; return 0
    fi
    # 2. Check any versioned Xcode (e.g. Xcode_16.app)
    for app in /Applications/Xcode*.app; do
        if [ -d "$app/Contents/Developer" ]; then
            echo "$app/Contents/Developer"; return 0
        fi
    done
    # 3. xcode-select (only if it points to a full Xcode, not CommandLineTools)
    local sel
    sel=$(xcode-select -p 2>/dev/null || true)
    if [ -n "$sel" ] && [[ "$sel" != *"CommandLineTools"* ]] && [ -d "$sel" ]; then
        echo "$sel"; return 0
    fi
    return 1
}

DEVELOPER_DIR=$(find_xcode_developer_dir) || {
    echo "❌ Xcode not found. Install Xcode from the App Store, then rerun."
    exit 1
}
export DEVELOPER_DIR
echo "✔ Xcode: $DEVELOPER_DIR"

# ─── Auto-detect best available iPhone simulator ──────────────────────
SIMULATOR_NAME=""
SIMULATOR_UDID=""

find_simulator() {
    local preferred=(
        "iPhone 17 Pro Max" "iPhone 17 Pro" "iPhone 17"
        "iPhone 16 Pro Max" "iPhone 16 Pro" "iPhone 16"
        "iPhone 15 Pro Max" "iPhone 15 Pro" "iPhone 15"
        "iPhone 14 Pro" "iPhone 14"
    )
    for name in "${preferred[@]}"; do
        local udid
        udid=$(xcrun simctl list devices available 2>/dev/null \
            | grep "$name (" | head -1 | grep -oE '[A-F0-9-]{36}' || true)
        if [ -n "$udid" ]; then
            SIMULATOR_NAME="$name"
            SIMULATOR_UDID="$udid"
            return 0
        fi
    done
    # Fallback: first available iPhone simulator
    local line
    line=$(xcrun simctl list devices available 2>/dev/null | grep "iPhone" | head -1 || true)
    SIMULATOR_UDID=$(echo "$line" | grep -oE '[A-F0-9-]{36}' || true)
    SIMULATOR_NAME=$(echo "$line" | sed 's/ (.*//;s/^[[:space:]]*//' || true)
    [ -n "$SIMULATOR_UDID" ]
}

find_simulator || {
    echo "❌ No iPhone simulator found."
    echo "   Open Xcode → Settings → Platforms and download an iOS simulator."
    exit 1
}
echo "✔ Simulator: $SIMULATOR_NAME ($SIMULATOR_UDID)"

# ─── Boot simulator ──────────────────────────────────────────────────
echo "── Booting simulator..."
xcrun simctl boot "$SIMULATOR_UDID" 2>/dev/null || true
open -a Simulator 2>/dev/null || true

# ─── Build with xcodebuild (triggers KMP Gradle build phase) ─────────
echo "── Building (this also compiles the KMP shared framework)..."
xcodebuild \
    -project "$XCODEPROJ" \
    -scheme "$SCHEME" \
    -configuration "$CONFIGURATION" \
    -destination "platform=iOS Simulator,id=$SIMULATOR_UDID" \
    ONLY_ACTIVE_ARCH=YES \
    build

# ─── Find the built .app bundle ──────────────────────────────────────
BUILT_PRODUCTS_DIR=$(xcodebuild \
    -project "$XCODEPROJ" \
    -scheme "$SCHEME" \
    -configuration "$CONFIGURATION" \
    -destination "platform=iOS Simulator,id=$SIMULATOR_UDID" \
    -showBuildSettings 2>/dev/null \
    | awk '/^ *BUILT_PRODUCTS_DIR /{print $3}')

APP_PATH=$(find "$BUILT_PRODUCTS_DIR" -name "*.app" -maxdepth 1 -type d 2>/dev/null | head -1)
if [ -z "$APP_PATH" ]; then
    echo "❌ Could not find .app in: $BUILT_PRODUCTS_DIR"
    exit 1
fi

BUNDLE_ID=$(defaults read "$APP_PATH/Info.plist" CFBundleIdentifier)
echo "✔ Built: $APP_PATH"
echo "✔ Bundle ID: $BUNDLE_ID"

# ─── Install & launch ────────────────────────────────────────────────
echo "── Installing on simulator..."
xcrun simctl install "$SIMULATOR_UDID" "$APP_PATH"

echo "── Launching $BUNDLE_ID..."
xcrun simctl launch "$SIMULATOR_UDID" "$BUNDLE_ID"

echo ""
echo "✅ App is running on: $SIMULATOR_NAME"
