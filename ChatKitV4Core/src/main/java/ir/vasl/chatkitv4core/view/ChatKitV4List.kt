package ir.vasl.chatkitv4core.view

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.vasl.chatkitv4core.databinding.LayoutChatkitV4ListBinding
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageConditionStatus
import ir.vasl.chatkitv4core.util.ChatStyle
import ir.vasl.chatkitv4core.util.helper.ChatKitV4MediaHelper
import ir.vasl.chatkitv4core.util.player.interfaces.MediaHelperCallback
import ir.vasl.chatkitv4core.view.adapter.ChatKitV4LoadStateAdapter
import ir.vasl.chatkitv4core.view.adapter.ChatKitV4MessageAdapter
import ir.vasl.chatkitv4core.view.interfaces.ChatKitV4ListAdapterCallback
import ir.vasl.chatkitv4core.view.interfaces.ChatKitV4ListCallback
import ir.vasl.chatkitv4core.viewmodel.ChatKitV4ViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest

class ChatKitV4List @kotlin.jvm.JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    ChatKitV4ListAdapterCallback,
    MediaHelperCallback {

    private val TAG = "ChatKitV4List"

    private var binding = LayoutChatkitV4ListBinding.inflate(LayoutInflater.from(context))

    private lateinit var chatStyle: ChatStyle
    private lateinit var chatKitV4ViewModel: ChatKitV4ViewModel
    private lateinit var chatKitV4MessageAdapter: ChatKitV4MessageAdapter
    private lateinit var chatKitV4ListCallback: ChatKitV4ListCallback
    private lateinit var jobChatKit: Job

    private val chatKitV4MediaHelper = ChatKitV4MediaHelper(this)

    init {
        addView(binding.root)
        initializeParsers(attrs)
        initializeChatKitV4List()
        initializeChatKitV4Listeners()
    }

    override fun onMessageClicked(messageModel: MessageModel?) {
        if (::chatKitV4ListCallback.isInitialized)
            chatKitV4ListCallback.onMessageClicked(messageModel)
    }

    override fun onMessagePressed(messageModel: MessageModel?) {
        if (::chatKitV4ListCallback.isInitialized)
            chatKitV4ListCallback.onMessagePressed(messageModel)
    }

    override fun onPlayPauseClicked(messageModel: MessageModel?) {

        if (::chatKitV4ListCallback.isInitialized)
            chatKitV4ListCallback.onPlayPauseClicked(messageModel)

        messageModel?.let {
            // update item in database
            chatKitV4ViewModel.updateMessageModel(it)

            // sync new states with player
            val playerCondition = messageModel.messageConditionStatus
            if (playerCondition == MessageConditionStatus.PLAYER_STARTED.name)
                chatKitV4MediaHelper.playVoice(messageModel)
            else
                chatKitV4MediaHelper.stopVoice()
        }
    }

    override fun onFileClicked(messageModel: MessageModel?) {
        if (::chatKitV4ListCallback.isInitialized)
            chatKitV4ListCallback.onFileClicked(messageModel)
    }

    override fun onDownloadFileClicked(messageModel: MessageModel?) {
        if (::chatKitV4ListCallback.isInitialized)
            chatKitV4ListCallback.onDownloadFileClicked(messageModel)

        messageModel?.let {
            // update item in database
            chatKitV4ViewModel.updateMessageModel(it)

            // sync new states with player
            val downloaderCondition = messageModel.messageConditionStatus
            if (downloaderCondition == MessageConditionStatus.DOWNLOAD_STARTED.name) {
                chatKitV4MediaHelper.downloadFile(messageModel)
            } else {
                chatKitV4MediaHelper.stopDownloadFile(messageModel)
            }
        }
    }

    override fun onUploadFileClicked(messageModel: MessageModel?) {
        if (::chatKitV4ListCallback.isInitialized)
            chatKitV4ListCallback.onUploadFileClicked(messageModel)
    }

    override fun onPreviewFileClicked(messageModel: MessageModel?) {
        if (::chatKitV4ListCallback.isInitialized)
            chatKitV4ListCallback.onPreviewFileClicked(messageModel)
    }

    override fun onMessageModelChanged(messageModel: MessageModel?) {
        if (::chatKitV4ListCallback.isInitialized)
            chatKitV4ListCallback.onMessageModelChange(messageModel)

        messageModel?.let {
            // update item in database
            chatKitV4ViewModel.updateMessageModel(it)
        }
    }

    override fun onMediaStateUpdated(
        messageModel: MessageModel,
        messageConditionStatus: MessageConditionStatus,
        progressPlayer: Int?,
        progressDownloader: Int?
    ) {
        messageModel.let {
            // update item in database
            Log.i(TAG, "onMediaStateUpdated: mediaHelperStatus -> ${messageConditionStatus.name}")

            // add delay on update | this will update the view! Really!!! :|
            Handler(Looper.getMainLooper()).postDelayed({
                chatKitV4ViewModel.updateMessageModel(it)
            }, 500)
        }
    }

    private fun initializeParsers(attributeSet: AttributeSet?) {
        this.chatStyle = ChatStyle.parse(context, attributeSet)
    }

    private fun initializeChatKitV4List() {
        this.chatKitV4MessageAdapter = ChatKitV4MessageAdapter(chatStyle)
        this.binding.recyclerView.adapter = chatKitV4MessageAdapter
        this.binding.recyclerView.setHasFixedSize(false)
        this.binding.recyclerView.isNestedScrollingEnabled = false
        this.binding.recyclerView.layoutManager = LinearLayoutManager(context).apply {
            orientation = LinearLayoutManager.VERTICAL
            stackFromEnd = true
            isSmoothScrollbarEnabled = false
        }
        this.binding.recyclerView.itemAnimator = null
        this.binding.recyclerView.adapter = chatKitV4MessageAdapter.withLoadStateHeaderAndFooter(
            header = ChatKitV4LoadStateAdapter { chatKitV4MessageAdapter.retry() },
            footer = ChatKitV4LoadStateAdapter { chatKitV4MessageAdapter.refresh() }
        )
        this.chatKitV4MessageAdapter.addLoadStateListener { loadState ->
            // show empty view if init data is empty
            val showEmptyView =
                loadState.refresh is LoadState.NotLoading && chatKitV4MessageAdapter.itemCount == 0
            showEmptyView(showEmptyView)

            // Show the retry state if initial load or refresh fails
            val showErrorView =
                loadState.source.refresh is LoadState.Error && chatKitV4MessageAdapter.itemCount == 0
            showErrorView(showErrorView)
        }
    }

    private fun initializeChatKitV4Listeners() {
        this.binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                if (!recyclerView.canScrollVertically(1) && !recyclerView.canScrollVertically(-1)) {
                    return
                } else if (!recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (::chatKitV4ListCallback.isInitialized) {
                        val currListItems = chatKitV4MessageAdapter.snapshot().items
                        if (currListItems.isNotEmpty()) {
                            val lastItem = currListItems[currListItems.size - 1]
                            chatKitV4ListCallback.onReachedToEnd(lastItem)
                        }
                    }
                } else if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (::chatKitV4ListCallback.isInitialized) {
                        val currListItems = chatKitV4MessageAdapter.snapshot().items
                        if (currListItems.isNotEmpty()) {
                            val lastItem = currListItems[0]
                            chatKitV4ListCallback.onReachedToStart(lastItem)
                        }
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun showEmptyView(showEmptyList: Boolean) {
        binding.includeEmptyView.root.isVisible = showEmptyList
    }

    private fun showErrorView(showErrorView: Boolean) {
        binding.includeErrorView.root.isVisible = showErrorView
    }

    suspend fun initializeChatKitV4ListViewModel(inputChatKitV4ViewModel: ChatKitV4ViewModel) {
        this.chatKitV4ViewModel = inputChatKitV4ViewModel
        this.chatKitV4ViewModel.messageListFromDatabase.collectLatest {
            chatKitV4MessageAdapter.submitData(it)
        }
    }

    fun initializeChatKitV4ListCallback(chatKitV4ListCallback: ChatKitV4ListCallback) {
        this.chatKitV4ListCallback = chatKitV4ListCallback
        this.chatKitV4MessageAdapter.initializeChatKitV4ListCallback(this)
    }

    fun addMessageModelIntoDatabase(messageModel: MessageModel) {
        chatKitV4ViewModel.addMessageIntoDatabase(messageModel)
    }

    fun addMessageModelListIntoDatabase(messageModelList: List<MessageModel>) {
        chatKitV4ViewModel.addMessageListIntoDatabase(messageModelList)
    }

    fun updateMessageModel(messageModel: MessageModel) {
        chatKitV4ViewModel.updateMessageModel(messageModel)
    }

    fun updateMessageCondition(messageId: String, messageConditionStatus: MessageConditionStatus) {
        chatKitV4ViewModel.updateMessageCondition(messageId, messageConditionStatus)
    }

    fun clearAll() {
        chatKitV4ViewModel.clearAll()
    }

    fun moveToEndOfChatKitV4(delayTime: Long = 500) {
        Handler(Looper.getMainLooper()).postDelayed({
            binding.recyclerView.smoothScrollToPosition(0)
        }, delayTime)
    }

    fun moveToStartOfChatKitV4(delayTime: Long = 500) {
        val targetIndex = binding.recyclerView.adapter?.itemCount ?: -1
        Handler(Looper.getMainLooper()).postDelayed({
            binding.recyclerView.smoothScrollToPosition(targetIndex)
        }, delayTime)
    }

}