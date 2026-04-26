# Gates of Janus

A secure password manager Android app built with Kotlin and Jetpack Compose for the ED5042 Mobile App Development module at the University of Limerick.

## About

Gates of Janus is a local password vault that allows users to securely store, manage, and retrieve login credentials for various services. Named after the Roman god of doorways and passages, the app focuses on controlling access to sensitive data through master password authentication and AES encryption.

## Features

- Master password authentication with SHA-256 hashed storage
- Full CRUD operations on saved credentials (create, read, update, delete)
- AES-256-GCM encryption of stored passwords via Android Keystore
- Built-in password generator with configurable length and character types
- Password visibility toggle (show/hide) on all password fields
- Copy username and password to clipboard
- Search and filter credentials by site name, username, or category
- Category organisation for credentials
- Delete confirmation dialog
- Logout functionality
- FLAG_SECURE screenshot prevention
- Custom Janus-themed colour scheme with light and dark mode
- Keyboard autocomplete disabled on sensitive fields
- Material 3 theming throughout

## Architecture

The app follows the MVVM (Model-View-ViewModel) architecture pattern with a clear separation of concerns across three layers:

**Presentation Layer** - Jetpack Compose screens that display data and capture user input. Screens are stateless where possible, receiving data as parameters and sending user actions back through callbacks (state hoisting).

**ViewModel Layer** - VaultViewModel holds UI state as StateFlows and handles user actions by delegating to the Repository. Uses viewModelScope for coroutine management.

**Data Layer** - CredentialRepository acts as the single source of truth, sitting between the ViewModel and the Room DAO. The Repository handles encryption and decryption of passwords transparently. Room manages the local SQLite database with reactive Flow queries.

Dependency injection follows the AppContainer pattern, with a custom Application class (JanusApplication) creating and holding the container, registered in the AndroidManifest.

## Tech Stack

- Kotlin
- Jetpack Compose (UI)
- Room (local database)
- Navigation Compose (screen navigation)
- ViewModel with StateFlow (state management)
- Coroutines (background operations)
- Material 3 (design system)
- SharedPreferences with SHA-256 hashing (master password storage)
- javax.crypto AES-GCM with Android Keystore (password encryption)

## Project Structure

```
com.example.jgate/
    data/
        Credential.kt            - Room entity (database table)
        CredentialDao.kt          - Data access object (database operations)
        CredentialRepository.kt   - Single source of truth for data
        VaultDatabase.kt          - Room database singleton
        CryptoManager.kt          - AES-GCM encryption and decryption
        MasterPasswordManager.kt  - Master password hashing and verification
        AppContainer.kt           - Dependency injection container
    ui/
        screens/
            JanusScreen.kt               - Navigation route definitions
            CredentialListScreen.kt      - Main list of credentials with search
            AddEditCredentialScreen.kt   - Shared form for add/edit
            CredentialDetailScreen.kt    - View single credential
            LoginScreen.kt              - Master password authentication
            PasswordGeneratorScreen.kt   - Random password generation
            JanusTopBar.kt              - Reusable themed TopAppBar
        theme/
            Color.kt, Theme.kt, Type.kt - Material 3 theming
    viewmodel/
        VaultViewModel.kt        - UI state and business logic
    JanusApplication.kt          - Custom Application class
    MainActivity.kt              - Entry point with NavHost
```

## Screens

1. **Login** - Master password entry with creation flow for first-time users
2. **Credential List** - LazyColumn displaying all saved credentials with search and FAB
3. **Add Credential** - Form to create a new credential entry
4. **Edit Credential** - Same form pre-filled with existing data
5. **Credential Detail** - Full view of a credential with copy, edit, and delete options
6. **Password Generator** - Configurable random password generation

## Security

- Master password is hashed with SHA-256 and stored in SharedPreferences, never in plain text
- Credential passwords are encrypted with AES-256-GCM before being written to the Room database
- Encryption keys are stored in the Android Keystore and never leave secure hardware
- Passwords are decrypted only when displayed or copied
- FLAG_SECURE prevents screenshots and hides the app from recent apps
- Keyboard autocomplete is disabled on all sensitive input fields
- All database queries use Room parameterised queries to prevent SQL injection

## Outside-Course Elements

The following features are not covered in the ED5042 lecture slides and are documented as required:

- **SHA-256 Password Hashing** (java.security.MessageDigest) - hashes the master password before storage. Source: Android developer documentation.
- **AES-GCM Encryption** (javax.crypto.Cipher) - encrypts credential passwords before storage. Source: Android developer documentation on cryptography.
- **Android Keystore Key Generation** (KeyGenParameterSpec.Builder) - generates and stores the AES key in secure hardware. Source: Android developer documentation.
- **FLAG_SECURE** (WindowManager.LayoutParams) - prevents screenshots and hides app from recent apps. Source: Android developer documentation.
- **ClipboardManager** (LocalClipboardManager in Compose) - enables copy-to-clipboard functionality. Source: Android developer documentation.
- **Material Icons Extended** (androidx.compose.material:material-icons-extended) - provides additional icons. Source: Jetpack Compose documentation.
- **AlertDialog** - Material 3 confirmation dialog for delete confirmation. Source: Android developer documentation.
- **Slider** - Material 3 slider for password length selection. Source: Android developer documentation.
- **Switch** - Material 3 toggle for character type selection. Source: Android developer documentation.
- **combine()** - combines two Flows for search filtering. Source: Kotlin coroutines documentation.
- **MutableStateFlow** - mutable StateFlow for search query state. Source: Kotlin coroutines documentation.

## Build

Built with Android Studio using AGP 9.0, Kotlin 2.0.21, and minimum SDK 24 (Android 7.0).