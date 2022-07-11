package com.mrndevs.moner.ui.recordform

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayoutMediator
import com.mrndevs.moner.R
import com.mrndevs.moner.databinding.ActivityAddRecordBinding
import com.mrndevs.moner.enum.EnumRecordType
import com.mrndevs.moner.utils.Constant
import com.mrndevs.moner.utils.views.ViewPagerAdapter

class RecordFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddRecordBinding
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private var recordType: String = Constant.STRING_EMPTY
    private var recordId: Int = Constant.ZERO
    private var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recordType = intent.getStringExtra(Constant.KEY_RECORD_TYPE).orEmpty()
        recordId = intent.getIntExtra(Constant.KEY_RECORD_ID, Constant.ZERO)
        isUpdate = intent.getBooleanExtra(Constant.KEY_UPDATE, false)

        setupViewPager()
    }

    private fun setupViewPager() {
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
        if (isUpdate) {
            binding.tvTitle.text = getString(R.string.text_placeholder_edit_record)
            binding.tlAddRecord.isVisible = false
            if (recordType == EnumRecordType.EXPENSE.type) {
                expenseFragment()
            } else {
                incomeFragment()
            }
        } else {
            binding.tvTitle.text = getString(R.string.text_placeholder_add_record)
            expenseFragment()
            incomeFragment()
        }
        binding.vpAddRecord.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tlAddRecord, binding.vpAddRecord, true) { tab, pos ->
            tab.text = viewPagerAdapter.getPageTitle(pos)
        }.attach()
    }

    private fun expenseFragment() {
        viewPagerAdapter.addFragment(
            RecordFormFragment.newInstance(EnumRecordType.EXPENSE.type, isUpdate, recordId),
            getString(R.string.text_title_expense)
        )
    }

    private fun incomeFragment() {
        viewPagerAdapter.addFragment(
            RecordFormFragment.newInstance(EnumRecordType.INCOME.type, isUpdate, recordId),
            getString(R.string.text_title_income)
        )
    }

    companion object {
        @JvmStatic
        fun startActivity(
            context: Context?,
            recordType: String = Constant.STRING_EMPTY,
            id: Int = Constant.ZERO,
            isUpdate: Boolean = false
        ) {
            val intent = Intent(context, RecordFormActivity::class.java).apply {
                putExtra(Constant.KEY_RECORD_TYPE, recordType)
                putExtra(Constant.KEY_RECORD_ID, id)
                putExtra(Constant.KEY_UPDATE, isUpdate)
            }
            context?.startActivity(intent)
        }
    }
}