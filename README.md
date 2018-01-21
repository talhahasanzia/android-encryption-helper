# Android Encryption Helper  [![Code Climate](https://img.shields.io/codeclimate/issues/github/me-and/mdf.svg)](https://github.com/talhahasanzia/android-encryption-helper/issues)   [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)


Simple Encryption-Decryption Helper classes for storing text data securely in Android.

## Getting Started

In your project level gradle, add:
```
maven { url "http://jitpack.io" }
```

In your app level gradle, add:
```
implementation 'com.github.talhahasanzia:android-encryption-helper:v0.1d'
```
for gradle below 3.0 use "compile" instead of "implementation".


### Prerequisites

Minimum API Level is 18. Since this helper is based off Keystore API.

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
