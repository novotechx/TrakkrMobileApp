# Trakkr ProGuard rules
-keep class com.novotech.trakkr.** { *; }
-keep class org.maplibre.** { *; }
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-dontwarn org.maplibre.**
