
# 🎌 Kuro Stream

> **Anime-Only TV Player. Zero Bloat. Built for Low-End Fire TV.**

<p align="center">
  <a href="#-features"><img src="https://img.shields.io/badge/features-TV%20Optimized%20%7C%201GB%20RAM%20%7C%20AniList%20Sync-blueviolet?style=for-the-badge" alt="Features"></a>
  <a href="#-download"><img src="https://img.shields.io/badge/download-latest%20APK-brightgreen?style=for-the-badge&logo=android" alt="Download"></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/license-GPL--3.0-orange?style=for-the-badge" alt="License"></a>
  <a href="#-community"><img src="https://img.shields.io/badge/chat-Discord-5865F2?style=for-the-badge&logo=discord" alt="Discord"></a>
  <br>
  <a href="https://github.com/yourusername/kuro-stream/releases"><img src="https://img.shields.io/github/v/release/yourusername/kuro-stream?style=flat-square&label=latest" alt="Release"></a>
  <a href="https://github.com/yourusername/kuro-stream/stargazers"><img src="https://img.shields.io/github/stars/yourusername/kuro-stream?style=flat-square" alt="Stars"></a>
  <a href="https://github.com/yourusername/kuro-stream/issues"><img src="https://img.shields.io/github/issues/yourusername/kuro-stream?style=flat-square" alt="Issues"></a>
</p>

<p align="center">
  <strong>🌐 Live Demo:</strong> <a href="https://kuro-stream-tv.lovable.app">https://kuro-stream-tv.lovable.app</a><br>
  <sub><em>Migration to kurostream.pages.dev planned — check back soon!</em></sub>
</p>

---

## ⚠️ Critical Legal Disclaimer

> **Kuro Stream is a media frontend only.** It does **NOT** host, index, or distribute any copyrighted content. All streams come from **user-installed third-party extensions** or **user-configured external proxy servers**. Users are **solely responsible** for compliance with local copyright laws and the sources they choose to access. This project is open-source under GPL-3.0 for educational and interoperability purposes.

---

## 📋 Description

Kuro Stream is a free, open-source anime player built exclusively for Android TV and Amazon Fire TV. Designed from the ground up for low-end devices (1GB RAM Fire Stick Lite), it aggregates streams via user-installed extensions, converts torrents to HTTP through external proxy servers, and plays them through ExoPlayer or VLC with zero local P2P processing. It features AniList progress sync, skip-intro/outro timestamps, multi-source subtitle aggregation, profile isolation with PIN lock, and a TV-first interface optimized for D-pad navigation. Kuro Stream does not host or distribute content. It is a frontend player only, and all media sources are managed entirely by the user.

---

## ✨ Features

### 🎬 Playback & Performance
| Feature | Benefit |
|---------|---------|
| **TV-Optimized UI** | Jetpack Compose Leanback, full D-pad navigation, high-contrast focus states, zero UI jank |
| **Dual-Player Engine** | ExoPlayer (HLS/DASH/HTTP) primary + VLC fallback (RTSP/exotic codecs/magnet) |
| **1GB RAM Ready** | 8s buffer caps, SurfaceView rendering, HW decoder priority, aggressive memory pooling |
| **Zero Local P2P** | All torrents routed through external proxy → HTTP stream. TV never handles BitTorrent directly |

### 🔌 Extensions & Sources
| Feature | Benefit |
|---------|---------|
| **Stremio Addon Support** | Install any Stremio-compatible addon via manifest URL |
| **CloudStream Plugin Loader** | Bundle or sideload Kotlin AAR extensions following `MainAPI` contract |
| **Unified Multi-Provider Search** | Query all active sources in parallel, dedupe results, rank by quality |
| **Same-Torrent Next Episode** | Auto-increment `file_idx` for seamless binge-watching without re-fetching |

### 🎯 Anime-Specific Features
| Feature | Benefit |
|---------|---------|
| **AniList Sync** | OAuth2 auth, dedicated watchlist tab, auto-scrobble at 80%, offline queue with retry |
| **Skip Timestamps** | AniSkip + IntroDB integration for instant intro/outro/recap skip overlay |
| **Multi-Source Subtitles** | Aggregate from all providers, dedupe by language/hash, ±5s sync slider |
| **Trailer Support** | YouTube + Cinemeta integration + custom trailer addon compatibility |

### 🔐 Privacy & Profiles
| Feature | Benefit |
|---------|---------|
| **Profile Isolation** | Separate addon lists, watch history, and settings per profile |
| **PIN Lock** | 4-digit PIN protection for kids mode or private profiles |
| **Local-First Storage** | All data stored in encrypted DataStore/Room; optional AniList cloud sync |
| **Zero Telemetry** | No analytics, no tracking, no data collection by default |

---

## 📸 Screenshots

<p align="center">
  <img src="https://YOUR-PROJECT.lovable.app/screenshots/home.png" width="300" alt="Home Screen"/>
  <img src="https://YOUR-PROJECT.lovable.app/screenshots/detail.png" width="300" alt="Anime Detail"/>
  <img src="https://YOUR-PROJECT.lovable.app/screenshots/player.png" width="300" alt="Player Overlay"/>
</p>

> 🖼️ *Screenshots hosted on live demo. Preview the UI directly at [https://YOUR-PROJECT.lovable.app](https://YOUR-PROJECT.lovable.app)*

---

## 🚀 Quick Start

### Prerequisites
- Amazon Fire TV Stick (Lite/4K) or Android TV device (API 24+)
- **1GB+ RAM** (optimized for low-end devices)
- Stable internet connection (5 Mbps+ recommended for HD)

### Installation (Sideloading)
1. **Enable Unknown Sources** on your Fire TV:
   ```
   Settings → My Fire TV → Developer Options → Apps from Unknown Sources → ON
   ```
2. **Download the APK** from [GitHub Releases](https://github.com/yourusername/kuro-stream/releases)
   - `arm64-v8a`: For Fire TV Stick 4K, newer devices
   - `armeabi-v7a`: For Fire TV Stick Lite, older devices
3. **Transfer & Install**:
   - Use [Send Files to TV](https://play.google.com/store/apps/details?id=com.yablio.sendfilestotv) app, or
   - Use `adb`: `adb install kuro-stream-v1.0.0-arm64-v8a.apk`
4. **Launch Kuro Stream** and follow the first-run setup wizard

### First-Run Setup
1. **Choose Language**: Select your preferred UI language and primary audio language
2. **Add Extensions** (Optional):
   - Go to Settings → Extensions → Add Repository
   - Paste a Stremio addon manifest URL or CloudStream plugin JSON
3. **Configure Proxy** (For Torrent Support):
   - Go to Settings → Streaming → Torrent Proxy
   - Enter your self-hosted proxy URL (e.g., `http://192.168.1.100:8080`)
   - [Self-hosting guide](docs/PROXY_SETUP.md)
4. **Sync AniList** (Optional):
   - Go to Settings → Accounts → AniList → Authorize
   - Grant permissions to enable watchlist sync and auto-scrobbling

---

## 🏗️ Architecture

```
Kuro Stream (Clean Architecture + MVVM)
│
├── 📱 UI Layer (Jetpack Compose for TV)
│   ├── Home, Discover, Detail, Player screens
│   ├── Profile management + PIN lock UI
│   └── Settings: language, subtitles, quality limits
│
├── 🧠 Domain Layer (Pure Kotlin)
│   ├── Anime model: episodes, studios, mal_id, anilist_id
│   ├── Repository interfaces: Metadata, Streams, Subtitles
│   └── UseCases: SearchAnime, GetStreams, SyncProgress
│
├── 💾 Data Layer
│   ├── Adapters: StremioAddon, CloudStreamPlugin, CinemetaApi
│   ├── Local: Room (watch history), DataStore (settings/profiles)
│   └── Remote: Retrofit (HTTP), QuickJS (JS addons), DexClassLoader (AAR plugins)
│
├── 🎬 Player Layer
│   ├── SmartRouter: ExoPlayer (primary) → VLC (fallback)
│   ├── TorrentProxyDataSource: magnet: → http:// proxy conversion
│   └── SkipOverlayController: AniSkip/IntroDB timestamp integration
│
└── 🔐 Core Services
    ├── AniListSyncManager: OAuth2 + GraphQL scrobble
    ├── ProfileRepository: Isolated settings per profile
    └── TorrentSessionTracker: Same-torrent next-episode logic
```

### Tech Stack
- **Language**: Kotlin (100%)
- **UI**: Jetpack Compose for TV + Material 3
- **Architecture**: Clean Architecture + MVVM + Unidirectional Data Flow
- **DI**: Hilt
- **Async**: Kotlin Coroutines + Flow + StateFlow
- **Networking**: Retrofit + OkHttp (with aggressive timeouts/retry)
- **Persistence**: DataStore (settings), Room (history/cache)
- **Players**: ExoPlayer (Media3) + libVLC (dynamic AAR)
- **Extensions**: QuickJS (JS addons) + DexClassLoader (Kotlin AAR plugins)

---

## ⚙️ Advanced Configuration

### Torrent Proxy Setup (Required for Torrent Playback)
Kuro Stream does **NOT** handle BitTorrent locally. All torrent streams require an external proxy server that converts `magnet:`/`.torrent` to sequential HTTP streams.

**Option A: Self-Host (Recommended)**
```bash
# Using perpetus/stream-server (Rust, open-source)
docker run -d \
  -p 8080:8080 \
  -v /data/torrent-cache:/cache \
  --name kuro-proxy \
  ghcr.io/perpetus/stream-server:latest \
  --cache-size 10GB \
  --max-connections 20
```
→ Enter `http://YOUR_SERVER_IP:8080` in Kuro Stream Settings → Streaming → Torrent Proxy

**Option B: Community Proxy** (Use at your own risk)
- Join our [Discord](#-community) for trusted proxy endpoints shared by community members
- ⚠️ Never use unknown proxies: they can log your IP and requests

[Full proxy setup guide](docs/PROXY_SETUP.md) • [Troubleshooting](docs/TROUBLESHOOTING.md)

### Low-End Device Optimization
For Fire TV Stick Lite (1GB RAM), enable these settings:
```
Settings → Performance:
  [✓] Limit resolution to 720p
  [✓] Reduce buffer size (8s min / 16s max)
  [✓] Disable HDR tonemapping
  [✓] Prefer hardware decoding
  [✓] Clear cache on app background
```

### Extension Development
Build your own Stremio addon or CloudStream plugin:
- [Stremio Addon SDK Docs](https://github.com/Stremio/stremio-addon-sdk)
- [CloudStream MainAPI Reference](https://github.com/recloudstream/cloudstream/blob/master/README.md)
- [Kuro Stream Provider Interface Spec](docs/PROVIDER_API.md)

---

## 🤝 Contributing

Kuro Stream is open-source under GPL-3.0. We welcome contributions from developers, designers, and anime enthusiasts.

### How to Contribute
```
1. Fork the repository
2. Create a feature branch: git checkout -b feat/your-feature
3. Follow code style: ./gradlew ktlintFormat
4. Add tests for new functionality (target: 70% domain layer coverage)
5. Submit a PR with clear description and screenshots (if UI change)
```

### Contribution Areas
| Area | What We Need | Difficulty |
|------|-------------|------------|
| **Code** | Bug fixes, player optimizations, new adapter implementations | 🟡 Medium |
| **Documentation** | Translate README, improve proxy/setup guides, write extension tutorials | 🟢 Easy |
| **Testing** | Test on low-end Fire TV devices, report bugs with `adb logcat`, validate ABI splits | 🟢 Easy |
| **Extensions** | Build Stremio/CloudStream adapters following our provider interface spec | 🟡 Medium |
| **Design** | Improve Compose TV focus states, accessibility, dark theme polish | 🟢 Easy |

### Development Setup
```bash
# Clone & open in Android Studio
git clone https://github.com/yourusername/kuro-stream.git
cd kuro-stream
# Open in Android Studio (Flamingo+) or VS Code with Android extensions

# Build debug APK
./gradlew :app:assembleFullDebug

# Run on device via adb
adb install app/build/outputs/apk/full/debug/app-full-debug.apk

# Run tests
./gradlew testDebugUnitTest
./gradlew connectedAndroidTest  # Requires emulator/device
```

### Code Standards
- **Null Safety**: All public APIs must be null-safe (`String?` where applicable)
- **Coroutines**: Use `viewModelScope`/`lifecycleScope`; avoid GlobalScope
- **DI**: All dependencies injected via Hilt; no manual `object` singletons
- **Testing**: Domain/usecase layer: 70%+ coverage; UI: screenshot tests for critical flows
- **License**: All new files must include GPL-3.0 header; retain third-party attributions

Read [CONTRIBUTING.md](CONTRIBUTING.md) for full guidelines.

---

## ❓ FAQ

**Q: Is Kuro Stream legal?**  
A: Yes. It is a media frontend/player only, like VLC or Kodi. It does not host, index, or distribute content. Users are responsible for the extensions and proxies they configure.

**Q: Why won't torrents play?**  
A: Torrents require an external proxy server. Ensure you've configured a working proxy in Settings → Streaming → Torrent Proxy. [Setup guide](docs/PROXY_SETUP.md)

**Q: Can I use this on my phone?**  
A: Technically yes, but the UI is optimized for TV D-pad navigation. Touch controls are minimal. For phone use, consider CloudStream or Stremio mobile apps.

**Q: How do I add subtitles?**  
A: Subtitles are fetched automatically from active providers. To adjust: Player Overlay → Subtitle Icon → Select language, font, size, or sync delay.

**Q: Will this get me banned from AniList?**  
A: No. AniList API usage is within rate limits. Scrobble only triggers at 80% episode progress or manual "Mark Watched".

**Q: How do I report a bug?**  
A: Use GitHub Issues with: device model, Android version, Kuro Stream version, exact steps to reproduce, and `adb logcat` output if possible.

---

## 🌐 Community

- 💬 **Discord**: [Join our server](https://discord.gg/your-invite) for help, proxy sharing, and extension discussions
- 🐛 **GitHub Issues**: [Report bugs or request features](https://github.com/yourusername/kuro-stream/issues)
- 🗨️ **GitHub Discussions**: [Share setups, extensions, or ideas](https://github.com/yourusername/kuro-stream/discussions)
- 🐦 **Twitter/X**: [@KuroStreamApp](https://twitter.com/KuroStreamApp) for updates and announcements
- 📰 **Reddit**: r/FireTV, r/AndroidTV, r/anime (search "Kuro Stream")
- 🌐 **Live Demo**: [https://YOUR-PROJECT.lovable.app](https://YOUR-PROJECT.lovable.app)

---

## 📜 License & Attribution

```
Kuro Stream
Copyright (C) 2026 Kuro Stream Contributors

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <https://www.gnu.org/licenses/>.
```

### Third-Party Attributions
Kuro Stream incorporates code and concepts from:
- [NuvioTV](https://github.com/NuvioMedia/NuvioTV) (GPL-3.0) – TV UI patterns, profile system, QuickJS integration
- [stremio-core](https://github.com/Stremio/stremio-core) (MIT/Apache-2.0) – Addon manifest parsing, torrent conversion logic
- [cloudstream](https://github.com/recloudstream/cloudstream) (GPL-3.0) – Extension contract, subtitle aggregation
- [ExoPlayer/Media3](https://github.com/androidx/media) (Apache-2.0) – Primary playback engine
- [VLC-Android](https://github.com/videolan/vlc-android) (LGPL-2.1) – Fallback player via dynamic libvlc AAR
- [AniSkip API](https://github.com/ani-skip/api) – Skip timestamp data
- [IntroDB](https://github.com/introdb/introdb) – Fallback skip timestamps

All licenses are respected. Source code for modifications is available in this repository.

---

## 🙏 Support the Project

Kuro Stream is free, open-source, and ad-free. If you find it useful, consider supporting development:

- 💖 **GitHub Sponsors**: [Sponsor @yourusername](https://github.com/sponsors/yourusername)
- ☕ **Buy Me a Coffee**: [ko-fi.com/kurostream](https://ko-fi.com/kurostream)
- 🔧 **Contribute Code/Docs**: See [Contributing](#-contributing) above
- 📢 **Spread the Word**: Star the repo, share on social, help others in Discord

Your support funds server costs for metadata caching, CI/CD infrastructure, and community tools—not content hosting.

---

<p align="center">
  <sub>Built with ❤️ for the anime community • No trackers • No ads • No bullshit</sub>
  <br>
  <sub>© 2026 Kuro Stream Contributors • GPL-3.0 Licensed</sub>
</p>

---

> 🔄 **Migration Notice**: This project is currently hosted at `https://YOUR-PROJECT.lovable.app`. We plan to migrate to `https://kurostream.pages.dev` soon. All links will be updated—bookmark the GitHub repo for permanent access.
