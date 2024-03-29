package me.kmmiller.baseui

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import me.kmmiller.baseui.interfaces.ICancel
import me.kmmiller.baseui.views.Progress

import java.lang.Exception

abstract class KmmBaseFragment : Fragment(), ICancel {
    private lateinit var progress: Progress

    abstract fun getTitle(): String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            progress = Progress(it as KmmBaseActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getTitle()
    }

    protected fun pushFragment(frag: Fragment, replace: Boolean, addToBackStack: Boolean, tag: String) {
        (activity as? KmmBaseActivity)?.pushFragment(frag, replace, addToBackStack, tag)
    }

    protected fun pushFragmentSynchronous(frag: Fragment, replace: Boolean, tag: String) {
        (activity as? KmmBaseActivity)?.pushFragmentSynchronous(frag, replace, tag)
    }

    protected fun handleError(e: Exception) {
        (activity as? KmmBaseActivity)?.handleError(e)
    }

    protected fun showAlert(title: String, message: String) {
        (activity as? KmmBaseActivity)?.showAlert(title, message)
    }

    protected fun showAlert(title: String, message: String, positiveListener: DialogInterface.OnClickListener) {
        (activity as? KmmBaseActivity)?.showAlert(title, message, positiveListener)
    }

    protected fun showAlert(title: String,
                            message: String,
                            positiveText: String,
                            positiveListener: DialogInterface.OnClickListener?) {
        (activity as? KmmBaseActivity)?.showAlert(title, message, positiveText, positiveListener)
    }

    protected fun showCancelableAlert(title: String, message: String, positiveListener: DialogInterface.OnClickListener) {
        (activity as? KmmBaseActivity)?.showCancelableAlert(title, message, positiveListener)
    }

    protected fun showCancelableAlert(title: String, message: String, positiveText: String, positiveListener: DialogInterface.OnClickListener) {
        (activity as? KmmBaseActivity)?.showCancelableAlert(title, message, positiveText, positiveListener)
    }

    protected fun showCancelableAlert(title: String,
                            message: String,
                            positiveText: String,
                            positiveListener: DialogInterface.OnClickListener?,
                            cancelText: String,
                            cancelListener: DialogInterface.OnClickListener?) {
        (activity as? KmmBaseActivity)?.showCancelableAlert(title, message, positiveText, positiveListener, cancelText, cancelListener)
    }

    protected fun getProgress(): Progress {
        return progress
    }

    protected fun showProgress(message: String) {
        if(context != null && !isDetached && !isRemoving) progress.progress(message)
    }

    protected fun showCancelableProgress(message: String) {
        if(context != null && !isDetached && !isRemoving) progress.progress(message, this)
    }

    protected fun showCancelableProgress(message: String, canceler: ICancel) {
        if(context != null && !isDetached && !isRemoving) progress.progress(message, canceler)
    }

    protected fun dismissProgress() {
        if(context != null && !isDetached && !isRemoving) progress.dismiss()
    }

    override fun onCancel() {
        // If anything needs to be done when a progress spinner is canceled, override this method in the inheriting class
    }
}