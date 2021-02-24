package ua.kpi.comsys.io8205.pms_app.ui.books;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import ua.kpi.comsys.io8205.pms_app.R;
import ua.kpi.comsys.io8205.pms_app.database.App;
import ua.kpi.comsys.io8205.pms_app.database.AppDatabase;

public class BookInfoView {

    @SuppressLint("StaticFieldLeak")
    private static View popupView;
    private static ProgressBar loadingImage;
    private static ImageView bookImage;
    private static Book book;
    private static AppDatabase database;

    @SuppressLint({"ClickableViewAccessibility", "InflateParams"})
    public void showPopupWindow(final View view, Book book) {
        view.getContext();
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        popupView = inflater.inflate(R.layout.book_info, null);
        BookInfoView.book = book;

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setAnimationStyle(R.style.popup_window_animation);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        loadingImage = popupView.findViewById(R.id.loadingImageInfo);
        bookImage = popupView.findViewById(R.id.book_info_image);

        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });

        database = App.getInstance().getDatabase();

        AsyncLoadBookInfo aTask = new AsyncLoadBookInfo();
        aTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, book.getIsbn13());
    }

    protected static void setInfoData(){
        if (book.getImagePath() != null) {
            bookImage.setVisibility(View.INVISIBLE);
            loadingImage.setVisibility(View.VISIBLE);
            new BooksFragment.DownloadImageTask(bookImage, loadingImage, popupView.getContext()).execute(book.getImagePath());
        }
        else {
            bookImage.setVisibility(View.INVISIBLE);
            loadingImage.setVisibility(View.VISIBLE);
            bookImage.setImageBitmap(book.getImage());
            loadingImage.setVisibility(View.GONE);
            bookImage.setVisibility(View.VISIBLE);
        }

        ((TextView) popupView.findViewById(R.id.book_info_authors)).setText(book.getAuthors());
        ((TextView) popupView.findViewById(R.id.book_info_description)).setText(book.getDesc());
        ((TextView) popupView.findViewById(R.id.book_info_pages)).setText(book.getPages());
        ((TextView) popupView.findViewById(R.id.book_info_publisher)).setText(book.getPublisher());
        ((TextView) popupView.findViewById(R.id.book_info_rating)).setText(book.getRating());
        ((TextView) popupView.findViewById(R.id.book_info_subtitle)).setText(book.getSubtitle());
        ((TextView) popupView.findViewById(R.id.book_info_title)).setText(book.getTitle());
        ((TextView) popupView.findViewById(R.id.book_info_year)).setText(book.getYear());
    }

    public static class AsyncLoadBookInfoToDB extends AsyncTask<Book, Void, Void> {
        @Override
        protected Void doInBackground(Book... books) {
            database.bookDao().setInfoByIsbn13(Long.parseLong(books[0].getIsbn13()),
                    books[0].getAuthors(),
                    books[0].getDesc(),
                    Long.parseLong(books[0].getPages()),
                    books[0].getPublisher(),
                    books[0].getRating(),
                    Long.parseLong(books[0].getYear()));
            return null;
        }
    }

    private static class AsyncLoadBookInfo extends AsyncTask<String, Void, Void> {
        private String getRequest(String url) throws IOException {
            StringBuilder result = new StringBuilder();
            URL getReq = new URL(url);
            URLConnection bookConnection = getReq.openConnection();
            //bookConnection.setConnectTimeout(15 * 1000);
            //bookConnection.setReadTimeout(15 * 1000);
            BufferedReader in = new BufferedReader(new InputStreamReader(bookConnection.getInputStream()));
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                result.append(inputLine).append("\n");

            in.close();

            return result.toString();
        }

        private void parseBookInfo(String jsonText) throws ParseException {
            JSONObject jsonObject = (JSONObject) new JSONParser().parse(jsonText);
            book.setAuthors((String) jsonObject.get("authors"));
            book.setPublisher((String) jsonObject.get("publisher"));
            book.setDesc((String) jsonObject.get("desc"));
            book.setPages((String) jsonObject.get("pages"));
            book.setRating(jsonObject.get("rating") + "/5");
            book.setYear((String) jsonObject.get("year"));
        }

        private void fromDB(long isbn){
            book = database.bookDao().getByIsbn13(isbn).makeBookInfo();
        }

        private void toDb(){
            new AsyncLoadBookInfoToDB().execute(book);
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private void search(String isbn13){
            String jsonResponse = String.format("https://api.itbook.store/1.0/books/%s", isbn13);
            try {
                String req = getRequest(jsonResponse);
                parseBookInfo(req);
                toDb();
            } catch (MalformedURLException e) {
                System.err.println(String.format("Incorrect URL <%s>!", jsonResponse));
                e.printStackTrace();
            } catch (UnknownHostException e) {
                System.err.println("Request timeout!");
                fromDB(Long.parseLong(isbn13));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                System.err.println("Incorrect content of JSON file!");
                e.printStackTrace();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected Void doInBackground(String... strings) {
            search(strings[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            BookInfoView.setInfoData();
        }
    }
}
