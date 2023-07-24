package fr.axelc.cefimmeteo.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import fr.axelc.cefimmeteo.R;
import fr.axelc.cefimmeteo.models.City;

import java.util.ArrayList;


public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<City> mCities;

    public FavoriteAdapter(Context mContext, ArrayList<City> mCities) {
        this.mContext = mContext;
        this.mCities = mCities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_favorite_city, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.mAdapter = this;

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        City city = mCities.get(position);
        holder.mCity = city;
        holder.mTextViewCityName.setText(city.getmName());
        holder.mTextViewCityDescription.setText(city.getmDescription());
        holder.mTextViewCityTemperature.setText(city.getmTemperature());
        holder.mImageViewCityIcon.setImageResource(city.getmWeatherIcon());
    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private FavoriteAdapter mAdapter;
        private City mCity;
        private TextView mTextViewCityName;
        private TextView mTextViewCityDescription;
        private TextView mTextViewCityTemperature;
        private ImageView mImageViewCityIcon;

        public ViewHolder(View view) {
            super(view);
            view.setOnLongClickListener(this);
            mTextViewCityName = view.findViewById(R.id.city_name);
            mTextViewCityDescription = view.findViewById(R.id.city_description);
            mTextViewCityTemperature = view.findViewById(R.id.city_temperature);
            mImageViewCityIcon = view.findViewById(R.id.city_icon);
        }

        @Override
        public boolean onLongClick(View view) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(mContext.getString(R.string.favorite_remove_title, mCity.getmName()));
            builder.setPositiveButton(R.string.favorite_remove_confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mCities.remove(mCity);
                    mAdapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton(R.string.favorite_remove_cancel, (dialogInterface, i) -> {
            });
            builder.create().show();
            return true;
        }
    }
}
