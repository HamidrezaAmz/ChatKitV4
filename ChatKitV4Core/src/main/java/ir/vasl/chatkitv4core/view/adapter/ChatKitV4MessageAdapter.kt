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
import ir.vasl.chatkitv4core.view.interfaces.ChatKitV4ListAdapterCallback

class ChatKitV4MessageAdapter(
    private var chatStyle: ChatStyle,
    private var chatKitV4ListAdapterCallback: ChatKitV4ListAdapterCallback? = null
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

    fun initializeChatKitV4ListCallback(chatKitV4ListAdapterCallback: ChatKitV4ListAdapterCallback) {
        this.chatKitV4ListAdapterCallback = chatKitV4ListAdapterCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {

            // alpha messages scope
            MessageOwnerType.ALPHA_TEXT.ordinal -> {
                ViewHolderAlphaTextMessage(
                    view = LayoutInflater.from(parent.context)
                        .inflate(chatStyle.alphaTextLayoutId, parent, false),
                    chatKitV4ListAdapterCallback = chatKitV4ListAdapterCallback
                )
            }
            MessageOwnerType.ALPHA_VOICE.ordinal -> {
                ViewHolderAlphaVoiceMessage(
                    view = LayoutInflater.from(parent.context)
                        .inflate(chatStyle.alphaVoiceLayoutId, parent, false),
                    chatKitV4ListAdapterCallback = chatKitV4ListAdapterCallback
                )
            }
            MessageOwnerType.ALPHA_VIDEO.ordinal -> {
                ViewHolderAlphaTextMessage(
                    view = LayoutInflater.from(parent.context)
                        .inflate(chatStyle.alphaTextLayoutId, parent, false),
                    chatKitV4ListAdapterCallback = chatKitV4ListAdapterCallback
                )
            }
            MessageOwnerType.ALPHA_IMAGE.ordinal -> {
                ViewHolderAlphaTextMessage(
                    view = LayoutInflater.from(parent.context)
                        .inflate(chatStyle.alphaTextLayoutId, parent, false),
                    chatKitV4ListAdapterCallback = chatKitV4ListAdapterCallback
                )
            }

            // beta messages scope
            MessageOwnerType.BETA_TEXT.ordinal -> {
                ViewHolderBetaTextMessage(
                    LayoutInflater.from(parent.context)
                        .inflate(chatStyle.betaTextLayoutId, parent, false),
                    chatKitV4ListAdapterCallback = chatKitV4ListAdapterCallback
                )
            }
            MessageOwnerType.BETA_VOICE.ordinal -> {
                ViewHolderBetaVoiceMessage(
                    LayoutInflater.from(parent.context)
                        .inflate(chatStyle.betaVoiceLayoutId, parent, false),
                    chatKitV4ListAdapterCallback = chatKitV4ListAdapterCallback
                )
            }
            MessageOwnerType.BETA_VIDEO.ordinal -> {
                ViewHolderBetaTextMessage(
                    LayoutInflater.from(parent.context)
                        .inflate(chatStyle.betaTextLayoutId, parent, false),
                    chatKitV4ListAdapterCallback = chatKitV4ListAdapterCallback
                )
            }
            MessageOwnerType.BETA_IMAGE.ordinal -> {
                ViewHolderBetaTextMessage(
                    LayoutInflater.from(parent.context)
                        .inflate(chatStyle.betaTextLayoutId, parent, false),
                    chatKitV4ListAdapterCallback = chatKitV4ListAdapterCallback
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
            is ViewHolderAlphaTextMessage -> {
                holder.bind(messageModel)
            }
            is ViewHolderAlphaVoiceMessage -> {
                holder.bind(messageModel)
            }
            is ViewHolderBetaTextMessage -> {
                holder.bind(messageModel)
            }
            is ViewHolderBetaVoiceMessage -> {
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
            MessageOwnerType.ALPHA.name -> {
                when (getItem(position)?.messageContentType) {
                    MessageContentType.TEXT.name -> {
                        MessageOwnerType.ALPHA_TEXT.ordinal
                    }
                    MessageContentType.VOICE.name -> {
                        MessageOwnerType.ALPHA_VOICE.ordinal
                    }
                    MessageContentType.VIDEO.name -> {
                        MessageOwnerType.ALPHA_VIDEO.ordinal
                    }
                    MessageContentType.IMAGE.name -> {
                        MessageOwnerType.ALPHA_IMAGE.ordinal
                    }
                    else -> {
                        MessageOwnerType.UNDEFINED.ordinal
                    }
                }
            }
            MessageOwnerType.BETA.name -> {
                when (getItem(position)?.messageContentType) {
                    MessageContentType.TEXT.name -> {
                        MessageOwnerType.BETA_TEXT.ordinal
                    }
                    MessageContentType.VOICE.name -> {
                        MessageOwnerType.BETA_VOICE.ordinal
                    }
                    MessageContentType.VIDEO.name -> {
                        MessageOwnerType.BETA_VIDEO.ordinal
                    }
                    MessageContentType.IMAGE.name -> {
                        MessageOwnerType.BETA_IMAGE.ordinal
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


}