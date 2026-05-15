# 🏆 Grama Kalyana Sports

Grama Kalyana Sports is an Android application developed using Kotlin, Jetpack Compose, and Firebase Authentication.

The application helps players and committee members participate in village sports activities with secure login and dashboard access.

---

# 🚀 Features

- 📱 Introduction Screen
- 🔐 Firebase Phone Authentication
- OTP Verification
- 👤 Player Dashboard
- 🚪 Logout Functionality
- ☁ Firebase Integration
- 🎨 Modern Jetpack Compose UI
- 📊 Realtime User Data Storage

---

# 🛠 Tech Stack

- Kotlin
- Jetpack Compose
- Material 3
- Firebase Authentication
- Firebase Realtime Database
- Android Studio

---

# 📂 Project Structure

```plaintext
app/
 ├── ui/
 │    ├── auth/
 │    │     ├── IntroActivity.kt
 │    │     ├── PhoneLoginActivity.kt
 │    │
 │    ├── dashboard/
 │    │     ├── PlayerDashboardActivity.kt
 │
 ├── firebase/
 │    ├── FirebaseAuthHelper.kt
 │
 ├── model/
 │    ├── User.kt
```

---

# 🔥 Firebase Setup

## 1. Create Firebase Project

Open Firebase Console:

https://console.firebase.google.com

---

## 2. Enable Authentication

Go to:

```plaintext
Authentication
   ↓
Sign-in Method
   ↓
Phone Authentication
```

Enable Phone Authentication.

---

## 3. Add SHA-1

Run:

```bash
.\gradlew signingReport
```

Copy SHA-1 and add it in:

```plaintext
Firebase Console
   ↓
Project Settings
   ↓
Add Fingerprint
```

---

## 4. Download google-services.json

Place file inside:

```plaintext
app/google-services.json
```

---

# ▶ Run Project

## Clone Repository

```bash
git clone https://github.com/shivu3114/grama_kalyana_sport.git
```

---

## Open in Android Studio

Open project folder in Android Studio.

---

## Sync Gradle

```plaintext
File
   ↓
Sync Project with Gradle Files
```

---

## Run App

Use a physical Android device for Firebase OTP verification.

---

# 📱 App Flow

```plaintext
Introduction Screen
        ↓
Phone Login
        ↓
OTP Verification
        ↓
Player Dashboard
```

---

# 👨‍💻 Developer

Shivraj Angadi

GitHub:
https://github.com/shivu3114

---

# 📜 License

This project is developed for educational and learning purposes.
