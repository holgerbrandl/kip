import de.mpicbg.scicomp.kip.*
import net.imglib2.type.numeric.real.FloatType


val image = bubbles()
val other = bubbles()

// image arithmetics
val imageDiff = image + other
val imageProp = image / other

// number arithmethics
val imageDiff = image - 3

// display as usual
image.show()

// save to file
image.save("my_image.png")

// open from file
val blobs = openImage<FloatType>("images/blobs32.tif")
//val image2 = openImage<FloatType>("/Users/brandl/projects/kotlin/kip/images/blobs32.tif")

// little helpers to ease some of the API pain
image.dim()

// misc operators to transform images
image.gauss()

// default parameters which are also named parameters
image.median(listOf(10f, 10f), shape = Shape.disk)

