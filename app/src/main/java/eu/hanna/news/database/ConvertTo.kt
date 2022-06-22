package eu.hanna.news.database

import androidx.room.TypeConverter
import eu.hanna.news.model.Source

class ConvertTo {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}