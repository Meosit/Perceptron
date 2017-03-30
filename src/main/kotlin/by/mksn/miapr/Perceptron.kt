package by.mksn.miapr

import java.io.File
import java.util.*
import javax.imageio.ImageIO

val RANDOM = Random()
fun Double.equalsWithPrecision(other: Double, precision: Double = 0.000001): Boolean
        = Math.abs(this - other) < precision

fun Double.sqr(): Double = this * this

fun imageToPixelArray(fileName: String): IntArray {
    val classLoader = Perceptron::class.java.classLoader
    val file = File(classLoader.getResource(fileName)!!.file)
    val image = ImageIO.read(file)
    val pixels = IntArray(image.width * image.height)
    for (x in 0..image.width - 1) {
        for (y in 0..image.height - 1) {
            pixels[x * image.width + y] = (if (image.getRGB(x, y) == 0xFFFFFFFF.toInt()) 0 else 1)
        }
    }
    return pixels
}

data class PictureImage(val pixelArray: IntArray, var isTargetImage: Int) {
    constructor(fileName: String, targetImageProbability: Int) :
            this(imageToPixelArray(fileName), targetImageProbability)

    fun calculateWeightSum(weights: IntArray): Int {
        var sum = 0
        weights.indices.forEach { i ->
            sum += weights[i] * pixelArray[i]
        }
        return sum
    }
}

class Perceptron(imagePixelCount: Int,
                 val threshold: Double = 0.0,
                 val iterationCount: Int = 10000) {

    val weights = IntArray(imagePixelCount)

    fun train(trainingSet: List<PictureImage>) {
        var prevPercent = -1
        for (it in 1..iterationCount) {
            trainingSet.randomItem { image ->
                val output = calculateOutput(image)
                if (image.isTargetImage != output) {
                    if (image.isTargetImage == 0) {
                        punish(image)
                    } else {
                        stimulate(image)
                    }
                }
            }
            val percent = (it.toDouble() / iterationCount * 100).toInt()
            if ((percent % 10 == 0) && prevPercent != percent) {
                prevPercent = percent
                println("Training... $percent%")
            }
        }
    }

    private fun punish(image: PictureImage) {
        weights.indices.forEach { i ->
            if (image.pixelArray[i] == 1) {
                weights[i] -= 1
            }
        }
    }

    private fun stimulate(image: PictureImage) {
        weights.indices.forEach { i ->
            if (image.pixelArray[i] == 1) {
                weights[i] += 1
            }
        }
    }

    private fun calculateOutput(image: PictureImage) =
            if (image.calculateWeightSum(weights) >= threshold) 1 else 0

    fun isTargetImage(image: PictureImage): Boolean
            = calculateOutput(image) == 1

    private val random = Random()
    private fun <E> List<E>.randomItem(action: (item: E) -> Unit) {
        action(this[random.nextInt(this.size)])
    }

}
