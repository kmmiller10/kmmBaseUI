package me.kmmiller.baseui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class BottomNavItemModel(val navId: Int, @DrawableRes val iconActive: Int, @DrawableRes val iconInactive: Int, @StringRes val label: Int)