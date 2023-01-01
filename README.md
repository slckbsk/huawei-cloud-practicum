# huawei-cloud-practicum
huawei-cloud-practicum-final-case

-Android Studio-

   dependencies {
   
    implementation 'com.huaweicloud:esdk-obs-android:3.21.12'
    implementation files('libs/java-sdk-core-3.0.6.jar')
    implementation 'com.huaweicloud.sdk:huaweicloud-sdk-image:3.1.18'  => For Image Taggin
    
}

    repositories {
        gradlePluginPortal()
        google()
        maven {
            url 'https://mirrors.huaweicloud.com/repository/maven/'
        }
        mavenCentral()
    }

   Manifest Permission
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        tools:ignore="ScopedStorage" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.GET_TOP_ACTIVITY_INFO"
        tools:ignore="ProtectedPermissions" />
        
        
