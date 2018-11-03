package com.mercandalli.android.apps.files.theme

import androidx.annotation.ColorRes

open class Theme(
    @ColorRes
    val windowBackgroundColorRes: Int,
    @ColorRes
    val toolbarBackgroundColorRes: Int,
    @ColorRes
    val textPrimaryColorRes: Int,
    @ColorRes
    val textSecondaryColorRes: Int,
    @ColorRes
    val bottomBarBlurOverlay: Int,
    @ColorRes
    val cardBackgroundColorRes: Int,
    @ColorRes
    val fileColumnDetailBackgroundColorRes: Int
)
