package de.mpicbg.scicomp;

import de.mpicbg.scicomp.kip.UtilKt;
import net.imglib2.img.Img;
import net.imglib2.type.numeric.real.FloatType;

/**
 * @author Holger Brandl
 */
public class JavaKip {
    public static void main(String[] args) {
        Img<FloatType> image = UtilKt.openImage("images/blobs32.tif");
        System.out.println(image.dimension(0));

    }
}
