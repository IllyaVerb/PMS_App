package ua.kpi.comsys.io8205.pms_app.ui.drawing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import ua.kpi.comsys.io8205.pms_app.R;

public class DrawingFragment extends Fragment {

    private static final float graph_border_a = -5.2f;
    private static final float graph_border_b = 5.2f;
    private static final double precision = 0.05;

    private DisplayMetrics displayMetrics = new DisplayMetrics();

    private int width = displayMetrics.widthPixels;
    private int height = displayMetrics.heightPixels;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch switchUpdate;
    private LinearLayout line_lay;
    private LineChart line;
    private PieChart pieChart;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_drawing, container, false);
        ((Activity) root.getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        /*line_lay = root.findViewById(R.id.chart_lay);
        line_lay.setLayoutParams(new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
*/

        line = root.findViewById(R.id.chart1);
        pieChart = root.findViewById(R.id.chart2);

        List<Entry> chartData_main = new ArrayList<>();
        List<Entry> chartData_X = new ArrayList<>();
        List<Entry> chartData_Y = new ArrayList<>();

        setGraphicsOnChart(line, chartData_main, chartData_X, chartData_Y);

        switchUpdate = root.findViewById(R.id.switch1);

        /*line_lay.post(() -> {
            int width = line_lay.getWidth();
            int height = line_lay.getHeight();
            System.out.println(width + " " + height);
            line_lay.setLayoutParams(new
                    LinearLayout.LayoutParams(Math.min(width, height), Math.min(width, height)));
        });*/

        switchUpdate.setOnClickListener(v -> {
            if (switchUpdate.isChecked()) {
                line.setVisibility(View.GONE);
                pieChart.setVisibility(View.VISIBLE);
            }
            else {
                pieChart.setVisibility(View.GONE);
                line.setVisibility(View.VISIBLE);
            }
        });

        //root.findViewById(R.id.nav_view)
        //        .setOnClickListener(v -> makeLaySizeCorrect(line_lay, switchUpdate, line, pieChart));

        updateGraphic(root, line, chartData_main, chartData_X, chartData_Y);
        updateDiagram(root, pieChart);

        return root;
    }

    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (switchUpdate.isChecked()) {
            line.setVisibility(View.GONE);
            pieChart.setVisibility(View.VISIBLE);
        }
        else {
            pieChart.setVisibility(View.GONE);
            line.setVisibility(View.VISIBLE);
        }

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            line_lay.setLayoutParams(new
                    LinearLayout.LayoutParams((int)(Math.min(width, height)*0.65), (int)(Math.min(width, height)*0.65)));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            line_lay.setLayoutParams(new
                    LinearLayout.LayoutParams(Math.min(width, height), Math.min(width, height)));
        }
    }*/

    private void initPieChart(PieChart pieChart){
        pieChart.getDescription().setEnabled(false);

        pieChart.setRotationEnabled(true);
        pieChart.setDragDecelerationFrictionCoef(0.95f);
        pieChart.setRotationAngle(0);

        pieChart.setHighlightPerTapEnabled(true);
        pieChart.animateY(1400, Easing.EaseInOutQuad);
        pieChart.spin(1600, 0, 80, Easing.EaseInOutQuad);

        pieChart.getLegend().setCustom(new ArrayList<>());
    }

    private void updateDiagram(View v, PieChart pieChart) {
        initPieChart(pieChart);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();

        ArrayList<Integer> typeAmountMap = new ArrayList<>();
        typeAmountMap.add(15); //"Yellow"
        typeAmountMap.add(25); //"Brown"
        typeAmountMap.add(45); //"Grey"
        typeAmountMap.add(10); //"Red"
        typeAmountMap.add(5); //"Violet"

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#DDC000"));
        colors.add(Color.parseColor("#703107"));
        colors.add(Color.parseColor("#434343"));
        colors.add(Color.parseColor("#CC0000"));
        colors.add(Color.parseColor("#6600AE"));

        for(int i=0; i<typeAmountMap.size(); i++){
            pieEntries.add(new PieEntry(typeAmountMap.get(i).floatValue(), ""));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setValueTextSize(0f);
        pieDataSet.setColors(colors);
        pieDataSet.setSliceSpace(2f);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(0f);
        pieData.setValueTextColor(Color.WHITE);

        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void updateGraphic(View v, LineChart lineChart,
                               List<Entry> chartData_main,
                               List<Entry> chartData_X,
                               List<Entry> chartData_Y){
        SortedMap<Double, Double> graph_main = new TreeMap<>();
        SortedMap<Double, Double> graph_X = new TreeMap<>();
        SortedMap<Double, Double> graph_Y = new TreeMap<>();

        lineChart.getXAxis().setAxisMinimum(graph_border_a);
        lineChart.getAxisLeft().setAxisMinimum(-graph_border_a*graph_border_a);
        lineChart.getAxisRight().setAxisMinimum(-graph_border_a*graph_border_a);
        lineChart.getXAxis().setAxisMaximum(graph_border_b);
        lineChart.getAxisLeft().setAxisMaximum(graph_border_b*graph_border_b);
        lineChart.getAxisRight().setAxisMaximum(graph_border_b*graph_border_b);

        lineChart.getAxisLeft().setCenterAxisLabels(true);

        for (double i = graph_border_a; i <= graph_border_b; i+=precision) {
            drawGraph(lineChart, graph_X, chartData_X, 1, i, 0);
        }
        for (double i = -3; i <= 3; i+=precision) {
            drawGraph(lineChart, graph_main, chartData_main, 0, i, i*i*i);
        }
        for (double i = -0.01; i <= 0.01; i+=precision/10) {
            drawGraph(lineChart, graph_Y, chartData_Y, 2, i,
                    graph_border_b*graph_border_b*graph_border_b*100*i);
        }
    }

    private void setGraphicsOnChart(LineChart line,
                                    List<Entry> chartData_main,
                                    List<Entry> chartData_X,
                                    List<Entry> chartData_Y){
        line.getDescription().setEnabled(false);

        line.getLegend().setCustom(new ArrayList<>());

        setAxisParams(line.getXAxis());
        setAxisParams(line.getAxisLeft());
        setAxisParams(line.getAxisRight());

        line.getXAxis().setAxisMinimum(1f);
        line.getXAxis().setAxisMaximum(5f);

        LineDataSet chartDataSet_main = new LineDataSet(chartData_main, "Function");
        chartDataSet_main.setColor(ContextCompat.getColor(getContext(), R.color.purple_500));

        LineDataSet chartDataSet_X = new LineDataSet(chartData_X, "X");
        chartDataSet_X.setColor(ContextCompat.getColor(getContext(), R.color.grey));

        LineDataSet chartDataSet_Y = new LineDataSet(chartData_Y, "Y");
        chartDataSet_Y.setColor(ContextCompat.getColor(getContext(), R.color.grey));

        setDataSetParams(chartDataSet_main, 1.5f, 5f, false);
        setDataSetParams(chartDataSet_X, 1.5f, 5f, false);
        setDataSetParams(chartDataSet_Y, 1.5f, 5f, false);

        List<ILineDataSet> charDataSets = new ArrayList<>();
        charDataSets.add(chartDataSet_main);
        charDataSets.add(chartDataSet_X);
        charDataSets.add(chartDataSet_Y);

        LineData lineData = new LineData(charDataSets);
        line.setData(lineData);
    }

    private void drawGraph(LineChart l, Map<Double, Double> m, List data,
                           int index, double key, double value){
        m.put(key, value);

        data.clear();
        for (double v: m.keySet()) {
            data.add(new Entry((float)v, m.get(v).floatValue()));
        }
        LineDataSet set = (LineDataSet)l.getData().getDataSetByIndex(index);
        set.setValues(data);
        set.notifyDataSetChanged();
        l.getData().notifyDataChanged();
        l.notifyDataSetChanged();
        l.invalidate();
    }

    private void setAxisParams(AxisBase axis){
        axis.setDrawLabels(false);
        axis.setDrawAxisLine(false);
        axis.setDrawGridLines(false);
    }

    private void setDataSetParams(LineDataSet dataSet,
                                  float lineWidth, float circleRadius, boolean drawCircle){
        dataSet.setLineWidth(lineWidth);
        dataSet.setCircleRadius(circleRadius);

        dataSet.setDrawCircleHole(true);

        dataSet.setFormLineWidth(1f);
        dataSet.setFormSize(15.f);

        dataSet.setValueTextSize(9f);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(drawCircle);
    }

}