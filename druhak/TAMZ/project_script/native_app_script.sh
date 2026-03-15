#!/usr/bin/env bash
set -euo pipefail

usage() {
	cat <<'USAGE'
Usage:
	native_app_script.sh [--expo|--ionic] [--web-ionic] [--serve] <relative_or_absolute_project_path>

Creates a new app at the given path.
Default is Expo with NativeWind + config files.
Use --ionic to create an Ionic React app.
Use --web-ionic to enable Ionic UI components on web builds only (Expo mode).

Options:
	--expo       Use Expo (default)
	--ionic      Use Ionic React (tabs template)
	--web-ionic  Expo only: add Ionic React for web-only UI
	--serve      For Ionic only: run ionic serve after creation
USAGE
}

mode="expo"
ionic_serve="false"
web_ionic="false"
target_input=""

while [[ $# -gt 0 ]]; do
	case "$1" in
		--expo)
			mode="expo"
			shift
			;;
		--ionic)
			mode="ionic"
			shift
			;;
		--web-ionic)
			web_ionic="true"
			shift
			;;
		--serve)
			ionic_serve="true"
			shift
			;;
		-h|--help)
			usage
			exit 0
			;;
		*)
			if [[ -z "$target_input" ]]; then
				target_input="$1"
				shift
			else
				echo "Error: unexpected argument: $1" >&2
				exit 1
			fi
			;;
	esac
done

if [[ -z "$target_input" ]]; then
	usage
	exit 1
fi

if ! command -v npx >/dev/null 2>&1; then
	echo "Error: npx is required but not found." >&2
	exit 1
fi

if ! command -v npm >/dev/null 2>&1; then
	echo "Error: npm is required but not found." >&2
	exit 1
fi

target_path=""
if [[ "$target_input" = /* ]]; then
	target_path="$target_input"
else
	target_path="$PWD/$target_input"
fi

if [[ -e "$target_path" ]]; then
	echo "Error: target path already exists: $target_path" >&2
	exit 1
fi

parent_dir="$(dirname "$target_path")"
if [[ ! -d "$parent_dir" ]]; then
	mkdir -p "$parent_dir"
fi

script_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
templates_dir="$script_dir"

if [[ "$mode" == "expo" ]]; then
	required_templates=(
		"tailwind.config.js"
		"global.css"
		"babel.config.js"
		"metro.config.js"
		"_layout.tsx"
		"nativewind-env.d.ts"
	)

	for f in "${required_templates[@]}"; do
		if [[ ! -f "$templates_dir/$f" ]]; then
			echo "Error: missing template file: $templates_dir/$f" >&2
			exit 1
		fi
	done

	echo "Creating Expo app at: $target_path"
	npx create-expo-app@latest "$target_path"

	cd "$target_path"

	echo "Installing dependencies"
	npm install nativewind react-native-reanimated react-native-safe-area-context
	npm install --save-dev tailwindcss@^3.4.17 prettier-plugin-tailwindcss@^0.5.11 babel-preset-expo

	if [[ "$web_ionic" == "true" ]]; then
		echo "Installing Ionic React for web-only UI"
		npm install @ionic/react @ionic/core ionicons

		echo "Creating web-only layout for Ionic"
		cat <<'EOF' > "$target_path/app/_layout.web.tsx"
import { Stack } from "expo-router";
import { IonApp, setupIonicReact } from "@ionic/react";
import "@ionic/react/css/core.css";
import "@ionic/react/css/normalize.css";
import "@ionic/react/css/structure.css";
import "@ionic/react/css/typography.css";
import "@ionic/react/css/padding.css";
import "@ionic/react/css/float-elements.css";
import "@ionic/react/css/text-alignment.css";
import "@ionic/react/css/text-transformation.css";
import "@ionic/react/css/flex-utils.css";
import "@ionic/react/css/display.css";
import "./global.css";

setupIonicReact();

export default function RootLayout() {
  return (
    <IonApp>
      <Stack />
    </IonApp>
  );
}
EOF
	fi

	echo "Initializing Tailwind config"
	npx tailwindcss init -p

	echo "Copying configuration files"
	cp "$templates_dir/tailwind.config.js" "$target_path/tailwind.config.js"
	cp "$templates_dir/global.css" "$target_path/app/global.css"
	cp "$templates_dir/babel.config.js" "$target_path/babel.config.js"
	cp "$templates_dir/metro.config.js" "$target_path/metro.config.js"
	cp "$templates_dir/_layout.tsx" "$target_path/app/_layout.tsx"
	cp "$templates_dir/nativewind-env.d.ts" "$target_path/nativewind-env.d.ts"

	echo "Done. Project created at: $target_path"
else
	if [[ "$web_ionic" == "true" ]]; then
		echo "Error: --web-ionic is only supported with --expo." >&2
		exit 1
	fi

	if ! command -v ionic >/dev/null 2>&1; then
		echo "Ionic CLI not found. Installing @ionic/cli globally..."
		npm install -g @ionic/cli
	fi

	app_name="$(basename "$target_path")"

	echo "Creating Ionic React app at: $target_path"
	(cd "$parent_dir" && ionic start "$app_name" tabs --type=react --no-interactive --confirm)

	if [[ "$ionic_serve" == "true" ]]; then
		cd "$target_path"
		ionic serve
	else
		echo "Done. Project created at: $target_path"
		echo "Run 'cd "$target_path" && ionic serve' to start the dev server."
	fi
fi
