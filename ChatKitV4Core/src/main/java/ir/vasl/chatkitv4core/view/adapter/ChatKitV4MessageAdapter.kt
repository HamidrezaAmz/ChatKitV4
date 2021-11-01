package ir.vasl.chatkitv4core.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ir.vasl.chatkitv4core.R
import ir.vasl.chatkitv4core.model.MessageModel
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageContentType
import ir.vasl.chatkitv4core.model.chatkitv4enums.MessageOwnerType
import ir.vasl.chatkitv4core.util.ChatStyle
import ir.vasl.chatkitv4core.view.adapter.viewholder.*
import ir.vasl.chatkitv4core.view.interfaces.ChatKitV4ListCallback

class ChatKitV4MessageAdapter(
    private var chatStyle: ChatStyle,
    private var chatKitV4ListCallback: ChatKitV4ListCallback? = null
) : PagingDataAdapter<MessageModel, RecyclerView.ViewHolder>(DIFF_UTILS) {

    companion object {
        val DIFF_UTILS = object : DiffUtil.ItemCallback<MessageModel>() {
            override fun areItemsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {

            // self messages scope
            MessageOwnerType.SELF_TEXT.ordinal -> {
                ViewHolderSelfTextMessage(
                    view = LayoutInflater.from(parent.context)
                        .inflate(chatStyle.selfTextLayoutId, parent, false),
                    chatKitV4ListCallback = chatKitV4ListCallback
                )
            }
            MessageOwnerType.SELF_VOICE.ordinal -> {
                ViewHolderSelfVoiceMessage(
                    view = LayoutInflater.from(parent.context)
                        .inflate(chatStyle.selfVoiceLayoutId, parent, false),
                    chatKitV4ListCallback = chatKitV4ListCallback
                )
            }
            MessageOwnerType.SELF_VIDEO.ordinal -> {
                ViewHolderSelfTextMessage(
                    view = LayoutInflater.from(parent.context)
                        .inflate(chatStyle.selfTextLayoutId, parent, false),
                    chatKitV4ListCallback = chatKitV4ListCallback
                )
            }
            MessageOwnerType.SELF_IMAGE.ordinal -> {
                ViewHolderSelfTextMessage(
                    view = LayoutInflater.from(parent.context)
                        .inflate(chatStyle.selfTextLayoutId, parent, false),
                    chatKitV4ListCallback = chatKitV4ListCallback
                )
            }

            // other messages scope
            MessageOwnerType.OTHER_TEXT.ordinal -> {
                ViewHolderOtherTextMessage(
                    LayoutInflater.from(parent.context)
                        .inflate(chatStyle.otherTextLayoutId, parent, false),
                    chatKitV4ListCallback = chatKitV4ListCallback
                )
            }
            MessageOwnerType.OTHER_VOICE.ordinal -> {
                ViewHolderOtherVoiceMessage(
                    LayoutInflater.from(parent.context)
                        .inflate(chatStyle.otherVoiceLayoutId, parent, false),
                    chatKitV4ListCallback = chatKitV4ListCallback
                )
            }
            MessageOwnerType.OTHER_VIDEO.ordinal -> {
                ViewHolderOtherTextMessage(
                    LayoutInflater.from(parent.context)
                        .inflate(chatStyle.otherTextLayoutId, parent, false),
                    chatKitV4ListCallback = chatKitV4ListCallback
                )
            }
            MessageOwnerType.OTHER_IMAGE.ordinal -> {
                ViewHolderOtherTextMessage(
                    LayoutInflater.from(parent.context)
                        .inflate(chatStyle.otherTextLayoutId, parent, false),
                    chatKitV4ListCallback = chatKitV4ListCallback
                )
            }

            // system messages scope
            MessageOwnerType.SYSTEM_TEXT.ordinal -> {
                ViewHolderSystemMessage(
                    LayoutInflater.from(parent.context)
                        .inflate(chatStyle.systemTextLayoutId, parent, false)
                )
            }

            // undefined scope
            else -> {
                ViewHolderNotSupported(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.defaul_not_supported_layout, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messageModel = getItem(position)
        when (holder) {
            is ViewHolderSelfTextMessage -> {
                holder.bind(messageModel)
            }
            is ViewHolderSelfVoiceMessage -> {
                holder.bind(messageModel)
            }
            is ViewHolderOtherTextMessage -> {
                holder.bind(messageModel)
            }
            is ViewHolderOtherVoiceMessage -> {
                holder.bind(messageModel)
            }
            is ViewHolderSystemMessage -> {
                holder.bind(messageModel)
            }
            else -> {
                // nothing
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)?.messageOwnerType) {
            MessageOwnerType.SELF.name -> {
                when (getItem(position)?.messageContentType) {
                    MessageContentType.TEXT.name -> {
                        MessageOwnerType.SELF_TEXT.ordinal
                    }
                    MessageContentType.VOICE.name -> {
                        MessageOwnerType.SELF_VOICE.ordinal
                    }
                    MessageContentType.VIDEO.name -> {
                        MessageOwnerType.SELF_VIDEO.ordinal
                    }
                    MessageContentType.IMAGE.name -> {
                        MessageOwnerType.SELF_IMAGE.ordinal
                    }
                    else -> {
                        MessageOwnerType.UNDEFINED.ordinal
                    }
                }
            }
            MessageOwnerType.OTHER.name -> {
                when (getItem(position)?.messageContentType) {
                    MessageContentType.TEXT.name -> {
                        MessageOwnerType.OTHER_TEXT.ordinal
                    }
                    MessageContentType.VOICE.name -> {
                        MessageOwnerType.OTHER_VOICE.ordinal
                    }
                    MessageContentType.VIDEO.name -> {
                        MessageOwnerType.OTHER_VIDEO.ordinal
                    }
                    MessageContentType.IMAGE.name -> {
                        MessageOwnerType.OTHER_IMAGE.ordinal
                    }
                    else -> {
                        MessageOwnerType.UNDEFINED.ordinal
                    }
                }
            }
            MessageOwnerType.SYSTEM.name -> {
                when (getItem(position)?.messageContentType) {
                    MessageContentType.TEXT.name -> {
                        MessageOwnerType.SYSTEM_TEXT.ordinal
                    }
                    MessageContentType.VOICE.name -> {
                        MessageOwnerType.SYSTEM_VOICE.ordinal
                    }
                    MessageContentType.VIDEO.name -> {
                        MessageOwnerType.SYSTEM_VIDEO.ordinal
                    }
                    MessageContentType.IMAGE.name -> {
                        MessageOwnerType.SYSTEM_IMAGE.ordinal
                    }
                    else -> {
                        MessageOwnerType.UNDEFINED.ordinal
                    }
                }
            }
            else -> MessageOwnerType.UNDEFINED.ordinal
        }
    }

    fun initializeChatKitV4ListCallback(chatKitV4ListCallback: ChatKitV4ListCallback) {
        this.chatKitV4ListCallback = chatKitV4ListCallback
    }

}