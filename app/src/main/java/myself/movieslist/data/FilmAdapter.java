package myself.movieslist.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import myself.movieslist.FilmFragment;
import myself.movieslist.R;

public class FilmAdapter extends CursorAdapter {
    //String orderBy;

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_film, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }


    public static class ViewHolder {
        public final ImageView checked;
        public final TextView title;
        public final TextView orderValue;

        public ViewHolder(View view) {
            title = (TextView) view.findViewById(R.id.title_textview);
            checked = (ImageView) view.findViewById(R.id.icon);
            orderValue=(TextView) view.findViewById(R.id.order_value);
        }
    }

    public FilmAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
       // this.orderBy=orderBy;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String filmTitle = cursor.getString(FilmFragment.COLUMN_TITLE_FILM);
        viewHolder.title.setText(filmTitle);

        String watched=cursor.getString(FilmFragment.COLUMN_WATCHED);

        if(watched.equals("false"))
            viewHolder.checked.setVisibility(View.GONE);
        else viewHolder.checked.setVisibility(View.VISIBLE);


        String orderBy=getOrderBy(context);


        if (orderBy.equals("rating"))
            viewHolder.orderValue.setText("Rating: " + cursor.getString(FilmFragment.COLUMN_RATING));
        else if (orderBy.equals("metascore"))
            viewHolder.orderValue.setText("Metascore: " +cursor.getString(FilmFragment.COLUMN_METASCORE));
        else if (orderBy.equals("votes"))
            viewHolder.orderValue.setText("imdbVotes: " + cursor.getString(FilmFragment.COLUMN_VOTES));
    }

    String getOrderBy(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String order = prefs.getString("order","rating");
        return order;
    }
}
