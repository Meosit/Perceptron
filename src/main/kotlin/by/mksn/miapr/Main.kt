package by.mksn.miapr

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    println("Reading images...")
    val images = (0..19).map { PictureImage("$it.jpg", if (it < 10) 1.0 else 0.0) }
    println("Perceptron training...")
    val perceptron = Perceptron(imagePixelCount = 100)
    for (i in 1..100000) {
        perceptron.train(images)
    }

    val image = BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB)
    for (i in 0..9) {
        for (j in 0..9) {
            image.setRGB(i, j, Color.HSBtoRGB(0.5.toFloat(), 0.5.toFloat(), perceptron.weights[i * 10 + j].toFloat()))
        }
    }
    ImageIO.write(image, "png", File("test.png"))


    println("Weights: ${Arrays.toString(perceptron.weights)}")
    println("Positive test: ${perceptron.checkImageProbability(images[0])}")
    println("Negative test: ${perceptron.checkImageProbability(images[10])}")
}

