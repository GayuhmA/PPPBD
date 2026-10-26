#!/usr/bin/env bash
set -euo pipefail

project_dir="$(cd -- "$(dirname -- "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$project_dir"

sdk_dir="${ANDROID_HOME:-${ANDROID_SDK_ROOT:-}}"
if [[ -z "$sdk_dir" && -f local.properties ]]; then
    sdk_dir="$(sed -n 's/^sdk\.dir=//p' local.properties | tr -d '\r')"
fi
if [[ -z "$sdk_dir" ]]; then
    for candidate in "$HOME/Android/Sdk" "$HOME/Library/Android/sdk"; do
        if [[ -d "$candidate" ]]; then
            sdk_dir="$candidate"
            break
        fi
    done
fi
if [[ -z "$sdk_dir" || ! -d "$sdk_dir" ]]; then
    echo 'Android SDK tidak ditemukan. Isi sdk.dir di local.properties atau set ANDROID_HOME.' >&2
    exit 1
fi
export ANDROID_HOME="$sdk_dir"
adb_bin="$sdk_dir/platform-tools/adb"
if [[ ! -x "$adb_bin" ]]; then
    echo "ADB tidak ditemukan: $adb_bin. Pasang Android SDK Platform-Tools." >&2
    exit 1
fi

case "${1:-help}" in
    devices)
        "$adb_bin" devices -l
        ;;
    emulator)
        emulator_bin="$sdk_dir/emulator/emulator"
        if [[ ! -x "$emulator_bin" ]]; then
            echo 'Android Emulator belum terpasang di SDK.' >&2
            exit 1
        fi
        avd_name="${2:-${ANDROID_AVD:-}}"
        if [[ -z "$avd_name" ]]; then
            mapfile -t avds < <("$emulator_bin" -list-avds | sed '/^[[:space:]]*$/d')
            if [[ "${#avds[@]}" -ne 1 ]]; then
                echo 'Pilih AVD: bash scripts/android.sh emulator NAMA_AVD' >&2
                "$emulator_bin" -list-avds
                exit 1
            fi
            avd_name="${avds[0]}"
        fi
        exec "$emulator_bin" -avd "$avd_name"
        ;;
    run)
        serial="${2:-${ANDROID_SERIAL:-}}"
        if [[ -z "$serial" ]]; then
            mapfile -t devices < <("$adb_bin" devices | awk 'NR > 1 && $2 == "device" { print $1 }')
            if [[ "${#devices[@]}" -ne 1 ]]; then
                echo 'Hubungkan satu perangkat yang siap, atau tentukan serial:' >&2
                echo 'bash scripts/android.sh run SERIAL' >&2
                "$adb_bin" devices -l
                exit 1
            fi
            serial="${devices[0]}"
        fi
        if [[ "$("$adb_bin" -s "$serial" get-state)" != "device" ]]; then
            echo "Perangkat $serial belum siap. Periksa USB debugging/emulator." >&2
            exit 1
        fi
        if [[ "$("$adb_bin" -s "$serial" shell getprop sys.boot_completed | tr -d '\r')" != "1" ]]; then
            echo 'Android masih booting. Tunggu sampai layar utama muncul, lalu jalankan lagi.' >&2
            exit 1
        fi
        bash ./gradlew :app:assembleDebug --console=plain --max-workers=2 --no-parallel \
            '-Dorg.gradle.jvmargs=-Xmx1024m -XX:MaxMetaspaceSize=512m -Dfile.encoding=UTF-8'
        "$adb_bin" -s "$serial" install -r app/build/outputs/apk/debug/app-debug.apk
        "$adb_bin" -s "$serial" shell am start -W -n com.example.intentapp/.MainActivity
        ;;
    *)
        echo 'Pemakaian: bash scripts/android.sh {devices|emulator [NAMA_AVD]|run [SERIAL]}'
        ;;
esac
