package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments.weather

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.presentation.base.BaseBottomSheetDialogFragment
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.intent.ContactEvent
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.ContactUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.state.ContactsScreenViewState
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel.ContactsViewModel
import ru.kpfu.itis.paramonov.androidtasks.utils.appComponent
import javax.inject.Inject

class ContactsBottomSheetFragment: BaseBottomSheetDialogFragment() {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: ContactsViewModel by viewModels { factory }

    private val behavior: BottomSheetBehavior<View>? get() {
        return dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.let {
            BottomSheetBehavior.from(it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(this)
    }

    override fun composeView(): ComposeView {
        return ComposeView(requireContext()).apply {
            setContent {
                ContactsSurface()
            }
        }
    }

    override fun init() {}

    override fun observeData() {
        viewModel.onEvent(ContactEvent.OnGet)
    }

    @Composable
    fun ContactsSurface() {
        val contactsState = viewModel.contactsDataFlow.collectAsStateWithLifecycle(
            lifecycleOwner = viewLifecycleOwner
        )
        val screenState = ContactsScreenViewState(contactsState)

        Surface(border = BorderStroke(1.dp, Color.Black),
            shadowElevation = 6.dp
        ) {
            ContactsContent(state = screenState)
        }
    }

    @Composable
    fun ContactsContent(
         state: ContactsScreenViewState
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .background(Color.White, RoundedCornerShape(4.dp))
        ) {
            Header()
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(4.dp)
            ) {
                if (state.contacts.value.isNotEmpty()) {
                    items(state.contacts.value.size) { index ->
                        Contact(state.contacts.value[index])
                    }
                }
            }
        }
    }

    @Composable
    private fun Header() {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(8.dp)
        ) {
            ExpandVectorDrawable()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.contacts),
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )
            }
        }
    }

    @OptIn(ExperimentalAnimationGraphicsApi::class)
    @Composable
    fun ExpandVectorDrawable() {
        val image = AnimatedImageVector.animatedVectorResource(R.drawable.arrow_down_up_anim)
        var atEnd by remember {
            mutableStateOf(false)
        }
        behavior?.addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onStateChanged(view: View, state: Int) {
                when(state) {
                    BottomSheetBehavior.STATE_COLLAPSED -> atEnd = false
                    BottomSheetBehavior.STATE_EXPANDED -> atEnd = true
                    BottomSheetBehavior.STATE_DRAGGING -> atEnd = !atEnd
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> atEnd = false
                    BottomSheetBehavior.STATE_HIDDEN -> {}
                    BottomSheetBehavior.STATE_SETTLING -> {}
                }
            }

            override fun onSlide(p0: View, p1: Float) {}
        })
        Image(
            painter = rememberAnimatedVectorPainter(animatedImageVector = image, atEnd = atEnd),
            contentDescription = stringResource(id = R.string.open),
            modifier = Modifier
                .clickable {
                    updateBottomSheetBehavior(!atEnd)
                    atEnd = !atEnd
                }
                .size(36.dp)
        )
    }

    private fun onContactClicked(contact: ContactUiModel) {
        val city = requireArguments().getString(CITY_KEY) ?: ""
        val temperature = requireArguments().getDouble(TEMPERATURE_KEY)

        val message = getString(R.string.sms_default_body, city, temperature)
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse(SMS_SCHEME.format(contact.phone))
            putExtra(SMS_BODY_KEY, message)
        }
        startActivity(intent)
    }

    @Composable
    fun Contact(item: ContactUiModel) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardColors(Color.White, Color.Black, Color.LightGray, Color.DarkGray),
            border = BorderStroke(1.dp, Color.Black),
            elevation = CardDefaults.elevatedCardElevation(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(IntrinsicSize.Min)
                .clickable {
                    onContactClicked(item)
                }
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.person),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f),
                    contentScale = ContentScale.FillBounds)
                ContactInfo(item = item)
            }
        }
    }
    
    @Composable
    fun ContactInfo(item: ContactUiModel) {
        Column {
            Text(text = item.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold)
            Text(text = item.phone,
                fontSize = 16.sp)
        }
    }

    private fun updateBottomSheetBehavior(expanded: Boolean) {
        behavior?.let {
            if (expanded) {
                it.state = BottomSheetBehavior.STATE_EXPANDED
            } else it.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    companion object {
        fun newInstance(city: String, temperature: Double): ContactsBottomSheetFragment {
            val args = bundleOf(CITY_KEY to city, TEMPERATURE_KEY to temperature)
            return ContactsBottomSheetFragment().apply {
                arguments = args
            }
        }

        private const val CITY_KEY = "city"
        private const val TEMPERATURE_KEY = "temperature"

        const val CONTACTS_BOTTOM_SHEET_TAG = "CONTACTS_BOTTOM_SHEET"

        private const val SMS_SCHEME = "smsto:%s"
        private const val SMS_BODY_KEY = "sms_body"
    }
}