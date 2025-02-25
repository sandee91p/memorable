cmake_minimum_required(VERSION 3.4.1)
enable_language(ASM)
project(zetalib)

# Preserved include directories
include_directories(
        JniHook
        ./
        ${CMAKE_CURRENT_SOURCE_DIR}/extra/dobby/include
)

# Preserved source collection
aux_source_directory(./ SRC1)
aux_source_directory(Hook SRC2)
aux_source_directory(JniHook SRC3)
add_compile_options(-w)

# Preserved library target
add_library(
        zetalib
        SHARED
        BoxCore.cpp
        ${SRC1}
        ${SRC2}
        ${SRC3}
)

# Preserved log library finding
find_library(
        log-lib
        log
)

# Preserved Dobby linking with ABI-specific paths
if(${ANDROID_ABI} STREQUAL "armeabi-v7a")
    target_link_libraries(zetalib ${CMAKE_CURRENT_SOURCE_DIR}/extra/dobby/armeabi-v7a/libdobby.a)
elseif(${ANDROID_ABI} STREQUAL "arm64-v8a")
    target_link_libraries(zetalib ${CMAKE_CURRENT_SOURCE_DIR}/extra/dobby/arm64-v8a/libdobby.a)
endif()

# Preserved final linking
target_link_libraries(
        zetalib
        ${log-lib}
        z
)

# Added Android 10+ compatibility flags
if(ANDROID_PLATFORM VERSION_GREATER_EQUAL 29)
    add_definitions(-DANDROID_Q_PLUS=1)
    add_compile_options(-fstack-protector-strong)
endif()