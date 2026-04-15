# Gates of Janus

A secure password manager Android app built with Kotlin and Jetpack Compose for the ED5042 Mobile App Development module at the University of Limerick.

## About

Gates of Janus is a local password vault that allows users to securely store, manage, and retrieve login credentials for various services. Named after the Roman god of doorways and passages, the app focuses on controlling access to sensitive data through master password authentication and AES encryption.

## Features

- Master password authentication with hashed storage
- Full CRUD operations on saved credentials (create, read, update, delete)
- AES-GCM encryption of stored passwords
- Built-in password generator with configurable length and character types
- Password visibility toggle (show/hide)
- Copy username and password to clipboard
- Search and filter credentials by site name
- Category organisation for credentials
- Delete confirmation dialog
- Material 3 theming

## Architecture

The app follows the MVVM (Model-View-ViewModel) architecture pattern with a clear separation of concerns across three layers:

**Presentation Layer** -- Jetpack Compose screens that display data and capture user input. Screens are stateless where possible, receiving data as parameters and sending user actions back through callbacks (state hoisting).

**ViewModel Layer** -- VaultViewModel holds UI state as StateFlows and handles user actions by delegating to the Repository. Uses viewModelScope for coroutine management.

**Data Layer** -- CredentialRepository acts as the single source of truth, sitting between the ViewModel and the Room DAO. The Repository handles encryption logic before passing data to the database. Room manages the local SQLite database with reactive Flow queries.

Dependency injection follows the AppContainer pattern, with a custom Application class (JanusApplication) creating and holding the container, registered in the AndroidManifest.

## Tech Stack

- Kotlin
- Jetpack Compose (UI)
- Room (local database)
- Navigation Compose (screen navigation)
- ViewModel with StateFlow (state management)
- Coroutines (background operations)
- Material 3 (design system)
- EncryptedSharedPreferences (master password storage)
- javax.crypto AES-GCM (password encryption)

## Project Structure

```
com.example.jgate/
    data/
        Credential.kt           -- Room entity (database table)
        CredentialDao.kt         -- Data access object (database operations)
        CredentialRepository.kt  -- Single source of truth for data
        VaultDatabase.kt         -- Room database singleton
        AppContainer.kt          -- Dependency injection container
    ui/
        screens/
            JanusScreen.kt               -- Navigation route definitions
            CredentialListScreen.kt      -- Main list of credentials
            AddEditCredentialScreen.kt   -- Shared form for add/edit
            CredentialDetailScreen.kt    -- View single credential
        theme/
            Color.kt, Theme.kt, Type.kt -- Material 3 theming
    viewmodel/
        VaultViewModel.kt        -- UI state and business logic
    JanusApplication.kt          -- Custom Application class
    MainActivity.kt              -- Entry point with NavHost
```

## Screens

1. **Login** -- Master password entry with creation flow for first-time users
2. **Credential List** -- LazyColumn displaying all saved credentials with search and FAB
3. **Add Credential** -- Form to create a new credential entry
4. **Edit Credential** -- Same form pre-filled with existing data
5. **Credential Detail** -- Full view of a credential with copy, edit, and delete options
6. **Password Generator** -- Configurable random password generation

## Security

- Master password is hashed and stored in EncryptedSharedPreferences, never in plain text
- Credential passwords are encrypted with AES-GCM before being written to the Room database
- Passwords are decrypted only when displayed or copied
- All database queries use Room parameterised queries to prevent SQL injection

## Outside-Course Elements

The following features are not covered in the ED5042 lecture slides and are documented as required:

- **AES-GCM Encryption** (javax.crypto.Cipher) -- encrypts credential passwords before storage. Source: Android developer documentation on cryptography.
- **EncryptedSharedPreferences** (androidx.security.crypto) -- securely stores the master password hash. Source: Android Jetpack Security library documentation.
- **ClipboardManager** (LocalClipboardManager in Compose) -- enables copy-to-clipboard functionality. Source: Android developer documentation.
- **Material Icons Extended** (androidx.compose.material:material-icons-extended) -- provides Visibility and ContentCopy icons. Source: Jetpack Compose documentation.

## Build

Built with Android Studio using AGP 9.0, Kotlin 2.0.21, and minimum SDK 24 (Android 7.0).

## Author

Dylan -- ED5042 Mobile App Development, University of Limerick