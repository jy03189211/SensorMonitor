cmake_minimum_required(VERSION 3.6.0)

# Specifies a library name, specifies whether the library is STATIC or
# SHARED, and provides relative paths to the source code. You can
# define multiple libraries by adding multiple add.library() commands,
# and CMake builds them for you. When you build your app, Gradle
# automatically packages shared libraries with your APK.

#Android NDK provides a static library named android_native_app_glue to help us execute callback functions and handle user inputs in a separate thread.
#[[
add_library(
        native_app_glue
        STATIC
        ${ANDROID_NDK}/sources/android/native_app_glue/android_native_app_glue.c )

# Specifies a path to native header files.
target_include_directories(
        native_app_glue
        PUBLIC
        ${ANDROID_NDK}/sources/android/native_app_glue)



set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -u ANativeActivity_onCreate")

add_library(
        native-lib
        SHARED
        src/main/cpp/native-lib.cpp)


# You need to link static libraries against your shared native library.
target_link_libraries(
        # Specifies the target library.
        native-lib
        android
        native_app_glue
        # Links the log library to the target library.
        ${log-lib} )
]]

find_library(
        # Defines the name of the path variable that stores the
        # location of the NDK library.
        log-lib

        # Specifies the name of the NDK library that
        # CMake needs to locate.
        log )

add_library(
        optimal_filtering-jni
        SHARED # not sure wether stati or dynamic lib is better
        src/main/cpp/optimal_filtering-jni.c)

include_directories(src/main/cpp/)

# Include libraries needed for optimal_filtering-jni lib
target_link_libraries(optimal_filtering-jni
        android
        log)