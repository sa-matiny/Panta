package com.iust.panta;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.json.JSONException;
import org.json.JSONObject;

public class PCardMainFragment extends Fragment {

    private static int[] COLORS = new int[]{Color.parseColor("#7AB317"), Color.parseColor("#666764")};
    private int[] pieChartValues = {40, 60};
    private CategorySeries mSeries = new CategorySeries("");
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private GraphicalView mChartView;


    private TextView mProName;
    private TextView mProManager;
    private FrameLayout mProProgress;
    private TextView mProProgressP;
    private TextView mProProgressP2;
    private TextView mProInfo;
    private TextView mProDeadline;
    private Bundle msg;
    private View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        rootView = inflater.inflate(R.layout.fragment_pcard_main, container, false);

        mProName = (TextView) rootView.findViewById(R.id.pro_name);
        mProManager = (TextView) rootView.findViewById(R.id.pro_manager);
        mProProgress = (FrameLayout) rootView.findViewById(R.id.pro_progress);
        mProProgressP = (TextView) rootView.findViewById(R.id.pro_progress_p);
        mProProgressP2 = (TextView) rootView.findViewById(R.id.pro_progress_p2);
        mProInfo = (TextView) rootView.findViewById(R.id.pro_info);
        mProDeadline = (TextView) rootView.findViewById(R.id.pro_date);
        msg = new Bundle();
        msg = getArguments();

        try {
            Log.d("PCardMain", msg.toString());
            JSONObject pro_info = new JSONObject(msg.getString("pro_info"));
            mProName.setText(pro_info.getString("projectName"));
            mProManager.setText(pro_info.getString("managerName"));
            mProProgressP.setText("انجام شده " + pro_info.getString("progress") + "%");
            mProProgressP2.setText("باقی مانده " + (100 - pro_info.getInt("progress")) + "%");
            //pieChartValues = new int[]{pro_info.getInt("progress"), 100 - pro_info.getInt("progress")};
            mProInfo.setText(pro_info.getString("project_info"));
            mProDeadline.setText(pro_info.getString("pDeadline"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRenderer.setPanEnabled(false);
        mRenderer.setClickEnabled(false);
        mRenderer.setShowLabels(false);
        mRenderer.setShowLegend(false);
        mRenderer.setStartAngle(270);
        if (mChartView == null) {
            mChartView = ChartFactory.getPieChartView(getActivity(), mSeries, mRenderer);

            mProProgress.addView(mChartView);
        } else {
            //mProProgress.addView(mChartView);

            Log.d("repaint", "yes");
        }


        Log.d("count_child", String.valueOf(mProProgress.getChildCount()));


        fillPieChart();
        return rootView;
    }

    public void fillPieChart() {
        for (int i = 0; i < pieChartValues.length; i++) {
            mSeries.add(pieChartValues[i]);
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
            mRenderer.addSeriesRenderer(renderer);
            if (mChartView != null) {
                mChartView.repaint();
            }
        }
    }
}