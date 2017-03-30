package by.mksn.miapr

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main(args: Array<String>) {
    println("Reading images...")
    val s1Images = ((0..3).map { PictureImage("new/0$it.jpg", 1) })
    val n1Images = ((0..3).map { PictureImage("new/1$it.jpg", 0) })
    val t1Images = ((0..3).map { PictureImage("new/2$it.jpg", 0) })
    val s2Images = ((0..3).map { PictureImage("new/0$it.jpg", 0) })
    val n2Images = ((0..3).map { PictureImage("new/1$it.jpg", 1) })
    val t2Images = ((0..3).map { PictureImage("new/2$it.jpg", 0) })
    val s3Images = ((0..3).map { PictureImage("new/0$it.jpg", 0) })
    val n3Images = ((0..3).map { PictureImage("new/1$it.jpg", 0) })
    val t3Images = ((0..3).map { PictureImage("new/2$it.jpg", 1) })

    val sImages = s1Images + n1Images + t1Images
    val nImages = s2Images + n2Images + t2Images
    val tImages = s3Images + n3Images + t3Images

    val perceptron1 = Perceptron(imagePixelCount = 36, iterationCount = 10000000)
    val perceptron2 = Perceptron(imagePixelCount = 36, iterationCount = 10000000)
    val perceptron3 = Perceptron(imagePixelCount = 36, iterationCount = 10000000)
    println("Perceptron 1 training...")
    perceptron1.train(sImages)
    println("Perceptron 2 training...")
    perceptron2.train(nImages)
    println("Perceptron 3 training...")
    perceptron3.train(tImages)
    println("Is positive test passed (S): ${perceptron1.isTargetImage(PictureImage("new/s-test.jpg", 1))}")
    println("Is positive test passed (N): ${perceptron2.isTargetImage(PictureImage("new/n-test.jpg", 1))}")
    println("Is positive test passed (T): ${perceptron3.isTargetImage(PictureImage("new/t-test.jpg", 1))}")
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
