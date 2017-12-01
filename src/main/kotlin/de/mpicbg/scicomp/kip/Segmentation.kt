package de.mpicbg.scicomp.kip

import net.imglib2.RandomAccessibleInterval
import net.imglib2.algorithm.labeling.ConnectedComponents
import net.imglib2.roi.labeling.ImgLabeling
import net.imglib2.type.NativeType
import net.imglib2.type.logic.BitType
import net.imglib2.type.numeric.IntegerType
import net.imglib2.type.numeric.RealType
import net.imglib2.view.Views

/**
 * @author Holger Brandl
 */


fun <T> RandomAccessibleInterval<T>.threshold(
    threshold: Float
): RandomAccessibleInterval<BitType> where T : RealType<T>, T : NativeType<T> {
    val T_threshold = randomAccess().get().createVariable()
    T_threshold.setReal(threshold)
    return opService.threshold().apply(Views.iterable<T>(this), T_threshold) as RandomAccessibleInterval<BitType>
}


//data class LabeledImage<T : IntegerType<T>?>(val labelImage: RandomAccessibleInterval<T>, val labeling: ImgLabeling<Int, T>)

fun <T> RandomAccessibleInterval<T>.label(
    threshold: Float = 0.5f  // if the image is logic value will translate to 0, 1
): ImgLabeling<Int, T> where T : IntegerType<T>, T : RealType<T>, T : NativeType<T> {

    //    val labeler = RleCCL<T>(this, threshold)
    val outputImage: RandomAccessibleInterval<T> = opService.create().img(this)
    val labeling: ImgLabeling<Int, T> = opService.labeling().cca(this, ConnectedComponents.StructuringElement.EIGHT_CONNECTED)

    labeling.mapping.labelsAtIndex(1)

    return labeling
}


fun main(args: Array<String>) {

    val labelImage = bubbles()
        .gauss(10)
        .showThen()
        .threshold(0.3f)
        .invert()
        .showThen()
        .label()

    labelImage.indexImg.show()
}
