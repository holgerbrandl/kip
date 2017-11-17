package de.mpicbg.scicomp.kip

import de.mpicbg.scicomp.kip.misc.Format
import net.imglib2.RandomAccessibleInterval
import net.imglib2.algorithm.stats.ComputeMinMax
import net.imglib2.type.NativeType
import net.imglib2.type.numeric.RealType


//
// Internal helpers
//


fun ONE_PIXEL(numDimensions: Int) = Format.perDim(listOf(1f, 1f).toTypedArray(), numDimensions)


enum class BoundaryMethod {
    zero, one, min, max, same, mirror, periodic, value
}

internal data class BoundaryConfig<T>(val bm: BoundaryMethod, val value: T?)

internal fun <T> RandomAccessibleInterval<T>.adjustBoundary(boundaryMethod: BoundaryMethod): BoundaryConfig<T> where T : RealType<T>, T : NativeType<T> {
    var valueT: T? = randomAccess().get().createVariable()


    val adjustedBM = if (boundaryMethod == BoundaryMethod.min) {
        valueT = calcMinMax().min
        BoundaryMethod.value
    } else if (boundaryMethod == BoundaryMethod.max) {
        valueT = calcMinMax().max
        BoundaryMethod.value
    } else if (boundaryMethod == BoundaryMethod.zero) {
        valueT!!.setZero()
        BoundaryMethod.value
    } else if (boundaryMethod == BoundaryMethod.one) {
        valueT!!.setOne()
        BoundaryMethod.value
    } else {
        boundaryMethod
    }
    return BoundaryConfig<T>(adjustedBM, valueT)
}

internal data class MinMax<T>(val min: T, val max: T)

internal fun <T> RandomAccessibleInterval<T>.calcMinMax(): MinMax<T> where T : RealType<T>, T : NativeType<T> {
    val minMax by lazy {
        val minT = randomAccess().get().createVariable()
        val maxT = minT!!.createVariable()

        ComputeMinMax.computeMinMax(this, minT, maxT)

        MinMax(minT, maxT)
    }
    return minMax
}

fun calcPixelRadius(nDim: Int, radius: List<Float>, pixelSize: Array<Float>): DoubleArray {
    val pixRadius = DoubleArray(nDim)
    for (d in 0 until (nDim - 1)) {
        pixRadius[d] = (radius[d] / pixelSize[d]).toDouble()
    }
    return pixRadius
}

