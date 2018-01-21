# Android Encryption Helper  [![GitHub issues](https://img.shields.io/github/issues/talhahasanzia/android-encryption-helper.svg)](https://github.com/talhahasanzia/android-encryption-helper/issues)   [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)



Simple Encryption-Decryption Helper classes for storing text data securely in Android.


## Release
Available Version:  [v0.1d](https://github.com/talhahasanzia/android-encryption-helper/releases/tag/v0.1d) on [jitpack.io](https://jitpack.io/#talhahasanzia/android-encryption-helper/v0.1d) 

## Getting Started
### Prerequisites

Minimum API Level is 18. Since this helper is based off Keystore API.

### Adding the library

In your project level gradle, add:
```
    maven { url "http://jitpack.io" }
```

In your app level gradle (4.0), add:
```
    implementation 'com.github.talhahasanzia:android-encryption-helper:v0.1d'
```
for gradle below 3.0 use:
```
    compile 'com.github.talhahasanzia:android-encryption-helper:v0.1d'
```
### Using in your project

* **Step 1 KeyStoreManager object:** Get an instance to KeyStoreManager using getInstance()
```
    KeyStoreManager keyStoreManager=KeyStoreManager.getInstance(context);
```
* **Step 2 Some Data:** Prepare data to be encrypted, in case of generating some random data use:
```
    String randomPhrase=keyStoreManager.getNewRandomPhrase();
```
* **Step 3 Encrypt:** Call encrypt with specified Alias to start encryption.
```
    String encrypt=keyStoreManager.encryptData(randomPhrase,"testAlias");
```
* **Step 4 Saving Encrypted Data:** Now you can save this encrypted text in a file, SharedPrefs or as DB entry. This can be used later anytime by the app. And only this device specific instance of app will be able to decrypt it. (Securely)


* **Step 5 Decrypt:** Once saving mechanism is decided, retrieve encrypted data and call decrypt which can be used to make data available in its original form again.
```
    String decrypt=keyStoreManager.decryptData(encrypt,"testAlias");
```

### How this works?
* Each data (String value) is encryted using RSA public key.
* Each data (String value) is decrypted using RSA private key.
* Keystore provides mechanism to generate these RSA keys.
* Provide an alias (a unique value) as string which will be used by keystore to save RSA keys against. (alias=key to retrieve RSA entry)
* Call same alias to encrypt and decrypt. Otherwise the operation will fail.
* If there are no RSA keys against given alias, code will create it.
* Its important remember the alias so you get right RSA keys everytime.


## Contributing

Contributions are welcomed as long as they dont break the code. Please create an issue and have a discussion before pull request.

## Hosting

Thanks to jitpack.io! Hosted at: https://jitpack.io/#talhahasanzia/android-encryption-helper/

## Authors

* **Talha** - *Initial work* - [@talhahasanzia](https://github.com/talhahasanzia)

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE.md](https://github.com/talhahasanzia/android-encryption-helper/blob/master/LICENSE) file for details

## Acknowledgments

* Inspiration : To avoid hefty work involved in using Keystore API.
* Thanks to [afollestad](https://github.com/afollestad/android-secure-storage/blob/master/library/src/main/java/com/afollestad/androidsecurestorage/RxSecureStorage.java) for Cipher initialization method.
