# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is an Android novel reading app with a multi-flavor white-label (马甲包) architecture. A single codebase builds into 18+ different app variants, each with its own branding, resources, and server endpoints.

## Build Commands

```bash
# Clean build
./gradlew clean

# Build a specific flavor (e.g., novelmuse)
./gradlew assembleNovelmuseDebug
./gradlew assembleNovelmuseRelease

# Build for Play Store (AAB format)
./gradlew bundleNovelmuseRelease

# Build all enabled flavors
./gradlew assembleDebug
```

## Architecture

### Layer Hierarchy (top to bottom)
1. **app** - Main application module with shared business logic
2. **layer_module** - Feature modules (payment, ads, subscriptions)
3. **layer_channel** - Per-flavor UI customization (resources, assets, minor code)
4. **layer_base** - Foundation libraries (network, API, utilities, UI components)

### Dependency Rule
Each layer depends on all layers below it. Same-layer dependencies use interface/implementation separation.

### Key Directories
- `app/src/main/java/com/benefit/` - Main business logic
- `layer_channel/{flavor}/src/main/res/` - Flavor-specific resources
- `layer_base/{module}/` - Shared base modules
- `buildSrc/` - Gradle build configuration (Groovy/Java)

## Build Configuration

- **Gradle**: 7.6.3
- **Android Gradle Plugin**: 7.4.2
- **Kotlin**: 2.2.0
- **compileSdk**: 34
- **targetSdk**: 35
- **minSdk**: 24
- **JDK**: 17

### Build Types
- `debug` - Debuggable, no minification
- `release` - Production with R8 minification
- `develop` - Test server with debug logging
- `publish` - Production with debug enabled

### Online package mapping dir
- `local` - /apks/{flavor}/v{versionName}(versionCode)/
- `remote` - s3://xcyh-app-front-log/aab/{flavor}/v{versionName}(versionCode)/

## Key Technologies

- **Architecture**: MVP pattern with ViewBinding/DataBinding
- **Language**: Java(primary) + Kotlin(It's best not to use) mixed codebase
- **DI**: Manual dependency injection via ConfigModule
- **Network**: Retrofit + OkHttp
- **Database**: DBFlow
- **Analytics**: Firebase + SensorsAnalytics (神策)
- **Auth**: Google Sign-In, Facebook Login
- **Payment**: Google Play Billing、Web page Billing (ex: PayPal、PayerMax、Stripe...)
- **Ads**: AdMob mediation (Meta, Pangle, Vungle, Unity)、Advertise Aggregator SDK
