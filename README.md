# Poker Calculator (Educational Project)

**Disclaimer:** This project is intended for educational purposes only. It does not encourage gambling and should not be used for gambling purposes.

## Overview

This Poker Calculator application is a Kotlin-based Android app designed to calculate and manage poker player balances. It features a simple interface for adding players and tracking their results, including the handling of rebuys.

## Features

- Add new players with their names.
- Automatically set initial balance and rebuys to 0.
- Calculate final player balances considering rebuys.
- Display player information in a RecyclerView.
- A modern and responsive UI design.

## Screenshots

<div align=center>
  <img alt="pejpero" src="https://github.com/user-attachments/assets/ed8b92c0-09ee-4fde-a9d8-2d83a7a437bc" height="800px"></a>
  <img alt="pejpero" src="https://github.com/user-attachments/assets/2d011a77-8d09-4766-a86c-34ae72b519f8" height="800px"></a>
</div>

## Development Roadmap

### Current Features
- Basic UI for adding players.
- Calculation logic for player balances including rebuys.

### Future Improvements
1. **Persist Player Data:**
   - Integrate a local database (e.g., SQLite or Room) to save player information.
   - Ensure player data persists between app sessions.

2. **Historical Data Analysis:**
   - Add functionality to store historical game results.
   - Implement analysis features to track player performance over time.

3. **Advanced Calculations:**
   - Include more advanced poker metrics and calculations.
   - Allow users to input and calculate more complex poker scenarios.

4. **Cross-Platform Support:**
   - Extend the application to iOS using Kotlin Multiplatform Mobile (KMM).
   - Ensure seamless functionality and UI experience across both platforms.

5. **Enhanced User Interface:**
   - Improve the design and usability of the app.
   - Add animations and transitions for better user experience.

## Code Structure
### MainActivity.kt
- Handles the main logic of the app including adding players and calculating balances.
### activity_main.xml
- Defines the main user interface layout.
### Player.kt
- Data class representing a poker player.
### PlayerAdapter.kt
- Adapter for binding player data to the RecyclerView.

## Getting Started

### Prerequisites
- Android Studio
- Kotlin

### Installation
1. Clone the repo:
   ```bash
   git clone https://github.com/PejperO/pokerCalculator.git
   ```
2. Open the project in Android Studio.
3. Build and run the project on an Android device or emulator.

### Installing on a Mobile Device
1. Go to the Releases section of this repository.
2. Download the latest app-release.apk file directly to your mobile device.
3. Once the download is complete, tap on the file to begin the installation process.
>Note: Depending on your Android version, you may need to allow installations from unknown sources. This option is typically found in your device's security settings.

## License

This project is licensed under the GPL-3.0 License. See the [LICENSE](LICENSE) file for details.

## What I Learned
- **Kotlin for Android Development:** Enhanced my understanding and proficiency in Kotlin.
- **Android UI Design:** Learned to design and implement user interfaces in Android.
- **RecyclerView and Adapters:** Gained experience in using RecyclerView to display lists of data.
- **Cross-Platform Mobile Development:** Explored the potential of Kotlin Multiplatform Mobile (KMM) for creating cross-platform apps.
- **Software Licensing:** Understood the implications of GPL-3.0 license for open-source projects.
