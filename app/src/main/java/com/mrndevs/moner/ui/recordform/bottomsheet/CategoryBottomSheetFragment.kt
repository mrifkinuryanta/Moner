package com.mrndevs.moner.ui.recordform.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mrndevs.moner.R
import com.mrndevs.moner.databinding.FragmentCategoryBottomSheetBinding
import com.mrndevs.moner.enum.EnumCategory
import com.mrndevs.moner.ui.recordform.bottomsheet.adapter.CategoryAdapter
import com.mrndevs.moner.utils.Constant

class CategoryBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentCategoryBottomSheetBinding
    private lateinit var adapter: CategoryAdapter
    private var category: EnumCategory? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            category = it.getSerializable(ARG_CATEGORY) as EnumCategory
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setOnShowListener {
                val bottomSheet =
                    findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                bottomSheet.setBackgroundResource(android.R.color.transparent)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        adapter = CategoryAdapter {
            dialogDismissWithResult(it)
        }
        val listCategory = when (category) {
            EnumCategory.EXPENSE -> {
                setTitleBottomSheet(getString(R.string.text_title_expanse_category))
                resources.getStringArray(R.array.expense_category).toMutableList()
            }
            EnumCategory.INCOME -> {
                setTitleBottomSheet(getString(R.string.text_title_income_category))
                resources.getStringArray(R.array.income_category).toMutableList()
            }
            else -> {
                setTitleBottomSheet(getString(R.string.text_title_payment_category))
                resources.getStringArray(R.array.payment_category).toMutableList()
            }
        }
        adapter.submitList(listCategory)
        binding.rvCategory.adapter = adapter
    }

    private fun setTitleBottomSheet(title: String) {
        binding.tvTitleBottomSheet.text = title
    }

    private fun dialogDismissWithResult(result: String) {
        val bundle = bundleOf(Constant.KEY_RESULT to result)
        parentFragmentManager.setFragmentResult(
            Constant.KEY_RESULT,
            bundle
        )
        dialog?.dismiss()
    }

    companion object {
        private const val ARG_CATEGORY = "arg_category"

        @JvmStatic
        fun newInstance(category: EnumCategory) =
            CategoryBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_CATEGORY, category)
                }
            }
    }
}