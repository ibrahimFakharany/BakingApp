package exampls.com.bakingapp.UI;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import exampls.com.bakingapp.R;
import exampls.com.bakingapp.data.Recipe;
import exampls.com.bakingapp.data.Step;
import io.realm.Realm;
import io.realm.RealmList;

/**
 * this class to show the video player and description of the step
 */


public class DescriptionFragment extends Fragment implements View.OnClickListener {
    public static final String TAG = "descriptionfragment";
    // position key is a var for position of the step in the step list
    private static final String POSITION_KEY = "position";
    public static final String STEP_KEY = "steps";
    // selected position is a key of the var that is stored in
    // bundle -> bundle.putLong(SELECTED_POSITION , position)
    // as position var is in the bundle is the current position of the player when the device is rotated
    private static final String SELECTED_POSITION = "position", STATE = "state";
    private static final String STEP_POSITION = "stepposition";
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    private DataSource.Factory mediaDataSourceFactory;
    Handler mainHandler;
    TextView descriptionTV;
    int stepPosition;
    long position;
    ImageView forwardBtn;
    ImageView previousBtn;
    Uri videoURI;
    Bundle bundle;
    RealmList<Step> steps;
    int listSize;
    long pos = 0;
    boolean state = true;
    int chk = -1;
    int isDestroyed = 0;
    Bundle bund = null;
    boolean onActivityCreated = false;

    public DescriptionFragment() {
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /**
         * position of the player to be stored
         */
        onActivityCreated = true;
        position = C.TIME_UNSET;
        setRetainInstance(true);
        if (savedInstanceState != null) {

            position = savedInstanceState.getLong(SELECTED_POSITION, C.TIME_UNSET);
            state = savedInstanceState.getBoolean(STATE);
            stepPosition = savedInstanceState.getInt(STEP_POSITION);

            chk = 1;

        }


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_description, container, false);

        try {
            bundle = getArguments();
            descriptionTV = (TextView) v.findViewById(R.id.tv);
            forwardBtn = (ImageView) v.findViewById(R.id.image_iv_next_video);
            previousBtn = (ImageView) v.findViewById(R.id.image_iv_previous_video);
            forwardBtn.setOnClickListener(this);
            previousBtn.setOnClickListener(this);

            if (bundle == null) {

                descriptionTV.setText("description will appear");

            } else {

                exoPlayerView = (SimpleExoPlayerView) v.findViewById(R.id.exo_player_view);

                // getting steps of recipe
                Realm.init(getActivity());
                Realm realm = Realm.getDefaultInstance();
                Recipe recipe = realm.where(Recipe.class).equalTo("id", bundle.getInt(RecipesActivity.RECIPE_KEY)).findFirst();

                steps = recipe.getSteps();
                setListSizs(steps.size());
                setPosition(bundle.getInt(POSITION_KEY));


            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return v;
    }

    public void releasePlayer() {

        if (exoPlayer != null) {
            position = exoPlayer.getCurrentPosition();
            state = exoPlayer.getPlayWhenReady();
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SELECTED_POSITION, position);
        outState.putBoolean(STATE, state);
        outState.putInt(STEP_POSITION, getPosition());
        bund = outState;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bundle == null) {

            descriptionTV.setText("here is will appear description");

        } else {
            if (isDestroyed == 0 && !onActivityCreated) {
                onActivityCreated(bund);
            }
            onActivityCreated = false;
            showStep(getPosition());
        }


    }

    @Override
    public void onClick(View v) {
        Log.e(TAG, "ON CLICK ");
        switch (v.getId()) {
            case R.id.image_iv_next_video:
                Log.e(TAG, "NEXT BTN");
                if (getPosition() < getListSize() - 1) {
                    releasePlayer();
                    int pos = getPosition();
                    setPosition(++pos);
                    showStep(getPosition());
                }
                break;
            case R.id.image_iv_previous_video:
                Log.e(TAG, "PREV BTN");
                if (getPosition() > 0) {
                    releasePlayer();
                    int pos = getPosition();
                    setPosition(--pos);
                    showStep(getPosition());
                }
                break;
        }
    }

    /**
     * method to show the step ( video , description )
     * called in onResume callback and onClick
     **/
    private void showStep(int position) {
        Step step = getStep(position);
        String description = step.getDescription();
        String url = step.getVideoURL();
        if (url.length() > 0) {
            if (!isVisible(exoPlayerView)) {
                makeshowViews(exoPlayerView);
            }
            showPlayer(url);
        } else
            makeHiddenViews(exoPlayerView);

        showDescription(description);
    }

    /**
     * method responsible showing video player
     * called in show step if the step have a video player link
     **/
    private void showPlayer(String url) {

        try {
            mainHandler = new Handler();
            mediaDataSourceFactory = new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), null));
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            if (url.length() > 0) {

                videoURI = Uri.parse(url);

                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(videoURI, dataSourceFactory, extractorsFactory, null, null);

                exoPlayerView.setPlayer(exoPlayer);


                if (chk != -1) {
                    exoPlayer.seekTo(position);

                    exoPlayer.setPlayWhenReady(state);
                }

                exoPlayer.prepare(mediaSource);
                if (chk == -1)
                    exoPlayer.setPlayWhenReady(true);

            } else {

            }


        } catch (Exception e) {

            Log.e("MainAcvtivity", " exoplayer error " + e.toString());

        }
    }

    /**
     * method to put the description text in textviiew
     * called in showStep method and oncreateview callback
     **/
    private void showDescription(String description) {
        descriptionTV.setText(description);
    }

    /**
     * convert hidden view to visible
     * called in showStep method
     **/
    private void makeshowViews(View... view) {
        for (View v : view) {
            v.setVisibility(View.VISIBLE);
        }
    }

    /**
     * convert visible views to hidden
     * called in show step
     **/
    private void makeHiddenViews(View... view) {
        for (View v : view) {
            v.setVisibility(View.GONE);
        }
    }

    /**
     * check the visibility of the exoplayer
     * called in showStep method
     **/
    public boolean isVisible(SimpleExoPlayerView v) {
        return v.getVisibility() == View.VISIBLE;
    }

    /**
     * get the position of the current step
     * method called in onResume, onClick to get the position of the step
     **/
    public int getPosition() {
        return stepPosition;
    }

    /**
     * set the list of the steps position
     * called in onCreateView callback
     **/
    public void setPosition(int position) {
        this.stepPosition = position;
    }

    /**
     * return _ int _ size of the list that holds steps
     * called in onClick when clicking previous or forward buttons
     **/
    public int getListSize() {
        return listSize;
    }

    /**
     * the list of the steps this method called in oncreateview callback after getting steps list
     * from bundle
     * called in onCreateView callback
     **/
    public void setListSizs(int listSizs) {
        this.listSize = listSizs;
    }

    /**
     * get steps from database
     */
    public RealmList<Step> getSteps() {
        return steps;
    }

    /**
     * method to get step at position given in parameter from steps list
     */
    private Step getStep(int position) {
        if (getSteps() != null)
            return getSteps().get(position);
        else return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isDestroyed = 1;
    }
}
