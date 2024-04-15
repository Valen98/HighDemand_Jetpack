package com.example.benchmark

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.PowerMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@RunWith(AndroidJUnit4::class)
class ExampleStartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @OptIn(ExperimentalMetricApi::class)
    @RequiresApi(Build.VERSION_CODES.Q)
    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = "com.example.highdemand_jetpack",
        metrics = listOf(StartupTimingMetric()),
        iterations = 10,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()
    }

    @Test
    fun scrollPostTest() = benchmarkRule.measureRepeated(
        packageName = "com.example.highdemand_jetpack",
        metrics = listOf(FrameTimingMetric()),
        iterations = 10,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()

        //Scroll on the post lazy column
        scrollPostList()
    }


    private fun MacrobenchmarkScope.scrollPostList() {
        val contentList = device.findObject(By.res("post_list"))

        device.waitForIdle()

        contentList.setGestureMargin(device.displayWidth / 3)

        contentList.scroll(Direction.DOWN, 300f)

        // Wait for the scroll to finish
        device.waitForIdle()
    }

    @Test
    fun scrollRowTest() = benchmarkRule.measureRepeated(
        packageName = "com.example.highdemand_jetpack",
        metrics = listOf(FrameTimingMetric()),
        iterations = 10,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()

        //Scroll on the row list
        scrollRowList()
    }
    private fun MacrobenchmarkScope.scrollRowList() {
        val contentList = device.findObject(By.res("row_list"))

        device.waitForIdle()

        contentList.setGestureMargin(device.displayWidth / 4)

        contentList.scroll(Direction.RIGHT, 300f)

        device.waitForIdle()
    }

    @Test
    fun openNav() = benchmarkRule.measureRepeated(
        packageName = "com.example.highdemand_jetpack",
        metrics = listOf(FrameTimingMetric()),
        iterations = 2,
        startupMode = StartupMode.COLD
    )
    {
        pressHome()
        startActivityAndWait()

        //Test to open navigation menu
        openNavigation()
        closeNavigation()
    }
    private fun MacrobenchmarkScope.openNavigation() {
        val openNav = device.findObject(By.res("open_nav"))

        device.waitForIdle()

        openNav.click()

        device.waitForIdle()
    }

    private fun MacrobenchmarkScope.closeNavigation() {
        val closeNav = device.findObject(By.res("close_nav"))

        device.waitForIdle()

        closeNav.click()

        device.waitForIdle()
    }
}