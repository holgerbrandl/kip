import de.mpicbg.scicomp.kip.*
import net.imglib2.type.numeric.real.FloatType


val image = bubbles()

opsService
pckgFun()


image.show()
image.save("my_image.png")


//val image2 = openImage<FloatType>("images/blobs32.tif")
val image2 = openImage<FloatType>("/Users/brandl/projects/kotlin/kip/images/blobs32.tif")

image2.dim()
image2.gauss()

