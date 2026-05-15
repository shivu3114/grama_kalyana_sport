# Firebase Setup — Grama Kalyana Sports

Follow these steps before running the app with real phone OTP.

## 1. Create Firebase project

1. Open [Firebase Console](https://console.firebase.google.com/)
2. Click **Add project** and complete the wizard
3. Click **Add app** → **Android**
4. Package name: `com.example.gramakalyana`
5. Download **google-services.json**
6. Replace `app/google-services.json` in this project with your downloaded file

## 2. Enable Firestore Database

1. Firebase Console → **Build** → **Firestore Database**
2. Click **Create database** → start in **test mode** (for development)
3. Choose a region close to your users

Collections used by the app: `users`, `teams`, `matches`, `leaderboard`

## 3. Enable Phone Authentication

1. Firebase Console → **Build** → **Authentication**
2. **Sign-in method** tab → enable **Phone**
3. Save

## 4. SHA-1 and SHA-256 fingerprints (required for Phone Auth)

Firebase uses your app signing certificate for Phone Auth / SafetyNet.

### Debug keystore (development)

From project root in terminal:

**Windows (PowerShell):**
```powershell
keytool -list -v -keystore "$env:USERPROFILE\.android\debug.keystore" -alias androiddebugkey -storepass android -keypass android
```

**macOS / Linux:**
```bash
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

Copy **SHA-1** and **SHA-256** from the output.

### Add fingerprints in Firebase

1. Firebase Console → Project **Settings** (gear icon)
2. Under **Your apps**, select the Android app
3. Click **Add fingerprint**
4. Paste SHA-1, then add SHA-256 the same way
5. Download the updated **google-services.json** again if prompted

### Release builds

Use your release keystore:

```bash
keytool -list -v -keystore path/to/your-release.keystore -alias your_alias
```

Add those SHA-1/SHA-256 values to Firebase as well.

## 5. Gradle (already configured)

Root `build.gradle.kts`:
```kotlin
id("com.google.gms.google-services") version "4.4.2" apply false
```

App `app/build.gradle.kts`:
```kotlin
plugins {
    id("com.google.gms.google-services")
}
implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
implementation("com.google.firebase:firebase-auth-ktx")
```

## 6. AndroidManifest permissions (already added)

- `INTERNET`
- `ACCESS_NETWORK_STATE`

## 7. Test phone numbers (optional)

For testing without SMS charges:

1. Authentication → **Sign-in method** → Phone → **Phone numbers for testing**
2. Add e.g. `+91 9876543210` with code `123456`

## 8. Run the app

1. Open project in Android Studio
2. Sync Gradle
3. Use a real device or emulator with Google Play services
4. Build and run

## Troubleshooting

| Issue | Fix |
|-------|-----|
| `missing-client-identifier` | Add SHA-1/SHA-256 to Firebase |
| OTP never arrives | Enable Phone auth; check number format (+91…) |
| `Invalid app info in play_integrity_token` | Use debug SHA on debug builds; wait a few minutes after adding SHA |
| Build fails on google-services | Replace placeholder `google-services.json` with real file from Firebase |
