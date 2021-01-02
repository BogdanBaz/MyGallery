package com.example.mygallery.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.mygallery.GlideApp;
import com.example.mygallery.R;
import com.example.response_processing.ImagesResponse;

import java.util.List;
import java.util.Objects;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CustomAdapter extends BaseAdapter {

    private List<ImagesResponse> imagesResponses;
    private Context context;
    private LayoutInflater layoutInflater;

    public CustomAdapter(List<ImagesResponse> imagesResponses, Context context) {
        this.imagesResponses = imagesResponses;
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imagesResponses.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.row_grid_item, parent, false);
        }
        ImageView imgCard = convertView.findViewById(R.id.imgCard);

        GlideApp.with(context).load(imagesResponses.get(position)
                .getUrls().getRegular())
                .into(Objects.requireNonNull(imgCard));

        return convertView;
    }

}
