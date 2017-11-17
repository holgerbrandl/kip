package de.mpicbg.scicomp.kip

import de.mpicbg.scicomp.kip.misc.Format
import net.imglib2.RandomAccessibleInterval
import net.imglib2.algorithm.stats.ComputeMinMax
import net.imglib2.img.Img
import net.imglib2.type.NativeType
import net.imglib2.type.numeric.RealType
import net.imglib2.type.numeric.real.FloatType
import net.imglib2.util.Util

// http://imagej.net/ImgLib2_Examples#Example_1b_-_Opening_an_ImgLib2_image

/**
 * @param radius radius is assumed to in the same unit as pixelSize
 */
fun <T> RandomAccessibleInterval<T>.gauss(
    //    inputImage: RandomAccessibleInterval<T>,
    radius: List<Float> = listOf(10f, 10f),
    boundaryMethod: BoundaryMethod = BoundaryMethod.mirror,
    pixRadius: DoubleArray = calcPixelRadius(numDimensions(), radius, ONE_PIXEL(numDimensions()))
): Img<T> where T : RealType<T>, T : NativeType<T> {

    val bmConfig = adjustBoundary(boundaryMethod)
    val outOfBoundFactory = Format.outOfBoundFactory(bmConfig.bm, bmConfig.value)

    val imgFactory = Util.getArrayOrCellImgFactory(this, FloatType(0f))
    val outputImage = imgFactory.create(this, FloatType(0f))

    // @OpService ops
    return opService.filter().gauss(outputImage, this, pixRadius, outOfBoundFactory) as Img<T>
}


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
        valueT = calcMinMax().first
        BoundaryMethod.value
    } else if (boundaryMethod == BoundaryMethod.max) {
        valueT = calcMinMax().second
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


internal fun <T> RandomAccessibleInterval<T>.calcMinMax(): Pair<T, T> where T : RealType<T>, T : NativeType<T> {
    val minMax by lazy {
        val minT = randomAccess().get().createVariable()
        val maxT = minT!!.createVariable()

        ComputeMinMax.computeMinMax(this, minT, maxT)

        minT to maxT
    }
    return minMax
}

fun calcPixelRadius(nDim: Int, radius: List<Float>, pixelSize: Array<Float>): DoubleArray {
    val pixRadius = DoubleArray(nDim)
    for (d in 0 until nDim) {
        pixRadius[d] = (radius[d] / pixelSize[d]).toDouble()
    }
    return pixRadius
}

