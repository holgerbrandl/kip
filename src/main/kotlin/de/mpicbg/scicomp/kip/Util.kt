package de.mpicbg.scicomp.kip

import ij.io.FileSaver
import net.imagej.ImageJ
import net.imagej.ops.OpService
import net.imagej.patcher.LegacyInjector
import net.imglib2.RandomAccessibleInterval
import net.imglib2.img.Img
import net.imglib2.img.array.ArrayImgs
import net.imglib2.img.display.imagej.ImageJFunctions
import net.imglib2.type.NativeType
import net.imglib2.type.logic.BitType
import net.imglib2.type.numeric.NumericType
import net.imglib2.type.numeric.RealType
import net.imglib2.type.numeric.real.FloatType
import org.scijava.Context
import org.scijava.console.ConsoleService
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO

/**
 * @author Holger Brandl
 */


//val myIJ by lazy { ij.ImageJ() }
@Deprecated("should not used/needed in a programmatic way")
val myIJ by lazy {
    LegacyInjector.preinit();
    ImageJ().apply {
        ui().showUI()
    }
}


val opService: OpService by lazy {
    //    get() = Context(OpService::class.java, ConsoleService::class.java).getService(OpService::class.java)

    Context(OpService::class.java, ConsoleService::class.java).getService(OpService::class.java)
}

//object Init{
//    init {
//        System.setProperty("java.awt.headless", "false")
//    }
//}

fun <T : NumericType<T>> RandomAccessibleInterval<T>.show() {
    // see https://youtrack.jetbrains.com/issue/KT-18181
    //    System.setProperty("java.awt.headless", "false")

    val isJupyter = false // so how to we detect that?

    if (isJupyter) {
        val bufferedImage = asBufferedImage()
        val buf = ByteArrayOutputStream()

        val writer = ImageIO.getImageWritersByMIMEType("image/jpeg").next()

        val ios = ImageIO.createImageOutputStream(buf)
        writer.output = ios
        writer.write(bufferedImage)
        ios.close()
        val b64img = Base64.getEncoder().encodeToString(buf.toByteArray())
        JupyterUtil.resultOf("image/jpeg" to b64img)
    } else {
        ImageJFunctions.show(this, "")
    }
}

fun <T> RandomAccessibleInterval<T>.asBufferedImage(): BufferedImage? where T : NumericType<T> {
    val imagePlus = ImageJFunctions.wrap<T>(this, "")
    return imagePlus.getBufferedImage()
}

fun <T> openImage(path: String): Img<T> where T : RealType<T>, T : NativeType<T> {
    // create the ImgOpener
    //    val imgOpener = ImgOpener()

    // open with ImgOpener. The type (e.g. ArrayImg, PlanarImg, CellImg) is
    // automatically determined. For a small image that fits in memory, this
    // should open as an ArrayImg.
    //    return imgOpener.openImg(path) as Img<T>
    return net.imglib2.img.ImagePlusAdapter.wrapImgPlus<T>(ij.io.Opener().openImage(path))
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

fun <T : NumericType<T>?> Img<T>.toFloat(): Img<FloatType> {
    opService
    return ImageJFunctions.wrap<FloatType>(ImageJFunctions.wrap(this, "foo"))
}


//todo use two types here and do internal conversion
// use core method for all arithmetics


// convenience accessors
fun <T> RandomAccessibleInterval<T>.dim() = LongArray(numDimensions(), { 0 }).apply { dimensions(this) }


fun pckgFun() = println("i'm sitting on root!")

