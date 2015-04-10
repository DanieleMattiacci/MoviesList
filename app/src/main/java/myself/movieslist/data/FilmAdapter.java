package myself.movieslist.data;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import myself.movieslist.R;
import myself.movieslist.data.pojo.ResponseFilm;

public class FilmAdapter extends ArrayAdapter<ResponseFilm> {
    Context context;
    private ArrayList<ResponseFilm> movies;
    private int resourceId;
    private String orderBy;

    public FilmAdapter(Context context, int resourceId, ArrayList<ResponseFilm> movies,String orderBy) {
        super(context, resourceId);
        this.context = context;
        this.resourceId = resourceId;
        this.movies=movies;
        this.orderBy=orderBy;

       // Log.i("","Movies costruttore: "+movies.size());
    }

    @Override
    public int getCount() {
        return this.movies.size();
    }

    @Override
    public ResponseFilm getItem(int position){
        return this.movies.get(position);

    }

    private class ViewHolder {
        ImageView checked;
        TextView title;
        TextView orderValue;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
      //  View rowView = convertView;
        ResponseFilm film = movies.get(position);
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_film, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title_textview);
            holder.checked = (ImageView) convertView.findViewById(R.id.icon);
            //holder.checked = (ImageView) convertView.findViewById(R.id.icon);
            holder.orderValue=(TextView) convertView.findViewById(R.id.order_value);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.title.setText(film.getTitle());
        if(film.getWatched().equals("false"))
            holder.checked.setVisibility(View.GONE);

        if (orderBy.equals("rating")) {
            holder.orderValue.setText("Rating: "+film.getImdbRating());
          //  Log.i("",""+film.getImdbRating());
        } else if (orderBy.equals("metascore")) {
            holder.orderValue.setText("Metascore: " + film.getMetascore());
          //  Log.i("",""+film.getMetascore());
        } else if (orderBy.equals("votes")) {
            holder.orderValue.setText("imdbVotes: " + film.getImdbVotes());
          //  Log.i("",""+film.getImdbVotes());
        }
        //holder.checked.setImageResource(rowItem.getImageId());

        return convertView;
        //Log.i("", "Movies Length: " + movies.size());
       // ResponseFilm film = new ResponseFilm();
      /*  ResponseFilm film = movies.get(position);
        TextView title= (TextView) rowView.findViewById(R.id.title_textview);
      //  title.setText(film.getTitle());
        Log.i("", "TextView title: " + title.getText());
        checked = (ImageView) rowView.findViewById(R.id.icon);
        if(film.getWatched().equals("true")) {
            checked = (ImageView) rowView.findViewById(R.id.icon);
        }else checked.setVisibility(View.GONE);

        TextView orderValue = (TextView) rowView.findViewById(R.id.order_value);
        if (orderBy.equals(R.string.pref_order_by_rating)) {
            orderValue.setText("Rating: "+film.getImdbRating());
        } else if (orderBy.equals(R.string.pref_order_by_label_metascore)) {
            orderValue.setText("Metascore: " + film.getMetascore());
        } else if (orderBy.equals(R.string.pref_order_by_votes)) {
            orderValue.setText("imdbVotes: " + film.getImdbVotes());
        } else if (orderBy.equals(R.string.pref_order_by_released_date)) {
            orderValue.setText("Release Date: " + film.getReleased());
        }

        return rowView;*/
    }
}
