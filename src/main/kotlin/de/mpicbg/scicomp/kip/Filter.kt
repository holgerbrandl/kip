package de.mpicbg.scicomp.kip

import de.mpicbg.scicomp.kip.misc.Format
import net.imagej.ops.Ops
import net.imagej.ops.special.computer.AbstractUnaryComputerOp
import net.imagej.ops.special.computer.Computers
import net.imagej.ops.special.computer.UnaryComputerOp
import net.imglib2.RandomAccessibleInterval
import net.imglib2.algorithm.morphology.StructuringElements
import net.imglib2.algorithm.neighborhood.HyperSphereShape
import net.imglib2.img.Img
import net.imglib2.type.NativeType
import net.imglib2.type.numeric.RealType
import net.imglib2.type.numeric.real.FloatType
import net.imglib2.util.Util
import net.imglib2.view.Views

/**
 * @author Holger Brandl
 */


internal class InvertComputationCIP<T : RealType<T>>(maxT: T, minT: T) : AbstractUnaryComputerOp<T, T>() {
    internal val max: Double
    internal val min: Double


    init {
        max = maxT.realDouble
        min = minT.realDouble
    }


    override fun compute(input: T, output: T) {
        output.setReal(max + min - input.realDouble)
    }
}

fun <T> RandomAccessibleInterval<T>.invert(): RandomAccessibleInterval<T> where T : RealType<T>, T : NativeType<T> {
    val inputImage = this
    val minMax = calcMinMax()

    val mapper = InvertComputationCIP<T>(minMax.min, minMax.max)

    val inputIter = Views.iterable<T>(inputImage)
    val outputImage: RandomAccessibleInterval<T> = opService.create().img(inputImage)
    return opService.map(
        outputImage,
        inputIter,
        mapper
    )
}


// http://imagej.net/ImgLib2_Examples#Example_1b_-_Opening_an_ImgLib2_image

/**
 * @param radius radius is assumed to in the same unit as pixelSize
 */
fun <T> RandomAccessibleInterval<T>.gauss(
    //    inputImage: RandomAccessibleInterval<T>,
    radius: Int = 10,
    radiusByDim: List<Float> = FloatArray(dim().size) { radius.toFloat() }.toList(),
    boundaryMethod: BoundaryMethod = BoundaryMethod.mirror,
    pixRadius: DoubleArray = calcPixelRadius(numDimensions(), radiusByDim, ONE_PIXEL(numDimensions()))
): Img<T> where T : RealType<T>, T : NativeType<T> {

    val bmConfig = adjustBoundary(boundaryMethod)
    val outOfBoundFactory = Format.outOfBoundFactory(bmConfig.bm, bmConfig.value)

    val imgFactory = Util.getArrayOrCellImgFactory(this, FloatType(0f))
    val outputImage = imgFactory.create(this, FloatType(0f))

    // @OpService ops
    return opService.filter().gauss(outputImage, this, pixRadius, outOfBoundFactory) as Img<T>
}


enum class Shape { disk, rectangle }


/**
 * @param radius  is assumed to in the same unit as pixelSize
 */
fun <T> RandomAccessibleInterval<T>.median(
    radius: List<Float>,
    shape: Shape = Shape.rectangle,
    boundaryMethod: BoundaryMethod = BoundaryMethod.mirror,
    pixRadius: DoubleArray = calcPixelRadius(numDimensions(), radius, ONE_PIXEL(numDimensions()))
): Img<T> where T : RealType<T>, T : NativeType<T> {

    val nDim = numDimensions()
    val inputImage = this

    val decompose = false // todo expose in signature

    require(shape != Shape.disk || (nDim == 2 && isIsotropic(nDim, pixRadius))) {
        "disk shapes only work in 2D at the moment"
    }

    val strels = when (shape) {
        Shape.disk -> {
            if (decompose)
                StructuringElements.disk(pixRadius[0].toLong(), nDim)
            else
                listOf(HyperSphereShape(pixRadius[0].toLong()))
        }
        else -> { // rectangle
            val intPixRadius = IntArray(nDim)

            for (d in 0 until nDim) {
                intPixRadius[d] = pixRadius[d].toInt()
            }
            StructuringElements.rectangle(intPixRadius, decompose)
        }
    }

    val bmConfig = this.adjustBoundary(boundaryMethod)
    val outOfBoundFactory = Format.outOfBoundFactory(bmConfig.bm, bmConfig.value)


    ///////////////////////////////////////////////////////////////////////
    // process the input image
    ///////////////////////////////////////////////////////////////////////

    // todo why not just opService.filter().median() ??

    val outputImage = opService.create().img(inputImage)
    //Iterable<T> outputIter = Views.flatIterable(outputImage);

    val valueT = outputImage.randomAccess().get()
    val filterOp = Computers.unary(
        opService,
        Ops.Stats.Median::class.java,
        valueT.javaClass,
        Iterable::class.java
    ) as UnaryComputerOp<*, *>

    val map = Computers.unary<RandomAccessibleInterval<T>, RandomAccessibleInterval<T>>(
        opService,
        Ops.Map::class.java,
        outputImage,
        inputImage,
        strels!![0],
        filterOp
    )

    map.compute(Views.interval<T>(
        Views.extend<T, RandomAccessibleInterval<T>>(inputImage, outOfBoundFactory),
        inputImage
    ), outputImage)

    return outputImage
}


private fun isIsotropic(nDim: Int, pixRadius: DoubleArray): Boolean {
    var isotropic = true
    for (d in 1 until nDim)
        if (pixRadius[d - 1].toInt() != pixRadius[d].toInt())
            isotropic = false
    return isotropic
}