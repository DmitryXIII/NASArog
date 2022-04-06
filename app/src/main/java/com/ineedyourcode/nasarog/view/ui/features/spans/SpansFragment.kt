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



class SpansFragment :
    BaseFragment<FragmentFeaturesSpansBinding>(FragmentFeaturesSpansBinding::inflate) {

    private var rainbowColorIndex = 0
    private var textSize = 25

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
        var spannableString = SpannableString(textView.text)
        textView.setText(spannableString, TextView.BufferType.SPANNABLE)
        spannableString = textView.text as SpannableString

        val rainbowColors = initRgbColorsList()
        runRainbow(rainbowColors, textView, spannableString)
    }

    private fun initAbsoluteSizeSpan(textView: TextView) {
        var spannableString = SpannableString(textView.text)
        textView.setText(spannableString, TextView.BufferType.SPANNABLE)
        spannableString = textView.text as SpannableString

        runTextSizeUp(textView, spannableString)
    }

    private fun initRelativeSizeSpan(textView: TextView) {
        val spannableString = SpannableString(textView.text)
        spannableString.setSpan(
            RelativeSizeSpan(1.5f),
            0,
            textView.length(),
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = spannableString
    }

    private fun initBackgroundColorSpan(textView: TextView) {
        val spannableString = SpannableString(textView.text)
        spannableString.setSpan(
            BackgroundColorSpan(Color.DKGRAY),
            0,
            textView.length(),
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = spannableString
    }

    private fun initTabStopSpan(textView: TextView) {
        val spannableStringBuilder = SpannableStringBuilder(textView.text)

        for (i in 1..5) {
            if (i == 1) {
                spannableStringBuilder.insert(0, "\t")
            }

            spannableStringBuilder.append("\n\t")
            spannableStringBuilder.append(textView.text)
        }

        for (i in 0..6) {
            spannableStringBuilder.setSpan(
                TabStopSpan.Standard((i + 1) * 50),
                textView.text.length * i,
                textView.text.length * (i + 1),
                0
            )
        }
        textView.text = spannableStringBuilder
    }

    /**
     * Скорость срабатывания SpannableString, видимо, имеет нижний предел,
     * быстрее которого уже не может отрисовывать изменения.
     * Здесь textView.animate() применяется только как эксперимент использования duration в качестве таймера для SpannableString.
     * При коротких duration на экране разницы уже не заметно (визуально duration = 500, duration = 100 и даже duration = 0 смотрятся
     * абсолютно одинаково). В массиве rainbowColors 1530 значений RGB, т.е. при duration = 1 я ожидал,
     * что SpannableString переберет все цвета за 1.5 сек, но на практике такой скорости не получается добиться,
     * независимо от выбора таймера (Timer, Handler.postDelayed(), CountDownTimer, .animate() - результат одинаковый)
     */
    private fun runRainbow(
        rainbowColors: List<List<Int>>,
        textView: TextView,
        spannableString: SpannableString
    ) {
        textView.animate()
            .alpha(1f)
            .withEndAction {
                spannableString.setSpan(
                    ForegroundColorSpan(
                        Color.rgb(
                            rainbowColors[rainbowColorIndex][0],
                            rainbowColors[rainbowColorIndex][1],
                            rainbowColors[rainbowColorIndex][2]
                        )
                    ), 0, textView.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                rainbowColorIndex++
                if (rainbowColorIndex > rainbowColors.size - 1) {
                    rainbowColorIndex = 0
                }
                runRainbow(rainbowColors, textView, spannableString)
            }
            .duration = 0
    }


    private fun runTextSizeUp(
        textView: TextView,
        spannableString: SpannableString
    ) {
        Handler(Looper.getMainLooper()).postDelayed({
            spannableString.setSpan(
                AbsoluteSizeSpan(textSize, true),
                0,
                textView.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textSize++
            if (textSize <= 30) {
                runTextSizeUp(textView, spannableString)
            } else {
                runTextSizeDown(textView, spannableString)
            }
        }, 50L)
    }

    private fun runTextSizeDown(
        textView: TextView,
        spannableString: SpannableString
    ) {
        Handler(Looper.getMainLooper()).postDelayed({
            spannableString.setSpan(
                AbsoluteSizeSpan(textSize, true),
                0,
                textView.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            textSize--
            if (textSize >= 25) {
                runTextSizeDown(textView, spannableString)
            } else {
                runTextSizeUp(textView, spannableString)
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
