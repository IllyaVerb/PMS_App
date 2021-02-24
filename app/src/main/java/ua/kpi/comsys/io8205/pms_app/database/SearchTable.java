package ua.kpi.comsys.io8205.pms_app.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;

@Entity
public class SearchTable {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String searchQueue;
    @TypeConverters({BooksConverter.class})
    public ArrayList<Long> searchedBooks;
}
