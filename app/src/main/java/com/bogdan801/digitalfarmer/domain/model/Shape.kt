package com.bogdan801.digitalfarmer.domain.model

import com.bogdan801.digitalfarmer.data.util.getPolygonCenterPoint
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil

open class ShapeBase(
    protected var _points: MutableList<LatLng>
){
    val points get() = _points.toList()

    val center: LatLng get() = getPolygonCenterPoint(_points)

    val area get() = SphericalUtil.computeArea(_points)

    val isEmpty get() = _points.isEmpty()

    val isNotEmpty get() = _points.isNotEmpty()

    val pointCount get() = _points.size

    val isShapeClosed: Boolean
        get() {
            if(_points.isEmpty() || _points.size < 3) return false
            return _points.first() == _points.last()
        }
}

@Suppress("MemberVisibilityCanBePrivate")
class MutableShape(shapePoints: List<LatLng>): ShapeBase(shapePoints.toMutableList()){
    constructor(shapeString: String) : this(mutableListOf()) {
        val coordinates = shapeString.split(";")
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
        return toShape()
    }

    fun insertPoint(point: LatLng, index: Int): Shape {
        _points.add(index, point)
        return toShape()
    }

    fun movePoint(newPosition: LatLng, pointIndex: Int): Shape {
        _points[pointIndex] = newPosition
        return toShape()
    }

    fun removeLast(): Shape {
        if(_points.isNotEmpty()) _points.removeLast()
        return toShape()
    }

    fun closeShape(): Shape{
        if(_points.isNotEmpty()) _points.add(_points.first())
        return toShape()
    }

    override fun toString() = buildString {
        _points.forEachIndexed { index,  point ->
            append("${point.latitude},")
            append("${point.longitude}")
            if(index != _points.lastIndex) append(";")
        }
    }

    fun toShape() = Shape(_points)

    override fun equals(other: Any?): Boolean {
        return this._points == (other as ShapeBase).points
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}

class Shape(shapePoints: List<LatLng> = listOf()): ShapeBase(shapePoints.toMutableList()){
    constructor(shapeString: String) : this(mutableListOf()) {
        val coordinates = shapeString.split(";")
        val listOfPoints = mutableListOf<LatLng>()
        coordinates.forEach { coordinatesString ->
            val splitCoordinates = coordinatesString.split(",")
            if(splitCoordinates.size == 2){
                listOfPoints.add(LatLng(splitCoordinates[0].toDouble(), splitCoordinates[1].toDouble()))
            }
        }
    }

    override fun toString() = buildString {
        _points.forEachIndexed { index,  point ->
            append("${point.latitude},")
            append("${point.longitude}")
            if(index != _points.lastIndex) append(";")
        }
    }

    fun toMutableShape() = MutableShape(_points)

    override fun equals(other: Any?): Boolean {
        return _points == (other as ShapeBase).points
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}
