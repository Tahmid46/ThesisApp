# ThesisDemoApp – Medicine Helper

An Android application that helps users manage their medications, scan prescriptions, track medical appointments, and access emergency ambulance services. Built as a thesis project using Java and the Android SDK.

---

## Table of Contents

- [About the Project](#about-the-project)
- [Features](#features)
- [Use Cases](#use-cases)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [How to Run](#how-to-run)
- [Project Structure](#project-structure)
- [Permissions](#permissions)

---

## About the Project

ThesisDemoApp is a personal healthcare companion app for Android. It stores a pre-loaded database of medicine information and lets users:

- Look up branded and generic drug details (indications, contraindications, side effects, dosage).
- Scan a physical prescription with the phone camera and use OCR to extract the medicine names automatically.
- Set daily medication reminders (morning / afternoon / night).
- Schedule and track doctor appointments.
- Record personal medical history.
- Call an ambulance service with one tap.

Authentication is handled locally through fingerprint biometrics or a pattern lock, so no account or internet connection is required for core functionality.

---

## Features

| Feature | Description |
|---|---|
| **Medicine Search** | Full-text search over a bundled SQLite database of medicines |
| **Prescription OCR** | Camera scan + Google Mobile Vision to extract medicine names from a prescription image |
| **Medication Reminders** | `AlarmManager`-based notifications for morning, afternoon, and night doses |
| **Appointment Manager** | Create, view, and delete upcoming doctor appointments |
| **Medical History** | Log past treatments, medicines, dosages, and dates |
| **Ambulance Services** | One-tap contact list for local emergency ambulance providers |
| **Biometric Auth** | Fingerprint (API 23+) or pattern-lock screen to protect personal data |

---

## Use Cases

### 1 – Look up a medicine
A patient wants to know the side effects of a drug before taking it. They open the app, type the brand or generic name in the search box, and the app displays full prescribing information from the local database – no internet required.

### 2 – Scan a prescription
A user receives a handwritten or printed prescription and wants to identify each medicine. They tap **Scan Prescription**, point the camera at the page, and the OCR engine highlights recognised text. The app then searches the database for each extracted name and shows the results.

### 3 – Set a medication reminder
A user is prescribed a twice-daily antibiotic. They open **Doses**, select the medicine, set a morning and night alarm, and the app schedules persistent notifications via `AlarmManager` (reminders survive a device reboot thanks to the `RECEIVE_BOOT_COMPLETED` permission).

### 4 – Manage appointments
Before a follow-up visit the user opens **Appointments**, adds the doctor's name and date, and the entry is saved to the local SQLite database. Past appointments can be reviewed or deleted at any time.

### 5 – Emergency ambulance
In an emergency, the user taps **Ambulance** to view a list of local services (Apollo Hospitals, Lab-aid, Ibrahim Cardiac, etc.) with direct-dial buttons.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java |
| Platform | Android (min SDK 23 / Android 6.0, target SDK 28) |
| Build | Gradle 4.6 (wrapper included) |
| UI | Android Support Library 28, ConstraintLayout, CardView, RecyclerView |
| OCR | Google Play Services Vision 15.0.2 |
| Database | SQLite (via `SQLiteAssetHelper` for bundled DB + standard `SQLiteOpenHelper` for user data) |
| Auth | Android BiometricPrompt (fingerprint) + PatternLockView |

---

## Prerequisites

- **Android Studio** (any recent version, or any IDE that supports Gradle-based Android projects)
- **JDK 8+**
- **Android SDK** with API level 28 platform and build-tools `28.0.2` installed
- A physical Android device or emulator running **Android 6.0 (API 23) or higher**
  - Fingerprint authentication requires a device with a fingerprint sensor enrolled in the OS.

---

## How to Run

### 1 – Clone the repository

```bash
git clone https://github.com/Tahmid46/ThesisApp.git
cd ThesisApp
```

### 2 – Open in Android Studio

1. Launch **Android Studio**.
2. Choose **Open an existing project** and select the cloned `ThesisApp` folder.
3. Let Gradle sync finish (it downloads all dependencies automatically).

### 3 – Connect a device or start an emulator

- **Physical device:** Enable **USB Debugging** in *Settings → Developer Options*, then connect via USB.
- **Emulator:** Open *AVD Manager* in Android Studio and create/start a virtual device with API 23 or higher.

### 4 – Run the app

Click the green **Run ▶** button in Android Studio, or use the Gradle wrapper from the command line:

```bash
# Build and install a debug APK on the connected device
./gradlew installDebug

# Or just build without installing
./gradlew assembleDebug
```

The compiled APK is placed in `app/build/outputs/apk/debug/app-debug.apk`.

### 5 – Run tests

```bash
# Local JVM unit tests
./gradlew test

# Instrumented tests (requires a connected device/emulator)
./gradlew connectedAndroidTest
```

### Other useful Gradle tasks

```bash
./gradlew assembleRelease   # Build a release APK (ProGuard enabled; requires a signing keystore)
./gradlew clean             # Delete build artefacts
./gradlew tasks             # List all available tasks
```

> **Note – release signing:** Before distributing a release APK you must configure a signing keystore. Add a `signingConfigs` block to `app/build.gradle` with your keystore path, alias, and passwords, then reference it in the `release` build type. See the [Android documentation](https://developer.android.com/studio/publish/app-signing) for details.

---

## Project Structure

```
ThesisApp/
├── app/
│   ├── libs/
│   │   └── mysql-connector-java-3.0.17-ga-bin.jar
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── assets/databases/
│       │   ├── MedicineDatabase.db        # Pre-loaded medicine reference data
│       │   └── MedicineInformation.db
│       ├── java/com/example/tahmid/Medicine_HelpV2/
│       │   ├── SettingActivity.java        # Launcher – auth setup
│       │   ├── MainActivity.java           # Home screen
│       │   ├── MedicineInfoActivity.java   # Medicine search
│       │   ├── ScanPrescription.java       # Camera + OCR flow
│       │   ├── ReminderActivity.java       # Medication reminders
│       │   ├── AppointmentActivity.java    # Doctor appointments
│       │   ├── AmbulanceActitivity.java    # Emergency contacts
│       │   ├── MyDatabaseHelper.java       # SQLite helper (user data)
│       │   ├── DatabaseAccess.java         # SQLite helper (medicine DB)
│       │   └── ...                         # 44 Java source files total
│       └── res/
│           ├── layout/                     # 36 XML layout files
│           ├── values/                     # strings, colors, styles
│           ├── anim/                       # Transition animations
│           └── font/                       # Custom fonts
├── build.gradle
├── settings.gradle
└── gradlew / gradlew.bat
```

---

## Permissions

The app declares the following Android permissions:

| Permission | Reason |
|---|---|
| `INTERNET` | Reserved for potential online features |
| `ACCESS_NETWORK_STATE` / `ACCESS_WIFI_STATE` | Check connectivity before network calls |
| `USE_BIOMETRIC` / `USE_FINGERPRINT` | Fingerprint authentication |
| `RECEIVE_BOOT_COMPLETED` | Restore medication reminders after reboot |
| `SET_ALARM` | Schedule dose notifications |
| `VIBRATE` | Notification vibration |
