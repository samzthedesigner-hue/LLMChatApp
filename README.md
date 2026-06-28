# LLMChatApp 🤖

An Android chatbot app that talks to a `gpt2` model running on Google Colab via ngrok. 

Built 100% on mobile with GitHub Actions. No Android Studio or PC needed.

### **Features**
- **Kotlin + Retrofit**: Connects to your Colab `/chat` API 
- **Local Memory**: Remembers last 3 messages in the session
- **0 PC Build**: APK compiles automatically in the cloud
- **Lightweight**: Uses `gpt2` 124MB model. Mobile data friendly.

### **How to Use**
1.  **Run Colab**: Start your `gpt2` FastAPI server in Colab and copy the `ngrok` link.
2.  **Edit URL**: Open `app/src/main/java/com/example/llmchat/MainActivity.kt` and replace `BASE_URL` with your ngrok link.
3.  **Build APK**: Push the change. GitHub `Actions` tab will build it in ~5 mins.
4.  **Install**: Download `app-debug.zip` from `Artifacts` -> Extract -> Install `.apk` on your phone.

### **Important Notes**
1.  **Ngrok Link Expires**: When Colab stops, your app will stop. Update `BASE_URL` + rebuild.
2.  **Model**: `gpt2` is small. For better answers use prompts like: `Q: ... A:` or `You are a teacher. Student: ... Teacher:`
3.  **Data**: First build uses ~2MB to upload. APK is ~8MB to download.

### **Tech Stack**
`Kotlin` `Android` `Retrofit` `FastAPI` `gpt2` `GitHub Actions`
