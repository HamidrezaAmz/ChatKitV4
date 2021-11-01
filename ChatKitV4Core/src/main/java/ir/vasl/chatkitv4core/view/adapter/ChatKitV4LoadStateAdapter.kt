package ir.vasl.chatkitv4core.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import ir.vasl.chatkitv4core.databinding.ViewLoadStateAdapterBinding

class ChatKitV4LoadStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ChatKitV4LoadStateAdapter.MovieLoadStateViewHolder>() {

    inner class MovieLoadStateViewHolder(
        private val binding: ViewLoadStateAdapterBinding,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonRetry.setOnClickListener { retry() }
        }

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.textViewError.text = loadState.error.localizedMessage
            }
            binding.progressbar.isVisible = loadState is LoadState.Loading
            binding.buttonRetry.isVisible = loadState is LoadState.Error
            binding.textViewError.isVisible = loadState is LoadState.Error
        }
    }

    override fun onBindViewHolder(holder: MovieLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState = loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): MovieLoadStateViewHolder = MovieLoadStateViewHolder(
        ViewLoadStateAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        retry
    )
}