# Welcome to SlapImage Apps

This is my attempt to create a utility Android Apps which rich features and useful functions using Android Studio Meerkat with the assistance of DeepSeek (https://www.deepseek.com/). Some of graphics are generated with Doubao 豆包 (https://www.doubao.com/chat/).

## Development Platform
SlapImage is developed using:
- my old MacBook Air 2017 with Intel Processor.
- Android Studio Meerkat 2024 3.1 Patch 1

## Platform tested:
I have tested mostly my code on:
- my Huawei P50 Pro mobile phone running EMUI 14.2.0 (Android 12) 6.6" display (2700 x 1228 pixels).

In addition I have also tested to some extend on:
- my son's Huawei Matepad 12 X Tablet running on Harmony OS 4.2.0. 12" display (2800 x 1840 pixels)\
  [Note: an attempt to get the apps to cover larger screen size]

Testing is also done on a Virtual Phone that I have setup in Android Studio:
- Virtual phone using Pixel 4 skin Android 10 (API 29) 5.7" display (1080 x 2280 pixels)

## Chronology of Development Events
- 16th Mar 2025: Started Android project SlapImage from scratch _Vibe Coding_ using DeepSeek in Kotlin.
- 20th Mar 2025: Completed the Apps initial view with a running banner on top, follow by rows of grid icons container in the middle and bottom navigation fragment icons.
- 25th Mar 2025: Assistance from DeepSeek to modify code so that the running banner code display transition more smoother and proffessionally handled.
- 1st Apr 2025: With DeepSeek help clone developed an Apple IOS Apple for SlapImage using Apple xcode platform. Tried to maintained as similar visual looks, banner, grid icon and bottom navigation buttons as Android. Successfully emulated for iPad and iPhone.
- 14th Apr 2025: Decision not to continue with Apple xcode apps development due to xcode not able to upload my apps to my new iPad Air M3. Some _missing strings_ error that i am not willing to spend further time. Most probably my old MacBook Air 2017 issue. Apple really sucks when comes to development using an old machine!#@#@*%
- 17th Apr 2025:
  - Completed implementing in HomeFragment.kt both horizontal grid icon scroller and two circular indicators below horizontal grid icon scroller with active page's indicator highlighted in blue.
  - SlapImage had reached a point of stable code of functions and features. I proceeded to commit it to GitHub.
- 
## Unresolvable Bug in Todo List
- When user press the back arrow button at the topbar of StockActivity.kt view, returning back to HomeFragment.kt view there is a large white rectangle covering lower two rows o the grid icon container. This only happens when user enters a Stock Ticker and check the stock price. However, if user uses the phone back button to return to HomeFragment.kt view, the bug doesn't happen.
- 
