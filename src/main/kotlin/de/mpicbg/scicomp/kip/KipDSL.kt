package de.mpicbg.scicomp.kip

import de.mpicbg.scicomp.kip.misc.ImageUtils.noiseImage
import ij.io.FileSaver
import net.imglib2.exception.IncompatibleTypeException
import net.imglib2.img.Img
import net.imglib2.img.display.imagej.ImageJFunctions
import net.imglib2.type.logic.BitType
import net.imglib2.type.numeric.NumericType
import net.imglib2.type.numeric.RealType
import net.imglib2.type.numeric.real.FloatType

/**
 * @author Holger Brandl
 */


fun <T : NumericType<T>> Img<T>.show() {
    // see https://youtrack.jetbrains.com/issue/KT-18181
    System.setProperty("java.awt.headless", "false")

    ImageJFunctions.show(this, "test")
}


inline fun <reified T : RealType<T>?> Img<T>.save(fileName: String): Boolean {
    val converted = when {
        randomAccess().get() is BitType -> ImageJFunctions.wrapBit(this, "foo")
        randomAccess().get() is FloatType -> ImageJFunctions.wrapFloat(this, "foo")
        else -> TODO("image type not yet supported")
    }

    return FileSaver(converted).saveAsPng(fileName)
}

fun bubbles(dims: IntArray = intArrayOf(500, 500), cutoff: Int = 129): Img<BitType> {
    // old IJ API
    // todo set seed here!! ij.process.ByteProcessor.noise() is using fresh Random()
    //        int options = NewImage.FILL_RANDOM + NewImage.CHECK_AVAILABLE_MEMORY;
    //        ImagePlus img = NewImage.createByteImage("Noise", dims[0], dims[1], dims[2], options);
    //        ImagePlusImg image = ImagePlusAdapter.wrap(img);
    //        Img<UnsignedByteType> image = ImageJFunctions.wrap(img);

    // more modern imglib2 API
    val longDims = java.util.Arrays.stream(dims).asLongStream().toArray()
    val image = noiseImage(net.imglib2.type.numeric.integer.UnsignedByteType(), longDims, java.util.Random(1))

    //        ImageJFunctions.show(image, "noise");


    // perform gaussian convolution with float precision (see http://imagej.net/ImgLib2_Examples)
    //        double[] sigma = IntStream.range(0, image.numDimensions()).mapToDouble(d -> 8).toArray();

    // first extend the image to infinity, zeropad
    val infiniteImg = net.imglib2.view.Views.extendValue(image, net.imglib2.type.numeric.integer.UnsignedByteType())

    val sigma: Int

    if (dims.size == 2) {
        sigma = 8
    } else {
        sigma = 3
    }

    try {
        net.imglib2.algorithm.gauss3.Gauss3.gauss(sigma.toDouble(), infiniteImg, image)
    } catch (e: IncompatibleTypeException) {
        throw RuntimeException(e)
    }

    //        ImageJFunctions.show(image, "nach_gauss");


    // threshold see https://github.com/StephanPreibisch/imglib2-introduction/blob/master/src/main/java/net/imglib2/introduction/ImgLib2_Threshold6.java

    // from http://wiki.cmci.info/documents/120206pyip_cooking/python_imagej_cookbook#threshold_to_create_a_mask_binary
    // note reasonable cutoffs are between 127 and 131 which changes from 1 to multiple components
    //        ImagePlus thresholdImg = ImageJFunctions.wrapFloat(image, "bar");
    //        thresholdImg.getProcessor().threshold(255);


    //        return threshold;
    //

    // this worked (also see gitter
    // from https://www.programcreek.com/java-api-examples/index.php?api=net.imglib2.converter.Converter
    //        RandomAccessibleInterval<ByteType> convert = Converters.convert(threshold, (input, output) -> {
    //            output.set((byte) (input.get() ? 1 : 0));
    //        }, new ByteType());
    //        Img<ByteType> wrap = ImgView.wrap(convert, new ArrayImgFactory<>());
    //        ImageJFunctions.show(wrap, "131");


    //
    //        Scale affine = new Scale(100);
    //        threshold = .affine(threshold, affine);


    // how to convert http://forum.imagej.net/t/how-to-convert-from-net-imagej-dataset-to-net-imglib2-img-img-floattype/1371/2
    //        Img<FloatType> img2 = ImageJFunctions.convertFloat(ImagePlusAdapter.wrap(convert));

    return net.imglib2.algorithm.binary.Thresholder.threshold(image, net.imglib2.type.numeric.integer.UnsignedByteType(cutoff.toByte().toInt()), false, 1)
    //        return ImagePlusAdapter.wrap(thresholdImg) ;
}

