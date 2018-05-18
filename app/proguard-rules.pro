# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/aashir/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Ignore Parse library extra requirements
-dontwarn com.squareup.okhttp.*
-dontwarn okio.*

# Keep SearchView class for Teachers Fragment
-keep class android.support.v7.widget.SearchView { *; }

# Keep Design Support Library usages
-keepattributes *Annotation*
-keep public class * extends android.support.design.widget.CoordinatorLayout.Behavior { *; }
-keep public class * extends android.support.design.widget.ViewOffsetBehavior { *; }

#-keepattributes *Annotation*
#-keep class com.parse.** { *; }
