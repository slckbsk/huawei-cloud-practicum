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


## Android SDK

https://support.huaweicloud.com/intl/en-us/sdk-android-devg-obs/obs_26_1801.html

## Object Storage Service 3.0 (OBS) 3.21.9h&s

https://doc.hcs.huawei.com/obs/doc/download/pdf/obs-usermanual.pdf

## How Do I Preview Objects in OBS Through a Browser?

https://support.huaweicloud.com/intl/en-us/obs_faq/obs_03_0087.html
- I Used second solution => "User-Defined Domain Name and CDN"

## Image Tagging SDK
https://developer.huaweicloud.com/intl/en-us/sdk?IMAGE

## Working Example (Short video)
https://www.loom.com/share/8e3417c85a4d4b36b504cb18cbf8e029

 Not: I didn't get AK and SK from edit text box, instead of this, I created Huawei Account information in java class and I pulled them, the reason is every each time I didn't want to fill the edit texts  :)


## Screenshots
https://github.com/slckbsk/huawei-cloud-practicum/tree/main/Project-Image



        
        
