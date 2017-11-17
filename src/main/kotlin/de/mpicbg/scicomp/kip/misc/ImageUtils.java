package de.mpicbg.scicomp.kip.misc;

import ij.io.FileSaver;
import net.imglib2.Cursor;
import net.imglib2.img.Img;
import net.imglib2.img.ImgFactory;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.img.display.imagej.ImageJFunctions;
import net.imglib2.type.NativeType;
import net.imglib2.type.logic.BitType;
import net.imglib2.type.numeric.RealType;

import java.util.Random;

import static de.mpicbg.scicomp.kip.BuilderKt.bubbles;

/**
 * @author Holger Brandl
 */
@SuppressWarnings("WeakerAccess")
public class ImageUtils {
    public static void main(String[] args) {
        Img<BitType> testImage = bubbles(new int[]{500, 500}, 129);

        new FileSaver(ImageJFunctions.wrapBit(testImage, "")).saveAsPng("image.png");
    }


    // from https://github.com/imglib/imglib2/blob/3016066a5c77969d1d5008a627475d6e98c251b4/src/test/java/tests/JUnitTestBase.java#L213
    public static <T extends RealType<T> & NativeType<T>> Img<T> noiseImage(final T type, final long[] dims, Random r) {
        final ImgFactory<T> factory = new ArrayImgFactory<T>();
        final Img<T> result = factory.create(dims, type);
        final Cursor<T> cursor = result.cursor();
        final long[] pos = new long[cursor.numDimensions()];
        while (cursor.hasNext()) {
            cursor.fwd();
            cursor.localize(pos);
            final float value = new Integer(r.nextInt()).byteValue();
            cursor.get().setReal(value);
        }
        return result;
    }
}
