package ir.vasl.chatkitv4core.view

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.vasl.chatkitv4core.databinding.LayoutChatkitV4ListBinding
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.util.ChatStyle
import ir.vasl.chatkitv4core.view.adapter.ChatKitV4LoadStateAdapter
import ir.vasl.chatkitv4core.view.adapter.ChatKitV4MessageAdapter
import ir.vasl.chatkitv4core.view.interfaces.ChatKitV4ListCallback
import ir.vasl.chatkitv4core.viewmodel.ChatKitV4ViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest

class ChatKitV4List @kotlin.jvm.JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val TAG = "ChatKitV4List"

    private var binding = LayoutChatkitV4ListBinding.inflate(LayoutInflater.from(context))

    private lateinit var chatStyle: ChatStyle

    private lateinit var chatKitV4MessageAdapter: ChatKitV4MessageAdapter

    private lateinit var chatKitV4ViewModel: ChatKitV4ViewModel

    private lateinit var jobChatKit: Job

    private lateinit var chatKitV4ListCallback: ChatKitV4ListCallback

    init {
        addView(binding.root)
        initializeParsers(attrs)
        initializeChatKitV4List()
        initializeChatKitV4Listeners()
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
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!recyclerView.canScrollVertically(1) && !recyclerView.canScrollVertically(-1)) {
                    return
                } else if (!recyclerView.canScrollVertically(-1)) {
                    if (::chatKitV4ListCallback.isInitialized)
                        chatKitV4ListCallback.onReachedToEnd()
                }
            }
        })
    }

    suspend fun initializeChatKitV4ListViewModel(inputChatKitV4ViewModel: ChatKitV4ViewModel) {
        this.chatKitV4ViewModel = inputChatKitV4ViewModel
        this.chatKitV4ViewModel.messageListFromDatabase.collectLatest {
            chatKitV4MessageAdapter.submitData(it)
        }
    }

    private fun showEmptyView(showEmptyList: Boolean) {
        binding.includeEmptyView.root.isVisible = showEmptyList
    }

    private fun showErrorView(showErrorView: Boolean) {
        binding.includeErrorView.root.isVisible = showErrorView
    }

    fun initializeChatKitV4ListCallback(chatKitV4ListCallback: ChatKitV4ListCallback) {
        this.chatKitV4ListCallback = chatKitV4ListCallback
        this.chatKitV4MessageAdapter.initializeChatKitV4ListCallback(chatKitV4ListCallback)
    }

    fun addMessageModelIntoDatabase(messageModel: MessageModel) {
        chatKitV4ViewModel.addMessageIntoDatabase(messageModel)
        moveToStartOfChatKitV4() // simulate move list to the end
    }

    fun addMessageModelListIntoDatabase(messageModelList: List<MessageModel>) {
        chatKitV4ViewModel.addMessageListIntoDatabase(messageModelList)
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