package com.hafizhanbiya.draganddrop

import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.View.DragShadowBuilder
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.chip.Chip

class MainActivity : AppCompatActivity() {

    private val dropArea by lazy { findViewById<CardView>(R.id.cv) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onInit()
    }

    private fun onInit() {
        val chip1 = findViewById<Chip>(R.id.chip_1)
        val chip2 = findViewById<Chip>(R.id.chip_2)
        val chip3 = findViewById<Chip>(R.id.chip_3)
        setDragAndDrop(chip1)
        setDragAndDrop(chip2)
        setDragAndDrop(chip3)
        dropArea.setOnDragListener(dragListener)
    }

    private fun setDragAndDrop(chip: Chip) {
        // 1
        chip.setOnLongClickListener { view: View ->

            // 2
            val item = ClipData.Item(chip.text)

            // 3
            val dataToDrag = ClipData(
                chip.text,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )

            // 4
            val maskShadow = DragShadow(view)

            // 5
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                //support pre-Nougat versions
                @Suppress("DEPRECATION")
                view.startDrag(dataToDrag, maskShadow, view, 0)
            } else {
                //supports Nougat and beyond
                view.startDragAndDrop(dataToDrag, maskShadow, view, 0)
            }

            // 6
//            view.visibility = View.INVISIBLE

            //7
            true
        }

    }

    private val dragListener = View.OnDragListener { view, dragEvent ->

        val draggableItem = dragEvent.localState as View

        when (dragEvent.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                true
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                dropArea.alpha = 0.3f
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                dropArea.alpha = 1.0f
                true
            }
            DragEvent.ACTION_DROP -> {
                dropArea.alpha = 1.0f
                if (dragEvent.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    val draggedData = dragEvent.clipData.getItemAt(0).text
                    findViewById<TextView>(R.id.tv).text = draggedData
                }
                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                true
            }
            else -> {
                false
            }
        }
    }


    private class DragShadow(view: View) : DragShadowBuilder(
        view
    ) {
        override fun onDrawShadow(canvas: Canvas) {
            view.draw(canvas)
        }

        override fun onProvideShadowMetrics(shadowSize: Point, shadowTouchPoint: Point) {
            val v = view
            val height = v.height
            val width = v.width
            shadowSize[width] = height
            shadowTouchPoint[width / 2] = height / 2
        }
    }


}