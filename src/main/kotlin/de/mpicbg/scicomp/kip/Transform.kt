package de.mpicbg.scicomp.kip

import invizio.cip.parameters.Format
import net.imglib2.algorithm.stats.ComputeMinMax
import net.imglib2.img.Img
import net.imglib2.type.NativeType
import net.imglib2.type.numeric.RealType
import net.imglib2.type.numeric.real.FloatType
import net.imglib2.util.Util

/**
 * @author Holger Brandl
 */


// http://imagej.net/ImgLib2_Examples#Example_1b_-_Opening_an_ImgLib2_image

fun <T> Img<T>.gauss(
    //    inputImage: RandomAccessibleInterval<T>,
    radius: List<Float> = listOf(10f, 10f),
    boundaryMethod: BoundaryMethod = BoundaryMethod.mirror,
    pixelSize: List<Float> = listOf(1f, 1f)
): Img<T> where T : RealType<T>, T : NativeType<T> {


    val nDim = numDimensions()

    val pixelSizeFormat = Format.perDim(pixelSize.toTypedArray(), nDim)

    val radiusFormat = Format.perDim(radius.toTypedArray(), nDim)

    // radius is assumed to in the same unit as pixelSize
    val pixRadius = DoubleArray(nDim)
    for (d in 0 until nDim) {
        pixRadius[d] = (radius[d] / pixelSize[d]).toDouble()
    }


    var valueT: T? = randomAccess().get().createVariable()


    val minMax by lazy {
        val minT = randomAccess().get().createVariable()
        val maxT = minT!!.createVariable()

        ComputeMinMax.computeMinMax(this, minT, maxT)

        minT to maxT
    }

    val adjustedBM = if (boundaryMethod == BoundaryMethod.min) {
        valueT = minMax.first
        BoundaryMethod.value
    } else if (boundaryMethod == BoundaryMethod.max) {
        valueT = minMax.second
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

    val outOfBoundFactory = Format.outOfBoundFactory(boundaryMethod.toString(), valueT)


    val imgFactory = Util.getArrayOrCellImgFactory(this, FloatType(0f))
    val outputImage = imgFactory.create(this, FloatType(0f))


    // @OpService ops
    return ij.op().filter().gauss(outputImage, this, pixRadius, outOfBoundFactory) as Img<T>
}

enum class BoundaryMethod {
    zero, one, min, max, same, mirror, periodic, value
}
