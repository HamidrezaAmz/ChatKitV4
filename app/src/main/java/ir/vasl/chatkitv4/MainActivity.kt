package ir.vasl.chatkitv4

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import ir.vasl.chatkitv4.databinding.ActivityMainBinding
import ir.vasl.chatkitv4.helpers.LocaleHelper
import ir.vasl.chatkitv4.utils.PublicValue
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageContentType
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageOwnerType
import ir.vasl.chatkitv4core.util.generator.SampleChatDataGenerator
import ir.vasl.chatkitv4core.util.generator.SampleRandomIdGenerator
import ir.vasl.chatkitv4core.view.interfaces.ChatKitV4InputCallback
import ir.vasl.chatkitv4core.view.interfaces.ChatKitV4ListCallback
import ir.vasl.chatkitv4core.viewmodel.ChatKitV4ViewModel
import ir.vasl.chatkitv4core.viewmodel.factory.ChatKitV4ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ChatKitV4ListCallback, ChatKitV4InputCallback {

    private val TAG = "MainActivity"

    lateinit var binding: ActivityMainBinding

    private lateinit var chatKitV4ViewModel: ChatKitV4ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        LocaleHelper.setApplicationLanguage(this)

        initializeChatKitV4()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete_database -> {

                binding.chatKitV4.clearAll()
                Toast.makeText(
                    this@MainActivity,
                    "Database deleted!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            R.id.action_add_server_text_message -> {

                binding.chatKitV4.addNewMessageIntoChatKitV4(
                    SampleChatDataGenerator.getMessage(
                        chatId = PublicValue.CHAT_ID,
                        messageOwnerType = MessageOwnerType.OTHER,
                        messageContentType = MessageContentType.TEXT
                    )
                )
            }

            R.id.action_add_server_voice_message -> {

                binding.chatKitV4.addNewMessageIntoChatKitV4(
                    SampleChatDataGenerator.getMessage(
                        chatId = PublicValue.CHAT_ID,
                        messageOwnerType = MessageOwnerType.OTHER,
                        messageContentType = MessageContentType.VOICE
                    )
                )
            }

            R.id.action_add_client_text_message -> {

                binding.chatKitV4.addNewMessageIntoChatKitV4(
                    SampleChatDataGenerator.getMessage(
                        chatId = PublicValue.CHAT_ID,
                        messageOwnerType = MessageOwnerType.SELF,
                        messageContentType = MessageContentType.TEXT
                    )
                )
            }

            R.id.action_add_client_voice_message -> {

                binding.chatKitV4.addNewMessageIntoChatKitV4(
                    SampleChatDataGenerator.getMessage(
                        chatId = PublicValue.CHAT_ID,
                        messageOwnerType = MessageOwnerType.SELF,
                        messageContentType = MessageContentType.VOICE
                    )
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initializeChatKitV4() {

        chatKitV4ViewModel = ViewModelProvider(this, ChatKitV4ViewModelFactory(
                application = application,
                chatId = PublicValue.CHAT_ID
            )
        ).get(ChatKitV4ViewModel::class.java)

        binding.chatKitV4.initializeChatKitV4ListCallback(this@MainActivity)
        binding.chatKitV4.initializeChatKitV4InputCallback(this@MainActivity)

        lifecycleScope.launch {
            binding.chatKitV4.initializeChatKitV4ViewModel(chatKitV4ViewModel)
        }
    }

    override fun onReachedToEnd() {
        Log.i(TAG, "MainActivity --> onReachedToEnd()")
    }

    override fun onMessageClicked(messageModel: MessageModel?) {
        Log.i(TAG, "onMessageClicked: messageModel --> ${messageModel.toString()}")
    }

    override fun onMessagePressed(messageModel: MessageModel?) {
        Log.i(TAG, "onMessagePressed: messageModel --> ${messageModel.toString()}")
    }

    override fun onSubmitNewMessage(message: String) {
        Log.i(TAG, "onSubmitNewMessage: message --> $message")
        val messageModel = MessageModel(
            id = SampleRandomIdGenerator.getRandomIntId(),
            createAt = System.currentTimeMillis(),
            subTitle = message,
            date = "12:00",
            messageOwnerType = MessageOwnerType.SELF.name,
            messageContentType = MessageContentType.TEXT.name,
            chatId = PublicValue.CHAT_ID
        )

        binding.chatKitV4.addNewMessageIntoChatKitV4(messageModel)
    }

}