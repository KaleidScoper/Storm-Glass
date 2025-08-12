# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 保留网络相关类
-keep class com.stormglass.weather.api.** { *; }
-keep class com.stormglass.weather.model.** { *; }
-keep class com.stormglass.weather.viewmodel.** { *; }

# 保留Retrofit相关类
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# 保留OkHttp相关类
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# 保留Gson相关类
-keep class com.google.gson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# 保留LiveData相关类
-keep class androidx.lifecycle.** { *; }

# 保留日志输出
-keepattributes SourceFile,LineNumberTable
