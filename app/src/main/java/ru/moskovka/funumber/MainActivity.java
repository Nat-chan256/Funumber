package ru.moskovka.funumber;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.moskovka.funumber.data.Serializer;
import ru.moskovka.funumber.network.NetworkService;
import ru.moskovka.funumber.network.NumberApi;
import ru.moskovka.funumber.util.ConnectionChecker;
import ru.moskovka.funumber.util.UIUtils;

public class MainActivity extends AppCompatActivity {

    private NumberApi numberApi;

    private TextView tvMathFact;
    private TextView tvDateFact;
    private TextView tvYearFact;
    private TextView tvTriviaFact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createAccordions();

        numberApi = NetworkService.getInstance().getNumberApi();
        tvMathFact = findViewById(R.id.tv_math_fact);
        tvDateFact = findViewById(R.id.tv_date_fact);
        tvYearFact = findViewById(R.id.tv_year_fact);
        tvTriviaFact = findViewById(R.id.tv_trivia_fact);
        try {
            if (ConnectionChecker.isConnectedToInternet(this))
                loadFactsViaInternet();
            else
                deserializeFacts();
        } catch (ExecutionException | InterruptedException | IOException e) {
            e.printStackTrace();
        }

        handleUpdateButtonsClick();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////Utility methods//////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    private void createAccordions(){
        final int ACCORDIONS_COUNT = 4;
        Button[] accordionButtons = new Button[ACCORDIONS_COUNT];
        accordionButtons[0] = findViewById(R.id.btn_math);
        accordionButtons[1] = findViewById(R.id.btn_date);
        accordionButtons[2] = findViewById(R.id.btn_year);
        accordionButtons[3] = findViewById(R.id.btn_trivia);

        LinearLayout[] accordionLayouts = new LinearLayout[ACCORDIONS_COUNT];
        accordionLayouts[0] = findViewById(R.id.lyt_math);
        accordionLayouts[1] = findViewById(R.id.lyt_date);
        accordionLayouts[2] = findViewById(R.id.lyt_year);
        accordionLayouts[3] = findViewById(R.id.lyt_trivia);

        for (int i = 0; i < ACCORDIONS_COUNT; ++i){
            final int index = i;
            accordionButtons[i].setOnClickListener(v -> {
                UIUtils.switchVisibility(accordionLayouts[index]);
            });
        }
    }

    private void deserializeFacts() throws IOException {
        Map<String, String> facts = Serializer.deserialize(this);
        tvMathFact.setText(facts.get("math"));
        tvDateFact.setText(facts.get("date"));
        tvYearFact.setText(facts.get("year"));
        tvTriviaFact.setText(facts.get("trivia"));
    }

    private void handleUpdateButtonsClick(){
        // Handle little buttons
        ImageButton btnUpdateMath = findViewById(R.id.btn_update_math);
        ImageButton btnUpdateDate = findViewById(R.id.btn_update_date);
        ImageButton btnUpdateYear = findViewById(R.id.btn_update_year);
        ImageButton btnUpdateTrivia = findViewById(R.id.btn_update_trivia);

        btnUpdateMath.setOnClickListener(v -> {
            loadFact(numberApi.getMathRandomFact(), tvMathFact);
        });
        btnUpdateDate.setOnClickListener(v -> {
            loadFact(numberApi.getDateRandomFact(), tvDateFact);
        });
        btnUpdateYear.setOnClickListener(v -> {
            loadFact(numberApi.getYearRandomFact(), tvYearFact);
        });
        btnUpdateTrivia.setOnClickListener(v -> {
            loadFact(numberApi.getTriviaRandomFact(), tvTriviaFact);
        });

        // Handle large button
        Button btnUpdateFacts = findViewById(R.id.btn_update_facts);
        btnUpdateFacts.setOnClickListener(v -> {
            try {
                loadFactsViaInternet();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadFact(Call<ResponseBody> myCall, TextView textView){
        myCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(
                    Call<ResponseBody> call,
                    Response<ResponseBody> response
            ) {
                try {
                    textView.setText(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                textView.setText(t.getMessage());
            }
        });
    }

    private void loadFactsViaInternet() throws ExecutionException, InterruptedException {
        loadFact(numberApi.getMathRandomFact(), tvMathFact);
        loadFact(numberApi.getDateRandomFact(), tvDateFact);
        loadFact(numberApi.getYearRandomFact(), tvYearFact);
        loadFact(numberApi.getTriviaRandomFact(), tvTriviaFact);
    }
}