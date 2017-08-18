package pl.fidano.apps.polishradio.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pl.fidano.apps.polishradio.R;
import pl.fidano.apps.polishradio.models.Radio;
import pl.fidano.apps.polishradio.ui.RadioListFragment.OnListFragmentInteractionListener;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Radio} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RadioRecyclerViewAdapter extends RecyclerView.Adapter<RadioRecyclerViewAdapter.ViewHolder> {

    private final List<Radio> mRadioList;
    private final OnListFragmentInteractionListener mListener;

    public RadioRecyclerViewAdapter(List<Radio> items, OnListFragmentInteractionListener listener) {
        mRadioList = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_radio_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mRadioList.get(position);


//        holder.mImageView.setImageIcon(mValues.get(position).id);
        holder.mContentView.setText(mRadioList.get(position).getName());

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRadioList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mImageView;
        public final TextView mContentView;
        public Radio mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mImageView = (ImageView) view.findViewById(R.id.radioIcon);
            mContentView = (TextView) view.findViewById(R.id.radioName);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
