package com.mrndevs.moner.ui.detail

import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.mrndevs.moner.R
import com.mrndevs.moner.base.BaseActivity
import com.mrndevs.moner.base.model.Resource
import com.mrndevs.moner.data.room.model.RecordEntity
import com.mrndevs.moner.databinding.ActivityDetailBinding
import com.mrndevs.moner.enum.EnumRecordType
import com.mrndevs.moner.ui.recordform.RecordFormActivity
import com.mrndevs.moner.utils.Constant
import com.mrndevs.moner.utils.Utils

class DetailActivity : BaseActivity<ActivityDetailBinding>(ActivityDetailBinding::inflate),
    DetailContract.View {
    private lateinit var viewModel: DetailViewModel
    private lateinit var record: RecordEntity

    private var recordId: Int = Constant.ZERO

    override fun initView() {
        setSupportActionBar(getViewBinding().toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recordId = intent.getIntExtra(Constant.KEY_RECORD_ID, Constant.ZERO)
        getRecordById(recordId)
        setClickListener()
    }

    override fun initViewModel() {
        val repository = DetailRepository(getDataSource())
        viewModel = DetailViewModel(repository)
    }

    override fun setClickListener() {
        getViewBinding().btnDelete.setOnClickListener {
            showAlertDialogDelete()
        }
    }

    override fun getRecordById(id: Int) {
        viewModel.getRecordById(id)
    }

    override fun observeViewModel() {
        viewModel.getRecordLiveData().observe(this) { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        if (it.first == Constant.CODE_READ) {
                            record = it.second
                            fetchData(it.second)
                        } else {
                            onBackPressed()
                        }
                    }
                }
                is Resource.Error -> {
                    showToast(getString(R.string.text_message_error))
                }
            }
        }
    }

    override fun fetchData(record: RecordEntity) {
        with(record) {
            if (recordType == EnumRecordType.EXPENSE.type) {
                getViewBinding().ivDetailRecord.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@DetailActivity,
                        R.drawable.ic_money_out_24
                    )
                )
                getViewBinding().tvDescType.text = getString(R.string.text_title_expense)
            } else {
                getViewBinding().ivDetailRecord.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@DetailActivity,
                        R.drawable.ic_money_in_24
                    )
                )
                getViewBinding().trPayment.isVisible = false
                getViewBinding().tvDescType.text = getString(R.string.text_title_income)
            }
            getViewBinding().tvDetailTitle.text = category
            getViewBinding().tvDetailDesc.text =
                note.orEmpty().ifEmpty { getString(R.string.text_placeholder_note_empty) }
            getViewBinding().tvDescDate.text =
                Utils.dateFormatter(date, Constant.MONTH_PATTERN_DB, Constant.MONTH_PATTERN)
            getViewBinding().tvDescPayment.text = payment
            getViewBinding().tvDescTotal.text = Utils.currencyFormatter(amount.toString())
        }
    }

    private fun showAlertDialogDelete() {
        val dialogTitle = getString(R.string.text_title_delete)
        val dialogMessage = getString(R.string.text_message_delete_transaction)
        AlertDialog.Builder(this).apply {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton(getString(R.string.text_placeholder_ok)) { dialog, _ ->
                dialog.dismiss()
                viewModel.deleteRecord(record)
            }
            setNegativeButton(getString(R.string.text_placeholder_cancel)) { dialog, _ ->
                dialog.cancel()
            }
        }.create().show()
    }

    private fun navigateToRecordForm() {
        RecordFormActivity.startActivity(this, record.recordType, recordId, true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.menu_edit -> {
                navigateToRecordForm()
                true
            }
            else -> true
        }
    }

    override fun onResume() {
        super.onResume()
        getRecordById(recordId)
    }

    companion object {
        @JvmStatic
        fun startActivity(context: Context?, id: Int) {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra(Constant.KEY_RECORD_ID, id)
            }
            context?.startActivity(intent)
        }
    }
}