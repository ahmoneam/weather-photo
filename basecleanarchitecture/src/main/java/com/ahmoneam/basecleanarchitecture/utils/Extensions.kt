package com.ahmoneam.basecleanarchitecture.utils

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.ahmoneam.basecleanarchitecture.R
import com.ahmoneam.basecleanarchitecture.base.data.model.Markable
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.text.SimpleDateFormat
import java.util.*


/**
 * Find the closest ancestor of the given type.
 */
inline fun <reified T> View.findParentOfType(): T? {
    var p = parent
    while (p != null && p !is T) p = p.parent
    return p as T?
}

/**
 * Scroll down the minimum needed amount to show [descendant] in full. More
 * precisely, reveal its bottom.
 */
fun ViewGroup.scrollDownTo(descendant: View, plus: Int = 0) {
    // Could use smoothScrollBy, but it sometimes over-scrolled a lot
    howFarDownIs(descendant)?.let { scrollBy(0, it + plus) }
}

/**
 * Calculate how many pixels below the visible portion of this [ViewGroup] is the
 * bottom of [descendant].
 *
 * In other words, how much you need to scroll down, to make [descendant]'s bottom
 * visible.
 */
fun ViewGroup.howFarDownIs(descendant: View): Int? {
    val bottom = Rect().also {
        // See https://stackoverflow.com/a/36740277/1916449
        descendant.getDrawingRect(it)
        offsetDescendantRectToMyCoords(descendant, it)
    }.bottom
    return (bottom - height - scrollY).takeIf { it > 0 }
}

fun String.isEmailValid(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun <T> MutableLiveData<T>.toLiveData(): LiveData<T> {
    return this
}

fun Any?.isNull(): Boolean = this == null

fun Any?.isNotNull(): Boolean = this != null

fun <T> LiveData<T>.isValueNull(): Boolean = this.value == null

fun <T> LiveData<T>.isValueNotNull(): Boolean = this.value != null

inline fun <reified T> getKoinInstance(): Lazy<T> {
    return lazy {
        return@lazy object : KoinComponent {
            val value: T by inject()
        }.value
    }
}

fun convertNumberArabicToEnglish(input: String): String =
    input.map { it.toInt() }
        .map {
            if (it in 1632..1641) {
                //arabic number
                (it - 1584).toChar()
            } else {
                //default
                it.toChar()
            }
        }.joinToString("")

fun convertNumberEnglishToArabic(input: String): String {
    var value = ""
    for (character in input.toCharArray()) {
        val ascii = character.toInt()
        value += if (ascii >= 49 && ascii <= 51) {
            //english number
            val valueOld = ascii + 1584
            val valueChar = valueOld.toChar()
            valueChar.toString()
        } else {
            //default
            character.toString()
        }
    }
    return value
}

inline fun <reified T> dependantLiveData(
    vararg dependencies: LiveData<out Any>,
    defaultValue: T? = null,
    crossinline mapper: () -> T?
): MutableLiveData<T> =
    MediatorLiveData<T>().also { mediatorLiveData ->
        val observer = Observer<Any> { mediatorLiveData.value = mapper() }
        dependencies.forEach { dependencyLiveData ->
            mediatorLiveData.addSource(dependencyLiveData, observer)
        }
    }.apply { value = defaultValue }


inline fun <reified T, reified R> dependantLiveDataWithHandler(
    vararg dependencies: LiveData<out R>,
    defaultValue: T? = null,
    crossinline handler: (MediatorLiveData<T>, R) -> Unit
): MutableLiveData<T> =
    MediatorLiveData<T>().also { mediatorLiveData ->
        val observer = Observer<R> { handler(mediatorLiveData, it) }
        dependencies.forEach { dependencyLiveData ->
            mediatorLiveData.addSource(dependencyLiveData, observer)
        }
    }.apply { value = defaultValue }


fun <E> Collection<E>.toMarkableList(): MutableList<Markable<E>> {
    return map { Markable(it) }.toMutableList()
}

fun <E> Collection<Markable<E>>.toItems(): MutableList<E> = map { it.data }.toMutableList()

fun ViewGroup.showError() {
    setBackgroundResource(R.drawable.bg_error_view)
}

fun ViewGroup.hideError() {
    background = null
}

fun ViewGroup.recursiveLoopChildren(
    handleViewGroup: ((ViewGroup) -> Unit)? = null,
    handleView: ((View) -> Unit)? = null
) {
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (child is ViewGroup) {
            child.recursiveLoopChildren(handleViewGroup)
            handleViewGroup?.invoke(child)
        } else {
            if (child != null) { // DO SOMETHING WITH VIEW
                handleView?.invoke(child)
            }
        }
    }
}

fun String.hasSpecialCharacter(): Boolean {
    return ".*[!@#$%&*()_+=|<>?{}\\[\\]~-].*".toRegex().matches(this)
}

tailrec fun Fragment.getRootParentFragment(): Fragment =
    if (parentFragment == null) this else parentFragment!!.getRootParentFragment()

fun String.toDateWithHoursMinuets24(): Date? {
    return SimpleDateFormat("HH:mm:ss", Locale.getDefault()).parse(this)
}

fun Date.toFormattedTime(): String? {
    return SimpleDateFormat("hh:mm a", Locale.getDefault()).format(this)
}
