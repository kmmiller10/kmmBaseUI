package me.kmmiller.baseui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import me.kmmiller.baseui.navigation.BottomNavAdapter
import me.kmmiller.baseui.navigation.BottomNavItemModel
import me.kmmiller.baseui.navigation.BottomNavRecyclerView
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent


abstract class KmmBaseActivity : AppCompatActivity(), BottomNavAdapter.BottomNavAdapterListener {
    private lateinit var bottomNavRecyclerView: BottomNavRecyclerView
    abstract var hasBottomNav: Boolean
    protected var currentNavId = 0

    private val navItems = ArrayList<BottomNavItemModel>()
    private var adapter: BottomNavAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.base_activity)
        bottomNavRecyclerView = findViewById(R.id.bottom_nav)
        updateNav()

        KeyboardVisibilityEvent.setEventListener(this) { isOpen ->
            if(hasBottomNav) {
                bottomNavRecyclerView.visibility = if(isOpen) View.GONE else View.VISIBLE
            }
        }

        savedInstanceState?.let {
            currentNavId = it.getInt(NAV_ID, defaultNavItem())
            adapter?.updateSelected(this, currentNavId)
            adapter?.notifyDataSetChanged()
        }

        if(savedInstanceState == null && hasBottomNav) {
            // Select home initially
            onNavItemSelected(defaultNavItem())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(NAV_ID, currentNavId)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }

    private fun updateNav() {
        if(hasBottomNav) {
            bottomNavRecyclerView.visibility = View.VISIBLE
            navItems.addAll(getNavItems())
            adapter = BottomNavAdapter(defaultNavItem(), getHighlightColor(), navItems)
            adapter?.let {
                bottomNavRecyclerView.setRecyclerViewAdapter(it)
            }
        } else {
            bottomNavRecyclerView.visibility = View.GONE
        }
    }

    abstract fun defaultNavItem(): Int
    abstract fun getNavItems(): ArrayList<BottomNavItemModel>
    @ColorInt
    abstract fun getHighlightColor(): Int

    override fun onNavItemSelected(itemId: Int) {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        if(currentNavId != itemId) {
            currentNavId = itemId
            adapter?.notifyDataSetChanged()
            navItemSelected(itemId)
        }
    }

    protected abstract fun navItemSelected(itemId: Int)

    protected fun updateSelected(itemId: Int) {
        adapter?.updateSelected(this, itemId)
    }

    /**
     * Override this method if the fragment container to push fragment in is nested or in a custom view.
     * Otherwise use default container
     */
    @IdRes
    protected fun getFragContainerId(): Int = R.id.fragment_container

    fun pushFragment(frag: Fragment, replace: Boolean, addToBackStack: Boolean, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        if(replace)
            transaction.replace(getFragContainerId(), frag, tag)
        else
            transaction.add(getFragContainerId(), frag, tag)

        if(addToBackStack) transaction.addToBackStack(tag)

        transaction.commit()
    }

    /**
     * Synchronously pushes a fragment, i.e. the transaction is committed immediately. addToBackStack cannot be used
     * when committing synchronously
     */
    fun pushFragmentSynchronous(frag: Fragment, replace: Boolean, tag: String) {
        val transaction = supportFragmentManager.beginTransaction()

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)

        if(replace)
            transaction.replace(getFragContainerId(), frag, tag)
        else
            transaction.add(getFragContainerId(), frag, tag)

        transaction.commitNow()
        if(supportFragmentManager.backStackEntryCount > 0) supportFragmentManager.popBackStack()
    }

    open fun handleError(e: Exception) {
        e.printStackTrace()
    }

    fun showAlert(title: String, message: String) {
        showAlert(title, message, getString(android.R.string.ok), null)
    }

    fun showAlert(title: String, message: String, positiveListener: DialogInterface.OnClickListener) {
        showAlert(title, message, getString(android.R.string.ok), positiveListener)
    }

    fun showAlert(title: String,
                            message: String,
                            positiveText: String,
                            positiveListener: DialogInterface.OnClickListener?) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText, positiveListener)
            .show()
    }

    fun showCancelableAlert(title: String, message: String, positiveListener: DialogInterface.OnClickListener) {
        showCancelableAlert(title, message, getString(android.R.string.ok), positiveListener)
    }

    fun showCancelableAlert(title: String, message: String, positiveText: String, positiveListener: DialogInterface.OnClickListener) {
        showCancelableAlert(title, message, positiveText, positiveListener, getString(android.R.string.cancel), null)
    }

    fun showCancelableAlert(title: String,
                                      message: String,
                                      positiveText: String,
                                      positiveListener: DialogInterface.OnClickListener?,
                                      cancelText: String,
                                      cancelListener: DialogInterface.OnClickListener?) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveText, positiveListener)
            .setNegativeButton(cancelText, cancelListener)
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Deliver results to fragments in case anything needs to be handled by the frag
        supportFragmentManager.fragments.forEach {
            if(it is KmmBaseFragment) {
                it.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    companion object {
        const val NAV_ID = "nav_id"
    }
}
