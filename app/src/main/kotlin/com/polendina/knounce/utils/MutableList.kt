package com.polendina.knounce.utils

/**
 * Swap two elements at a mutable list.
 * If the initial destination is out of bounds, then simply remove & append the other element.
 *
 * @param first index of the initial destination.
 * @param second index of the latter destination.
 */
fun <T> MutableList<T>.swap(first: Int, second: Int) {
    if (first >= this.size) {
        this.add(this[second]); this.remove(this[second])
    } else {
        this[first] = this[second].also { this[second] = this[first] }
    }
}