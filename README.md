# :mouse: Welcome to SlapImage

<p float="left">
 <img src="app/src/main/res/drawable/animated_logo.gif" width="400">
</p>

## Table of Contents
- [Description](#scroll-description)
- [APK Download](#gift-apk-download)
- [Development Platform](#computer-development-platform)
- [Platform Tested](#iphone-platform-tested)
- [Screenshots](#film_strip-screenshots)
- [Chronology of Development Events](#hourglass_flowing_sand-chronology-of-development-events)
- [Unresolvable Bug in Todo List](#beetle-unresolvable-bug-in-todo-list)
- [Incomplete Todo Tasks](#plate_with_cutlery-incomplete-todo-tasks)
- [Buy Me a Coffee](#coffee-buy-me-a-coffee)
- [License](#page_with_curl-license)
- [Feedback and Suggestions](#speech_balloon-feedback-and-suggestions)

## :scroll: Description
<p float="left">
 <img src="app/src/main/res/drawable/slapimage_yellow.png" width="200">
</p>
This is my attempt to create a utility Android Apps which is feature-rich and contains useful functions using Android Studio Meerkat with the assistance of DeepSeek (https://www.deepseek.com/). Some of graphics are generated with Doubao 豆包 (https://www.doubao.com/chat/). <br>
Basically it allows me to personally experience the recent hype of Vibe Coding and whether AI can actually realistically replace software engineers? :grin: 

## :gift: APK Download
Try it yourself and I would love to hear your feedback :smiley: :mouse: <br>
Download [SlapImage-v1.0.0-beta.1.apk](https://github.com/chanlhock/SlapImage/releases/tag/v1.0.0-beta.1)

## :computer: Development Platform
SlapImage is developed using:
- old MacBook Air 2017 with Intel Processor.
- Android Studio Meerkat 2024 3.1 Patch 1
- DeepSeek AI for code and activity generation, debug and review
- GitHub Copilot for some minor code review and debug

## :iphone: Platform tested:
I have tested mostly my code on:
- Huawei P50 Pro mobile phone running EMUI 14.2.0 (Android 12) 6.6" display (1228 x 2700 pixels).
- Honor 200 mobile phone running Android 15 6.78" display (1224 x 2700 pixels).
- Huawei Mate 30 mobile phone running EMUI 12.0.0 (Android 10) 6.62" display (1080 x 2340 pixels)
  
In addition I have also tested to some extent on:
- Huawei Matepad 12 X Tablet running on Harmony OS 4.2.0 (Android 12) 12" display (1840 x 2800 pixels)\
  [Note: an attempt to develop the apps to cover larger screen size]

Testing is also done on a Virtual Phone that I have setup in Android Studio:
- Virtual phone using Pixel 4 skin Android 10 (API 29) 5.7" display (1080 x 2280 pixels)
- Virtual Medium Tablet Android 10 (API 29) 10.05" display (1600 x 2560 pixels)

## :film_strip: Screenshots
<p float="left">
  <img src="screenshots/Screenshot_20250429_134303_com.example.slapimage.jpg" width="140" />
  <img src="screenshots/Screenshot_20250430_184358_com.example.slapimage.jpg" width="140" /> 
  <img src="screenshots/Screenshot_20250429_134939_com.example.slapimage.jpg" width="140" />
</p>
<p float="left">
  <img src="screenshots/Screenshot_20250429_134944_com.example.slapimage.jpg" width="140" /> 
  <img src="screenshots/Screenshot_20250430_192357_com.example.slapimage.jpg" width="140" />
  <img src="screenshots/Screenshot_20250429_134948_com.example.slapimage.jpg" width="140" />
</p>


## :hourglass_flowing_sand: Chronology of Development Events
- 16th Mar 2025: Started Android project SlapImage from scratch _Vibe Coding_ using DeepSeek in Kotlin.
- 20th Mar 2025: Completed the Apps initial view with a running banner on top, follow by rows of grid icons container in the middle and bottom navigation fragment icons.
- 25th Mar 2025: Assistance from DeepSeek to modify code so that the running banner code display transition more smoother and professionally handled.
- 1st Apr 2025: With DeepSeek help clone developed an Apple IOS Apple for SlapImage using Apple xcode platform. Tried to maintain as similar visual look, banner, grid icon and bottom navigation buttons as Android. Successfully emulated for iPad and iPhone.
- 2nd to 14th Apr 2025: Added features of Open Photo, Open & Play Video, Open Text File, simple Calculator, simple Calendar, Game of Life Simulator, Stock Price Checker, Gallery browser and Play MP3 song. 
- 14th Apr 2025: Decision not to continue with Apple xcode apps development due to xcode not able to upload my apps to new iPad Air M3. Some _missing strings_ error that i am not willing to spend further time. Most probably it's an old MacBook Air 2017 issue. Apple really sucks when comes to development using an old machine!#@#@*% :rage:
- 17th Apr 2025:
  - Completed implementing in HomeFragment.kt both horizontal grid icon scroller and two circular indicators below horizontal grid icon scroller with active page's indicator highlighted in blue.
  - SlapImage had reached a point of stable code of functions and features. I proceeded to commit it to GitHub.
- 19th Apr 2025: Added an activity to support Google Gemini AI with DeepSeek generated production ready chat dialog.
- 22nd Apr 2025: Removed API Keys from source code and secured them in local.properties for apps build.
- 24th Apr 2025: Modified and added a simple and feature-rich Music Player from Github. (https://github.com/HarshAndroid/MusicPlayer-Android-Kotlin)
- 25th Apr 2025: Modified and added a tetris game fully built using Jetpack Compose source code from Github. Need to migrate from Material to Material3 to resolve build error. (https://github.com/vitaviva/compose-tetris)
- 2nd May 2025: Modified and added a new calculator from Github. (https://github.com/AyushAgnihotri2025/Calculator)
- 7th May 2025: Successfully modified, fixed bugs and added a TicTacToe from Github. (https://github.com/yamin8000/Dooz )
- 8th May 2025: Release the initial stable version of SlapImage v1.0.0-beta.1 apk. :tada:

## :beetle: Unresolvable Bug in Todo List
- [X] (Issue fixed 19th Apr 2025) When user pressed the back arrow button at the topbar of StockActivity.kt view, returning back to HomeFragment.kt view there is a large white rectangle covering lower two rows of the grid icon container. This only happens when user enters a Stock Ticker and check the stock price. However, if user uses the phone back button to return to HomeFragment.kt view, the bug doesn't happen. One other observation is that the bug doesn't happen on Android 15 phone. :eyes:
   - Resolution: Added these lines of code to navigate back to MainActivity (which hosts HomeFragment) solved the issue.
```
     toolbar.setNavigationOnClickListener {
            // Navigate back to MainActivity (which hosts HomeFragment)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            super.finish() }   
```
- [X] (Issue fixed 18th Apr 2025) When user clicked Gallery button to launch, SlapImage crashes. :warning: (Issue only specifically occurred on Huawei Matepad 12 X).  ----- 
   - Resolution: The key improvements are: (refer to GalleryActivity.kt for more details)
     - Better null safety checks
     - Huawei-specific MediaStore handling
```
    val uri = if (Build.MANUFACTURER.equals("huawei", ignoreCase = true)) {
        // Huawei specific URI
        MediaStore.Images.Media.getContentUri("external")
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }
```
## :plate_with_cutlery: Incomplete Todo Tasks
- [X] Add an activity to support Google Gemini AI Chat Dialog.
- [X] Improve the MP3 song player features by adapting good open source solution from GitHub.
- [X] Add an activity to support Textris game activity by adapting good open source solution from GitHub.
- [ ] Explore :eye:possibility of creating access of DeepSeek tensorflow lite model locally on device to run the DeepSeek Chat Dialog activity?
  - :pencil:Managed to generate ONNX file from Deepseek R1 model, unsuccessful in proceeding to generate tensorflow lite model. Create a Chat Dialog activity using ONNX model instead. Model able to load but when send message gotten error response. Pending further study and debugging...

## :coffee: Buy Me a Coffee
If you appreciate my work, do support me by...<br>
<a href="https://www.buymeacoffee.com/chanlhock" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/default-yellow.png" alt="Buy Me A Coffee" height="41" width="174"></a>

## :page_with_curl: License
See the GNU General Public License for more details.

## :speech_balloon: Feedback and Suggestions
For any feedback or suggestions, feel free to contact me via email:\
:email: chanlhock@gmail.com :mouse:

