# Minimum CMake version required
cmake_minimum_required(VERSION 4.0.1)

# Set the project name
project(droidzebra C)

# Define the source files
set(SOURCE_FILES
    droidzebra-jni.c
    droidzebra-autop.c
    droidzebra-display.c
    droidzebra-json.c
    droidzebra-msg.c
    zebra/bitbcnt.c
    zebra/bitbmob.c
    zebra/bitboard.c
    zebra/bitbtest.c
    zebra/cntflip.c
    zebra/counter.c
    zebra/doflip.c
    zebra/end.c
    zebra/epcstat.c
    zebra/eval.c
    zebra/game.c
    zebra/getcoeff.c
    zebra/globals.c
    zebra/hash.c
    zebra/learn.c
    zebra/midgame.c
    zebra/moves.c
    zebra/myrandom.c
    zebra/opname.c
    zebra/osfbook.c
    zebra/patterns.c
    zebra/pcstat.c
    zebra/probcut.c
    zebra/safemem.c
    zebra/search.c
    zebra/stable.c
    zebra/thordb.c
    zebra/timer.c
    zebra/unflip.c
)

# Create the shared library
add_library(
    droidzebra
    SHARED
    ${SOURCE_FILES}
)

# Set compile definitions (DEFS) and options
target_compile_definitions(droidzebra PRIVATE
    ZLIB_STATIC
    __linux__
    __CYGWIN__
    ANDROID
)

# Set compiler warning flags and optimization options
target_compile_options(droidzebra PRIVATE
    -Wall
    -Wcast-align
    -Wwrite-strings
    -Wstrict-prototypes
    -Winline
    -O3
    -s
    -fomit-frame-pointer
    -falign-functions=32
    -finline-limit=3200
)

# Link libraries
target_link_libraries(droidzebra
    m
    z
)