package com.bogdan801.digitalfarmer.domain.model

import com.bogdan801.digitalfarmer.data.util.getPolygonCenterPoint
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.SphericalUtil

open class ShapeBase(
    protected var _points: MutableList<LatLng>
){
    val points get() = _points.toList()

    val center: LatLng get() = getPolygonCenterPoint(_points)

    val area get() = SphericalUtil.computeArea(_points)

    val isEmpty get() = _points.isEmpty()

    val bounds get() = LatLngBounds.Builder().apply {
        _points.forEach { include(it) }
    }.build()

    val isNotEmpty get() = _points.isNotEmpty()

    val pointCount get() = _points.size

    val isShapeClosed: Boolean
        get() {
            if(_points.isEmpty() || _points.size < 3) return false
            return _points.first() == _points.last()
        }
}

@Suppress("MemberVisibilityCanBePrivate")
class MutableShape(shapePoints: List<LatLng>, private var listOfActions: MutableList<String> = mutableListOf()): ShapeBase(shapePoints.toMutableList()){
    constructor(shapeString: String) : this(mutableListOf()) {
        val coordinates = shapeString.replace(" ", "").split(";")
        val listOfPoints = mutableListOf<LatLng>()
        coordinates.forEach { coordinatesString ->
            val splitCoordinates = coordinatesString.split(",")
            if(splitCoordinates.size == 2){
                listOfPoints.add(LatLng(splitCoordinates[0].toDouble(), splitCoordinates[1].toDouble()))
            }
        }
        _points = listOfPoints
    }

    fun setPoint(point: LatLng): Shape {
        _points.add(point)
        listOfActions.add("a:${_points.lastIndex}")
        return toShape()
    }

    fun insertPoint(point: LatLng, index: Int): Shape {
        _points.add(index, point)
        listOfActions.add("a:${index}")
        return toShape()
    }

    fun movePoint(newPosition: LatLng, pointIndex: Int, saveForUndo: Boolean = false): Shape {
        listOfActions.add("m:${pointIndex}:${_points[pointIndex].latitude},${_points[pointIndex].longitude}")
        _points[pointIndex] = newPosition
        return toShape()
    }

    fun undo(): Shape {
        if(_points.isNotEmpty() && listOfActions.isNotEmpty()) {
            val action = listOfActions.last().split(":")
            when(action[0]){
                "a" -> _points.removeAt(action[1].toInt())
                "m" -> {
                    val coords = action[2].split(",").map { it.toDouble() }
                    val oldCoordinates = LatLng(coords[0], coords[1])
                    _points[action[1].toInt()] = oldCoordinates
                }
            }
            listOfActions.removeLast()
        }
        return toShape()
    }

    fun closeShape(): Shape{
        if(_points.isNotEmpty()) {
            _points.add(_points.first())
            listOfActions.add("a:${_points.lastIndex}")
        }
        return toShape()
    }

    override fun toString() = buildString {
        _points.forEachIndexed { index,  point ->
            append("${point.latitude},")
            append("${point.longitude}")
            if(index != _points.lastIndex) append(";")
        }
    }

    fun toShape() = Shape(_points, listOfActions)

    override fun equals(other: Any?): Boolean {
        return this._points == (other as ShapeBase).points
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

class Shape(shapePoints: List<LatLng> = listOf(), private var listOfActions: MutableList<String> = mutableListOf()): ShapeBase(shapePoints.toMutableList()){
    constructor(shapeString: String) : this(mutableListOf()) {
        val coordinates = shapeString.replace(" ", "").split(";")
        val listOfPoints = mutableListOf<LatLng>()
        coordinates.forEach { coordinatesString ->
            val splitCoordinates = coordinatesString.split(",")
            if(splitCoordinates.size == 2){
                listOfPoints.add(LatLng(splitCoordinates[0].toDouble(), splitCoordinates[1].toDouble()))
            }
        }
        _points = listOfPoints
    }

    override fun toString() = buildString {
        _points.forEachIndexed { index,  point ->
            append("${point.latitude},")
            append("${point.longitude}")
            if(index != _points.lastIndex) append(";")
        }
    }

    fun toMutableShape() = MutableShape(_points, listOfActions)

    override fun equals(other: Any?): Boolean {
        return _points == (other as ShapeBase).points
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
