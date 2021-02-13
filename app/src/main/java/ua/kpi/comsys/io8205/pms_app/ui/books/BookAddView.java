package ua.kpi.comsys.io8205.pms_app.ui.books;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.RequiresApi;

import ua.kpi.comsys.io8205.pms_app.R;

public class BookAddView {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    public Object[] showPopupWindow(final View view) {
        view.getContext();
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View popupView = inflater.inflate(R.layout.book_add, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;

        boolean focusable = true;

        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.setAnimationStyle(R.style.popup_window_animation);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener((v, event) -> {
            popupWindow.dismiss();
            return true;
        });

        /*EditText inputTitle = popupView.findViewById(R.id.input_title);
        EditText inputSubtitle = popupView.findViewById(R.id.input_subtitle);
        EditText inputPrice = popupView.findViewById(R.id.input_price);

        Button buttonAdd = popupView.findViewById(R.id.button_add_add);
        buttonAdd.setOnClickListener(v -> {
            if (inputTitle.getText().toString().length() != 0 &&
                inputSubtitle.getText().toString().length() != 0 &&
                inputPrice.getText().toString().length() != 0) {
                int[] attrs = new int[]{R.attr.selectableItemBackground};
                TypedArray typedArray = booksFragment.obtainStyledAttributes(attrs);
                int backgroundResource = typedArray.getResourceId(0, 0);

                new BooksFragment().addNewBook(root, bookList, booksMap,
                                                new Book(inputTitle.getText().toString(),
                                                        inputSubtitle.getText().toString(),
                                                        inputPrice.getText().toString()),
                                                backgroundResource);
                popupWindow.dismiss();
            }
            else{
                Toast.makeText(booksFragment, "You must fill all fields!",
                        Toast.LENGTH_LONG).show();
            }
        });*/

        return new Object[] {popupView, popupWindow};
    }
}
