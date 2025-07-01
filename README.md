# 🚀 NOVA Voice Assistant

**NOVA** is an ultra-powerful, completely offline Android voice assistant that handles **150+ built-in phone automation and utility commands** via voice or buttons. Built with modern Android technologies and Material Design 3.

![NOVA Banner](https://img.shields.io/badge/NOVA-Voice%20Assistant-58A6FF?style=for-the-badge&logo=android)

## ✨ Features

### 🎤 Voice Recognition
- **Wake word detection**: "Hey Nova" using Android's SpeechRecognizer
- **Offline speech-to-text** using Android's native APIs
- **Real-time voice processing** with no internet dependency
- **Continuous listening mode** with wake word activation

### 🎨 Modern UI/UX
- **Material Design 3** with dark futuristic theme
- **Animated waveform** visualization while listening
- **Responsive command list** with categorized browsing
- **Floating action button** with mic controls
- **Smooth animations** and transitions throughout

### 📱 Command Categories (150+ Total)

#### 🔧 Device Control (35+ commands)
- Toggle WiFi, mobile data, hotspot
- Enable/disable Bluetooth, flashlight, airplane mode
- Adjust brightness and volume
- Open apps (WhatsApp, YouTube, Spotify, etc.)
- Battery status and device information

#### 📞 Communication (15+ commands)
- Make calls to contacts or numbers
- Send SMS messages
- WhatsApp integration
- Read latest messages
- Call log management

#### ⏰ Alarms & Reminders (12+ commands)
- Set alarms for specific times
- Create countdown timers
- Schedule reminders
- Manage existing alarms

#### 🧰 Utilities (30+ commands)
- Open calculator, camera, calendar
- Take screenshots and screen recording
- QR code scanning and generation
- Storage and system information

#### 🎵 Media (20+ commands)
- Music playback controls
- Volume adjustment
- Video playback
- Audio recording
- Streaming app integration

#### 📅 Productivity (20+ commands)
- Calendar event creation
- Note taking and voice notes
- Task management
- Schedule viewing

#### 🌐 Web & Tools (15+ commands)
- Web searches
- Website navigation
- Text translation
- Currency conversion
- Weather and news (offline capable)

#### 🎮 Fun & Extras (10+ commands)
- Tell jokes
- Flip coins and roll dice
- Rock paper scissors
- Random facts and entertainment

#### 🔒 System & Security (10+ commands)
- Screen lock
- VPN management
- Cache clearing
- Internet speed testing

#### 📂 File Manager (10+ commands)
- File browsing and management
- File sharing and renaming
- Directory navigation

## 🛠️ Technical Stack

### Core Technologies
- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with StateFlow
- **Voice Recognition**: Android SpeechRecognizer
- **Text-to-Speech**: Android TTS Engine
- **Minimum SDK**: Android 8.0 (API 26)
- **Target SDK**: Android 14 (API 34)

### Key Dependencies
```gradle
// Core Android
androidx.compose.bom:2023.10.01
androidx.lifecycle:lifecycle-*:2.7.0
androidx.activity:activity-compose:1.8.2

// Voice Processing
androidx.speech:speech:1.0.0-alpha05

// Material Design
androidx.compose.material3:material3
androidx.compose.material:material-icons-extended

// Coroutines
kotlinx-coroutines-android:1.7.3
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Flamingo or later
- Android SDK 26+
- Kotlin 1.9.10+

### Installation

1. **Clone the repository**
```bash
git clone https://github.com/yourusername/nova-voice-assistant.git
cd nova-voice-assistant
```

2. **Open in Android Studio**
- Open Android Studio
- Select "Open an existing project"
- Navigate to the cloned directory

3. **Build and Run**
- Sync project with Gradle files
- Connect an Android device or start an emulator
- Run the app (Ctrl+R / Cmd+R)

### Required Permissions
The app requests the following permissions:
- **RECORD_AUDIO**: Voice recognition
- **CALL_PHONE**: Making phone calls
- **SEND_SMS**: Sending text messages
- **READ_CONTACTS**: Accessing contact information
- **CAMERA**: Camera and flashlight features
- **ACCESS_FINE_LOCATION**: Location-based features

## 📖 Usage

### Voice Commands
1. **Activate**: Say "Hey Nova" or tap the microphone button
2. **Speak**: Give a clear voice command
3. **Confirm**: NOVA will execute and provide audio feedback

### Example Commands
```
"Hey Nova, turn on WiFi"
"Call Mom"
"Set alarm for 7 AM"
"Take a screenshot"
"Play music"
"What's my battery level?"
"Tell me a joke"
"Open YouTube"
```

### Manual Operation
- Browse commands by category in the Commands tab
- Tap any command to execute directly
- Use the search feature to find specific commands
- Access settings for customization

## 🔒 Privacy & Security

### Offline-First Design
- **No internet required** for core functionality
- **Local processing** of all voice data
- **No cloud dependencies** for speech recognition
- **Zero data transmission** to external servers

### Data Protection
- Voice recordings are processed locally
- No personal data leaves your device
- Optional voice data clearing in settings
- Transparent permission usage

## 🏗️ Architecture

### Project Structure
```
app/
├── src/main/kotlin/com/nova/assistant/
│   ├── MainActivity.kt                    # Main entry point
│   ├── viewmodel/
│   │   └── NovaViewModel.kt              # Main ViewModel
│   ├── ui/
│   │   ├── screens/
│   │   │   └── MainScreen.kt             # Main UI screen
│   │   ├── components/
│   │   │   ├── VoiceScreen.kt            # Voice interface
│   │   │   ├── CommandsScreen.kt         # Commands list
│   │   │   └── SettingsScreen.kt         # Settings panel
│   │   └── theme/
│   │       ├── Theme.kt                  # Material Design theme
│   │       └── Type.kt                   # Typography
│   ├── service/
│   │   ├── VoiceRecognitionService.kt    # Speech-to-text
│   │   ├── TextToSpeechService.kt        # Text-to-speech
│   │   └── CommandExecutorService.kt     # Command execution
│   └── data/
│       ├── model/
│       │   └── Command.kt                # Command data models
│       └── repository/
│           └── CommandRepository.kt      # Command management
```

### Design Patterns
- **MVVM**: Clear separation of concerns
- **Repository Pattern**: Data layer abstraction
- **State Management**: Unidirectional data flow with StateFlow
- **Dependency Injection**: Service composition
- **Command Pattern**: Extensible command system

## 🎨 Design System

### Color Palette
- **Primary**: Nova Blue (#58A6FF)
- **Secondary**: Purple (#7C3AED)
- **Accent**: Orange (#FFB347)
- **Error**: Red (#FF6B6B)
- **Background**: Dark (#0D1117)

### Typography
- Modern, readable font stack
- Consistent sizing and spacing
- Accessibility-compliant contrast ratios

## 🤝 Contributing

We welcome contributions! Please see our [Contributing Guidelines](CONTRIBUTING.md) for details.

### Development Setup
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit changes (`git commit -m 'Add amazing feature'`)
4. Push to branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📋 Roadmap

### Version 1.1
- [ ] Vosk integration for improved offline recognition
- [ ] Custom wake word training
- [ ] Voice response customization
- [ ] Command scheduling

### Version 1.2
- [ ] Plugin system for custom commands
- [ ] Smart home integration
- [ ] Voice shortcuts automation
- [ ] Multi-language support

### Version 2.0
- [ ] AI-powered command understanding
- [ ] Context-aware responses
- [ ] Learning user preferences
- [ ] Advanced automation workflows

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Android team for excellent STT/TTS APIs
- Material Design team for the beautiful design system
- Jetpack Compose team for the modern UI toolkit
- Open source community for inspiration and tools

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/yourusername/nova-voice-assistant/issues)
- **Discussions**: [GitHub Discussions](https://github.com/yourusername/nova-voice-assistant/discussions)
- **Email**: support@nova-assistant.com

---

**Built with ❤️ by the NOVA team**

*Making voice control accessible, private, and powerful for everyone.*