package pl.fidano.apps.polishradio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import pl.fidano.apps.polishradio.models.Radio;


public class RadiosAdapter extends ArrayAdapter<Radio> {
    public RadiosAdapter(Context context, ArrayList<Radio> radios) {
        super(context, 0, radios);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Radio radio = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.radiolist_row, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.radioName);
        TextView url = (TextView) convertView.findViewById(R.id.radioUrl);

        name.setText(radio.getName());
        url.setText(radio.getUrl());

        return convertView;
    }


}
