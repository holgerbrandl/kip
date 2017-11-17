package de.mpicbg.scicomp.kip

import ij.io.FileSaver
import io.scif.img.ImgOpener
import net.imagej.ImageJ
import net.imglib2.img.Img
import net.imglib2.img.array.ArrayImgs
import net.imglib2.img.display.imagej.ImageJFunctions
import net.imglib2.type.logic.BitType
import net.imglib2.type.numeric.NumericType
import net.imglib2.type.numeric.RealType
import net.imglib2.type.numeric.real.FloatType

/**
 * @author Holger Brandl
 */


val ij = ImageJ()


fun <T : NumericType<T>> Img<T>.show() {
    // see https://youtrack.jetbrains.com/issue/KT-18181
    System.setProperty("java.awt.headless", "false")

    ImageJFunctions.show(this, "test")
}


fun <T> openImage(path: String): Img<T> {
    // create the ImgOpener
    val imgOpener = ImgOpener()

    // open with ImgOpener. The type (e.g. ArrayImg, PlanarImg, CellImg) is
    // automatically determined. For a small image that fits in memory, this
    // should open as an ArrayImg.
    return imgOpener.openImg(path) as Img<T>

}


fun Array<Float>.asImage(vararg dims: Int) =
    ArrayImgs.floats(toFloatArray(), *dims.map { it.toLong() }.toLongArray())

fun Array<Int>.asImage(vararg dims: Int) =
    ArrayImgs.ints(toIntArray(), *dims.map { it.toLong() }.toLongArray())


fun fromMatrix(data: Array<Int>, vararg dims: Int) {
    ArrayImgs.ints(data.toIntArray(), *dims.map(Int::toLong).toLongArray())
}


inline fun <reified T : RealType<T>?> Img<T>.save(fileName: String): Boolean {
    val converted = when {
        randomAccess().get() is BitType -> ImageJFunctions.wrapBit(this, "foo")
        randomAccess().get() is FloatType -> ImageJFunctions.wrapFloat(this, "foo")
        else -> TODO("image type not yet supported")
    }

    return FileSaver(converted).saveAsPng(fileName)
}

fun <T : NumericType<T>?> Img<T>.toFloat() = ImageJFunctions.wrap<FloatType>(ImageJFunctions.wrap(this, "foo"))
