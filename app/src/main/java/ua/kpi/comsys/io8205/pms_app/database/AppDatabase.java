package ua.kpi.comsys.io8205.pms_app.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {BooksTable.class, SearchTable.class, GalleryEntity.class}, version = 6)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BooksTableDao bookDao();
    public abstract SearchTableDao searchTableDao();
    public abstract GalleryDao galleryDao();
}
