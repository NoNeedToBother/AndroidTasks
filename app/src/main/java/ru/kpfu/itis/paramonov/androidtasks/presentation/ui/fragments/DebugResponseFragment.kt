package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments

import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentDebugResponseBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.base.BaseFragment
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.response.ResponseUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel.DebugResponseViewModel
import ru.kpfu.itis.paramonov.androidtasks.utils.Params
import ru.kpfu.itis.paramonov.androidtasks.utils.appComponent
import ru.kpfu.itis.paramonov.androidtasks.utils.lazyViewModel
import java.lang.StringBuilder

class DebugResponseFragment: BaseFragment(R.layout.fragment_debug_response) {

    private val binding: FragmentDebugResponseBinding by viewBinding(FragmentDebugResponseBinding::bind)

    private val viewModel: DebugResponseViewModel by lazyViewModel {
        val pos = requireArguments().getInt(POS_KEY)
        requireContext().appComponent.debugResponseViewModelFactory().create(pos)
    }


    override fun init() {
        viewModel.getResponseInfo()
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                launch {
                    collectResponseData()
                }
            }
        }
    }

    private suspend fun collectResponseData() {
        viewModel.responseFlow.collect { result ->
            result?.let {
                when(it) {
                    is DebugResponseViewModel.ResponseDataResult.Success ->
                        showResponseData(it.getValue())
                    is DebugResponseViewModel.ResponseDataResult.Failure ->
                        showError(it.getException())
                }
            }
        }
    }

    private fun showResponseData(response: ResponseUiModel) {
        with(binding) {
            tvMethod.text = getString(R.string.req_method, response.method)
            tvUrl.text = getString(R.string.req_url, response.url)
            tvHeaders.text = getString(R.string.req_headers, getHeadersString(response.headers))
            tvRequestBody.text = getString(R.string.req_body, response.requestBody)
            tvResponseBody.text = getString(R.string.resp_body, response.responseBody)
        }
    }

    private fun getHeadersString(headers: Map<String, String>): String {
        if (headers.isEmpty()) return Params.HEADERS_EMPTY
        val res = StringBuilder()
        for (key in headers.keys) {
            res.append(key).append(": ").append(headers[key]).append("\n")
        }
        return res.toString()
    }

    companion object {
        const val DEBUG_RESPONSE_FRAGMENT_TAG = "DEBUG_RESPONSE_FRAGMENT"

        private const val POS_KEY = "pos"

        fun newInstance(pos: Int) = DebugResponseFragment().apply {
            arguments = bundleOf(
                POS_KEY to pos
            )
        }
    }
}