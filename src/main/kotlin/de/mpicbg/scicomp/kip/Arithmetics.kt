package de.mpicbg.scicomp.kip

import net.imglib2.RandomAccessibleInterval
import net.imglib2.img.Img
import net.imglib2.view.Views

/**
 * @author Holger Brandl
 */


infix operator fun <T> RandomAccessibleInterval<T>.plus(other: RandomAccessibleInterval<T>) =
    arith(this, other, ImgArithOp.ADD)

infix operator fun <T> RandomAccessibleInterval<T>.minus(other: RandomAccessibleInterval<T>) =
    arith(this, other, ImgArithOp.SUBTRACT)

infix operator fun <T> RandomAccessibleInterval<T>.div(other: RandomAccessibleInterval<T>) =
    arith(this, other, ImgArithOp.DIVIDE)

infix operator fun <T> RandomAccessibleInterval<T>.times(other: RandomAccessibleInterval<T>) =
    arith(this, other, ImgArithOp.MULTIPLY)


internal enum class ImgArithOp { ADD, SUBTRACT, DIVIDE, MULTIPLY }

internal fun <T> arith(inputImage1: RandomAccessibleInterval<T>, inputImage2: RandomAccessibleInterval<T>, op: ImgArithOp): RandomAccessibleInterval<T> {

    // https://stackoverflow.com/questions/35272761/how-to-compare-two-arrays-in-kotlin
    require(inputImage1.dim() contentEquals inputImage2.dim()) {
        "dimensions do not match"
    }

    val in1Iter = Views.iterable<T>(inputImage1)
    val in2Iter = Views.iterable<T>(inputImage2)

    return if (in1Iter.iterationOrder() == in2Iter.iterationOrder()) {
        opService.run("math." + op.toString().toLowerCase(), in1Iter, in2Iter) as Img<T>
    } else {
        opService.run("math." + op.toString().toLowerCase(), in1Iter, inputImage2) as Img<T>
    }
}
