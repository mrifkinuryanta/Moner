package com.mrndevs.moner.ui.main.transaction

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.view.isVisible
import com.mrndevs.moner.R
import com.mrndevs.moner.base.BaseFragment
import com.mrndevs.moner.base.model.Resource
import com.mrndevs.moner.databinding.FragmentTransactionBinding
import com.mrndevs.moner.databinding.LayoutMonthYearPickerDialogBinding
import com.mrndevs.moner.ui.detail.DetailActivity
import com.mrndevs.moner.ui.main.transaction.adapter.TransactionAdapter
import com.mrndevs.moner.utils.Constant
import com.mrndevs.moner.utils.SpacesItemDecoration
import com.mrndevs.moner.utils.Utils

class TransactionFragment :
    BaseFragment<FragmentTransactionBinding>(FragmentTransactionBinding::inflate),
    TransactionContract.View {
    private lateinit var viewModel: TransactionViewModel
    private lateinit var adapter: TransactionAdapter
    private var month: String? = null
    private var year: String? = null
    private var date: String? = null

    override fun initView() {
        (activity as AppCompatActivity).apply {
            setSupportActionBar(getViewBinding().layoutTitle.toolbar)
            supportActionBar?.title = getString(R.string.text_title_transaction)
        }

        initAdapter()
        setClickListener()
    }

    override fun initViewModel() {
        val repository = TransactionRepository(getDataSource())
        viewModel = TransactionViewModel(repository)
        getMonthAndYear()
    }

    override fun initAdapter() {
        adapter = TransactionAdapter { navigateToDetail(it.id) }
        getViewBinding().rvTransaction.apply {
            adapter = this@TransactionFragment.adapter
            addItemDecoration(
                SpacesItemDecoration(
                    Utils.dpToPixels(
                        requireContext(),
                        Constant.EIGHT
                    )
                )
            )
        }
    }

    override fun setClickListener() {
        getViewBinding().layoutTitle.btnCurrentDate.setOnClickListener {
            showSelectDateDialog()
        }
    }

    override fun saveMonthAndYear() {
        viewModel.saveMonthAndYearQuery(month.orEmpty(), year.orEmpty())
    }

    override fun getMonthAndYear() {
        viewModel.getMonthAndYear()
    }

    override fun getRecords() {
        viewModel.getRecords(date.orEmpty())
    }

    override fun observeViewModel() {
        viewModel.getDateLiveData().observe(viewLifecycleOwner) { value ->
            month = value.month
            year = value.year
            date = "$year-${Utils.getMonthNumber(month.orEmpty())}"
            val wrd = "$month $year"
            getViewBinding().layoutTitle.btnCurrentDate.text = wrd
            getRecords()
        }

        viewModel.getRecordsLiveData().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        adapter.submitList(it)
                        getViewBinding().layoutPlaceholder.clPlaceholder.isVisible = it.isEmpty()
                        getViewBinding().rvTransaction.isVisible = it.isNotEmpty()
                    }
                }
                is Resource.Error -> {
                    showToast(getString(R.string.text_message_error_retrieving_data))
                }
            }
        }
    }

    private fun showSelectDateDialog() {
        val dialogBinding = LayoutMonthYearPickerDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        dialogBinding.etMonth.setText(month)
        dialogBinding.etYear.setText(year)

        dialogBinding.etMonth.setOnClickListener {
            openDateList(it, true) { position ->
                month = Utils.getMonths()[position]
                dialogBinding.etMonth.setText(month)
            }
        }

        dialogBinding.etYear.setOnClickListener {
            openDateList(it, false) { position ->
                year = Utils.getYears()[position]
                dialogBinding.etYear.setText(year)
            }
        }

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnOk.setOnClickListener {
            saveMonthAndYear()
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun openDateList(view: View, isMonth: Boolean, itemClick: (Int) -> Unit) {
        val list = if (isMonth) Utils.getMonths() else Utils.getYears()
        ListPopupWindow(requireContext()).apply {
            setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    list
                )
            )
            anchorView = view
            setOnItemClickListener { _, _, position, _ ->
                itemClick(position)
                this.dismiss()
            }
        }.show()
    }

    private fun navigateToDetail(id: Int) {
        DetailActivity.startActivity(requireContext(), id)
    }

    override fun onResume() {
        super.onResume()
        getRecords()
    }
}