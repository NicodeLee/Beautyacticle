# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\Android\adt-bundle-windows-x86_64-20140321\adt-bundle-windows-x86_64-20140321\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

-keep class android.webkit.** { *; }
-dontwarn android.webkit.**

-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keep public class com.nicodelee.beautyarticle.R$*{
		public static final int *;
}

-keep class com.nicodelee.beautyarticle.** { *;}

# Keep the support library
-keep class android.support.** { *; }
-keep interface android.support.** { *; }

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Remove logging calls
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# For Guava:
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe

# For RxJava:
-dontwarn org.mockito.**
-dontwarn org.junit.**
-dontwarn org.robolectric.**

-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-dontwarn okio.**
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

-dontwarn retrofit.**
-dontwarn retrofit.appengine.UrlFetchClient
-keep class retrofit.** { *; }
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}


-dontwarn rx.**

## 使用注解
-keepattributes *Annotation*,Signature
#
## 保持混淆时类的实名及行号(——————— 调试时打开 ———————)
-keepattributes SourceFile,LineNumberTable
#
# 枚举需要keep see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    **[] $VALUES;
    public *;
}

#dbflow
-keep class com.raizlabs.android.dbflow.config.GeneratedDatabaseHolder

-dontwarn uk.co.senab.photoview.**
-keep class uk.co.senab.photoview.** { *;}

# Crashlytics 1.+

-keep class com.crashlytics.** { *; }
-keepattributes SourceFile,LineNumberTable

## ----------------------------------
##      sharesdk
## ----------------------------------
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}
-dontwarn cn.sharesdk.**
-dontwarn **.R$*
-keep class android.net.http.SslError
-keep class android.webkit.**{*;}
-keep class cn.sharesdk.**{*;}
-keep class m.framework.**{*;}

#指定不混淆所有的JNI方法
-keepclasseswithmembernames class * {
    native <methods>;
}

-keep class com.commonsware.cwac.cam2.**{*;}
-dontwarn com.commonsware.cwac.cam2.**
-keep class jp.co.cyberagent.android.gpuimage.**{*;}
-dontwarn jp.co.cyberagent.android.gpuimage.**
-keep class com.github.clans.fab.**{*;}
-dontwarn com.github.clans.fab.**
-keep class com.nostra13.universalimageloader.** { *; }
-keepclassmembers class com.nostra13.universalimageloader.** {*;}