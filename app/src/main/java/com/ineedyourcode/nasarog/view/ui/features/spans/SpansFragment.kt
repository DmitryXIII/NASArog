package com.ineedyourcode.nasarog.view.ui.features.spans

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.*
import android.view.View
import android.widget.TextView
import androidx.core.provider.FontRequest
import androidx.core.provider.FontsContractCompat
import com.ineedyourcode.nasarog.R
import com.ineedyourcode.nasarog.databinding.FragmentFeaturesSpansBinding
import com.ineedyourcode.nasarog.view.basefragment.BaseFragment

private var rainbowColorIndex = 0
private var textSize = 25

class SpansFragment :
    BaseFragment<FragmentFeaturesSpansBinding>(FragmentFeaturesSpansBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTypeface(binding.tvForegroundColorSpan, "Lobster")

        initForegroundColorSpan(binding.tvForegroundColorSpan)

        initAbsoluteSizeSpan(binding.tvAbsoluteSizeSpan)

        initRelativeSizeSpan(binding.tvRelativeSizeSpan)

        initBackgroundColorSpan(binding.tvBackgroundColorSpan)

        initTabStopSpan(binding.tvTabStopSpan)
    }

    private fun setTypeface(textView: TextView, fontQuery: String) {
        FontsContractCompat.requestFont(
            requireContext(), FontRequest(
                getString(R.string.font_request_provider_authority), getString(
                    R.string.font_request_provider_package
                ), fontQuery, R.array.com_google_android_gms_fonts_certs
            ), object : FontsContractCompat.FontRequestCallback() {
                override fun onTypefaceRetrieved(typeface: Typeface?) {
                    textView.typeface = typeface
                    super.onTypefaceRetrieved(typeface)
                }
            }, Handler((Looper.myLooper()!!))
        )
    }

    private fun initForegroundColorSpan(textView: TextView) {
        var spannableStringForegroundColorSpan = SpannableString(textView.text)
        textView.setText(spannableStringForegroundColorSpan, TextView.BufferType.SPANNABLE)
        spannableStringForegroundColorSpan = textView.text as SpannableString

        val rainbowColors = initRgbColorsList()
        runRainbow(rainbowColors, textView, spannableStringForegroundColorSpan)
    }

    private fun initAbsoluteSizeSpan(textView: TextView) {
        var spannableStringAbsoluteSizeSpan = SpannableString(textView.text)
        textView.setText(spannableStringAbsoluteSizeSpan, TextView.BufferType.SPANNABLE)
        spannableStringAbsoluteSizeSpan = textView.text as SpannableString

        runTextSizeUp(textView, spannableStringAbsoluteSizeSpan)
    }

    private fun initRelativeSizeSpan(textView: TextView) {
        val spannableRelativeSizeSpan = SpannableString(textView.text)
        spannableRelativeSizeSpan.setSpan(
            RelativeSizeSpan(1.5f),
            0,
            textView.length(),
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = spannableRelativeSizeSpan
    }

    private fun initBackgroundColorSpan(textView: TextView) {
        val spannableBackgroundColorSpan = SpannableString(textView.text)
        spannableBackgroundColorSpan.setSpan(
            BackgroundColorSpan(Color.DKGRAY),
            0,
            textView.length(),
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = spannableBackgroundColorSpan
    }

    private fun initTabStopSpan(textView: TextView) {
        val spannableStringBuilderBulletSpan = SpannableStringBuilder(textView.text)

        for (i in 1..5) {
            if (i == 1) {
                spannableStringBuilderBulletSpan.insert(0, "\t")
            }
            if (i < 6) {
                spannableStringBuilderBulletSpan.append("\n\t")
            }
            spannableStringBuilderBulletSpan.append(textView.text)

        }

        for (i in 0..6) {
            spannableStringBuilderBulletSpan.setSpan(
                TabStopSpan.Standard((i + 1) * 50),
                textView.text.length * i,
                textView.text.length * (i + 1),
                0
            )
        }
        textView.text = spannableStringBuilderBulletSpan
    }

    private fun runRainbow(
        rainbowColors: List<List<Int>>,
        textView: TextView,
        spannableStringForegroundColorSpan: SpannableString
    ) {
        spannableStringForegroundColorSpan.setSpan(
            ForegroundColorSpan(
                Color.rgb(
                    rainbowColors[rainbowColorIndex][0],
                    rainbowColors[rainbowColorIndex][1],
                    rainbowColors[rainbowColorIndex][2]
                )
            ), 0, textView.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        Handler(Looper.getMainLooper()).postDelayed({
            rainbowColorIndex++
            if (rainbowColorIndex > rainbowColors.size - 1) {
                rainbowColorIndex = 0
            }
            runRainbow(rainbowColors, textView, spannableStringForegroundColorSpan)
        }, 0L)
    }

    private fun runTextSizeUp(
        textView: TextView,
        spannableStringAbsoluteSizeSpan: SpannableString
    ) {
        Handler(Looper.getMainLooper()).postDelayed({
            spannableStringAbsoluteSizeSpan.setSpan(
                AbsoluteSizeSpan(textSize, true),
                0,
                textView.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textSize++
            if (textSize <= 30) {
                runTextSizeUp(textView, spannableStringAbsoluteSizeSpan)
            } else {
                runTextSizeDown(textView, spannableStringAbsoluteSizeSpan)
            }
        }, 50L)
    }

    private fun runTextSizeDown(
        textView: TextView,
        spannableStringAbsoluteSizeSpan: SpannableString
    ) {
        Handler(Looper.getMainLooper()).postDelayed({
            spannableStringAbsoluteSizeSpan.setSpan(
                AbsoluteSizeSpan(textSize, true),
                0,
                textView.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textSize--
            if (textSize >= 25) {
                runTextSizeDown(textView, spannableStringAbsoluteSizeSpan)
            } else {
                runTextSizeUp(textView, spannableStringAbsoluteSizeSpan)
            }
        }, 50L)
    }

    private fun initRgbColorsList(): MutableList<List<Int>> {
        val rgbColorsList = mutableListOf<List<Int>>()

        for (g in 0..255) {
            rgbColorsList.add(listOf(255, g, 0))
        }

        for (r in 254 downTo 0) {
            rgbColorsList.add(listOf(r, 255, 0))
        }

        for (b in 1..255) {
            rgbColorsList.add(listOf(0, 255, b))
        }

        for (g in 254 downTo 0) {
            rgbColorsList.add(listOf(0, g, 255))
        }

        for (r in 1..255) {
            rgbColorsList.add(listOf(r, 0, 255))
        }

        for (b in 254 downTo 1) {
            rgbColorsList.add(listOf(255, 0, b))
        }
        return rgbColorsList
    }
}