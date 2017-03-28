package by.mksn.miapr

import java.io.File
import java.util.*
import javax.imageio.ImageIO

val RANDOM = Random()
fun Double.equalsWithPrecision(other: Double, precision: Double = 0.000001): Boolean
        = Math.abs(this - other) < precision

fun Double.sqr(): Double = this * this

fun imageToPixelArray(fileName: String): DoubleArray {
    val classLoader = Perceptron::class.java.classLoader
    val file = File(classLoader.getResource(fileName)!!.file)
    val image = ImageIO.read(file)
    val pixels = DoubleArray(image.width * image.height)
    for (x in 0..image.width - 1) {
        for (y in 0..image.height - 1) {
            pixels[x * image.width + y] = (if (image.getRGB(x, y) == 0xFFFFFFFF.toInt()) 0.0 else 1.0)
        }
    }
    return pixels
}

data class PictureImage(val pixelArray: DoubleArray, val targetImageProbability: Double) {
    constructor(fileName: String, targetImageProbability: Double) :
            this(imageToPixelArray(fileName), targetImageProbability)

    fun calculateWeightSum(weights: DoubleArray, bias: Double): Double {
        var sum = 0.0
        weights.indices.forEach { i ->
            sum += weights[i] * pixelArray[i]
        }
        return sum + bias
    }
}

class Perceptron(imagePixelCount: Int,
                 val threshold: Double = 0.0,
                 val iterationLimit: Int = 100,
                 val learningRate: Double = 0.001) {

    val weights = DoubleArray(imagePixelCount, { RANDOM.nextDouble() })
    var bias = RANDOM.nextDouble()

    fun train(trainingSet: List<PictureImage>) {
        var localError: Double
        var globalError: Double
        var iterationIndex: Int = 0
        var output: Int
        do {
            iterationIndex++
            globalError = 0.0
            trainingSet.forEach { image ->
                output = calculateOutput(image)
                localError = image.targetImageProbability - output
                weights.indices.forEach {
                    i ->
                    weights[i] += (learningRate * localError * image.targetImageProbability)
                }
                bias += learningRate * localError

                globalError += learningRate.sqr()
            }
        } while (globalError.equalsWithPrecision(0.0) && (iterationIndex < iterationLimit))
    }

    private fun calculateOutput(image: PictureImage) =
            if (image.calculateWeightSum(weights, bias) >= threshold) 1 else 0

    fun checkImageProbability(image: PictureImage): Boolean
            = calculateOutput(image) == 1
}