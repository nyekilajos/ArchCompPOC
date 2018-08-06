@file:JvmName("UiUtils")

package com.epam.nyekilajos.archcomppoc.util

import android.content.Context
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

@JvmOverloads
fun createLinearLayoutManager(
        context: Context,
        @RecyclerView.Orientation orientation: Int = LinearLayout.VERTICAL,
        reverseLayout: Boolean = false
): RecyclerView.LayoutManager = LinearLayoutManager(context, orientation, reverseLayout)

@JvmOverloads
fun createDividerItemDecoration(context: Context, orientation: Int = LinearLayout.VERTICAL) = DividerItemDecoration(context, orientation)
