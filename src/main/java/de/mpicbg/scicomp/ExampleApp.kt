package de.mpicbg.scicomp

import net.imagej.ImageJ

/**
 * @author Holger Brandl
 */

fun main(args: Array<String>) {
    val ij = ImageJ()
    ij.ui().showUI()

    println("foo")
}