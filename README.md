# kip - Common {i}mage {p}rocessing with {K}otlin


A concise DSL to perform common image processing tasks using Kotlin

![](.README_images/b0faf8b6.png)

## Features

* Fully typed method signatures
* Concise and easy to read
* Excellent tooling, interactive REPL and IDE support
* Extension methods only, no new types
* Sensible default parameters where possible
* Immutable (for now)
* Ready for jupyter

## Examples

```kotlin
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

// default parameters which are also named parameters
val medianImage = image.median(listOf(10f,10f), shape = Shape.disk)

// method chaining
image.gauss().show()
image.gauss().median().apply{ save("some.png") }.show()


// segmentation & labeling
 val labelImage = bubbles()
    .gauss(listOf(10f))
    .showThen()
    .threshold(0.3f)
    .invert()
    .showThen()
    .label()

labelImage.show()

```

## Methods

Filters
* [X] Median Filter
* [X] Gauss Filter
* [X] Inversion

Segmentation
* [X] Threshold
* [X] Labeling


Still missing but on our list (drop us a ticket if needed)
* Morphologial Ops (Erode, Dilate, Open, Close)
* Projections
* Scalar Arithmetics
* Slicing
* Watershed
* TopHat Filter

Meanwhile you can for sure mix `ops` or `imglib2` api directly


## Next steps

* [ ] Add remaining operators from CIP
* [ ] Provide kscript and jupyter example notebooks
* [ ] port some examples from [here](imagej-scripting/0.6.0/imagej-scripting-0.6.0.jar!/script_templates/Tutorials) to `kip`


## Acknowledgments

We stole most bits and pieces from [CIP](https://github.com/benoitlo/CIP)

Also thanks to the imglib2 gitter community.