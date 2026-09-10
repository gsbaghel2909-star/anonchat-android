# Building the app in GitHub Codespaces (copy-paste commands)

Once your Codespace terminal is open (a black text window at the bottom of the browser tab), copy and paste these commands **one block at a time**, pressing Enter after each, and waiting for it to finish before pasting the next.

## 1. Install Gradle (the tool that builds Android apps)
```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install gradle 8.9
```
When it asks "Do you want gradle to be set as default? (Y/n)", type `Y` and press Enter.

## 2. Install the Android SDK command-line tools
```bash
mkdir -p ~/android-sdk/cmdline-tools
cd ~/android-sdk/cmdline-tools
curl -o cmdtools.zip https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip
unzip -q cmdtools.zip
mv cmdline-tools latest
cd ~
export ANDROID_HOME=$HOME/android-sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools
yes | sdkmanager --licenses
sdkmanager "platform-tools" "platforms;android-35" "build-tools;35.0.0"
```
This step downloads a few hundred MB — it may take a couple of minutes. That's normal.

## 3. Add your Supabase keys
```bash
cd /workspaces/anonchat-android
echo "SUPABASE_URL=https://nsarcnncamprfunpcheq.supabase.co" >> local.properties
echo "SUPABASE_ANON_KEY=PASTE_YOUR_ANON_KEY_HERE" >> local.properties
echo "sdk.dir=$HOME/android-sdk" >> local.properties
```
Replace `PASTE_YOUR_ANON_KEY_HERE` with the anon key you copied from Supabase (Project Settings → API). If your repository name is different from `anonchat-android`, adjust the `cd` path to match.

## 4. Build the app
```bash
gradle assembleDebug
```
This will take a few minutes the first time. When it's done, you'll see `BUILD SUCCESSFUL`.

## 5. Find and download the finished app file
The built file will be at:
```
app/build/outputs/apk/debug/app-debug.apk
```
In the file explorer on the left side of the Codespaces window, navigate to that folder, right-click `app-debug.apk`, and choose **Download**. It'll save to your laptop's Downloads folder.

## 6. Get it onto your phone
Easiest options:
- Email the `app-debug.apk` file to yourself and open the email on your phone, or
- Upload it to Google Drive and open the Drive app on your phone, or
- Use a USB cable to copy it to your phone's Downloads folder.

On your phone, tap the file to install it. Android will likely warn "install from unknown sources" — this is expected for an app not from the Play Store; tap **Settings** in that warning, allow it for your file manager/browser app, then go back and tap Install.
