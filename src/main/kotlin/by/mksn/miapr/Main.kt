package by.mksn.miapr

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    println("Reading images...")
    val images = (0..19).map { PictureImage("$it.jpg", if (it < 10) 1 else 0) }
    println("Perceptron training...")
    val perceptron = Perceptron(imagePixelCount = 100, iterationCount = 10000000)
    perceptron.train(images)
    drawWeightMap(perceptron.weights)
    println("Weights: ${Arrays.toString(perceptron.weights)}")
    println("Is positive test passed: ${perceptron.isTargetImage(PictureImage("test/positive.jpg", 1))}")
    println("Is negative test passed: ${perceptron.isTargetImage(PictureImage("test/negative.jpg", 0))}")
}

fun drawWeightMap(weights: IntArray) {
    val image = BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB)
    val normalize = weights.min()!!
    for (i in 0..9) {
        for (j in 0..9) {
            image.setRGB(i, j, Color.HSBtoRGB(0F, 0F, 1F - ((weights[i * 10 + j].toFloat() - normalize) / (10 - normalize))))
        }
    }
    ImageIO.write(image, "png", File("weight_diagramm.png"))

}
