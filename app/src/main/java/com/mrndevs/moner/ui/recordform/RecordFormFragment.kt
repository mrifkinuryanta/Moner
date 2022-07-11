package com.mrndevs.moner.ui.recordform

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.mrndevs.moner.R
import com.mrndevs.moner.base.BaseFragment
import com.mrndevs.moner.base.model.Resource
import com.mrndevs.moner.data.room.model.RecordEntity
import com.mrndevs.moner.databinding.FragmentRecordFormBinding
import com.mrndevs.moner.enum.EnumCategory
import com.mrndevs.moner.enum.EnumRecordType
import com.mrndevs.moner.ui.recordform.bottomsheet.CategoryBottomSheetFragment
import com.mrndevs.moner.utils.Constant
import com.mrndevs.moner.utils.Utils

class RecordFormFragment :
    BaseFragment<FragmentRecordFormBinding>(FragmentRecordFormBinding::inflate),
    RecordFormContract.View {
    private lateinit var viewModel: RecordFormViewModel
    private lateinit var record: RecordEntity
    private var recordId: Int = 0
    private var recordType: String? = null
    private var isUpdated = false
    private var isPaymentCategory = false
    private var amount: String? = null
    private var payment: String? = null
    private var category: String? = null
    private var recordDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            recordType = it.getString(ARG_RECORD_TYPE)
            isUpdated = it.getBoolean(ARG_IS_UPDATED)
            recordId = it.getInt(ARG_RECORD_ID)
        }
    }

    override fun initView() {
        if (recordType == EnumRecordType.INCOME.type) {
            getViewBinding().tilPayment.isVisible = false
        }

        if (isUpdated) viewModel.getRecordById(recordId)

        enableActionButton()
        setClickListener()
        initEditTextPrice()
    }

    override fun initViewModel() {
        val repository = RecordFormRepository(getDataSource())
        viewModel = RecordFormViewModel(repository)
    }

    override fun setClickListener() {
        getViewBinding().etPayment.setOnClickListener {
            isPaymentCategory = true
            openCategoryBottomSheet(EnumCategory.PAYMENT)
        }
        getViewBinding().etCategory.setOnClickListener {
            isPaymentCategory = false
            val category = when (EnumRecordType.state(recordType.orEmpty())) {
                EnumRecordType.EXPENSE -> EnumCategory.EXPENSE
                EnumRecordType.INCOME -> EnumCategory.INCOME
            }
            openCategoryBottomSheet(category)
        }
        getViewBinding().etDate.setOnClickListener {
            openDialogDateRangePicker()
        }
        getViewBinding().btnAction.setOnClickListener {
            if (isUpdated) {
                updateRecord()
            } else {
                saveRecord()
            }
        }
    }

    override fun initEditTextPrice() {
        amount = ""
        getViewBinding().etAmount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val stringText = s.toString()

                if (stringText != amount) {
                    getViewBinding().etAmount.removeTextChangedListener(this)
                    val formatted = Utils.currencyFormatter(stringText)
                    amount = formatted
                    getViewBinding().etAmount.setText(formatted)
                    getViewBinding().etAmount.setSelection(formatted.length)
                    getViewBinding().etAmount.addTextChangedListener(this)
                }
                enableActionButton()
            }
        })
    }

    override fun observeViewModel() {
        viewModel.getResultLiveData().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        when (it.first) {
                            Constant.CODE_READ -> {
                                record = it.second
                                fetchData(it.second)
                            }
                            else -> {
                                requireActivity().finish()
                            }
                        }
                    }
                }
                is Resource.Error -> {
                    showToast(getString(R.string.text_message_error))
                }
            }
        }
    }

    private fun fetchData(record: RecordEntity) {
        val calendar =
            Utils.dateFormatter(record.date, Constant.MONTH_PATTERN_DB, Constant.MONTH_PATTERN)
        amount = Utils.currencyFormatter(record.amount.toString())
        payment = record.payment
        category = record.category
        recordDate = record.date
        getViewBinding().etAmount.setText(amount)
        getViewBinding().etPayment.setText(payment)
        getViewBinding().etCategory.setText(category)
        getViewBinding().etDate.setText(calendar)
        getViewBinding().etNote.setText(record.note)
        enableActionButton()
    }

    private fun openDialogDateRangePicker() {
        val constraintsBuilder = CalendarConstraints.Builder().apply {
            setValidator(DateValidatorPointBackward.now())
        }.build()
        val datePicker = MaterialDatePicker.Builder.datePicker().apply {
            setCalendarConstraints(constraintsBuilder)
            setTitleText(getString(R.string.text_placeholder_select_date))
            setTheme(com.google.android.material.R.style.ThemeOverlay_MaterialComponents_MaterialCalendar)
        }.build()
        datePicker.show(childFragmentManager, datePicker.tag)
        datePicker.addOnPositiveButtonClickListener { selection ->
            selection.let { recordDate = Utils.getDate(it, Constant.MONTH_PATTERN_DB) }
            val calendar = Utils.getDate(selection, Constant.MONTH_PATTERN)
            getViewBinding().etDate.setText(calendar)
            enableActionButton()
        }
    }

    private fun openCategoryBottomSheet(category: EnumCategory) {
        CategoryBottomSheetFragment.newInstance(category).show(childFragmentManager, null)

        childFragmentManager.setFragmentResultListener(
            Constant.KEY_RESULT,
            this
        ) { key, bundle ->
            if (key == Constant.KEY_RESULT) {
                val result = bundle.getString(Constant.KEY_RESULT)
                if (isPaymentCategory) {
                    payment = result
                    getViewBinding().etPayment.setText(result)
                    isPaymentCategory = false
                    enableActionButton()
                } else {
                    this@RecordFormFragment.category = result
                    getViewBinding().etCategory.setText(result)
                    enableActionButton()
                }
            }
        }
    }

    private fun enableActionButton() {
        getViewBinding().btnAction.text =
            if (isUpdated) {
                getString(R.string.text_placeholder_update)
            } else {
                getString(R.string.text_placeholder_save)
            }
        getViewBinding().btnAction.isEnabled = validateForm()
        val color = if (validateForm()) {
            ContextCompat.getColor(requireContext(), R.color.color_secondary)
        } else {
            ContextCompat.getColor(requireContext(), R.color.yellow)
        }
        getViewBinding().btnAction.backgroundTintList = ColorStateList.valueOf(color)
    }

    private fun saveRecord() {
        val note = getViewBinding().etNote.text.toString().trim()
        val record = RecordEntity(
            id = 0,
            amount = Utils.currencyToInt(amount.orEmpty()),
            payment = payment.orEmpty(),
            category = category.orEmpty(),
            date = recordDate.orEmpty(),
            note = note,
            recordType = recordType.orEmpty(),
            createAt = Utils.getUpdateDate(),
            updateAt = Utils.getUpdateDate()
        )
        viewModel.insertRecord(record)
    }

    private fun updateRecord() {
        val note = getViewBinding().etNote.text.toString().trim()
        val record = RecordEntity(
            id = this@RecordFormFragment.record.id,
            amount = Utils.currencyToInt(amount.orEmpty()),
            payment = payment.orEmpty(),
            category = category.orEmpty(),
            date = recordDate.orEmpty(),
            note = note,
            recordType = recordType.orEmpty(),
            createAt = this@RecordFormFragment.record.createAt,
            updateAt = Utils.getUpdateDate()
        )
        viewModel.insertRecord(record)
    }

    private fun validateForm(): Boolean {
        val amount = Utils.currencyToInt(amount.orEmpty())

        return if (recordType == EnumRecordType.EXPENSE.type) {
            amount > 0 && payment.orEmpty().isNotEmpty() && category.orEmpty().isNotEmpty()
                    && recordDate.orEmpty().isNotEmpty()
        } else {
            amount > 0 && category.orEmpty().isNotEmpty() && recordDate.orEmpty().isNotEmpty()
        }
    }

    companion object {
        private const val ARG_RECORD_TYPE = "arg_record_type"
        private const val ARG_IS_UPDATED = "arg_is_updated"
        private const val ARG_RECORD_ID = "arg_record_id"

        @JvmStatic
        fun newInstance(
            enumRecordType: String,
            isUpdated: Boolean,
            recordId: Int = 0
        ) =
            RecordFormFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_RECORD_TYPE, enumRecordType)
                    putBoolean(ARG_IS_UPDATED, isUpdated)
                    putInt(ARG_RECORD_ID, recordId)
                }
            }
    }
}