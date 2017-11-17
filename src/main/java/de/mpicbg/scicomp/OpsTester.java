package de.mpicbg.scicomp;

import ij.io.Opener;
import io.scif.img.ImgIOException;
import net.imagej.ImgPlus;
import net.imagej.ops.OpService;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.array.ArrayImg;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.img.basictypeaccess.array.DoubleArray;
import net.imglib2.type.numeric.real.DoubleType;
import org.scijava.Context;

import static net.imglib2.img.ImagePlusAdapter.wrapImgPlus;

/**
 * @author Holger Brandl
 */
public class OpsTester {

    public static void main(String[] args) throws ImgIOException {
//        ImageJ ij = new net.imagej.ImageJ();
//        ij.ui().showUI();
//        System.out.println("foo");

        Context ctx = new Context();
        OpService ops = ctx.getService(OpService.class);
//        ops.filter();

        ArrayImg<DoubleType, DoubleArray> bubbles = ArrayImgs.doubles(new double[]{1., 1., 2., 2.}, 2, 2);


        // open with ImgOpener. The type (e.g. ArrayImg, PlanarImg, CellImg) is
        // automatically determined. For a small image that fits in memory, this
        // should open as an ArrayImg.
//        SCIFIOImgPlus<?> image = new ImgOpener().openImgs("image.png").get(0);
        ImgPlus image = wrapImgPlus(new Opener().openImage("/Users/brandl/projects/kotlin/kip/images/blobs32.tif"));
        System.out.println(image.dimension(0));

        RandomAccessibleInterval filt = ops.filter().gauss(bubbles, 1., 1.);
        System.out.println("done: " + filt);
    }
}
