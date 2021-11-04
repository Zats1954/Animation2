package ru.netology.nmedia.util


import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import ru.netology.nmedia.dto.Post
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


object PostDelegate: ReadWriteProperty<Bundle, Post?> {
    override fun setValue(thisRef: Bundle, property: KProperty<*>, value: Post?) {
        thisRef.putParcelable(property.name, value)
    }

    override fun getValue(thisRef: Bundle, property: KProperty<*>): Post?  =
        thisRef.getParcelable(property.name) as?  Post
}

