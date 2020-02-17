# Android Encryption Helper [![Release](https://jitpack.io/v/talhahasanzia/android-encryption-helper.svg)](https://jitpack.io/#talhahasanzia/android-encryption-helper/0.3.1)  [![GitHub issues](https://img.shields.io/github/issues/talhahasanzia/android-encryption-helper.svg)](https://github.com/talhahasanzia/android-encryption-helper/issues)   [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)


`[DEPRECATED]` Project no longer maintained. See [Jetpack Security](https://developer.android.com/jetpack/androidx/releases/security) (requires minimum API 23)


Simple Encryption-Decryption Library containing helper classes for encrypting text data in Android. This will alow apps to decrypt data on runtime only (thanks to KeyStore API), making data storage more secure against concerns like reversing apk or decompilation of jars.


## Release
Available Version:  [0.3.1](https://github.com/talhahasanzia/android-encryption-helper/releases/tag/0.3.1) on [jitpack.io](https://jitpack.io/#talhahasanzia/android-encryption-helper/0.3.1) 


## Library Source
[Jump to library source.](https://github.com/talhahasanzia/android-encryption-helper/tree/master/SQLcipherDemo/keystorehelper)

## Getting Started
### Prerequisites

Minimum API Level is 18. Since this helper is based off Keystore API.

### Adding the library

In your project level gradle, add:
```
    maven { url "http://jitpack.io" }
```

In your app level gradle **(4.0+)**, add:
```
    implementation 'com.github.talhahasanzia:android-encryption-helper:0.3.1'
```
for gradle versions **below 4.0** use:
```
    compile 'com.github.talhahasanzia:android-encryption-helper:0.3.1'
```
## Using in your project

* **Step 1- KeyStoreManager initialization:** Initialize KeyStoreManager using init()
```
    KeyStoreManager.init(context);
```
* **Step 2- Some Data:** Prepare data to be encrypted, in case of generating some random data use static method getNewRandomPhrase( numberOfChars ):
```
    KeyStoreManager.getNewRandomPhrase(5);
```
* **Step 3- Encrypt:** Call static encrypt with specified Alias to start encryption.
```
    String encrypt=KeyStoreManager.encryptData(randomPhrase,"testAlias");
```
* **Step 4- Saving Encrypted Data:** Now you can save this encrypted text in a file, SharedPrefs or as DB entry. This can be used later anytime by the app. And only this device specific instance of app will be able to decrypt it. (Securely)


* **Step 5- Decrypt:** Once saving mechanism is decided, retrieve encrypted data and call decrypt which can be used to make data available in its original form again.
```
    String decrypt=KeyStoreManager.decryptData(encrypt,"testAlias");
```

### How this works?
* Each data (String value) is encryted using RSA public key.
* Each data (String value) is decrypted using RSA private key.
* Keystore provides mechanism to generate these RSA keys.
* Provide an alias (a unique value) as string which will be used by keystore to save RSA keys against. (alias=key to retrieve RSA entry)
* Call same alias to encrypt and decrypt. Otherwise the operation will fail.
* If there are no RSA keys against given alias, code will create it.
* Its important remember the alias so you get right RSA keys everytime.

## Demo Project

The demo project implements a use-case where database password is securely saved in shared preferences in encrypted form. No one can open database without decrypted password. This encrypted text can only be decrypted at runtime using private keys provided by KeyStore API against specified alias.

* [Encryption Sample](https://github.com/talhahasanzia/android-encryption-helper/tree/master/encryption-samples) (Library project)
* [Simple Encryption Demo](https://github.com/talhahasanzia/simplified-demo/) [OUTDATED]

## Participating in Discussion
Head over to issues section to find out about current bugs, feature updates or release discussion.
Also report any bug, or request a feature using right labels.
To find out how these labels are meant to be used see [Labels.md](https://github.com/talhahasanzia/android-encryption-helper/blob/master/Labels.md) for detailed explanation of each label.

## Contributing

Contributions are welcomed as long as they dont break the code. Please create an issue and have a discussion before pull request.

## Hosting

Thanks to jitpack.io! Hosted at: https://jitpack.io/#talhahasanzia/android-encryption-helper/

## Authors

* **Talha** - *Initial work* - [@talhahasanzia](https://github.com/talhahasanzia)

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE.md](https://github.com/talhahasanzia/android-encryption-helper/blob/master/LICENSE) file for details.

*Sources from Android and Android APIs are subject to the licensing terms as per Android Open Source Project (AOSP).*

## Acknowledgments

* Inspiration : To avoid hefty work involved in using Keystore API.
* Thanks to [afollestad](https://github.com/afollestad/android-secure-storage/blob/master/library/src/main/java/com/afollestad/androidsecurestorage/RxSecureStorage.java) for Cipher initialization method.
