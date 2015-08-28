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
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-keep class butterknife.** { *; }
#-dontwarn butterknife.internal.**
#-keep class **$$ViewBinder { *; }
#
#-keepclasseswithmembernames class * {
#    @butterknife.* <fields>;
#}
#
#-keepclasseswithmembernames class * {
#    @butterknife.* <methods>;
#}
#
#-keep public class com.nicodelee.beautyarticle.R$*{
#		public static final int *;
#}
#
## Keep the support library
#-keep class android.support.** { *; }
#-keep interface android.support.** { *; }
#
## Gson specific classes
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
#
#-keepclassmembers enum * {
#    public static **[] values();
#    public static ** valueOf(java.lang.String);
#}
#
#-keep class * implements android.os.Parcelable {
#  public static final android.os.Parcelable$Creator *;
#}
#
#-dontwarn retrofit.**
#-keep class retrofit.** { *; }
#-keepclasseswithmembers class * {
#    @retrofit.http.* <methods>;
#}
#
#-keep class sun.misc.Unsafe { *; }
#
#-dontwarn java.lang.invoke.*
#
## 使用注解
#-keepattributes *Annotation*,Signature
#
## 保持混淆时类的实名及行号(——————— 调试时打开 ———————)
#-keepattributes SourceFile,LineNumberTable
#
## 枚举需要keep see http://proguard.sourceforge.net/manual/examples.html#enumerations
#-keepclassmembers enum * {
#    **[] $VALUES;
#    public *;
#}
#
##shareSDK
#-keep class cn.sharesdk.**{*;}
#-keep class com.sina.**{*;}
#-keep class **.R