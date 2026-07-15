package com.basim.block

import com.android.build.api.dsl.CommonExtension
import org.gradle.kotlin.dsl.get

// Contravariant => We can use the parent where ever the child is used, achieved using the `in` keyword in generics in kotlin.
// Covariant => We can use the subtype where ever the type is used, achieved with `out` keyword in generics in kotlin.

// The aestrik here means the star projection in kotlin.
internal fun configureGradleManagedDevices(
    commonExtension: CommonExtension
) {
    val pixel4 = DeviceConfig("Pixel 4", 30, "aosp-atd")
    val pixel6 = DeviceConfig("Pixel 6", 31, "aosp")
    val pixelC = DeviceConfig("Pixel C", 30, "aosp-atd")

    //Devices that can be used for testing
    val allDevices = listOf(pixel4, pixel6, pixelC)
    //Devices that can be used for CI => Don't know when we will use this but adding just for the sake of it.
    val ciDevices = listOf(pixel4, pixelC)

    val managedDevices = commonExtension.testOptions.managedDevices

    allDevices.forEach { deviceConfig ->
        //maybeCreate => function from gradle, creates the object and add to the container if the object is not in the container.
        managedDevices.localDevices.maybeCreate(deviceConfig.taskName).apply {
            device = deviceConfig.device
            apiLevel = deviceConfig.apiLevel
            systemImageSource = deviceConfig.systemImageSource
        }
    }
    managedDevices.groups.maybeCreate("ci").apply {
        ciDevices.forEach { deviceConfig ->
            targetDevices.add(managedDevices.localDevices[deviceConfig.taskName])
        }
    }
}

private data class DeviceConfig(
    val device: String,
    val apiLevel: Int,
    val systemImageSource: String,
) {
    val taskName = buildString {
        append(device.lowercase().replace(" ", ""))
        append("api")
        append(apiLevel.toString())
        append(systemImageSource.replace("-", ""))
    }
}
