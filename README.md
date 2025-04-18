# :mouse: Welcome to SlapImage

<picture>
 <source media="(prefers-color-scheme: dark)" srcset="app/src/main/res/drawable/animated_logo.gif" width="400">
 <source media="(prefers-color-scheme: light)" srcset="app/src/main/res/drawable/animated_logo.gif" width="400">
 <img alt="SlapImage title yellow over black" src="app/src/main/res/drawable/animated_logo.gif" width="400">
</picture>

## Table of Contents
- [Description](#scroll-description)
- [APK Download](#gift-apk-download)
- [Development Platform](#computer-development-platform)
- [Platform Tested](#iphone-platform-tested)
- [Chronology of Development Events](#hourglass_flowing_sand-chronology-of-development-events)
- [Unresolvable Bug in Todo List](#beetle-unresolvable-bug-in-todo-list)
- [Uncomplete Todo Tasks](#plate_with_cutlery-uncomplete-todo-tasks)
- [Feedback and Suggestions](#speech_balloon-feedback-and-suggestions)

## :scroll: Description
<picture>
 <source media="(prefers-color-scheme: dark)" srcset="app/src/main/res/drawable/slapimage_yellow.png" width="200">
 <source media="(prefers-color-scheme: light)" srcset="app/src/main/res/drawable/slapimage_yellow.png" width="200">
 <img alt="SlapImage title yellow over black" src="app/src/main/res/drawable/slapimage_yellow.png" width="200">
</picture><br>
This is my attempt to create a utility Android Apps which is feature-rich and contains useful functions using Android Studio Meerkat with the assistance of DeepSeek (https://www.deepseek.com/). Some of graphics are generated with Doubao 豆包 (https://www.doubao.com/chat/). <br>
Basically it allows me to personally experience the recent hype of Vibe Coding and whether AI can actually realistically replace software engineers? :grin: 

## :gift: APK Download
Try it yourself and I would love to hear your feedback :smiley: :mouse:

## :computer: Development Platform
SlapImage is developed using:
- old MacBook Air 2017 with Intel Processor.
- Android Studio Meerkat 2024 3.1 Patch 1

## :iphone: Platform tested:
I have tested mostly my code on:
- Huawei P50 Pro mobile phone running EMUI 14.2.0 (Android 12) 6.6" display (1228 x 2700 pixels).
- Honor 200 mobile phone running Android 14 6.78" display (1224 x 2700 pixels).
  
In addition I have also tested to some extent on:
- Huawei Matepad 12 X Tablet running on Harmony OS 4.2.0 (Android 12) 12" display (1840 x 2800 pixels)\
  [Note: an attempt to develop the apps to cover larger screen size]

Testing is also done on a Virtual Phone that I have setup in Android Studio:
- Virtual phone using Pixel 4 skin Android 10 (API 29) 5.7" display (1080 x 2280 pixels)

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
- 
## :beetle: Unresolvable Bug in Todo List
- [ ] When user pressed the back arrow button at the topbar of StockActivity.kt view, returning back to HomeFragment.kt view there is a large white rectangle covering lower two rows of the grid icon container. This only happens when user enters a Stock Ticker and check the stock price. However, if user uses the phone back button to return to HomeFragment.kt view, the bug doesn't happen.
- [ ] When user clicked Gallery button to launch, SlapImage crashes. :warning: (Issue only specifically occurred on Huawei Matepad 12 X)

## :plate_with_cutlery: Incomplete Todo Tasks
- [ ] Add an activity to support Google Gemini AI Chat Dialog.
- [ ] Explore possibility of creating access of DeepSeek tensorflow lite model locally on device to run the DeepSeek Chat Dialog activity? :eye:
- [ ] Add an activity to support Textris game activity.
- [ ] Improve the MP3 song player features by adapting good open source solution in GitHub.

      
## :speech_balloon: Feedback and Suggestions
For any feedback or suggestions, feel free to contact me via email:\
:email: chanlhock@gmail.com :mouse:

