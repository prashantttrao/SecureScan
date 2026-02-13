# SecureScan

**Native Android Vulnerability & Permission Analyzer**

SecureScan is an Android security utility designed to audit installed applications for privacy risks. It provides immediate visibility into application behaviors by flagging high-risk permissions and identifying legacy applications that target outdated Android SDKs to bypass modern security protocols.

## Key Features

* **Real-Time Audit:** Scans all user-installed applications to retrieve manifest data.
* **Risk Scoring Engine:**
    * **High Risk:** Identifies apps requesting sensitive permissions (Camera, Location, Microphone) combined with outdated target SDKs.
    * **Legacy Detection:** Specifically flags apps targeting Android 9 (API 28) or lower.
* **Deep-Link Mitigation:** Utilizes `Settings.ACTION_APPLICATION_DETAILS_SETTINGS` to provide direct access to system settings for uninstalling or force-stopping applications.
* **Offline Operation:** Runs entirely locally with no internet permission requested.

## Technical Architecture

The application is built using a native Android architecture.

* **Language:** Java
* **Build System:** Gradle
* **UI:** XML Layouts (Material Design)
* **Core API:** `PackageManager` (utilizing `QUERY_ALL_PACKAGES` permission)

**Risk Logic:**
The core analysis evaluates applications based on a heuristic model:
1.  **SDK Version Check:** Checks `applicationInfo.targetSdkVersion`.
2.  **Permission Analysis:** Iterates through `requestedPermissions` arrays to identify dangerous permission constants.

## Installation

Download the latest signed APK from the [Releases Page](https://github.com/prashantttrao/SecureScan/releases).

**Build from Source:**
1.  Clone the repository.
2.  Open in Android Studio.
3.  Sync Gradle project.
4.  Run on device/emulator.

## Team

* **Prashant Rao** - Lead Developer
* **Soham Wankhade** - Frontend Engineer
## License

This project is licensed under the MIT License.
