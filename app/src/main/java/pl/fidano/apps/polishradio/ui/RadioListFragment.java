package pl.fidano.apps.polishradio.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.fidano.apps.polishradio.R;
import pl.fidano.apps.polishradio.VolleySingleton;
import pl.fidano.apps.polishradio.models.Radio;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class RadioListFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener {

    private static final String TAG = "RadioListFragment";

    private static final String API_URL = "http://testradio.fidano.pl/api/v1/radios";
    private static final String API2_URL = "http://www.radiomoob.com/radiomoob/api.php?cat_id=53";
    private static final String IMAGES_BASE_URL = "http://radiomoob.com/radiomoob/upload/";

    private List<Radio> feed = new ArrayList<>();
    private RadioRecyclerViewAdapter adapter;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RadioListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        VolleySingleton.getInstance(this.getActivity()).addToRequestQueue(new JsonArrayRequest(API_URL, this, this));
        VolleySingleton.getInstance(this.getActivity()).addToRequestQueue(new JsonObjectRequest(API2_URL, null, this, this));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radio_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            adapter = new RadioRecyclerViewAdapter(feed, mListener);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

//    /**
//     * Called when a response is received.
//     *
//     * @param response
//     */
//    @Override
//    public void onResponse(JSONArray response) {
//        int size = response.length();
//        Log.d("API RESPONSE", response.toString());
//        Toast.makeText(getActivity(), "Items in array: " + size, Toast.LENGTH_SHORT).show();
//
//        feed.clear();
//        for (int i = 0; i < size; i++) {
//            try {
//                JSONObject obj = response.getJSONObject(i);
//                feed.add(new Radio(obj.getString("logo_url"), obj.getString("name"), obj.getString("url"), obj.getString("stream_url")));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        adapter.notifyDataSetChanged();
//    }


    /**
     * Called when a response is received.
     *
     * @param response
     */
    @Override
    public void onResponse(JSONObject response) {
        Log.d(TAG, "API RESPONSE");
        try {
            JSONArray jsonArray = response.getJSONArray("Json");

            feed.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                final String radio_name = item.getString("radio_name");
                final String radio_image = IMAGES_BASE_URL + item.getString("radio_image");
                final String radio_url = item.getString("radio_url");

                feed.add(new Radio(radio_image, radio_name, "www is empty", radio_url));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Callback method that an error has been occurred with the
     * provided error code and optional user-readable message.
     *
     * @param error
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("API ERROR RESPONSE", error.toString());
        Toast.makeText(getActivity(), "Fetching data failed", Toast.LENGTH_SHORT).show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Radio item);
    }
}
