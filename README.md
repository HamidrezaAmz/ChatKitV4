# ChatKitV4 
This is our new version of chatkit. 

[![API](https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=23)
[![](https://jitpack.io/v/HamidrezaAmz/ChatKitV4.svg)](https://jitpack.io/#HamidrezaAmz/ChatKitV4)
[![](https://jitpack.io/v/HamidrezaAmz/ChatKitV4/month.svg)](https://jitpack.io/#HamidrezaAmz/ChatKitV4)

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

#### Note 1: 
CHAT_ID is a string hash used as Id, this will help us to seprate chats and use this library on more than one chat view

#### Note 2: 
```initializeChatKitV4ViewModel()``` Is a **suspend** function, so you should call It inside a coroutine scope ;)

## Callbacks
We have impelement some interfaces to receive use intractions with chat view and chat messages. If you need to use them, you can use it in this way:

```bash
chatKitV4.initializeChatKitV4ListCallback(this)
chatKitV4.initializeChatKitV4InputCallback(this)
```

## UI
In this version of our ChatKit series, we seprate our UI layer and core layer. we have implement some sort of default UI XML and use it as default chat item UI, but If you want to use your own UI as row items of chat kit you can initialize and overide chat kit xmls. Just use our default ids to make library find and use your custom UI elements. 


| layout id  | usage |
| ------------- | ------------- |
| self_text_layout_id  | This layout reference used for **client** side **text** style chat view  |
| self_voice_layout_id  | This layout reference used for **client** side **voice** style chat view  |
| other_text_layout_id  | This layout reference used for **other** side **text** style chat view  |
| other_voice_layout_id  | This layout reference used for **other** side **voice** style chat view  |


## UI - THEME
Do not forget to implement ChatKitV4.theme, you can customize each section of **ChatKit** 
```xml
    <style name="ChatKitV4.Theme.Custom" parent="ChatKitV4.Theme">
        <item name="self_text_layout_id">@layout/self_text_layout</item>
        <item name="self_voice_layout_id">@layout/self_voice_layout</item>
        <item name="other_text_layout_id">@layout/other_text_layout</item>
        <item name="other_voice_layout_id">@layout/other_voice_layout</item>
        <item name="chatkitv4_input_background">@color/lightWhite</item>
        <item name="chatkitv4_input_send_button_icon">@drawable/ic_baseline_send_24</item>
        <item name="chatkitv4_input_send_button_background">@drawable/shape_circle</item>
        <item name="chatkitv4_input_edittext_background">@drawable/shape_rectangle_round_corner</item>
        <item name="chatkitv4_input_edittext_hint">@string/chatkitv4_input_hint</item>
        <item name="chatkitv4_list_empty_view_icon">@drawable/ic_baseline_shopping_basket_24</item>
        <item name="chatkitv4_list_empty_view_icon_color">@color/red</item>
        <item name="chatkitv4_list_empty_view_hint">@string/chatkitv4_list_empty_view_hint</item>
        <item name="chatkitv4_list_empty_view_hint_color">@color/black</item>
        <item name="chatkitv4_list_error_view_icon">@drawable/ic_baseline_sentiment_very_dissatisfied_24</item>
        <item name="chatkitv4_list_error_view_icon_color">@color/default_list_error_view_icon_color</item>
        <item name="chatkitv4_list_error_view_hint">@string/title_chatkitv4_list_error_view_hint</item>
        <item name="chatkitv4_list_error_view_hint_color">@color/teal</item>
    </style>
```



