# ChatKitV4 (This read me is under develop)
This is our new version of chatkit. [![](https://www.jitpack.io/v/HamidrezaAmz/ChatKitV4.svg)](https://www.jitpack.io/#HamidrezaAmz/ChatKitV4)

Here we seprate **Core Logic** and **UI** sections. You can use it as seprate module into your applications. **ChatKitV4** can support all issues and needs related 
to chat scenarios and all sections that is based on chat like support sections, ticketing section and etc.

## Installation
ChatKitV4 is hosted on **[jitpack](https://www.jitpack.io/#HamidrezaAmz/ChatKitV4)**, you can find all versions on this repository. 
To get a Git project into your build:

**Step 1.** Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

```bash
allprojects {
        repositories {
            ...
            maven { url 'https://www.jitpack.io' }
        }
    }
```

**Step 2.** Add the dependency

```bash
dependencies {
    implementation 'com.github.HamidrezaAmz:ChatKitV4:{latest version}'
}
```

## Usage

Before you start using ChatKitV4, you should initialize It's view-model factory in this way:

```bash
var chatKitV4ViewModel = ViewModelProvider(this, ChatKitV4ViewModelFactory(
                application = application,
                chatId = PublicValue.CHAT_ID
            )
        ).get(ChatKitV4ViewModel::class.java)
chatKitV4.initializeChatKitV4ViewModel(chatKitV4ViewModel)
```
#### Note: CHAT_ID is a string hash used as Id, this will help us to seprate chats and use this library on more than one chat view

## Callbacks
We have impelement some interfaces to receive use intractions with chat view and chat messages. If you need to use them, you can use it in this way:

```bash
chatKitV4.initializeChatKitV4ListCallback(this)
chatKitV4.initializeChatKitV4InputCallback(this)
```


