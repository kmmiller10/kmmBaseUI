package me.kmmiller.sample

import androidx.core.content.ContextCompat
import me.kmmiller.baseui.KmmBaseActivity
import me.kmmiller.baseui.navigation.BottomNavItemModel

class MainActivity : KmmBaseActivity() {

    override fun getHighlightColor(): Int = ContextCompat.getColor(this, R.color.colorPrimary)
    override var hasBottomNav: Boolean = true

    override fun defaultNavItem(): Int = R.id.nav_home

    override fun getNavItems(): ArrayList<BottomNavItemModel> {
        val navItems = ArrayList<BottomNavItemModel>()
        val home = BottomNavItemModel(R.id.nav_home, R.drawable.ic_home_active, R.drawable.ic_home_inactive, R.string.home)
        navItems.add(home)
        val dailies = BottomNavItemModel(R.id.nav_dailies, R.drawable.ic_dailies_active,  R.drawable.ic_dailies_inactive, R.string.dailies)
        navItems.add(dailies)
        val weeklies = BottomNavItemModel(R.id.nav_weeklies, R.drawable.ic_weeklies_active,  R.drawable.ic_weeklies_inactive, R.string.weeklies)
        navItems.add(weeklies)
        return navItems
    }

    override fun navItemSelected(itemId: Int) {
        when(itemId) {
            R.id.nav_home -> {
                pushFragmentSynchronous(MainFragment(), true, MainFragment::class.java.name)
            }
            R.id.nav_dailies -> {
                pushFragmentSynchronous(MainFragment(), true, MainFragment::class.java.name)
            }
            R.id.nav_weeklies -> {
                pushFragmentSynchronous(MainFragment(), true, MainFragment::class.java.name)
            }
        }
    }
}
